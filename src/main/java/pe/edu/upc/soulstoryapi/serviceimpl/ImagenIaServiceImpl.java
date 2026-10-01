package pe.edu.upc.soulstoryapi.serviceimpl;

import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pe.edu.upc.soulstoryapi.dto.GenerarImagenIaDTO;
import pe.edu.upc.soulstoryapi.dto.ImagenIaRespuestaDTO;
import pe.edu.upc.soulstoryapi.dto.RecuerdoRespuestaDTO;
import pe.edu.upc.soulstoryapi.entity.Asignacion;
import pe.edu.upc.soulstoryapi.entity.GaleriaIa;
import pe.edu.upc.soulstoryapi.exception.ImagenIaPendienteNoEncontradaException;
import pe.edu.upc.soulstoryapi.repository.GaleriaIaRepository;
import pe.edu.upc.soulstoryapi.service.ArchivoRecuerdoService;
import pe.edu.upc.soulstoryapi.service.AsignacionService;
import pe.edu.upc.soulstoryapi.service.ImagenIaPendiente;
import pe.edu.upc.soulstoryapi.service.ImagenIaService;
import pe.edu.upc.soulstoryapi.service.OpenAiImageService;
import pe.edu.upc.soulstoryapi.service.RecuerdoService;

import java.time.LocalDateTime;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class ImagenIaServiceImpl implements ImagenIaService {

    private final OpenAiImageService openAiImageService;
    private final ArchivoRecuerdoService archivoRecuerdoService;
    private final RecuerdoService recuerdoService;
    private final AsignacionService asignacionService;
    private final GaleriaIaRepository galeriaIaRepository;

    private final ConcurrentHashMap<String, ImagenIaPendiente>
            imagenesPendientes = new ConcurrentHashMap<>();

    public ImagenIaServiceImpl(
            OpenAiImageService openAiImageService,
            ArchivoRecuerdoService archivoRecuerdoService,
            RecuerdoService recuerdoService,
            AsignacionService asignacionService,
            GaleriaIaRepository galeriaIaRepository) {

        this.openAiImageService = openAiImageService;
        this.archivoRecuerdoService = archivoRecuerdoService;
        this.recuerdoService = recuerdoService;
        this.asignacionService = asignacionService;
        this.galeriaIaRepository = galeriaIaRepository;
    }

    @Override
    public ImagenIaRespuestaDTO generarImagen(
            GenerarImagenIaDTO dto) {

        validarSolicitud(dto);

        byte[] imagenGenerada = openAiImageService
                .generarImagen(dto.getPrompt());

        String rutaRelativa = archivoRecuerdoService
                .guardarImagenIa(imagenGenerada);

        String token = UUID.randomUUID().toString();

        ImagenIaPendiente pendiente = new ImagenIaPendiente(
                token,
                dto.getTitulo(),
                dto.getPrompt(),
                rutaRelativa,
                "image/png",
                LocalDateTime.now()
        );

        imagenesPendientes.put(token, pendiente);

        return convertirRespuesta(pendiente);
    }

    @Override
    public Resource obtenerPreview(String token) {

        ImagenIaPendiente pendiente = buscarPendiente(token);

        return archivoRecuerdoService.obtenerArchivo(
                "ia",
                pendiente.getRutaRelativa()
        );
    }

    @Override
    @Transactional
    public RecuerdoRespuestaDTO confirmarComoAdultoMayor(
            Long idAdultoMayor,
            String token) {

        recuerdoService.comprobarAdultoMayor(idAdultoMayor);

        return confirmarImagen(
                idAdultoMayor,
                null,
                token
        );
    }

    @Override
    @Transactional
    public RecuerdoRespuestaDTO confirmarComoCuidador(
            Long idCuidador,
            Long idAdultoMayor,
            String token) {

        Asignacion asignacion = asignacionService
                .obtenerAsignacionAutorizada(
                        idCuidador,
                        idAdultoMayor
                );

        return confirmarImagen(
                idAdultoMayor,
                asignacion.getIdAsignacion(),
                token
        );
    }

    private RecuerdoRespuestaDTO confirmarImagen(
            Long idAdultoMayor,
            Long idAsignacion,
            String token) {

        ImagenIaPendiente pendiente = buscarPendiente(token);

        RecuerdoRespuestaDTO recuerdo = recuerdoService
                .crearRecuerdoImagenIa(
                        idAdultoMayor,
                        idAsignacion,
                        pendiente.getTitulo(),
                        pendiente.getRutaRelativa()
                );

        GaleriaIa galeriaIa = new GaleriaIa();

        galeriaIa.setPromptDescripcion(pendiente.getPrompt());
        galeriaIa.setTitulo(pendiente.getTitulo());
        galeriaIa.setUrlImagen(pendiente.getRutaRelativa());
        galeriaIa.setFechaCreacion(LocalDateTime.now());
        galeriaIa.setIdRecuerdo(recuerdo.getIdRecuerdo());

        galeriaIaRepository.save(galeriaIa);

        imagenesPendientes.remove(token);

        return recuerdo;
    }

    private ImagenIaPendiente buscarPendiente(String token) {

        ImagenIaPendiente pendiente = imagenesPendientes.get(token);

        if (pendiente == null) {
            throw new ImagenIaPendienteNoEncontradaException(
                    "La imagen generada no existe o ya fue confirmada"
            );
        }

        return pendiente;
    }

    private void validarSolicitud(GenerarImagenIaDTO dto) {

        if (dto == null
                || dto.getTitulo() == null
                || dto.getTitulo().isBlank()) {

            throw new IllegalArgumentException(
                    "El título es obligatorio"
            );
        }

        if (dto.getPrompt() == null || dto.getPrompt().isBlank()) {
            throw new IllegalArgumentException(
                    "La descripción es obligatoria"
            );
        }

        if (dto.getPrompt().length()
                > GenerarImagenIaDTO.MAX_PROMPT_IA) {

            throw new IllegalArgumentException(
                    "La descripción excede la longitud máxima permitida"
            );
        }
    }

    private ImagenIaRespuestaDTO convertirRespuesta(
            ImagenIaPendiente pendiente) {

        return new ImagenIaRespuestaDTO(
                pendiente.getToken(),
                pendiente.getTitulo(),
                pendiente.getPrompt(),
                "/api/ia/imagenes/" + pendiente.getToken(),
                "Imagen generada correctamente"
        );
    }
}

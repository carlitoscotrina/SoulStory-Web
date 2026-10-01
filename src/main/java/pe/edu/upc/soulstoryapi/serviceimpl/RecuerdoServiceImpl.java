package pe.edu.upc.soulstoryapi.serviceimpl;

import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import pe.edu.upc.soulstoryapi.dto.ActualizarRecuerdoDTO;
import pe.edu.upc.soulstoryapi.dto.CrearRecuerdoTextoDTO;
import pe.edu.upc.soulstoryapi.dto.RecuerdoRespuestaDTO;
import pe.edu.upc.soulstoryapi.entity.Asignacion;
import pe.edu.upc.soulstoryapi.entity.Recuerdo;
import pe.edu.upc.soulstoryapi.exception.AccesoNoAutorizadoException;
import pe.edu.upc.soulstoryapi.exception.AdultoMayorNoEncontradoException;
import pe.edu.upc.soulstoryapi.exception.RecuerdoNoEncontradoException;
import pe.edu.upc.soulstoryapi.repository.AdultoMayorRepository;
import pe.edu.upc.soulstoryapi.repository.RecuerdoRepository;
import pe.edu.upc.soulstoryapi.service.ArchivoRecuerdoService;
import pe.edu.upc.soulstoryapi.service.AsignacionService;
import pe.edu.upc.soulstoryapi.service.RecuerdoService;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class RecuerdoServiceImpl implements RecuerdoService {

    private final RecuerdoRepository recuerdoRepository;
    private final AdultoMayorRepository adultoMayorRepository;
    private final ArchivoRecuerdoService archivoRecuerdoService;
    private final AsignacionService asignacionService;

    public RecuerdoServiceImpl(
            RecuerdoRepository recuerdoRepository,
            AdultoMayorRepository adultoMayorRepository,
            ArchivoRecuerdoService archivoRecuerdoService,
            AsignacionService asignacionService) {

        this.recuerdoRepository = recuerdoRepository;
        this.adultoMayorRepository = adultoMayorRepository;
        this.archivoRecuerdoService = archivoRecuerdoService;
        this.asignacionService = asignacionService;
    }

    // HU-11
    @Override
    public RecuerdoRespuestaDTO crearRecuerdoTexto(
            Long idAdultoMayor,
            CrearRecuerdoTextoDTO dto) {

        validarAdultoMayor(idAdultoMayor);

        return guardarRecuerdoTexto(
                idAdultoMayor,
                dto,
                null
        );
    }

    // HU-12
    @Override
    public RecuerdoRespuestaDTO crearRecuerdoAudio(
            Long idAdultoMayor,
            String tituloRecuerdo,
            MultipartFile archivo) {

        validarAdultoMayor(idAdultoMayor);
        return guardarRecuerdoAudio(
                idAdultoMayor,
                tituloRecuerdo,
                archivo,
                null
        );
    }

    // HU-13
    @Override
    public RecuerdoRespuestaDTO crearRecuerdoImagen(
            Long idAdultoMayor,
            String tituloRecuerdo,
            MultipartFile archivo) {

        validarAdultoMayor(idAdultoMayor);
        return guardarRecuerdoImagen(
                idAdultoMayor,
                tituloRecuerdo,
                archivo,
                null
        );
    }

    // HU-14
    @Override
    public RecuerdoRespuestaDTO crearRecuerdoTextoComoCuidador(
            Long idCuidador,
            Long idAdultoMayor,
            CrearRecuerdoTextoDTO dto) {

        Asignacion asignacion = asignacionService
                .obtenerAsignacionAutorizada(
                        idCuidador,
                        idAdultoMayor
                );

        return guardarRecuerdoTexto(
                idAdultoMayor,
                dto,
                asignacion.getIdAsignacion()
        );
    }

    @Override
    public RecuerdoRespuestaDTO crearRecuerdoAudioComoCuidador(
            Long idCuidador,
            Long idAdultoMayor,
            String tituloRecuerdo,
            MultipartFile archivo) {

        Asignacion asignacion = asignacionService
                .obtenerAsignacionAutorizada(
                        idCuidador,
                        idAdultoMayor
                );

        return guardarRecuerdoAudio(
                idAdultoMayor,
                tituloRecuerdo,
                archivo,
                asignacion.getIdAsignacion()
        );
    }

    @Override
    public RecuerdoRespuestaDTO crearRecuerdoImagenComoCuidador(
            Long idCuidador,
            Long idAdultoMayor,
            String tituloRecuerdo,
            MultipartFile archivo) {

        Asignacion asignacion = asignacionService
                .obtenerAsignacionAutorizada(
                        idCuidador,
                        idAdultoMayor
                );

        return guardarRecuerdoImagen(
                idAdultoMayor,
                tituloRecuerdo,
                archivo,
                asignacion.getIdAsignacion()
        );
    }

    // HU-17
    @Override
    public void comprobarAdultoMayor(Long idAdultoMayor) {

        validarAdultoMayor(idAdultoMayor);
    }

    @Override
    public RecuerdoRespuestaDTO crearRecuerdoImagenIa(
            Long idAdultoMayor,
            Long idAsignacion,
            String tituloRecuerdo,
            String rutaRelativa) {

        validarAdultoMayor(idAdultoMayor);
        validarTitulo(tituloRecuerdo);

        if (rutaRelativa == null || rutaRelativa.isBlank()) {
            throw new IllegalArgumentException(
                    "La ruta de la imagen generada es obligatoria"
            );
        }

        Recuerdo recuerdo = crearRecuerdoBase(
                idAdultoMayor,
                tituloRecuerdo,
                "IMAGEN",
                rutaRelativa,
                "image/png",
                idAsignacion
        );

        return convertirDTO(
                recuerdoRepository.save(recuerdo)
        );
    }

    @Override
    public List<RecuerdoRespuestaDTO>
    listarRecuerdosAdultoMayor(
            Long idAdultoMayor) {

        validarAdultoMayor(idAdultoMayor);

        return recuerdoRepository
                .findByIdAdultoMayorOrderByFechaCreacionDesc(
                        idAdultoMayor
                )
                .stream()
                .map(this::convertirDTO)
                .toList();
    }

    @Override
    public RecuerdoRespuestaDTO obtenerRecuerdo(
            Long idRecuerdo) {

        return convertirDTO(
                buscarRecuerdo(idRecuerdo)
        );
    }

    // HU-21 y HU-22
    @Override
    public Recuerdo obtenerEntidadRecuerdo(
            Long idRecuerdo) {

        return buscarRecuerdo(idRecuerdo);
    }

    // HU-21 y HU-22
    @Override
    public RecuerdoRespuestaDTO convertirRespuesta(
            Recuerdo recuerdo) {

        return convertirDTO(recuerdo);
    }

    @Override
    public Resource obtenerArchivoRecuerdo(
            Long idRecuerdo) {

        Recuerdo recuerdo = buscarRecuerdo(idRecuerdo);

        if ("AUDIO".equals(recuerdo.getTipoRecuerdo())) {
            return archivoRecuerdoService.obtenerArchivo(
                    "audio",
                    recuerdo.getContenido()
            );
        }

        if ("IMAGEN".equals(recuerdo.getTipoRecuerdo())) {
            return archivoRecuerdoService.obtenerArchivo(
                    "imagen",
                    recuerdo.getContenido()
            );
        }

        throw new IllegalArgumentException(
                "El recuerdo no tiene un archivo para visualizar"
        );
    }

    // HU-15
    @Override
    public RecuerdoRespuestaDTO actualizarRecuerdoComoAdultoMayor(
            Long idAdultoMayor,
            Long idRecuerdo,
            ActualizarRecuerdoDTO dto) {

        validarAdultoMayor(idAdultoMayor);

        Recuerdo recuerdo = buscarRecuerdo(idRecuerdo);

        validarPropietarioAdultoMayor(
                recuerdo,
                idAdultoMayor
        );

        return actualizarRecuerdo(recuerdo, dto);
    }

    @Override
    public RecuerdoRespuestaDTO actualizarRecuerdoComoCuidador(
            Long idCuidador,
            Long idRecuerdo,
            ActualizarRecuerdoDTO dto) {

        Recuerdo recuerdo = buscarRecuerdo(idRecuerdo);

        asignacionService.obtenerAsignacionAutorizada(
                idCuidador,
                recuerdo.getIdAdultoMayor()
        );

        return actualizarRecuerdo(recuerdo, dto);
    }

    // HU-16
    @Override
    public RecuerdoRespuestaDTO alternarFavoritoComoAdultoMayor(
            Long idAdultoMayor,
            Long idRecuerdo) {

        validarAdultoMayor(idAdultoMayor);

        Recuerdo recuerdo = buscarRecuerdo(idRecuerdo);

        validarPropietarioAdultoMayor(
                recuerdo,
                idAdultoMayor
        );

        return alternarFavorito(recuerdo);
    }

    @Override
    public RecuerdoRespuestaDTO alternarFavoritoComoCuidador(
            Long idCuidador,
            Long idRecuerdo) {

        Recuerdo recuerdo = buscarRecuerdo(idRecuerdo);

        asignacionService.obtenerAsignacionAutorizada(
                idCuidador,
                recuerdo.getIdAdultoMayor()
        );

        return alternarFavorito(recuerdo);
    }

    @Override
    public List<RecuerdoRespuestaDTO>
    listarFavoritosAdultoMayor(
            Long idAdultoMayor) {

        validarAdultoMayor(idAdultoMayor);

        return listarFavoritos(idAdultoMayor);
    }

    @Override
    public List<RecuerdoRespuestaDTO>
    listarFavoritosComoCuidador(
            Long idCuidador,
            Long idAdultoMayor) {

        asignacionService.obtenerAsignacionAutorizada(
                idCuidador,
                idAdultoMayor
        );

        return listarFavoritos(idAdultoMayor);
    }

    private Recuerdo crearRecuerdoBase(
            Long idAdultoMayor,
            String tituloRecuerdo,
            String tipoRecuerdo,
            String contenido,
            String formato,
            Long idAsignacion) {

        Recuerdo recuerdo = new Recuerdo();

        recuerdo.setTituloRecuerdo(tituloRecuerdo);
        recuerdo.setTipoRecuerdo(tipoRecuerdo);
        recuerdo.setContenido(contenido);
        recuerdo.setFormato(formato);
        recuerdo.setFavorito(false);
        recuerdo.setFechaCreacion(LocalDateTime.now());
        recuerdo.setIdAdultoMayor(idAdultoMayor);
        recuerdo.setIdAsignacion(idAsignacion);

        return recuerdo;
    }

    private RecuerdoRespuestaDTO guardarRecuerdoTexto(
            Long idAdultoMayor,
            CrearRecuerdoTextoDTO dto,
            Long idAsignacion) {

        validarTitulo(dto.getTituloRecuerdo());
        validarContenidoTexto(dto.getContenido());

        Recuerdo recuerdo = crearRecuerdoBase(
                idAdultoMayor,
                dto.getTituloRecuerdo(),
                "TEXTO",
                dto.getContenido(),
                "text/plain",
                idAsignacion
        );

        return convertirDTO(
                recuerdoRepository.save(recuerdo)
        );
    }

    private RecuerdoRespuestaDTO guardarRecuerdoAudio(
            Long idAdultoMayor,
            String tituloRecuerdo,
            MultipartFile archivo,
            Long idAsignacion) {

        validarTitulo(tituloRecuerdo);

        String rutaRelativa = archivoRecuerdoService
                .guardarAudio(archivo);

        Recuerdo recuerdo = crearRecuerdoBase(
                idAdultoMayor,
                tituloRecuerdo,
                "AUDIO",
                rutaRelativa,
                archivo.getContentType(),
                idAsignacion
        );

        return convertirDTO(
                recuerdoRepository.save(recuerdo)
        );
    }

    private RecuerdoRespuestaDTO guardarRecuerdoImagen(
            Long idAdultoMayor,
            String tituloRecuerdo,
            MultipartFile archivo,
            Long idAsignacion) {

        validarTitulo(tituloRecuerdo);

        String rutaRelativa = archivoRecuerdoService
                .guardarImagen(archivo);

        Recuerdo recuerdo = crearRecuerdoBase(
                idAdultoMayor,
                tituloRecuerdo,
                "IMAGEN",
                rutaRelativa,
                archivo.getContentType(),
                idAsignacion
        );

        return convertirDTO(
                recuerdoRepository.save(recuerdo)
        );
    }

    private RecuerdoRespuestaDTO actualizarRecuerdo(
            Recuerdo recuerdo,
            ActualizarRecuerdoDTO dto) {

        validarTitulo(dto.getTituloRecuerdo());

        recuerdo.setTituloRecuerdo(dto.getTituloRecuerdo());

        if ("TEXTO".equals(recuerdo.getTipoRecuerdo())) {
            validarContenidoTexto(dto.getContenido());
            recuerdo.setContenido(dto.getContenido());
        }

        return convertirDTO(
                recuerdoRepository.save(recuerdo)
        );
    }

    private RecuerdoRespuestaDTO alternarFavorito(
            Recuerdo recuerdo) {

        recuerdo.setFavorito(
                !Boolean.TRUE.equals(recuerdo.getFavorito())
        );

        return convertirDTO(
                recuerdoRepository.save(recuerdo)
        );
    }

    private List<RecuerdoRespuestaDTO> listarFavoritos(
            Long idAdultoMayor) {

        return recuerdoRepository
                .findByIdAdultoMayorAndFavoritoTrueOrderByFechaCreacionDesc(
                        idAdultoMayor
                )
                .stream()
                .map(this::convertirDTO)
                .toList();
    }

    private void validarAdultoMayor(
            Long idAdultoMayor) {

        adultoMayorRepository
                .findById(idAdultoMayor)
                .orElseThrow(() ->
                        new AdultoMayorNoEncontradoException(
                                "El adulto mayor no existe"
                        )
                );
    }

    private void validarTitulo(String tituloRecuerdo) {

        if (tituloRecuerdo == null || tituloRecuerdo.isBlank()) {
            throw new IllegalArgumentException(
                    "El título del recuerdo es obligatorio"
            );
        }
    }

    private void validarContenidoTexto(String contenido) {

        if (contenido == null || contenido.isBlank()) {
            throw new IllegalArgumentException(
                    "El contenido del recuerdo es obligatorio"
            );
        }

        if (contenido.length()
                > CrearRecuerdoTextoDTO.MAX_CONTENIDO_TEXTO) {

            throw new IllegalArgumentException(
                    "El contenido del recuerdo no puede superar los 5000 caracteres"
            );
        }
    }

    private void validarPropietarioAdultoMayor(
            Recuerdo recuerdo,
            Long idAdultoMayor) {

        if (!recuerdo.getIdAdultoMayor().equals(idAdultoMayor)) {
            throw new AccesoNoAutorizadoException(
                    "El recuerdo no pertenece a este adulto mayor"
            );
        }
    }

    private Recuerdo buscarRecuerdo(Long idRecuerdo) {

        return recuerdoRepository
                .findById(idRecuerdo)
                .orElseThrow(() ->
                        new RecuerdoNoEncontradoException(
                                "El recuerdo no existe"
                        )
                );
    }

    private RecuerdoRespuestaDTO convertirDTO(
            Recuerdo recuerdo) {

        return new RecuerdoRespuestaDTO(
                recuerdo.getIdRecuerdo(),
                recuerdo.getTituloRecuerdo(),
                recuerdo.getTipoRecuerdo(),
                recuerdo.getContenido(),
                recuerdo.getFormato(),
                recuerdo.getFavorito(),
                recuerdo.getFechaCreacion(),
                recuerdo.getIdAdultoMayor(),
                recuerdo.getIdAsignacion()
        );
    }
}

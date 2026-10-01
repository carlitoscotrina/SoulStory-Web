package pe.edu.upc.soulstoryapi.serviceimpl;

import org.springframework.stereotype.Service;
import pe.edu.upc.soulstoryapi.dto.EnviarMensajeDTO;
import pe.edu.upc.soulstoryapi.dto.MensajeConversacionDTO;
import pe.edu.upc.soulstoryapi.entity.Asignacion;
import pe.edu.upc.soulstoryapi.entity.Mensaje;
import pe.edu.upc.soulstoryapi.repository.MensajeRepository;
import pe.edu.upc.soulstoryapi.service.AsignacionService;
import pe.edu.upc.soulstoryapi.service.MensajeService;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class MensajeServiceImpl implements MensajeService {

    private final MensajeRepository mensajeRepository;
    private final AsignacionService asignacionService;

    public MensajeServiceImpl(
            MensajeRepository mensajeRepository,
            AsignacionService asignacionService) {

        this.mensajeRepository = mensajeRepository;
        this.asignacionService = asignacionService;
    }

    // HU-25
    @Override
    public MensajeConversacionDTO enviarMensajeAdultoMayor(
            Long idAdultoMayor,
            Long idCuidador,
            EnviarMensajeDTO dto) {

        Asignacion asignacion = asignacionService
                .obtenerAsignacionAutorizada(
                        idCuidador,
                        idAdultoMayor
                );

        return guardarMensaje(
                asignacion,
                dto,
                "ADULTO_MAYOR"
        );
    }

    // HU-26
    @Override
    public MensajeConversacionDTO enviarMensajeCuidador(
            Long idCuidador,
            Long idAdultoMayor,
            EnviarMensajeDTO dto) {

        Asignacion asignacion = asignacionService
                .obtenerAsignacionAutorizada(
                        idCuidador,
                        idAdultoMayor
                );

        return guardarMensaje(
                asignacion,
                dto,
                "CUIDADOR"
        );
    }

    @Override
    public List<MensajeConversacionDTO> listarConversacion(
            Long idAdultoMayor,
            Long idCuidador) {

        Asignacion asignacion = asignacionService
                .obtenerAsignacionAutorizada(
                        idCuidador,
                        idAdultoMayor
                );

        return listarMensajesAsignacion(asignacion);
    }

    // HU-26 y HU-27
    @Override
    public List<MensajeConversacionDTO>
    listarConversacionComoCuidador(
            Long idCuidador,
            Long idAdultoMayor) {

        Asignacion asignacion = asignacionService
                .obtenerAsignacionAutorizada(
                        idCuidador,
                        idAdultoMayor
                );

        return listarMensajesAsignacion(asignacion);
    }

    private MensajeConversacionDTO guardarMensaje(
            Asignacion asignacion,
            EnviarMensajeDTO dto,
            String tipoRemitente) {

        validarContenido(dto);

        Mensaje mensaje = new Mensaje();
        mensaje.setContenido(dto.getContenido());
        mensaje.setFechaHora(LocalDateTime.now());
        mensaje.setLeido(false);
        mensaje.setTipoRemitente(tipoRemitente);
        mensaje.setIdAsignacion(asignacion.getIdAsignacion());

        return convertirDTO(mensajeRepository.save(mensaje));
    }

    private List<MensajeConversacionDTO>
    listarMensajesAsignacion(Asignacion asignacion) {

        return mensajeRepository
                .findByIdAsignacionOrderByFechaHoraAsc(
                        asignacion.getIdAsignacion()
                )
                .stream()
                .map(this::convertirDTO)
                .toList();
    }

    private void validarContenido(EnviarMensajeDTO dto) {

        if (dto == null
                || dto.getContenido() == null
                || dto.getContenido().isBlank()) {

            throw new IllegalArgumentException(
                    "El mensaje no puede estar vacío"
            );
        }
    }

    private MensajeConversacionDTO convertirDTO(
            Mensaje mensaje) {

        return new MensajeConversacionDTO(
                mensaje.getIdMensaje(),
                mensaje.getContenido(),
                mensaje.getFechaHora(),
                mensaje.getLeido(),
                mensaje.getTipoRemitente(),
                mensaje.getIdAsignacion()
        );
    }
}

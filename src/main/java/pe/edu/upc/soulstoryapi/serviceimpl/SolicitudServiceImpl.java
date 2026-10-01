package pe.edu.upc.soulstoryapi.serviceimpl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pe.edu.upc.soulstoryapi.dto.CrearSolicitudDTO;
import pe.edu.upc.soulstoryapi.dto.SolicitudRespuestaDTO;
import pe.edu.upc.soulstoryapi.entity.Asignacion;
import pe.edu.upc.soulstoryapi.entity.Solicitud;
import pe.edu.upc.soulstoryapi.exception.*;
import pe.edu.upc.soulstoryapi.repository.AdultoMayorRepository;
import pe.edu.upc.soulstoryapi.repository.AsignacionRepository;
import pe.edu.upc.soulstoryapi.repository.CuidadorRepository;
import pe.edu.upc.soulstoryapi.repository.SolicitudRepository;
import pe.edu.upc.soulstoryapi.service.SolicitudService;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class SolicitudServiceImpl implements SolicitudService {

    private final SolicitudRepository solicitudRepository;
    private final AdultoMayorRepository adultoMayorRepository;
    private final CuidadorRepository cuidadorRepository;
    private final AsignacionRepository asignacionRepository;

    public SolicitudServiceImpl(
            SolicitudRepository solicitudRepository,
            AdultoMayorRepository adultoMayorRepository,
            CuidadorRepository cuidadorRepository,
            AsignacionRepository asignacionRepository) {

        this.solicitudRepository = solicitudRepository;
        this.adultoMayorRepository = adultoMayorRepository;
        this.cuidadorRepository = cuidadorRepository;
        this.asignacionRepository = asignacionRepository;
    }

    // ============================================
    // HU-08 - ENVIAR SOLICITUD
    // ============================================

    @Override
    public SolicitudRespuestaDTO crearSolicitud(
            Long idAdultoMayor,
            CrearSolicitudDTO dto) {

        adultoMayorRepository
                .findById(idAdultoMayor)
                .orElseThrow(() ->
                        new AdultoMayorNoEncontradoException(
                                "El adulto mayor no existe"
                        )
                );

        cuidadorRepository
                .findById(dto.getIdCuidador())
                .orElseThrow(() ->
                        new CuidadorNoEncontradoException(
                                "El cuidador seleccionado no existe"
                        )
                );

        boolean existePendiente =
                solicitudRepository
                        .existsByIdAdultoMayorAndIdCuidadorAndEstado(
                                idAdultoMayor,
                                dto.getIdCuidador(),
                                "PENDIENTE"
                        );

        if (existePendiente) {

            throw new SolicitudDuplicadaException(
                    "Ya existe una solicitud pendiente para este cuidador"
            );
        }

        Solicitud solicitud =
                new Solicitud();

        solicitud.setIniciadoPor(
                "ADULTO_MAYOR"
        );

        solicitud.setEstado(
                "PENDIENTE"
        );

        solicitud.setFechaCreacion(
                LocalDateTime.now()
        );

        solicitud.setMensaje(
                dto.getMensaje()
        );

        solicitud.setIdCuidador(
                dto.getIdCuidador()
        );

        solicitud.setIdAdultoMayor(
                idAdultoMayor
        );

        Solicitud guardada =
                solicitudRepository.save(
                        solicitud
                );

        return convertirDTO(
                guardada
        );
    }

    // ============================================
    // HU-09 - LISTAR SOLICITUDES DEL CUIDADOR
    // ============================================

    @Override
    public List<SolicitudRespuestaDTO>
    listarSolicitudesCuidador(
            Long idCuidador) {

        cuidadorRepository
                .findById(idCuidador)
                .orElseThrow(() ->
                        new CuidadorNoEncontradoException(
                                "El cuidador no existe"
                        )
                );

        return solicitudRepository
                .findByIdCuidadorOrderByFechaCreacionDesc(
                        idCuidador
                )
                .stream()
                .map(this::convertirDTO)
                .toList();
    }

    // ============================================
    // HU-09 - ACEPTAR
    // ============================================

    @Override
    @Transactional
    public SolicitudRespuestaDTO aceptarSolicitud(
            Long idCuidador,
            Long idSolicitud) {

        Solicitud solicitud =
                obtenerSolicitudDelCuidador(
                        idCuidador,
                        idSolicitud
                );

        validarPendiente(
                solicitud
        );

        solicitud.setEstado(
                "ACEPTADA"
        );

        Solicitud actualizada =
                solicitudRepository.save(
                        solicitud
                );

        /*
         * La HU-09 exige cambiar el estado a aceptada.
         *
         * Además, como el ERD aprobado posee la relación
         * Solicitud -> Asignacion y las HU-05/06/07 dependen
         * de una asignación, al aceptar materializamos esa
         * relación.
         */

        if (!asignacionRepository
                .existsByIdSolicitud(
                        idSolicitud
                )) {

            Asignacion asignacion =
                    new Asignacion();

            asignacion.setFechaInicio(
                    LocalDateTime.now()
            );

            asignacion.setFechaFin(
                    null
            );

            asignacion.setEstado(
                    "ACTIVA"
            );

            asignacion.setIdSolicitud(
                    idSolicitud
            );

            asignacion.setIdCuidador(
                    solicitud.getIdCuidador()
            );

            asignacion.setIdAdultoMayor(
                    solicitud.getIdAdultoMayor()
            );

            asignacionRepository.save(
                    asignacion
            );
        }

        return convertirDTO(
                actualizada
        );
    }

    // ============================================
    // HU-09 - RECHAZAR
    // ============================================

    @Override
    @Transactional
    public SolicitudRespuestaDTO rechazarSolicitud(
            Long idCuidador,
            Long idSolicitud) {

        Solicitud solicitud =
                obtenerSolicitudDelCuidador(
                        idCuidador,
                        idSolicitud
                );

        validarPendiente(
                solicitud
        );

        solicitud.setEstado(
                "RECHAZADA"
        );

        Solicitud actualizada =
                solicitudRepository.save(
                        solicitud
                );

        return convertirDTO(
                actualizada
        );
    }

    // ============================================
    // HU-10 - LISTAR SOLICITUDES DEL ADULTO
    // ============================================

    @Override
    public List<SolicitudRespuestaDTO>
    listarSolicitudesAdultoMayor(
            Long idAdultoMayor) {

        adultoMayorRepository
                .findById(idAdultoMayor)
                .orElseThrow(() ->
                        new AdultoMayorNoEncontradoException(
                                "El adulto mayor no existe"
                        )
                );

        return solicitudRepository
                .findByIdAdultoMayorOrderByFechaCreacionDesc(
                        idAdultoMayor
                )
                .stream()
                .map(this::convertirDTO)
                .toList();
    }

    // ============================================
    // HU-10 - CONSULTAR SOLICITUD ESPECÍFICA
    // ============================================

    @Override
    public SolicitudRespuestaDTO
    obtenerSolicitudAdultoMayor(
            Long idAdultoMayor,
            Long idSolicitud) {

        adultoMayorRepository
                .findById(idAdultoMayor)
                .orElseThrow(() ->
                        new AdultoMayorNoEncontradoException(
                                "El adulto mayor no existe"
                        )
                );

        Solicitud solicitud =
                solicitudRepository
                        .findByIdSolicitudAndIdAdultoMayor(
                                idSolicitud,
                                idAdultoMayor
                        )
                        .orElseThrow(() ->
                                new SolicitudNoEncontradaException(
                                        "La solicitud no existe"
                                )
                        );

        return convertirDTO(
                solicitud
        );
    }

    // ============================================
    // AUXILIARES
    // ============================================

    private Solicitud obtenerSolicitudDelCuidador(
            Long idCuidador,
            Long idSolicitud) {

        cuidadorRepository
                .findById(idCuidador)
                .orElseThrow(() ->
                        new CuidadorNoEncontradoException(
                                "El cuidador no existe"
                        )
                );

        Solicitud solicitud =
                solicitudRepository
                        .findById(idSolicitud)
                        .orElseThrow(() ->
                                new SolicitudNoEncontradaException(
                                        "La solicitud no existe"
                                )
                        );

        if (!solicitud
                .getIdCuidador()
                .equals(idCuidador)) {

            throw new AccesoNoAutorizadoException(
                    "La solicitud no pertenece a este cuidador"
            );
        }

        return solicitud;
    }

    private void validarPendiente(
            Solicitud solicitud) {

        if (!"PENDIENTE"
                .equalsIgnoreCase(
                        solicitud.getEstado()
                )) {

            throw new SolicitudProcesadaException(
                    "La solicitud ya fue procesada"
            );
        }
    }

    private SolicitudRespuestaDTO convertirDTO(
            Solicitud solicitud) {

        return new SolicitudRespuestaDTO(
                solicitud.getIdSolicitud(),
                solicitud.getIniciadoPor(),
                solicitud.getEstado(),
                solicitud.getFechaCreacion(),
                solicitud.getMensaje(),
                solicitud.getIdCuidador(),
                solicitud.getIdAdultoMayor()
        );
    }
}
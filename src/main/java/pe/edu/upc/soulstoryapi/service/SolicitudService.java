package pe.edu.upc.soulstoryapi.service;

import pe.edu.upc.soulstoryapi.dto.CrearSolicitudDTO;
import pe.edu.upc.soulstoryapi.dto.SolicitudRespuestaDTO;

import java.util.List;

public interface SolicitudService {

    // ============================================
    // HU-08 - ENVIAR SOLICITUD
    // ============================================
    public SolicitudRespuestaDTO crearSolicitud(Long idAdultoMayor, CrearSolicitudDTO dto);

    // ============================================
    // HU-09 - LISTAR SOLICITUDES DEL CUIDADOR
    // ============================================
    public List<SolicitudRespuestaDTO> listarSolicitudesCuidador(Long idCuidador);

    // ============================================
    // HU-09 - ACEPTAR
    // ============================================
    public SolicitudRespuestaDTO aceptarSolicitud(Long idCuidador, Long idSolicitud);

    // ============================================
    // HU-09 - RECHAZAR
    // ============================================
    public SolicitudRespuestaDTO rechazarSolicitud(Long idCuidador, Long idSolicitud);

    // ============================================
    // HU-10 - LISTAR SOLICITUDES DEL ADULTO
    // ============================================
    public List<SolicitudRespuestaDTO> listarSolicitudesAdultoMayor(Long idAdultoMayor);

    // ============================================
    // HU-10 - CONSULTAR SOLICITUD ESPECÍFICA
    // ============================================
    public SolicitudRespuestaDTO obtenerSolicitudAdultoMayor(Long idAdultoMayor, Long idSolicitud);

}

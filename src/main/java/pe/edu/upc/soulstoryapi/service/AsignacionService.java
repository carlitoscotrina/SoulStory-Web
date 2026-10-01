package pe.edu.upc.soulstoryapi.service;

import pe.edu.upc.soulstoryapi.dto.AdultoMayorRespuestaDTO;
import pe.edu.upc.soulstoryapi.dto.CuidadorRespuestaDTO;
import pe.edu.upc.soulstoryapi.entity.Asignacion;

import java.util.List;

public interface AsignacionService {

    // ============================================
    // HU-05
    // ============================================
    public CuidadorRespuestaDTO obtenerCuidadorAsignado(Long idAdultoMayor);

    // ============================================
    // HU-06
    // ============================================
    public List<AdultoMayorRespuestaDTO> listarAdultosAsignados(Long idCuidador);

    // ============================================
    // HU-07
    // ============================================
    public AdultoMayorRespuestaDTO consultarAdultoAsignado(Long idCuidador, Long idAdultoMayor);

    // ============================================
    // AUTORIZACION TEMPORAL PARA HU-14 A HU-16
    // ============================================
    public Asignacion obtenerAsignacionAutorizada(Long idCuidador, Long idAdultoMayor);

}

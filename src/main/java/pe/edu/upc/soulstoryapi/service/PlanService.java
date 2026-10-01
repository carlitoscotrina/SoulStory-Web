package pe.edu.upc.soulstoryapi.service;

import pe.edu.upc.soulstoryapi.dto.PlanRespuestaDTO;

import java.util.List;

public interface PlanService {

    // ============================================
    // HU-31 - LISTAR PLANES
    // ============================================
    public List<PlanRespuestaDTO> listarPlanes(Long idAdultoMayor);

    // ============================================
    // HU-31 - DETALLE DE PLAN
    // ============================================
    public PlanRespuestaDTO obtenerPlan(Long idAdultoMayor, Long idPlan);

}

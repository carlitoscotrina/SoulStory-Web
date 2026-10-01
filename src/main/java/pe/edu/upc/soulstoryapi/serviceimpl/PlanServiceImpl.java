package pe.edu.upc.soulstoryapi.serviceimpl;

import org.springframework.stereotype.Service;
import pe.edu.upc.soulstoryapi.dto.PlanRespuestaDTO;
import pe.edu.upc.soulstoryapi.entity.Plan;
import pe.edu.upc.soulstoryapi.exception.AdultoMayorNoEncontradoException;
import pe.edu.upc.soulstoryapi.exception.PlanNoEncontradoException;
import pe.edu.upc.soulstoryapi.repository.AdultoMayorRepository;
import pe.edu.upc.soulstoryapi.repository.PlanRepository;
import pe.edu.upc.soulstoryapi.service.PlanService;

import java.util.List;

@Service
public class PlanServiceImpl implements PlanService {

    private final PlanRepository planRepository;
    private final AdultoMayorRepository adultoMayorRepository;

    public PlanServiceImpl(
            PlanRepository planRepository,
            AdultoMayorRepository adultoMayorRepository) {

        this.planRepository = planRepository;
        this.adultoMayorRepository = adultoMayorRepository;
    }

    // ============================================
    // HU-31 - LISTAR PLANES
    // ============================================

    @Override
    public List<PlanRespuestaDTO> listarPlanes(
            Long idAdultoMayor) {

        adultoMayorRepository
                .findById(idAdultoMayor)
                .orElseThrow(() ->
                        new AdultoMayorNoEncontradoException(
                                "El adulto mayor no existe"
                        )
                );

        return planRepository
                .findAll()
                .stream()
                .map(this::convertirDTO)
                .toList();
    }

    // ============================================
    // HU-31 - DETALLE DE PLAN
    // ============================================

    @Override
    public PlanRespuestaDTO obtenerPlan(
            Long idAdultoMayor,
            Long idPlan) {

        adultoMayorRepository
                .findById(idAdultoMayor)
                .orElseThrow(() ->
                        new AdultoMayorNoEncontradoException(
                                "El adulto mayor no existe"
                        )
                );

        Plan plan =
                planRepository
                        .findById(idPlan)
                        .orElseThrow(() ->
                                new PlanNoEncontradoException(
                                        "El plan no existe"
                                )
                        );

        return convertirDTO(plan);
    }

    private PlanRespuestaDTO convertirDTO(
            Plan plan) {

        return new PlanRespuestaDTO(
                plan.getIdPlan(),
                plan.getNombrePlan(),
                plan.getPrecio(),
                plan.getLimiteRecuerdos(),
                plan.getDescripcion()
        );
    }
}
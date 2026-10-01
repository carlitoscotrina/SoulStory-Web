package pe.edu.upc.soulstoryapi.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pe.edu.upc.soulstoryapi.dto.PlanRespuestaDTO;
import pe.edu.upc.soulstoryapi.service.PlanService;

import java.util.List;

@RestController
@CrossOrigin("*")
@RequestMapping("/api/adultos-mayores")
public class PlanController {

    private final PlanService planService;

    public PlanController(
            PlanService planService) {

        this.planService = planService;
    }


    @GetMapping("/{idAdultoMayor}/planes")
    public ResponseEntity<List<PlanRespuestaDTO>> listarPlanes(
            @PathVariable Long idAdultoMayor) {

        return ResponseEntity.ok(
                planService.listarPlanes(idAdultoMayor)
        );
    }


    @GetMapping("/{idAdultoMayor}/planes/{idPlan}")
    public ResponseEntity<PlanRespuestaDTO> obtenerPlan(
            @PathVariable Long idAdultoMayor,
            @PathVariable Long idPlan) {

        return ResponseEntity.ok(
                planService.obtenerPlan(
                        idAdultoMayor,
                        idPlan
                )
        );
    }
}
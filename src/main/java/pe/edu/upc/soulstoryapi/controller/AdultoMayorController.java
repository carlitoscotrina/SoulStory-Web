package pe.edu.upc.soulstoryapi.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pe.edu.upc.soulstoryapi.dto.CuidadorRespuestaDTO;
import pe.edu.upc.soulstoryapi.service.AsignacionService;

@RestController
@CrossOrigin("*")
@RequestMapping("/api/adultos-mayores")
public class AdultoMayorController {

    private final AsignacionService asignacionService;

    public AdultoMayorController(
            AsignacionService asignacionService) {

        this.asignacionService =
                asignacionService;
    }

    // HU-05
    @GetMapping("/{idAdultoMayor}/cuidador")
    public ResponseEntity<CuidadorRespuestaDTO>
    obtenerCuidadorAsignado(
            @PathVariable Long idAdultoMayor) {

        return ResponseEntity.ok(
                asignacionService
                        .obtenerCuidadorAsignado(
                                idAdultoMayor
                        )
        );
    }
}
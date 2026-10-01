package pe.edu.upc.soulstoryapi.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pe.edu.upc.soulstoryapi.dto.AdultoMayorRespuestaDTO;
import pe.edu.upc.soulstoryapi.service.AsignacionService;

import java.util.List;

@RestController
@CrossOrigin("*")
@RequestMapping("/api/cuidadores")
public class CuidadorController {

    private final AsignacionService asignacionService;

    public CuidadorController(
            AsignacionService asignacionService) {

        this.asignacionService =
                asignacionService;
    }

    // HU-06
    @GetMapping("/{idCuidador}/adultos-mayores")
    public ResponseEntity<List<AdultoMayorRespuestaDTO>>
    listarAdultosAsignados(
            @PathVariable Long idCuidador) {

        return ResponseEntity.ok(
                asignacionService
                        .listarAdultosAsignados(
                                idCuidador
                        )
        );
    }

    // HU-07
    @GetMapping(
            "/{idCuidador}/adultos-mayores/{idAdultoMayor}"
    )
    public ResponseEntity<AdultoMayorRespuestaDTO>
    consultarAdultoAsignado(
            @PathVariable Long idCuidador,
            @PathVariable Long idAdultoMayor) {

        return ResponseEntity.ok(
                asignacionService
                        .consultarAdultoAsignado(
                                idCuidador,
                                idAdultoMayor
                        )
        );
    }
}
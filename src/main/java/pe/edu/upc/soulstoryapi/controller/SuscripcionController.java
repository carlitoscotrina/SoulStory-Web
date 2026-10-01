package pe.edu.upc.soulstoryapi.controller;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pe.edu.upc.soulstoryapi.dto.EstadoSuscripcionDTO;
import pe.edu.upc.soulstoryapi.dto.PagoRespuestaDTO;
import pe.edu.upc.soulstoryapi.dto.ProcesarPagoDTO;
import pe.edu.upc.soulstoryapi.dto.SuscripcionRespuestaDTO;
import pe.edu.upc.soulstoryapi.service.SuscripcionService;

@RestController
@CrossOrigin("*")
@RequestMapping("/api/adultos-mayores")
public class SuscripcionController {

    private final SuscripcionService suscripcionService;

    public SuscripcionController(
            SuscripcionService suscripcionService) {

        this.suscripcionService = suscripcionService;
    }

    // HU-32
    @PostMapping("/{idAdultoMayor}/suscripciones/planes/{idPlan}")
    public ResponseEntity<SuscripcionRespuestaDTO> seleccionarPlan(
            @PathVariable Long idAdultoMayor,
            @PathVariable Long idPlan) {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(
                        suscripcionService.seleccionarPlan(
                                idAdultoMayor,
                                idPlan
                        )
                );
    }

    // HU-33
    @PostMapping("/{idAdultoMayor}/suscripciones/{idSuscripcion}/pago")
    public ResponseEntity<PagoRespuestaDTO> procesarPago(
            @PathVariable Long idAdultoMayor,
            @PathVariable Integer idSuscripcion,
            @Valid @RequestBody ProcesarPagoDTO dto) {

        return ResponseEntity.ok(
                suscripcionService.procesarPago(
                        idAdultoMayor,
                        idSuscripcion,
                        dto
                )
        );
    }

    // HU-34
    @GetMapping("/{idAdultoMayor}/suscripciones/estado")
    public ResponseEntity<EstadoSuscripcionDTO> consultarEstado(
            @PathVariable Long idAdultoMayor) {

        return ResponseEntity.ok(
                suscripcionService.consultarEstado(idAdultoMayor)
        );
    }
}

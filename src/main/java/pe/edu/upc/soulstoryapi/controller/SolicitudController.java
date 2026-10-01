package pe.edu.upc.soulstoryapi.controller;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pe.edu.upc.soulstoryapi.dto.CrearSolicitudDTO;
import pe.edu.upc.soulstoryapi.dto.SolicitudRespuestaDTO;
import pe.edu.upc.soulstoryapi.service.SolicitudService;

import java.util.List;

@RestController
@CrossOrigin("*")
@RequestMapping("/api")
public class SolicitudController {

    private final SolicitudService solicitudService;

    public SolicitudController(
            SolicitudService solicitudService) {

        this.solicitudService =
                solicitudService;
    }


    // ============================================
    // HU-08
    // ============================================

    @PostMapping(
            "/adultos-mayores/{idAdultoMayor}/solicitudes"
    )
    public ResponseEntity<SolicitudRespuestaDTO>
    crearSolicitud(
            @PathVariable Long idAdultoMayor,
            @Valid @RequestBody CrearSolicitudDTO dto) {

        SolicitudRespuestaDTO respuesta =
                solicitudService.crearSolicitud(
                        idAdultoMayor,
                        dto
                );

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(respuesta);
    }


    // ============================================
    // HU-09
    // ============================================

    @GetMapping(
            "/cuidadores/{idCuidador}/solicitudes"
    )
    public ResponseEntity<List<SolicitudRespuestaDTO>>
    listarSolicitudesCuidador(
            @PathVariable Long idCuidador) {

        return ResponseEntity.ok(
                solicitudService
                        .listarSolicitudesCuidador(
                                idCuidador
                        )
        );
    }


    @PatchMapping(
            "/cuidadores/{idCuidador}/solicitudes/{idSolicitud}/aceptar"
    )
    public ResponseEntity<SolicitudRespuestaDTO>
    aceptarSolicitud(
            @PathVariable Long idCuidador,
            @PathVariable Long idSolicitud) {

        return ResponseEntity.ok(
                solicitudService
                        .aceptarSolicitud(
                                idCuidador,
                                idSolicitud
                        )
        );
    }


    @PatchMapping(
            "/cuidadores/{idCuidador}/solicitudes/{idSolicitud}/rechazar"
    )
    public ResponseEntity<SolicitudRespuestaDTO>
    rechazarSolicitud(
            @PathVariable Long idCuidador,
            @PathVariable Long idSolicitud) {

        return ResponseEntity.ok(
                solicitudService
                        .rechazarSolicitud(
                                idCuidador,
                                idSolicitud
                        )
        );
    }


    // ============================================
    // HU-10
    // ============================================

    @GetMapping(
            "/adultos-mayores/{idAdultoMayor}/solicitudes"
    )
    public ResponseEntity<List<SolicitudRespuestaDTO>>
    listarSolicitudesAdultoMayor(
            @PathVariable Long idAdultoMayor) {

        return ResponseEntity.ok(
                solicitudService
                        .listarSolicitudesAdultoMayor(
                                idAdultoMayor
                        )
        );
    }


    @GetMapping(
            "/adultos-mayores/{idAdultoMayor}/solicitudes/{idSolicitud}"
    )
    public ResponseEntity<SolicitudRespuestaDTO>
    obtenerSolicitudAdultoMayor(
            @PathVariable Long idAdultoMayor,
            @PathVariable Long idSolicitud) {

        return ResponseEntity.ok(
                solicitudService
                        .obtenerSolicitudAdultoMayor(
                                idAdultoMayor,
                                idSolicitud
                        )
        );
    }
}
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
import pe.edu.upc.soulstoryapi.dto.CrearGrupoDTO;
import pe.edu.upc.soulstoryapi.dto.GrupoRespuestaDTO;
import pe.edu.upc.soulstoryapi.dto.AdultoMayorRespuestaDTO;
import pe.edu.upc.soulstoryapi.dto.RecuerdoRespuestaDTO;
import pe.edu.upc.soulstoryapi.service.GrupoCompartidoService;
import pe.edu.upc.soulstoryapi.service.GrupoService;

import java.util.List;

@RestController
@CrossOrigin("*")
@RequestMapping("/api/adultos-mayores")
public class GrupoController {

    private final GrupoService grupoService;
    private final GrupoCompartidoService grupoCompartidoService;

    public GrupoController(
            GrupoService grupoService,
            GrupoCompartidoService grupoCompartidoService) {

        this.grupoService = grupoService;
        this.grupoCompartidoService = grupoCompartidoService;
    }

    // HU-19
    @PostMapping("/{idAdultoMayor}/grupos")
    public ResponseEntity<GrupoRespuestaDTO> crearGrupo(
            @PathVariable Long idAdultoMayor,
            @Valid @RequestBody CrearGrupoDTO dto) {

        GrupoRespuestaDTO respuesta = grupoService.crearGrupo(
                idAdultoMayor,
                dto
        );

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(respuesta);
    }

    @GetMapping("/{idAdultoMayor}/grupos")
    public ResponseEntity<List<GrupoRespuestaDTO>> listarGrupos(
            @PathVariable Long idAdultoMayor) {

        return ResponseEntity.ok(
                grupoService.listarGrupos(idAdultoMayor)
        );
    }

    @GetMapping("/{idAdultoMayor}/grupos/{idGrupo}")
    public ResponseEntity<GrupoRespuestaDTO> obtenerGrupo(
            @PathVariable Long idAdultoMayor,
            @PathVariable Long idGrupo) {

        return ResponseEntity.ok(
                grupoService.obtenerGrupo(
                        idAdultoMayor,
                        idGrupo
                )
        );
    }

    // HU-20
    @PostMapping(
            "/{idAdministrador}/grupos/{idGrupo}/integrantes/{idAdultoMayor}"
    )
    public ResponseEntity<AdultoMayorRespuestaDTO>
    agregarIntegrante(
            @PathVariable Long idAdministrador,
            @PathVariable Long idGrupo,
            @PathVariable Long idAdultoMayor) {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(
                        grupoCompartidoService.agregarIntegrante(
                                idAdministrador,
                                idGrupo,
                                idAdultoMayor
                        )
                );
    }

    @GetMapping(
            "/{idAdministrador}/grupos/{idGrupo}/integrantes"
    )
    public ResponseEntity<List<AdultoMayorRespuestaDTO>>
    listarIntegrantes(
            @PathVariable Long idAdministrador,
            @PathVariable Long idGrupo) {

        return ResponseEntity.ok(
                grupoCompartidoService.listarIntegrantes(
                        idAdministrador,
                        idGrupo
                )
        );
    }

    // HU-21
    @PostMapping(
            "/{idAdministrador}/grupos/{idGrupo}/recuerdos/{idRecuerdo}"
    )
    public ResponseEntity<RecuerdoRespuestaDTO>
    agregarRecuerdo(
            @PathVariable Long idAdministrador,
            @PathVariable Long idGrupo,
            @PathVariable Long idRecuerdo) {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(
                        grupoCompartidoService.agregarRecuerdo(
                                idAdministrador,
                                idGrupo,
                                idRecuerdo
                        )
                );
    }

    @GetMapping(
            "/{idAdministrador}/grupos/{idGrupo}/recuerdos"
    )
    public ResponseEntity<List<RecuerdoRespuestaDTO>>
    listarRecuerdosAdministracion(
            @PathVariable Long idAdministrador,
            @PathVariable Long idGrupo) {

        return ResponseEntity.ok(
                grupoCompartidoService
                        .listarRecuerdosAdministracion(
                                idAdministrador,
                                idGrupo
                        )
        );
    }

    // HU-22
    @GetMapping(
            "/{idAdultoMayor}/grupos/{idGrupo}/recuerdos-compartidos"
    )
    public ResponseEntity<List<RecuerdoRespuestaDTO>>
    listarRecuerdosCompartidos(
            @PathVariable Long idAdultoMayor,
            @PathVariable Long idGrupo) {

        return ResponseEntity.ok(
                grupoCompartidoService
                        .listarRecuerdosCompartidos(
                                idAdultoMayor,
                                idGrupo
                        )
        );
    }
}

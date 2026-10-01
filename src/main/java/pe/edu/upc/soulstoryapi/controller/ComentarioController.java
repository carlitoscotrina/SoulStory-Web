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
import pe.edu.upc.soulstoryapi.dto.ComentarioRespuestaDTO;
import pe.edu.upc.soulstoryapi.dto.CrearComentarioDTO;
import pe.edu.upc.soulstoryapi.service.ComentarioService;

import java.util.List;

@RestController
@CrossOrigin("*")
@RequestMapping("/api/usuarios")
public class ComentarioController {

    private final ComentarioService comentarioService;

    public ComentarioController(
            ComentarioService comentarioService) {

        this.comentarioService = comentarioService;
    }

    // HU-23
    @PostMapping(
            "/{idUsuario}/recuerdos/{idRecuerdo}/comentarios"
    )
    public ResponseEntity<ComentarioRespuestaDTO>
    crearComentario(
            @PathVariable Long idUsuario,
            @PathVariable Long idRecuerdo,
            @Valid @RequestBody CrearComentarioDTO dto) {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(
                        comentarioService.crearComentario(
                                idUsuario,
                                idRecuerdo,
                                dto
                        )
                );
    }

    // HU-24
    @GetMapping(
            "/{idUsuario}/recuerdos/{idRecuerdo}/comentarios"
    )
    public ResponseEntity<List<ComentarioRespuestaDTO>>
    listarComentarios(
            @PathVariable Long idUsuario,
            @PathVariable Long idRecuerdo) {

        return ResponseEntity.ok(
                comentarioService.listarComentarios(
                        idUsuario,
                        idRecuerdo
                )
        );
    }
}

package pe.edu.upc.soulstoryapi.controller;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pe.edu.upc.soulstoryapi.dto.RegistroUsuarioDTO;
import pe.edu.upc.soulstoryapi.dto.UsuarioRespuestaDTO;
import pe.edu.upc.soulstoryapi.service.UsuarioService;
import pe.edu.upc.soulstoryapi.dto.LoginUsuarioDTO;
import pe.edu.upc.soulstoryapi.dto.LoginRespuestaDTO;
import pe.edu.upc.soulstoryapi.dto.ActualizarPerfilDTO;
import pe.edu.upc.soulstoryapi.dto.MensajeRespuestaDTO;
import pe.edu.upc.soulstoryapi.dto.RecuperarContrasenaDTO;
import pe.edu.upc.soulstoryapi.dto.RestablecerContrasenaDTO;
import pe.edu.upc.soulstoryapi.service.RecuperacionContrasenaService;


@RestController
@CrossOrigin("*")
@RequestMapping("/api/usuarios")
public class UsuarioController {

    private final UsuarioService usuarioService;
    private final RecuperacionContrasenaService recuperacionContrasenaService;

    public UsuarioController(
            UsuarioService usuarioService,
            RecuperacionContrasenaService recuperacionContrasenaService) {

        this.usuarioService = usuarioService;
        this.recuperacionContrasenaService =
                recuperacionContrasenaService;
    }

    @PostMapping("/registro")
    public ResponseEntity<UsuarioRespuestaDTO> registrarUsuario(
            @Valid @RequestBody RegistroUsuarioDTO dto) {

        UsuarioRespuestaDTO respuesta =
                usuarioService.registrarUsuario(dto);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(respuesta);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginRespuestaDTO> iniciarSesion(
            @Valid @RequestBody LoginUsuarioDTO dto) {

        LoginRespuestaDTO respuesta =
                usuarioService.iniciarSesion(dto);

        return ResponseEntity.ok(respuesta);
    }

    @GetMapping("/{idUsuario}")
    public ResponseEntity<UsuarioRespuestaDTO> obtenerPerfil(
            @PathVariable Long idUsuario) {

        UsuarioRespuestaDTO respuesta =
                usuarioService.obtenerPerfil(idUsuario);

        return ResponseEntity.ok(respuesta);
    }

    @PutMapping("/{idUsuario}")
    public ResponseEntity<UsuarioRespuestaDTO> actualizarPerfil(
            @PathVariable Long idUsuario,
            @Valid @RequestBody ActualizarPerfilDTO dto) {

        UsuarioRespuestaDTO respuesta =
                usuarioService.actualizarPerfil(
                        idUsuario,
                        dto
                );

        return ResponseEntity.ok(respuesta);
    }

    @PostMapping("/recuperar-contrasena")
    public ResponseEntity<MensajeRespuestaDTO> recuperarContrasena(
            @Valid @RequestBody RecuperarContrasenaDTO dto) {

        recuperacionContrasenaService
                .solicitarRecuperacion(dto);

        return ResponseEntity.ok(
                new MensajeRespuestaDTO(
                        "Se enviaron las instrucciones para restablecer la contraseña"
                )
        );
    }

    @PostMapping("/restablecer-contrasena")
    public ResponseEntity<MensajeRespuestaDTO> restablecerContrasena(
            @Valid @RequestBody RestablecerContrasenaDTO dto) {

        recuperacionContrasenaService
                .restablecerContrasena(dto);

        return ResponseEntity.ok(
                new MensajeRespuestaDTO(
                        "Contraseña actualizada correctamente"
                )
        );
    }
}
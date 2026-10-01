package pe.edu.upc.soulstoryapi.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pe.edu.upc.soulstoryapi.dto.ConfiguracionRespuestaDTO;
import pe.edu.upc.soulstoryapi.service.ConfiguracionService;

@RestController
@CrossOrigin("*")
@RequestMapping("/api/usuarios")
public class ConfiguracionController {

    private final ConfiguracionService configuracionService;

    public ConfiguracionController(
            ConfiguracionService configuracionService) {

        this.configuracionService = configuracionService;
    }

    // HU-28
    @GetMapping("/{idUsuario}/configuracion")
    public ResponseEntity<ConfiguracionRespuestaDTO>
    obtenerConfiguracion(@PathVariable Long idUsuario) {

        return ResponseEntity.ok(
                configuracionService.obtenerConfiguracion(idUsuario)
        );
    }

    @PatchMapping("/{idUsuario}/configuracion/fuente/aumentar")
    public ResponseEntity<ConfiguracionRespuestaDTO>
    aumentarFuente(@PathVariable Long idUsuario) {

        return ResponseEntity.ok(
                configuracionService.aumentarFuente(idUsuario)
        );
    }

    @PatchMapping("/{idUsuario}/configuracion/fuente/disminuir")
    public ResponseEntity<ConfiguracionRespuestaDTO>
    disminuirFuente(@PathVariable Long idUsuario) {

        return ResponseEntity.ok(
                configuracionService.disminuirFuente(idUsuario)
        );
    }

    // HU-29
    @PatchMapping("/{idUsuario}/configuracion/tema/claro")
    public ResponseEntity<ConfiguracionRespuestaDTO>
    activarTemaClaro(@PathVariable Long idUsuario) {

        return ResponseEntity.ok(
                configuracionService.activarTemaClaro(idUsuario)
        );
    }

    @PatchMapping("/{idUsuario}/configuracion/tema/oscuro")
    public ResponseEntity<ConfiguracionRespuestaDTO>
    activarTemaOscuro(@PathVariable Long idUsuario) {

        return ResponseEntity.ok(
                configuracionService.activarTemaOscuro(idUsuario)
        );
    }

    // HU-30
    @PatchMapping("/{idUsuario}/configuracion/notificaciones/activar")
    public ResponseEntity<ConfiguracionRespuestaDTO>
    activarNotificaciones(@PathVariable Long idUsuario) {

        return ResponseEntity.ok(
                configuracionService.activarNotificaciones(idUsuario)
        );
    }

    @PatchMapping("/{idUsuario}/configuracion/notificaciones/desactivar")
    public ResponseEntity<ConfiguracionRespuestaDTO>
    desactivarNotificaciones(@PathVariable Long idUsuario) {

        return ResponseEntity.ok(
                configuracionService.desactivarNotificaciones(idUsuario)
        );
    }
}

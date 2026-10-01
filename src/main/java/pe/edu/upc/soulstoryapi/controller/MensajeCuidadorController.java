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
import pe.edu.upc.soulstoryapi.dto.ConversacionResumenDTO;
import pe.edu.upc.soulstoryapi.dto.EnviarMensajeDTO;
import pe.edu.upc.soulstoryapi.dto.MensajeConversacionDTO;
import pe.edu.upc.soulstoryapi.service.ConversacionService;
import pe.edu.upc.soulstoryapi.service.MensajeService;

import java.util.List;

@RestController
@CrossOrigin("*")
@RequestMapping("/api/cuidadores")
public class MensajeCuidadorController {

    private final MensajeService mensajeService;
    private final ConversacionService conversacionService;

    public MensajeCuidadorController(
            MensajeService mensajeService,
            ConversacionService conversacionService) {

        this.mensajeService = mensajeService;
        this.conversacionService = conversacionService;
    }

    // HU-26
    @PostMapping("/{idCuidador}/adultos-mayores/{idAdultoMayor}/mensajes")
    public ResponseEntity<MensajeConversacionDTO>
    enviarMensajeCuidador(
            @PathVariable Long idCuidador,
            @PathVariable Long idAdultoMayor,
            @Valid @RequestBody EnviarMensajeDTO dto) {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(
                        mensajeService.enviarMensajeCuidador(
                                idCuidador,
                                idAdultoMayor,
                                dto
                        )
                );
    }

    // HU-26 y HU-27
    @GetMapping("/{idCuidador}/adultos-mayores/{idAdultoMayor}/mensajes")
    public ResponseEntity<List<MensajeConversacionDTO>>
    listarConversacionComoCuidador(
            @PathVariable Long idCuidador,
            @PathVariable Long idAdultoMayor) {

        return ResponseEntity.ok(
                mensajeService.listarConversacionComoCuidador(
                        idCuidador,
                        idAdultoMayor
                )
        );
    }

    // HU-27
    @GetMapping("/{idCuidador}/conversaciones")
    public ResponseEntity<List<ConversacionResumenDTO>>
    listarConversacionesCuidador(
            @PathVariable Long idCuidador) {

        return ResponseEntity.ok(
                conversacionService.listarConversacionesCuidador(
                        idCuidador
                )
        );
    }
}

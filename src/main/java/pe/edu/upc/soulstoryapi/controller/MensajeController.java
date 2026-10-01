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
import pe.edu.upc.soulstoryapi.dto.EnviarMensajeDTO;
import pe.edu.upc.soulstoryapi.dto.MensajeConversacionDTO;
import pe.edu.upc.soulstoryapi.service.MensajeService;

import java.util.List;

@RestController
@CrossOrigin("*")
@RequestMapping("/api/adultos-mayores")
public class MensajeController {

    private final MensajeService mensajeService;

    public MensajeController(MensajeService mensajeService) {

        this.mensajeService = mensajeService;
    }

    // HU-25
    @PostMapping("/{idAdultoMayor}/cuidadores/{idCuidador}/mensajes")
    public ResponseEntity<MensajeConversacionDTO>
    enviarMensajeAdultoMayor(
            @PathVariable Long idAdultoMayor,
            @PathVariable Long idCuidador,
            @Valid @RequestBody EnviarMensajeDTO dto) {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(
                        mensajeService.enviarMensajeAdultoMayor(
                                idAdultoMayor,
                                idCuidador,
                                dto
                        )
                );
    }

    @GetMapping("/{idAdultoMayor}/cuidadores/{idCuidador}/mensajes")
    public ResponseEntity<List<MensajeConversacionDTO>>
    listarConversacion(
            @PathVariable Long idAdultoMayor,
            @PathVariable Long idCuidador) {

        return ResponseEntity.ok(
                mensajeService.listarConversacion(
                        idAdultoMayor,
                        idCuidador
                )
        );
    }
}

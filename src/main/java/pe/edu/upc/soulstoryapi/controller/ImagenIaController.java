package pe.edu.upc.soulstoryapi.controller;

import jakarta.validation.Valid;
import org.springframework.core.io.Resource;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pe.edu.upc.soulstoryapi.dto.GenerarImagenIaDTO;
import pe.edu.upc.soulstoryapi.dto.ImagenIaRespuestaDTO;
import pe.edu.upc.soulstoryapi.dto.RecuerdoRespuestaDTO;
import pe.edu.upc.soulstoryapi.service.ImagenIaService;

@RestController
@CrossOrigin("*")
@RequestMapping("/api")
public class ImagenIaController {

    private final ImagenIaService imagenIaService;

    public ImagenIaController(
            ImagenIaService imagenIaService) {

        this.imagenIaService = imagenIaService;
    }

    // HU-17 y HU-18
    @PostMapping("/ia/imagenes/generar")
    public ResponseEntity<ImagenIaRespuestaDTO>
    generarImagen(
            @Valid @RequestBody GenerarImagenIaDTO dto) {

        return ResponseEntity.ok(
                imagenIaService.generarImagen(dto)
        );
    }

    @GetMapping("/ia/imagenes/{token}")
    public ResponseEntity<Resource> obtenerPreview(
            @PathVariable String token) {

        Resource imagen = imagenIaService.obtenerPreview(token);

        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_PNG)
                .header(
                        HttpHeaders.CONTENT_DISPOSITION,
                        ContentDisposition.inline().build().toString()
                )
                .body(imagen);
    }

    @PostMapping(
            "/adultos-mayores/{idAdultoMayor}/recuerdos/ia/confirmar/{token}"
    )
    public ResponseEntity<RecuerdoRespuestaDTO>
    confirmarComoAdultoMayor(
            @PathVariable Long idAdultoMayor,
            @PathVariable String token) {

        return ResponseEntity.ok(
                imagenIaService.confirmarComoAdultoMayor(
                        idAdultoMayor,
                        token
                )
        );
    }

    @PostMapping(
            "/cuidadores/{idCuidador}/adultos-mayores/{idAdultoMayor}/recuerdos/ia/confirmar/{token}"
    )
    public ResponseEntity<RecuerdoRespuestaDTO>
    confirmarComoCuidador(
            @PathVariable Long idCuidador,
            @PathVariable Long idAdultoMayor,
            @PathVariable String token) {

        return ResponseEntity.ok(
                imagenIaService.confirmarComoCuidador(
                        idCuidador,
                        idAdultoMayor,
                        token
                )
        );
    }
}

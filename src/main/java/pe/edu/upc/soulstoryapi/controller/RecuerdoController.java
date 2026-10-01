package pe.edu.upc.soulstoryapi.controller;

import jakarta.validation.Valid;
import org.springframework.core.io.Resource;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import pe.edu.upc.soulstoryapi.dto.ActualizarRecuerdoDTO;
import pe.edu.upc.soulstoryapi.dto.CrearRecuerdoTextoDTO;
import pe.edu.upc.soulstoryapi.dto.RecuerdoRespuestaDTO;
import pe.edu.upc.soulstoryapi.service.RecuerdoService;

import java.util.List;

@RestController
@CrossOrigin("*")
@RequestMapping("/api")
public class RecuerdoController {

    private final RecuerdoService recuerdoService;

    public RecuerdoController(
            RecuerdoService recuerdoService) {

        this.recuerdoService = recuerdoService;
    }

    // HU-11
    @PostMapping(
            "/adultos-mayores/{idAdultoMayor}/recuerdos/texto"
    )
    public ResponseEntity<RecuerdoRespuestaDTO>
    crearRecuerdoTexto(
            @PathVariable Long idAdultoMayor,
            @Valid @RequestBody CrearRecuerdoTextoDTO dto) {

        RecuerdoRespuestaDTO respuesta =
                recuerdoService.crearRecuerdoTexto(
                        idAdultoMayor,
                        dto
                );

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(respuesta);
    }

    // HU-12
    @PostMapping(
            value = "/adultos-mayores/{idAdultoMayor}/recuerdos/audio",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public ResponseEntity<RecuerdoRespuestaDTO>
    crearRecuerdoAudio(
            @PathVariable Long idAdultoMayor,
            @RequestParam String tituloRecuerdo,
            @RequestParam(
                    value = "archivo",
                    required = false
            ) MultipartFile archivo) {

        RecuerdoRespuestaDTO respuesta =
                recuerdoService.crearRecuerdoAudio(
                        idAdultoMayor,
                        tituloRecuerdo,
                        archivo
                );

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(respuesta);
    }

    // HU-13
    @PostMapping(
            value = "/adultos-mayores/{idAdultoMayor}/recuerdos/imagen",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public ResponseEntity<RecuerdoRespuestaDTO>
    crearRecuerdoImagen(
            @PathVariable Long idAdultoMayor,
            @RequestParam String tituloRecuerdo,
            @RequestParam(
                    value = "archivo",
                    required = false
            ) MultipartFile archivo) {

        RecuerdoRespuestaDTO respuesta =
                recuerdoService.crearRecuerdoImagen(
                        idAdultoMayor,
                        tituloRecuerdo,
                        archivo
                );

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(respuesta);
    }

    // HU-14
    @PostMapping(
            "/cuidadores/{idCuidador}/adultos-mayores/{idAdultoMayor}/recuerdos/texto"
    )
    public ResponseEntity<RecuerdoRespuestaDTO>
    crearRecuerdoTextoComoCuidador(
            @PathVariable Long idCuidador,
            @PathVariable Long idAdultoMayor,
            @Valid @RequestBody CrearRecuerdoTextoDTO dto) {

        RecuerdoRespuestaDTO respuesta =
                recuerdoService.crearRecuerdoTextoComoCuidador(
                        idCuidador,
                        idAdultoMayor,
                        dto
                );

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(respuesta);
    }

    @PostMapping(
            value = "/cuidadores/{idCuidador}/adultos-mayores/{idAdultoMayor}/recuerdos/audio",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public ResponseEntity<RecuerdoRespuestaDTO>
    crearRecuerdoAudioComoCuidador(
            @PathVariable Long idCuidador,
            @PathVariable Long idAdultoMayor,
            @RequestParam String tituloRecuerdo,
            @RequestParam(
                    value = "archivo",
                    required = false
            ) MultipartFile archivo) {

        RecuerdoRespuestaDTO respuesta =
                recuerdoService.crearRecuerdoAudioComoCuidador(
                        idCuidador,
                        idAdultoMayor,
                        tituloRecuerdo,
                        archivo
                );

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(respuesta);
    }

    @PostMapping(
            value = "/cuidadores/{idCuidador}/adultos-mayores/{idAdultoMayor}/recuerdos/imagen",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public ResponseEntity<RecuerdoRespuestaDTO>
    crearRecuerdoImagenComoCuidador(
            @PathVariable Long idCuidador,
            @PathVariable Long idAdultoMayor,
            @RequestParam String tituloRecuerdo,
            @RequestParam(
                    value = "archivo",
                    required = false
            ) MultipartFile archivo) {

        RecuerdoRespuestaDTO respuesta =
                recuerdoService.crearRecuerdoImagenComoCuidador(
                        idCuidador,
                        idAdultoMayor,
                        tituloRecuerdo,
                        archivo
                );

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(respuesta);
    }

    @GetMapping(
            "/adultos-mayores/{idAdultoMayor}/recuerdos"
    )
    public ResponseEntity<List<RecuerdoRespuestaDTO>>
    listarRecuerdosAdultoMayor(
            @PathVariable Long idAdultoMayor) {

        return ResponseEntity.ok(
                recuerdoService
                        .listarRecuerdosAdultoMayor(
                                idAdultoMayor
                        )
        );
    }

    @GetMapping("/recuerdos/{idRecuerdo}")
    public ResponseEntity<RecuerdoRespuestaDTO>
    obtenerRecuerdo(
            @PathVariable Long idRecuerdo) {

        return ResponseEntity.ok(
                recuerdoService.obtenerRecuerdo(idRecuerdo)
        );
    }

    @GetMapping("/recuerdos/{idRecuerdo}/archivo")
    public ResponseEntity<Resource> obtenerArchivoRecuerdo(
            @PathVariable Long idRecuerdo) {

        RecuerdoRespuestaDTO recuerdo =
                recuerdoService.obtenerRecuerdo(idRecuerdo);

        Resource archivo = recuerdoService
                .obtenerArchivoRecuerdo(idRecuerdo);

        return ResponseEntity.ok()
                .contentType(
                        MediaType.parseMediaType(
                                recuerdo.getFormato()
                        )
                )
                .header(
                        HttpHeaders.CONTENT_DISPOSITION,
                        ContentDisposition.inline().build().toString()
                )
                .body(archivo);
    }

    // HU-15
    @PutMapping(
            "/adultos-mayores/{idAdultoMayor}/recuerdos/{idRecuerdo}"
    )
    public ResponseEntity<RecuerdoRespuestaDTO>
    actualizarRecuerdoComoAdultoMayor(
            @PathVariable Long idAdultoMayor,
            @PathVariable Long idRecuerdo,
            @Valid @RequestBody ActualizarRecuerdoDTO dto) {

        return ResponseEntity.ok(
                recuerdoService.actualizarRecuerdoComoAdultoMayor(
                        idAdultoMayor,
                        idRecuerdo,
                        dto
                )
        );
    }

    @PutMapping(
            "/cuidadores/{idCuidador}/recuerdos/{idRecuerdo}"
    )
    public ResponseEntity<RecuerdoRespuestaDTO>
    actualizarRecuerdoComoCuidador(
            @PathVariable Long idCuidador,
            @PathVariable Long idRecuerdo,
            @Valid @RequestBody ActualizarRecuerdoDTO dto) {

        return ResponseEntity.ok(
                recuerdoService.actualizarRecuerdoComoCuidador(
                        idCuidador,
                        idRecuerdo,
                        dto
                )
        );
    }

    // HU-16
    @PatchMapping(
            "/adultos-mayores/{idAdultoMayor}/recuerdos/{idRecuerdo}/favorito"
    )
    public ResponseEntity<RecuerdoRespuestaDTO>
    alternarFavoritoComoAdultoMayor(
            @PathVariable Long idAdultoMayor,
            @PathVariable Long idRecuerdo) {

        return ResponseEntity.ok(
                recuerdoService.alternarFavoritoComoAdultoMayor(
                        idAdultoMayor,
                        idRecuerdo
                )
        );
    }

    @PatchMapping(
            "/cuidadores/{idCuidador}/recuerdos/{idRecuerdo}/favorito"
    )
    public ResponseEntity<RecuerdoRespuestaDTO>
    alternarFavoritoComoCuidador(
            @PathVariable Long idCuidador,
            @PathVariable Long idRecuerdo) {

        return ResponseEntity.ok(
                recuerdoService.alternarFavoritoComoCuidador(
                        idCuidador,
                        idRecuerdo
                )
        );
    }

    @GetMapping(
            "/adultos-mayores/{idAdultoMayor}/recuerdos/favoritos"
    )
    public ResponseEntity<List<RecuerdoRespuestaDTO>>
    listarFavoritosAdultoMayor(
            @PathVariable Long idAdultoMayor) {

        return ResponseEntity.ok(
                recuerdoService.listarFavoritosAdultoMayor(
                        idAdultoMayor
                )
        );
    }

    @GetMapping(
            "/cuidadores/{idCuidador}/adultos-mayores/{idAdultoMayor}/recuerdos/favoritos"
    )
    public ResponseEntity<List<RecuerdoRespuestaDTO>>
    listarFavoritosComoCuidador(
            @PathVariable Long idCuidador,
            @PathVariable Long idAdultoMayor) {

        return ResponseEntity.ok(
                recuerdoService.listarFavoritosComoCuidador(
                        idCuidador,
                        idAdultoMayor
                )
        );
    }
}

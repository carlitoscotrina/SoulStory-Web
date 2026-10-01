package pe.edu.upc.soulstoryapi.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(CorreoRegistradoException.class)
    public ResponseEntity<Map<String, String>> manejarCorreoRegistrado(
            CorreoRegistradoException exception) {

        Map<String, String> respuesta = new HashMap<>();

        respuesta.put("error", exception.getMessage());

        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(respuesta);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> manejarValidaciones(
            MethodArgumentNotValidException exception) {

        Map<String, String> errores = new HashMap<>();

        exception.getBindingResult()
                .getFieldErrors()
                .forEach(error ->
                        errores.put(
                                error.getField(),
                                error.getDefaultMessage()
                        )
                );

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(errores);
    }

    @ExceptionHandler(CredencialesInvalidasException.class)
    public ResponseEntity<Map<String, String>> manejarCredencialesInvalidas(
            CredencialesInvalidasException exception) {

        Map<String, String> respuesta = new HashMap<>();

        respuesta.put("error", exception.getMessage());

        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(respuesta);
    }

    @ExceptionHandler(UsuarioNoEncontradoException.class)
    public ResponseEntity<Map<String, String>> manejarUsuarioNoEncontrado(
            UsuarioNoEncontradoException exception) {

        Map<String, String> respuesta = new HashMap<>();

        respuesta.put("error", exception.getMessage());

        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(respuesta);
    }

    @ExceptionHandler(CorreoNoRegistradoException.class)
    public ResponseEntity<Map<String, String>> manejarCorreoNoRegistrado(
            CorreoNoRegistradoException exception) {

        Map<String, String> respuesta = new HashMap<>();

        respuesta.put(
                "error",
                exception.getMessage()
        );

        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(respuesta);
    }

    @ExceptionHandler(TokenRecuperacionException.class)
    public ResponseEntity<Map<String, String>> manejarTokenRecuperacion(
            TokenRecuperacionException exception) {

        Map<String, String> respuesta = new HashMap<>();

        respuesta.put(
                "error",
                exception.getMessage()
        );

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(respuesta);
    }

    @ExceptionHandler({
            AdultoMayorNoEncontradoException.class,
            CuidadorNoEncontradoException.class,
            AsignacionNoEncontradaException.class,
            RecuerdoNoEncontradoException.class,
            ImagenIaPendienteNoEncontradaException.class,
            GrupoNoEncontradoException.class,
            SuscripcionNoEncontradaException.class
    })
    public ResponseEntity<Map<String, String>>
    manejarNoEncontrado(RuntimeException exception) {

        Map<String, String> respuesta =
                new HashMap<>();

        respuesta.put(
                "error",
                exception.getMessage()
        );

        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(respuesta);
    }


    @ExceptionHandler(AccesoNoAutorizadoException.class)
    public ResponseEntity<Map<String, String>>
    manejarAccesoNoAutorizado(
            AccesoNoAutorizadoException exception) {

        Map<String, String> respuesta =
                new HashMap<>();

        respuesta.put(
                "error",
                exception.getMessage()
        );

        return ResponseEntity
                .status(HttpStatus.FORBIDDEN)
                .body(respuesta);
    }

    @ExceptionHandler(SolicitudDuplicadaException.class)
    public ResponseEntity<Map<String, String>>
    manejarSolicitudDuplicada(
            SolicitudDuplicadaException exception) {

        Map<String, String> respuesta =
                new HashMap<>();

        respuesta.put(
                "error",
                exception.getMessage()
        );

        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(respuesta);
    }

    @ExceptionHandler({
            IntegranteGrupoDuplicadoException.class,
            RecuerdoGrupoDuplicadoException.class,
            SuscripcionYaActivaException.class
    })
    public ResponseEntity<Map<String, String>>
    manejarDuplicadoGrupo(RuntimeException exception) {

        Map<String, String> respuesta = new HashMap<>();

        respuesta.put(
                "error",
                exception.getMessage()
        );

        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(respuesta);
    }


    @ExceptionHandler(SolicitudProcesadaException.class)
    public ResponseEntity<Map<String, String>>
    manejarSolicitudProcesada(
            SolicitudProcesadaException exception) {

        Map<String, String> respuesta =
                new HashMap<>();

        respuesta.put(
                "error",
                exception.getMessage()
        );

        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(respuesta);
    }


    @ExceptionHandler(SolicitudNoEncontradaException.class)
    public ResponseEntity<Map<String, String>>
    manejarSolicitudNoEncontrada(
            SolicitudNoEncontradaException exception) {

        Map<String, String> respuesta =
                new HashMap<>();

        respuesta.put(
                "error",
                exception.getMessage()
        );

        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(respuesta);
    }

    @ExceptionHandler({
            ArchivoRequeridoException.class,
            FormatoArchivoNoPermitidoException.class,
            IllegalArgumentException.class
    })
    public ResponseEntity<Map<String, String>>
    manejarSolicitudIncorrecta(
            RuntimeException exception) {

        Map<String, String> respuesta =
                new HashMap<>();

        respuesta.put(
                "error",
                exception.getMessage()
        );

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(respuesta);
    }

    @ExceptionHandler(AlmacenamientoArchivoException.class)
    public ResponseEntity<Map<String, String>>
    manejarAlmacenamientoArchivo(
            AlmacenamientoArchivoException exception) {

        Map<String, String> respuesta =
                new HashMap<>();

        respuesta.put(
                "error",
                exception.getMessage()
        );

        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(respuesta);
    }

    @ExceptionHandler(ServicioIaNoDisponibleException.class)
    public ResponseEntity<Map<String, String>>
    manejarServicioIaNoDisponible(
            ServicioIaNoDisponibleException exception) {

        Map<String, String> respuesta =
                new HashMap<>();

        respuesta.put(
                "error",
                exception.getMessage()
        );

        return ResponseEntity
                .status(HttpStatus.SERVICE_UNAVAILABLE)
                .body(respuesta);
    }

    @ExceptionHandler(PlanNoEncontradoException.class)
    public ResponseEntity<Map<String, String>>
    manejarPlanNoEncontrado(
            PlanNoEncontradoException exception) {

        Map<String, String> respuesta =
                new HashMap<>();

        respuesta.put(
                "error",
                exception.getMessage()
        );

        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(respuesta);
    }

    @ExceptionHandler(PagoRechazadoException.class)
    public ResponseEntity<Map<String, String>> manejarPagoRechazado(
            PagoRechazadoException exception) {

        Map<String, String> respuesta = new HashMap<>();

        respuesta.put("error", exception.getMessage());

        return ResponseEntity
                .status(422)
                .body(respuesta);
    }

    @ExceptionHandler(ServicioPagoNoDisponibleException.class)
    public ResponseEntity<Map<String, String>>
    manejarServicioPagoNoDisponible(
            ServicioPagoNoDisponibleException exception) {

        Map<String, String> respuesta = new HashMap<>();

        respuesta.put("error", exception.getMessage());

        return ResponseEntity
                .status(HttpStatus.SERVICE_UNAVAILABLE)
                .body(respuesta);
    }




}

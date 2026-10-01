package pe.edu.upc.soulstoryapi.serviceimpl;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;
import pe.edu.upc.soulstoryapi.exception.ServicioIaNoDisponibleException;
import pe.edu.upc.soulstoryapi.service.OpenAiImageService;

import java.util.Base64;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
public class OpenAiImageServiceImpl implements OpenAiImageService {

    private final RestClient restClient;
    private final String apiKey;
    private final String modelo;

    public OpenAiImageServiceImpl(
            @Value("${app.ai.image.base-url}") String baseUrl,
            @Value("${app.ai.image.api-key}") String apiKey,
            @Value("${app.ai.image.model}") String modelo) {

        this.restClient = RestClient.builder()
                .baseUrl(baseUrl)
                .build();

        this.apiKey = apiKey;
        this.modelo = modelo;
    }

    @Override
    public byte[] generarImagen(String prompt) {

        if (apiKey == null || apiKey.isBlank()) {
            throw new ServicioIaNoDisponibleException(
                    "El servicio de generación de imágenes no está configurado"
            );
        }

        try {
            Object respuesta = restClient
                    .post()
                    .uri("/images/generations")
                    .header(
                            HttpHeaders.AUTHORIZATION,
                            "Bearer " + apiKey
                    )
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(crearSolicitud(prompt))
                    .retrieve()
                    .body(Object.class);

            return obtenerImagen(respuesta);
        } catch (ServicioIaNoDisponibleException exception) {
            throw exception;
        } catch (RestClientException | IllegalArgumentException exception) {
            throw new ServicioIaNoDisponibleException(
                    "El servicio de generación de imágenes no está disponible en este momento",
                    exception
            );
        }
    }

    private Map<String, Object> crearSolicitud(String prompt) {

        Map<String, Object> solicitud = new LinkedHashMap<>();

        solicitud.put("model", modelo);
        solicitud.put("prompt", prompt);
        solicitud.put("n", 1);
        solicitud.put("size", "1024x1024");
        solicitud.put("quality", "low");
        solicitud.put("output_format", "png");

        return solicitud;
    }

    private byte[] obtenerImagen(Object respuesta) {

        if (respuesta == null
                || !(respuesta instanceof Map<?, ?> cuerpo)
                || !(cuerpo.get("data") instanceof List<?> datos)
                || datos.isEmpty()
                || !(datos.get(0) instanceof Map<?, ?> imagen)
                || !(imagen.get("b64_json") instanceof String base64)
                || base64.isBlank()) {

            throw new ServicioIaNoDisponibleException(
                    "El servicio de generación de imágenes no devolvió una imagen válida"
            );
        }

        try {
            return Base64.getDecoder().decode(base64);
        } catch (IllegalArgumentException exception) {
            throw new ServicioIaNoDisponibleException(
                    "El servicio de generación de imágenes devolvió una imagen inválida",
                    exception
            );
        }
    }
}

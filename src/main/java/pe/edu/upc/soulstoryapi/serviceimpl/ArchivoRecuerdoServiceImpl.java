package pe.edu.upc.soulstoryapi.serviceimpl;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import pe.edu.upc.soulstoryapi.exception.AlmacenamientoArchivoException;
import pe.edu.upc.soulstoryapi.exception.ArchivoRequeridoException;
import pe.edu.upc.soulstoryapi.exception.FormatoArchivoNoPermitidoException;
import pe.edu.upc.soulstoryapi.service.ArchivoRecuerdoService;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

@Service
public class ArchivoRecuerdoServiceImpl implements ArchivoRecuerdoService {

    private static final Map<String, Set<String>> TIPOS_AUDIO =
            Map.of(
                    "mp3", Set.of("audio/mpeg"),
                    "wav", Set.of("audio/wav", "audio/x-wav"),
                    "m4a", Set.of("audio/mp4", "audio/x-m4a")
            );

    private static final Map<String, Set<String>> TIPOS_IMAGEN =
            Map.of(
                    "jpg", Set.of("image/jpeg"),
                    "jpeg", Set.of("image/jpeg"),
                    "png", Set.of("image/png"),
                    "webp", Set.of("image/webp")
            );

    private final Path directorioRaiz;

    public ArchivoRecuerdoServiceImpl(
            @Value("${app.storage.recuerdos}")
            String rutaRecuerdos) {

        this.directorioRaiz = Paths
                .get(rutaRecuerdos)
                .toAbsolutePath()
                .normalize();

        crearDirectorios();
    }

    @Override
    public String guardarAudio(MultipartFile archivo) {

        return guardarArchivo(
                archivo,
                "audio",
                TIPOS_AUDIO,
                "Debe adjuntar un archivo de audio",
                "Formato de audio no permitido. Formatos compatibles: MP3, WAV y M4A."
        );
    }

    @Override
    public String guardarImagen(MultipartFile archivo) {

        return guardarArchivo(
                archivo,
                "imagen",
                TIPOS_IMAGEN,
                "Debe adjuntar una imagen",
                "Formato de imagen no permitido. Formatos compatibles: JPG, JPEG, PNG y WEBP."
        );
    }

    @Override
    public String guardarImagenIa(byte[] contenido) {

        if (contenido == null || contenido.length == 0) {
            throw new AlmacenamientoArchivoException(
                    "No se recibió una imagen generada válida"
            );
        }

        String nombreSeguro = UUID.randomUUID() + ".png";
        Path destino = crearDestinoSeguro(
                "ia",
                nombreSeguro
        );

        try {
            Files.createDirectories(destino.getParent());
            Files.write(destino, contenido);
        } catch (IOException exception) {
            throw new AlmacenamientoArchivoException(
                    "No se pudo guardar la imagen generada",
                    exception
            );
        }

        return "ia/" + nombreSeguro;
    }

    @Override
    public Resource obtenerArchivo(
            String carpeta,
            String rutaRelativa) {

        if (rutaRelativa == null || rutaRelativa.isBlank()) {
            throw new AlmacenamientoArchivoException(
                    "La ruta del archivo del recuerdo no es válida"
            );
        }

        Path directorioTipo = directorioRaiz
                .resolve(carpeta)
                .normalize();

        Path archivo = directorioRaiz
                .resolve(rutaRelativa)
                .normalize();

        if (!directorioTipo.startsWith(directorioRaiz)
                || !archivo.startsWith(directorioTipo)
                || !Files.isRegularFile(archivo)) {

            throw new AlmacenamientoArchivoException(
                    "No se pudo encontrar el archivo del recuerdo"
            );
        }

        try {
            Resource recurso = new UrlResource(archivo.toUri());

            if (!recurso.exists() || !recurso.isReadable()) {
                throw new AlmacenamientoArchivoException(
                        "No se pudo leer el archivo del recuerdo"
                );
            }

            return recurso;
        } catch (MalformedURLException exception) {
            throw new AlmacenamientoArchivoException(
                    "No se pudo acceder al archivo del recuerdo",
                    exception
            );
        }
    }

    private String guardarArchivo(
            MultipartFile archivo,
            String carpeta,
            Map<String, Set<String>> tiposPermitidos,
            String mensajeArchivoRequerido,
            String mensajeFormatoNoPermitido) {

        if (archivo == null || archivo.isEmpty()) {
            throw new ArchivoRequeridoException(
                    mensajeArchivoRequerido
            );
        }

        String extension = obtenerExtension(
                archivo.getOriginalFilename()
        );

        String tipoContenido = normalizarTipoContenido(
                archivo.getContentType()
        );

        if (!tiposPermitidos.containsKey(extension)
                || !tiposPermitidos
                .get(extension)
                .contains(tipoContenido)) {

            throw new FormatoArchivoNoPermitidoException(
                    mensajeFormatoNoPermitido
            );
        }

        String nombreSeguro = UUID.randomUUID() + "." + extension;

        Path destino = crearDestinoSeguro(
                carpeta,
                nombreSeguro
        );

        try (InputStream entrada = archivo.getInputStream()) {
            Files.createDirectories(destino.getParent());
            Files.copy(
                    entrada,
                    destino,
                    StandardCopyOption.REPLACE_EXISTING
            );
        } catch (IOException exception) {
            throw new AlmacenamientoArchivoException(
                    "No se pudo guardar el archivo del recuerdo",
                    exception
            );
        }

        return carpeta + "/" + nombreSeguro;
    }

    private void crearDirectorios() {

        try {
            Files.createDirectories(
                    directorioRaiz.resolve("audio")
            );

            Files.createDirectories(
                    directorioRaiz.resolve("imagen")
            );

            Files.createDirectories(
                    directorioRaiz.resolve("ia")
            );
        } catch (IOException exception) {
            throw new AlmacenamientoArchivoException(
                    "No se pudo preparar el almacenamiento de recuerdos",
                    exception
            );
        }
    }

    private Path crearDestinoSeguro(
            String carpeta,
            String nombreArchivo) {

        Path directorioDestino = directorioRaiz
                .resolve(carpeta)
                .normalize();

        Path destino = directorioDestino
                .resolve(nombreArchivo)
                .normalize();

        if (!directorioDestino.startsWith(directorioRaiz)
                || !destino.startsWith(directorioDestino)) {

            throw new AlmacenamientoArchivoException(
                    "La ruta del archivo no es válida"
            );
        }

        return destino;
    }

    private String obtenerExtension(
            String nombreOriginal) {

        if (nombreOriginal == null) {
            return "";
        }

        String nombreArchivo = Paths
                .get(nombreOriginal)
                .getFileName()
                .toString();

        int ultimoPunto = nombreArchivo.lastIndexOf('.');

        if (ultimoPunto < 0
                || ultimoPunto == nombreArchivo.length() - 1) {

            return "";
        }

        return nombreArchivo
                .substring(ultimoPunto + 1)
                .toLowerCase(Locale.ROOT);
    }

    private String normalizarTipoContenido(
            String tipoContenido) {

        if (tipoContenido == null || tipoContenido.isBlank()) {
            return "";
        }

        int separadorParametros = tipoContenido.indexOf(';');

        String tipoSinParametros = separadorParametros >= 0
                ? tipoContenido.substring(0, separadorParametros)
                : tipoContenido;

        return tipoSinParametros
                .trim()
                .toLowerCase(Locale.ROOT);
    }
}

package pe.edu.upc.soulstoryapi.service;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

public interface ArchivoRecuerdoService {

    public String guardarAudio(MultipartFile archivo);

    public String guardarImagen(MultipartFile archivo);

    public String guardarImagenIa(byte[] contenido);

    public Resource obtenerArchivo(String carpeta, String rutaRelativa);

}

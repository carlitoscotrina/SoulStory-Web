package pe.edu.upc.soulstoryapi.service;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;
import pe.edu.upc.soulstoryapi.dto.ActualizarRecuerdoDTO;
import pe.edu.upc.soulstoryapi.dto.CrearRecuerdoTextoDTO;
import pe.edu.upc.soulstoryapi.dto.RecuerdoRespuestaDTO;
import pe.edu.upc.soulstoryapi.entity.Recuerdo;

import java.util.List;

public interface RecuerdoService {

    // HU-11
    public RecuerdoRespuestaDTO crearRecuerdoTexto(Long idAdultoMayor, CrearRecuerdoTextoDTO dto);

    // HU-12
    public RecuerdoRespuestaDTO crearRecuerdoAudio(
            Long idAdultoMayor,
            String tituloRecuerdo,
            MultipartFile archivo
    );

    // HU-13
    public RecuerdoRespuestaDTO crearRecuerdoImagen(
            Long idAdultoMayor,
            String tituloRecuerdo,
            MultipartFile archivo
    );

    // HU-14
    public RecuerdoRespuestaDTO crearRecuerdoTextoComoCuidador(
            Long idCuidador,
            Long idAdultoMayor,
            CrearRecuerdoTextoDTO dto
    );

    public RecuerdoRespuestaDTO crearRecuerdoAudioComoCuidador(
            Long idCuidador,
            Long idAdultoMayor,
            String tituloRecuerdo,
            MultipartFile archivo
    );

    public RecuerdoRespuestaDTO crearRecuerdoImagenComoCuidador(
            Long idCuidador,
            Long idAdultoMayor,
            String tituloRecuerdo,
            MultipartFile archivo
    );

    // HU-17
    public void comprobarAdultoMayor(Long idAdultoMayor);

    public RecuerdoRespuestaDTO crearRecuerdoImagenIa(
            Long idAdultoMayor,
            Long idAsignacion,
            String tituloRecuerdo,
            String rutaRelativa
    );

    public List<RecuerdoRespuestaDTO> listarRecuerdosAdultoMayor(Long idAdultoMayor);

    public RecuerdoRespuestaDTO obtenerRecuerdo(Long idRecuerdo);

    // HU-21 y HU-22
    public Recuerdo obtenerEntidadRecuerdo(Long idRecuerdo);

    // HU-21 y HU-22
    public RecuerdoRespuestaDTO convertirRespuesta(Recuerdo recuerdo);

    public Resource obtenerArchivoRecuerdo(Long idRecuerdo);

    // HU-15
    public RecuerdoRespuestaDTO actualizarRecuerdoComoAdultoMayor(
            Long idAdultoMayor,
            Long idRecuerdo,
            ActualizarRecuerdoDTO dto
    );

    public RecuerdoRespuestaDTO actualizarRecuerdoComoCuidador(
            Long idCuidador,
            Long idRecuerdo,
            ActualizarRecuerdoDTO dto
    );

    // HU-16
    public RecuerdoRespuestaDTO alternarFavoritoComoAdultoMayor(Long idAdultoMayor, Long idRecuerdo);

    public RecuerdoRespuestaDTO alternarFavoritoComoCuidador(Long idCuidador, Long idRecuerdo);

    public List<RecuerdoRespuestaDTO> listarFavoritosAdultoMayor(Long idAdultoMayor);

    public List<RecuerdoRespuestaDTO> listarFavoritosComoCuidador(Long idCuidador, Long idAdultoMayor);

}

package pe.edu.upc.soulstoryapi.service;

import org.springframework.core.io.Resource;
import pe.edu.upc.soulstoryapi.dto.GenerarImagenIaDTO;
import pe.edu.upc.soulstoryapi.dto.ImagenIaRespuestaDTO;
import pe.edu.upc.soulstoryapi.dto.RecuerdoRespuestaDTO;

public interface ImagenIaService {

    public ImagenIaRespuestaDTO generarImagen(GenerarImagenIaDTO dto);

    public Resource obtenerPreview(String token);

    public RecuerdoRespuestaDTO confirmarComoAdultoMayor(Long idAdultoMayor, String token);

    public RecuerdoRespuestaDTO confirmarComoCuidador(Long idCuidador, Long idAdultoMayor, String token);

}

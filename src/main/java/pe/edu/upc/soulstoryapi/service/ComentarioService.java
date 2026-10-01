package pe.edu.upc.soulstoryapi.service;

import pe.edu.upc.soulstoryapi.dto.ComentarioRespuestaDTO;
import pe.edu.upc.soulstoryapi.dto.CrearComentarioDTO;

import java.util.List;

public interface ComentarioService {

    // HU-23
    public ComentarioRespuestaDTO crearComentario(Long idUsuario, Long idRecuerdo, CrearComentarioDTO dto);

    // HU-24
    public List<ComentarioRespuestaDTO> listarComentarios(Long idUsuario, Long idRecuerdo);

}

package pe.edu.upc.soulstoryapi.service;

import pe.edu.upc.soulstoryapi.dto.CrearGrupoDTO;
import pe.edu.upc.soulstoryapi.dto.GrupoRespuestaDTO;

import java.util.List;

public interface GrupoService {

    public GrupoRespuestaDTO crearGrupo(Long idAdultoMayor, CrearGrupoDTO dto);

    public List<GrupoRespuestaDTO> listarGrupos(Long idAdultoMayor);

    public GrupoRespuestaDTO obtenerGrupo(Long idAdultoMayor, Long idGrupo);

}

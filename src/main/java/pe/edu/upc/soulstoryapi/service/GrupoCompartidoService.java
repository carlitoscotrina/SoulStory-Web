package pe.edu.upc.soulstoryapi.service;

import pe.edu.upc.soulstoryapi.dto.AdultoMayorRespuestaDTO;
import pe.edu.upc.soulstoryapi.dto.RecuerdoRespuestaDTO;
import pe.edu.upc.soulstoryapi.entity.Grupo;

import java.util.List;

public interface GrupoCompartidoService {

    // HU-20 y HU-21: el propietario del grupo es el administrador temporal.
    public Grupo obtenerGrupoAdministrable(Long idAdministrador, Long idGrupo);

    public AdultoMayorRespuestaDTO agregarIntegrante(Long idAdministrador, Long idGrupo, Long idAdultoMayor);

    public List<AdultoMayorRespuestaDTO> listarIntegrantes(Long idAdministrador, Long idGrupo);

    public RecuerdoRespuestaDTO agregarRecuerdo(Long idAdministrador, Long idGrupo, Long idRecuerdo);

    public List<RecuerdoRespuestaDTO> listarRecuerdosAdministracion(Long idAdministrador, Long idGrupo);

    public List<RecuerdoRespuestaDTO> listarRecuerdosCompartidos(Long idAdultoMayor, Long idGrupo);

}

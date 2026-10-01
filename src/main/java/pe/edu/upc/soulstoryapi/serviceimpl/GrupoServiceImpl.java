package pe.edu.upc.soulstoryapi.serviceimpl;

import org.springframework.stereotype.Service;
import pe.edu.upc.soulstoryapi.dto.CrearGrupoDTO;
import pe.edu.upc.soulstoryapi.dto.GrupoRespuestaDTO;
import pe.edu.upc.soulstoryapi.entity.Grupo;
import pe.edu.upc.soulstoryapi.exception.AccesoNoAutorizadoException;
import pe.edu.upc.soulstoryapi.exception.AdultoMayorNoEncontradoException;
import pe.edu.upc.soulstoryapi.exception.GrupoNoEncontradoException;
import pe.edu.upc.soulstoryapi.repository.AdultoMayorRepository;
import pe.edu.upc.soulstoryapi.repository.GrupoRepository;
import pe.edu.upc.soulstoryapi.service.GrupoService;

import java.util.List;

@Service
public class GrupoServiceImpl implements GrupoService {

    private final GrupoRepository grupoRepository;
    private final AdultoMayorRepository adultoMayorRepository;

    public GrupoServiceImpl(
            GrupoRepository grupoRepository,
            AdultoMayorRepository adultoMayorRepository) {

        this.grupoRepository = grupoRepository;
        this.adultoMayorRepository = adultoMayorRepository;
    }

    @Override
    public GrupoRespuestaDTO crearGrupo(
            Long idAdultoMayor,
            CrearGrupoDTO dto) {

        validarAdultoMayor(idAdultoMayor);

        Grupo grupo = new Grupo();

        grupo.setNombreGrupo(dto.getNombreGrupo());
        grupo.setDescripcion(dto.getDescripcion());
        grupo.setIdAdultoMayor(idAdultoMayor);

        return convertirDTO(
                grupoRepository.save(grupo)
        );
    }

    @Override
    public List<GrupoRespuestaDTO> listarGrupos(
            Long idAdultoMayor) {

        validarAdultoMayor(idAdultoMayor);

        return grupoRepository
                .findByIdAdultoMayorOrderByIdGrupoDesc(
                        idAdultoMayor
                )
                .stream()
                .map(this::convertirDTO)
                .toList();
    }

    @Override
    public GrupoRespuestaDTO obtenerGrupo(
            Long idAdultoMayor,
            Long idGrupo) {

        validarAdultoMayor(idAdultoMayor);

        Grupo grupo = grupoRepository
                .findById(idGrupo)
                .orElseThrow(() ->
                        new GrupoNoEncontradoException(
                                "El grupo no existe"
                        )
                );

        if (!grupo.getIdAdultoMayor().equals(idAdultoMayor)) {
            throw new AccesoNoAutorizadoException(
                    "El grupo no pertenece a este adulto mayor"
            );
        }

        return convertirDTO(grupo);
    }

    private void validarAdultoMayor(
            Long idAdultoMayor) {

        adultoMayorRepository
                .findById(idAdultoMayor)
                .orElseThrow(() ->
                        new AdultoMayorNoEncontradoException(
                                "El adulto mayor no existe"
                        )
                );
    }

    private GrupoRespuestaDTO convertirDTO(Grupo grupo) {

        return new GrupoRespuestaDTO(
                grupo.getIdGrupo(),
                grupo.getNombreGrupo(),
                grupo.getDescripcion(),
                grupo.getIdAdultoMayor()
        );
    }
}

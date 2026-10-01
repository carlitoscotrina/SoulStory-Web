package pe.edu.upc.soulstoryapi.serviceimpl;

import org.springframework.stereotype.Service;
import pe.edu.upc.soulstoryapi.dto.AdultoMayorRespuestaDTO;
import pe.edu.upc.soulstoryapi.dto.RecuerdoRespuestaDTO;
import pe.edu.upc.soulstoryapi.entity.AdultoMayor;
import pe.edu.upc.soulstoryapi.entity.AdultoMayorGrupo;
import pe.edu.upc.soulstoryapi.entity.Grupo;
import pe.edu.upc.soulstoryapi.entity.Recuerdo;
import pe.edu.upc.soulstoryapi.entity.RecuerdoGrupo;
import pe.edu.upc.soulstoryapi.entity.Usuario;
import pe.edu.upc.soulstoryapi.exception.AccesoNoAutorizadoException;
import pe.edu.upc.soulstoryapi.exception.AdultoMayorNoEncontradoException;
import pe.edu.upc.soulstoryapi.exception.GrupoNoEncontradoException;
import pe.edu.upc.soulstoryapi.exception.IntegranteGrupoDuplicadoException;
import pe.edu.upc.soulstoryapi.exception.RecuerdoGrupoDuplicadoException;
import pe.edu.upc.soulstoryapi.exception.UsuarioNoEncontradoException;
import pe.edu.upc.soulstoryapi.repository.AdultoMayorGrupoRepository;
import pe.edu.upc.soulstoryapi.repository.AdultoMayorRepository;
import pe.edu.upc.soulstoryapi.repository.GrupoRepository;
import pe.edu.upc.soulstoryapi.repository.RecuerdoGrupoRepository;
import pe.edu.upc.soulstoryapi.repository.UsuarioRepository;
import pe.edu.upc.soulstoryapi.service.GrupoCompartidoService;
import pe.edu.upc.soulstoryapi.service.RecuerdoService;

import java.util.List;

@Service
public class GrupoCompartidoServiceImpl implements GrupoCompartidoService {

    private final GrupoRepository grupoRepository;
    private final AdultoMayorRepository adultoMayorRepository;
    private final UsuarioRepository usuarioRepository;
    private final AdultoMayorGrupoRepository adultoMayorGrupoRepository;
    private final RecuerdoGrupoRepository recuerdoGrupoRepository;
    private final RecuerdoService recuerdoService;

    public GrupoCompartidoServiceImpl(
            GrupoRepository grupoRepository,
            AdultoMayorRepository adultoMayorRepository,
            UsuarioRepository usuarioRepository,
            AdultoMayorGrupoRepository adultoMayorGrupoRepository,
            RecuerdoGrupoRepository recuerdoGrupoRepository,
            RecuerdoService recuerdoService) {

        this.grupoRepository = grupoRepository;
        this.adultoMayorRepository = adultoMayorRepository;
        this.usuarioRepository = usuarioRepository;
        this.adultoMayorGrupoRepository = adultoMayorGrupoRepository;
        this.recuerdoGrupoRepository = recuerdoGrupoRepository;
        this.recuerdoService = recuerdoService;
    }

    // HU-20 y HU-21: el propietario del grupo es el administrador temporal.
    @Override
    public Grupo obtenerGrupoAdministrable(
            Long idAdministrador,
            Long idGrupo) {

        validarAdultoMayor(idAdministrador);

        Grupo grupo = buscarGrupo(idGrupo);

        if (!grupo.getIdAdultoMayor().equals(idAdministrador)) {
            throw new AccesoNoAutorizadoException(
                    "El adulto mayor no administra este grupo"
            );
        }

        return grupo;
    }

    @Override
    public AdultoMayorRespuestaDTO agregarIntegrante(
            Long idAdministrador,
            Long idGrupo,
            Long idAdultoMayor) {

        obtenerGrupoAdministrable(idAdministrador, idGrupo);

        AdultoMayor adultoMayor = buscarAdultoMayor(idAdultoMayor);

        if (adultoMayorGrupoRepository
                .existsByIdAdultoMayorAndIdGrupo(
                        idAdultoMayor,
                        idGrupo
                )) {
            throw new IntegranteGrupoDuplicadoException(
                    "El adulto mayor ya pertenece al grupo"
            );
        }

        AdultoMayorGrupo integrante = new AdultoMayorGrupo();
        integrante.setIdAdultoMayor(idAdultoMayor);
        integrante.setIdGrupo(idGrupo);

        adultoMayorGrupoRepository.save(integrante);

        return convertirAdultoMayorDTO(adultoMayor);
    }

    @Override
    public List<AdultoMayorRespuestaDTO> listarIntegrantes(
            Long idAdministrador,
            Long idGrupo) {

        obtenerGrupoAdministrable(idAdministrador, idGrupo);

        return adultoMayorGrupoRepository
                .findByIdGrupo(idGrupo)
                .stream()
                .map(AdultoMayorGrupo::getIdAdultoMayor)
                .map(this::buscarAdultoMayor)
                .map(this::convertirAdultoMayorDTO)
                .toList();
    }

    @Override
    public RecuerdoRespuestaDTO agregarRecuerdo(
            Long idAdministrador,
            Long idGrupo,
            Long idRecuerdo) {

        obtenerGrupoAdministrable(idAdministrador, idGrupo);

        Recuerdo recuerdo = recuerdoService
                .obtenerEntidadRecuerdo(idRecuerdo);

        // Regla tecnica temporal: solo se comparten recuerdos de integrantes.
        if (!adultoMayorGrupoRepository
                .existsByIdAdultoMayorAndIdGrupo(
                        recuerdo.getIdAdultoMayor(),
                        idGrupo
                )) {
            throw new AccesoNoAutorizadoException(
                    "El adulto mayor propietario del recuerdo no pertenece al grupo"
            );
        }

        if (recuerdoGrupoRepository
                .existsByIdRecuerdoAndIdGrupo(
                        idRecuerdo,
                        idGrupo
                )) {
            throw new RecuerdoGrupoDuplicadoException(
                    "El recuerdo ya pertenece al grupo"
            );
        }

        RecuerdoGrupo recuerdoGrupo = new RecuerdoGrupo();
        recuerdoGrupo.setIdRecuerdo(idRecuerdo);
        recuerdoGrupo.setIdGrupo(idGrupo);

        recuerdoGrupoRepository.save(recuerdoGrupo);

        return recuerdoService.convertirRespuesta(recuerdo);
    }

    @Override
    public List<RecuerdoRespuestaDTO>
    listarRecuerdosAdministracion(
            Long idAdministrador,
            Long idGrupo) {

        obtenerGrupoAdministrable(idAdministrador, idGrupo);

        return listarRecuerdosGrupo(idGrupo);
    }

    @Override
    public List<RecuerdoRespuestaDTO>
    listarRecuerdosCompartidos(
            Long idAdultoMayor,
            Long idGrupo) {

        validarAdultoMayor(idAdultoMayor);
        buscarGrupo(idGrupo);

        if (!adultoMayorGrupoRepository
                .existsByIdAdultoMayorAndIdGrupo(
                        idAdultoMayor,
                        idGrupo
                )) {
            throw new AccesoNoAutorizadoException(
                    "El adulto mayor no pertenece a este grupo"
            );
        }

        return listarRecuerdosGrupo(idGrupo);
    }

    private List<RecuerdoRespuestaDTO>
    listarRecuerdosGrupo(Long idGrupo) {

        return recuerdoGrupoRepository
                .findByIdGrupo(idGrupo)
                .stream()
                .map(RecuerdoGrupo::getIdRecuerdo)
                .map(recuerdoService::obtenerEntidadRecuerdo)
                .map(recuerdoService::convertirRespuesta)
                .toList();
    }

    private void validarAdultoMayor(Long idAdultoMayor) {

        buscarAdultoMayor(idAdultoMayor);
    }

    private AdultoMayor buscarAdultoMayor(Long idAdultoMayor) {

        return adultoMayorRepository
                .findById(idAdultoMayor)
                .orElseThrow(() ->
                        new AdultoMayorNoEncontradoException(
                                "El adulto mayor no existe"
                        )
                );
    }

    private Grupo buscarGrupo(Long idGrupo) {

        return grupoRepository
                .findById(idGrupo)
                .orElseThrow(() ->
                        new GrupoNoEncontradoException(
                                "El grupo no existe"
                        )
                );
    }

    private AdultoMayorRespuestaDTO convertirAdultoMayorDTO(
            AdultoMayor adultoMayor) {

        Usuario usuario = usuarioRepository
                .findById(adultoMayor.getIdUsuario())
                .orElseThrow(() ->
                        new UsuarioNoEncontradoException(
                                "El usuario no existe"
                        )
                );

        return new AdultoMayorRespuestaDTO(
                adultoMayor.getIdAdultoMayor(),
                usuario.getNombreCompleto(),
                usuario.getEmail(),
                adultoMayor.getFechaNacimiento(),
                adultoMayor.getDireccion()
        );
    }
}

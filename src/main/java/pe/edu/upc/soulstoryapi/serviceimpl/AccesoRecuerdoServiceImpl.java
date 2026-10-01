package pe.edu.upc.soulstoryapi.serviceimpl;

import org.springframework.stereotype.Service;
import pe.edu.upc.soulstoryapi.entity.AdultoMayor;
import pe.edu.upc.soulstoryapi.entity.AdultoMayorGrupo;
import pe.edu.upc.soulstoryapi.entity.Cuidador;
import pe.edu.upc.soulstoryapi.entity.Recuerdo;
import pe.edu.upc.soulstoryapi.exception.AccesoNoAutorizadoException;
import pe.edu.upc.soulstoryapi.exception.RecuerdoNoEncontradoException;
import pe.edu.upc.soulstoryapi.exception.UsuarioNoEncontradoException;
import pe.edu.upc.soulstoryapi.repository.AdultoMayorGrupoRepository;
import pe.edu.upc.soulstoryapi.repository.AdultoMayorRepository;
import pe.edu.upc.soulstoryapi.repository.AsignacionRepository;
import pe.edu.upc.soulstoryapi.repository.CuidadorRepository;
import pe.edu.upc.soulstoryapi.repository.RecuerdoGrupoRepository;
import pe.edu.upc.soulstoryapi.repository.RecuerdoRepository;
import pe.edu.upc.soulstoryapi.repository.UsuarioRepository;
import pe.edu.upc.soulstoryapi.service.AccesoRecuerdoService;

import java.util.Optional;

@Service
public class AccesoRecuerdoServiceImpl implements AccesoRecuerdoService {

    private final UsuarioRepository usuarioRepository;
    private final RecuerdoRepository recuerdoRepository;
    private final AdultoMayorRepository adultoMayorRepository;
    private final CuidadorRepository cuidadorRepository;
    private final AsignacionRepository asignacionRepository;
    private final AdultoMayorGrupoRepository adultoMayorGrupoRepository;
    private final RecuerdoGrupoRepository recuerdoGrupoRepository;

    public AccesoRecuerdoServiceImpl(
            UsuarioRepository usuarioRepository,
            RecuerdoRepository recuerdoRepository,
            AdultoMayorRepository adultoMayorRepository,
            CuidadorRepository cuidadorRepository,
            AsignacionRepository asignacionRepository,
            AdultoMayorGrupoRepository adultoMayorGrupoRepository,
            RecuerdoGrupoRepository recuerdoGrupoRepository) {

        this.usuarioRepository = usuarioRepository;
        this.recuerdoRepository = recuerdoRepository;
        this.adultoMayorRepository = adultoMayorRepository;
        this.cuidadorRepository = cuidadorRepository;
        this.asignacionRepository = asignacionRepository;
        this.adultoMayorGrupoRepository = adultoMayorGrupoRepository;
        this.recuerdoGrupoRepository = recuerdoGrupoRepository;
    }

    @Override
    public Recuerdo validarAccesoUsuarioARecuerdo(
            Long idUsuario,
            Long idRecuerdo) {

        usuarioRepository
                .findById(idUsuario)
                .orElseThrow(() ->
                        new UsuarioNoEncontradoException(
                                "El usuario no existe"
                        )
                );

        Recuerdo recuerdo = recuerdoRepository
                .findById(idRecuerdo)
                .orElseThrow(() ->
                        new RecuerdoNoEncontradoException(
                                "El recuerdo no existe"
                        )
                );

        Optional<AdultoMayor> adultoMayor = adultoMayorRepository
                .findByIdUsuario(idUsuario);

        if (adultoMayor.isPresent()
                && adultoMayor.get().getIdAdultoMayor()
                .equals(recuerdo.getIdAdultoMayor())) {

            return recuerdo;
        }

        Optional<Cuidador> cuidador = cuidadorRepository
                .findByIdUsuario(idUsuario);

        if (cuidador.isPresent()
                && asignacionRepository
                .findFirstByIdCuidadorAndIdAdultoMayorAndEstadoIgnoreCaseOrderByFechaInicioDesc(
                        cuidador.get().getIdCuidador(),
                        recuerdo.getIdAdultoMayor(),
                        "ACTIVA"
                )
                .isPresent()) {

            return recuerdo;
        }

        if (adultoMayor.isPresent()
                && tieneAccesoGrupal(
                        adultoMayor.get().getIdAdultoMayor(),
                        recuerdo.getIdRecuerdo()
                )) {

            return recuerdo;
        }

        throw new AccesoNoAutorizadoException(
                "El usuario no tiene acceso a este recuerdo"
        );
    }

    private boolean tieneAccesoGrupal(
            Long idAdultoMayor,
            Long idRecuerdo) {

        return adultoMayorGrupoRepository
                .findByIdAdultoMayor(idAdultoMayor)
                .stream()
                .map(AdultoMayorGrupo::getIdGrupo)
                .anyMatch(idGrupo -> recuerdoGrupoRepository
                        .existsByIdRecuerdoAndIdGrupo(
                                idRecuerdo,
                                idGrupo
                        )
                );
    }
}

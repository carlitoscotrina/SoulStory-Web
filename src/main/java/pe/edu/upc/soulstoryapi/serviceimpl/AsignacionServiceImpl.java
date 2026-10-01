package pe.edu.upc.soulstoryapi.serviceimpl;

import org.springframework.stereotype.Service;
import pe.edu.upc.soulstoryapi.dto.AdultoMayorRespuestaDTO;
import pe.edu.upc.soulstoryapi.dto.CuidadorRespuestaDTO;
import pe.edu.upc.soulstoryapi.entity.AdultoMayor;
import pe.edu.upc.soulstoryapi.entity.Asignacion;
import pe.edu.upc.soulstoryapi.entity.Cuidador;
import pe.edu.upc.soulstoryapi.entity.Usuario;
import pe.edu.upc.soulstoryapi.exception.AccesoNoAutorizadoException;
import pe.edu.upc.soulstoryapi.exception.AdultoMayorNoEncontradoException;
import pe.edu.upc.soulstoryapi.exception.AsignacionNoEncontradaException;
import pe.edu.upc.soulstoryapi.exception.CuidadorNoEncontradoException;
import pe.edu.upc.soulstoryapi.repository.AdultoMayorRepository;
import pe.edu.upc.soulstoryapi.repository.AsignacionRepository;
import pe.edu.upc.soulstoryapi.repository.CuidadorRepository;
import pe.edu.upc.soulstoryapi.repository.UsuarioRepository;
import pe.edu.upc.soulstoryapi.service.AsignacionService;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class AsignacionServiceImpl implements AsignacionService {

    private final AsignacionRepository asignacionRepository;
    private final AdultoMayorRepository adultoMayorRepository;
    private final CuidadorRepository cuidadorRepository;
    private final UsuarioRepository usuarioRepository;

    public AsignacionServiceImpl(
            AsignacionRepository asignacionRepository,
            AdultoMayorRepository adultoMayorRepository,
            CuidadorRepository cuidadorRepository,
            UsuarioRepository usuarioRepository) {

        this.asignacionRepository =
                asignacionRepository;

        this.adultoMayorRepository =
                adultoMayorRepository;

        this.cuidadorRepository =
                cuidadorRepository;

        this.usuarioRepository =
                usuarioRepository;
    }

    // ============================================
    // HU-05
    // ============================================

    @Override
    public CuidadorRespuestaDTO obtenerCuidadorAsignado(
            Long idAdultoMayor) {

        adultoMayorRepository
                .findById(idAdultoMayor)
                .orElseThrow(() ->
                        new AdultoMayorNoEncontradoException(
                                "El adulto mayor no existe"
                        )
                );

        Asignacion asignacion =
                asignacionRepository
                        .findFirstByIdAdultoMayorOrderByFechaInicioDesc(
                                idAdultoMayor
                        )
                        .orElseThrow(() ->
                                new AsignacionNoEncontradaException(
                                        "Actualmente no tiene un cuidador asignado"
                                )
                        );

        Cuidador cuidador =
                cuidadorRepository
                        .findById(
                                asignacion.getIdCuidador()
                        )
                        .orElseThrow(() ->
                                new CuidadorNoEncontradoException(
                                        "El cuidador no existe"
                                )
                        );

        Usuario usuario =
                usuarioRepository
                        .findById(
                                cuidador.getIdUsuario()
                        )
                        .orElseThrow(() ->
                                new CuidadorNoEncontradoException(
                                        "No existe el usuario asociado al cuidador"
                                )
                        );

        return crearCuidadorDTO(
                cuidador,
                usuario
        );
    }

    // ============================================
    // HU-06
    // ============================================

    @Override
    public List<AdultoMayorRespuestaDTO>
    listarAdultosAsignados(Long idCuidador) {

        cuidadorRepository
                .findById(idCuidador)
                .orElseThrow(() ->
                        new CuidadorNoEncontradoException(
                                "El cuidador no existe"
                        )
                );

        List<Asignacion> asignaciones =
                asignacionRepository
                        .findByIdCuidador(idCuidador);

        List<AdultoMayorRespuestaDTO> respuesta =
                new ArrayList<>();

        Set<Long> idsAgregados =
                new HashSet<>();

        for (Asignacion asignacion : asignaciones) {

            Long idAdultoMayor =
                    asignacion.getIdAdultoMayor();

            if (idsAgregados.contains(
                    idAdultoMayor)) {

                continue;
            }

            AdultoMayor adultoMayor =
                    adultoMayorRepository
                            .findById(idAdultoMayor)
                            .orElseThrow(() ->
                                    new AdultoMayorNoEncontradoException(
                                            "El adulto mayor no existe"
                                    )
                            );

            Usuario usuario =
                    usuarioRepository
                            .findById(
                                    adultoMayor.getIdUsuario()
                            )
                            .orElseThrow(() ->
                                    new AdultoMayorNoEncontradoException(
                                            "No existe el usuario asociado al adulto mayor"
                                    )
                            );

            respuesta.add(
                    crearAdultoMayorDTO(
                            adultoMayor,
                            usuario
                    )
            );

            idsAgregados.add(
                    idAdultoMayor
            );
        }

        return respuesta;
    }

    // ============================================
    // HU-07
    // ============================================

    @Override
    public AdultoMayorRespuestaDTO
    consultarAdultoAsignado(
            Long idCuidador,
            Long idAdultoMayor) {

        cuidadorRepository
                .findById(idCuidador)
                .orElseThrow(() ->
                        new CuidadorNoEncontradoException(
                                "El cuidador no existe"
                        )
                );

        AdultoMayor adultoMayor =
                adultoMayorRepository
                        .findById(idAdultoMayor)
                        .orElseThrow(() ->
                                new AdultoMayorNoEncontradoException(
                                        "El adulto mayor no existe"
                                )
                        );

        boolean estaAsignado =
                asignacionRepository
                        .existsByIdCuidadorAndIdAdultoMayor(
                                idCuidador,
                                idAdultoMayor
                        );

        if (!estaAsignado) {

            throw new AccesoNoAutorizadoException(
                    "El adulto mayor no está asignado al cuidador"
            );
        }

        Usuario usuario =
                usuarioRepository
                        .findById(
                                adultoMayor.getIdUsuario()
                        )
                        .orElseThrow(() ->
                                new AdultoMayorNoEncontradoException(
                                        "No existe el usuario asociado al adulto mayor"
                                )
                        );

        return crearAdultoMayorDTO(
                adultoMayor,
                usuario
        );
    }

    // ============================================
    // AUTORIZACION TEMPORAL PARA HU-14 A HU-16
    // ============================================

    @Override
    public Asignacion obtenerAsignacionAutorizada(
            Long idCuidador,
            Long idAdultoMayor) {

        cuidadorRepository
                .findById(idCuidador)
                .orElseThrow(() ->
                        new CuidadorNoEncontradoException(
                                "El cuidador no existe"
                        )
                );

        adultoMayorRepository
                .findById(idAdultoMayor)
                .orElseThrow(() ->
                        new AdultoMayorNoEncontradoException(
                                "El adulto mayor no existe"
                        )
                );

        return asignacionRepository
                .findFirstByIdCuidadorAndIdAdultoMayorAndEstadoIgnoreCaseOrderByFechaInicioDesc(
                        idCuidador,
                        idAdultoMayor,
                        "ACTIVA"
                )
                .orElseThrow(() ->
                        new AccesoNoAutorizadoException(
                                "El cuidador no tiene una asignación activa con el adulto mayor"
                        )
                );
    }

    // ============================================
    // MÉTODOS AUXILIARES
    // ============================================

    private CuidadorRespuestaDTO crearCuidadorDTO(
            Cuidador cuidador,
            Usuario usuario) {

        return new CuidadorRespuestaDTO(
                cuidador.getIdCuidador(),
                usuario.getNombreCompleto(),
                usuario.getEmail(),
                cuidador.getTelefono(),
                cuidador.getEspecialidad(),
                cuidador.getBiografia(),
                cuidador.getNotificacionEncendida()
        );
    }

    private AdultoMayorRespuestaDTO
    crearAdultoMayorDTO(
            AdultoMayor adultoMayor,
            Usuario usuario) {

        return new AdultoMayorRespuestaDTO(
                adultoMayor.getIdAdultoMayor(),
                usuario.getNombreCompleto(),
                usuario.getEmail(),
                adultoMayor.getFechaNacimiento(),
                adultoMayor.getDireccion()
        );
    }
}

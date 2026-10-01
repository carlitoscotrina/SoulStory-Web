package pe.edu.upc.soulstoryapi.serviceimpl;

import org.springframework.stereotype.Service;
import pe.edu.upc.soulstoryapi.dto.ConversacionResumenDTO;
import pe.edu.upc.soulstoryapi.entity.AdultoMayor;
import pe.edu.upc.soulstoryapi.entity.Asignacion;
import pe.edu.upc.soulstoryapi.entity.Mensaje;
import pe.edu.upc.soulstoryapi.entity.Usuario;
import pe.edu.upc.soulstoryapi.exception.AdultoMayorNoEncontradoException;
import pe.edu.upc.soulstoryapi.exception.CuidadorNoEncontradoException;
import pe.edu.upc.soulstoryapi.exception.UsuarioNoEncontradoException;
import pe.edu.upc.soulstoryapi.repository.AdultoMayorRepository;
import pe.edu.upc.soulstoryapi.repository.AsignacionRepository;
import pe.edu.upc.soulstoryapi.repository.CuidadorRepository;
import pe.edu.upc.soulstoryapi.repository.MensajeRepository;
import pe.edu.upc.soulstoryapi.repository.UsuarioRepository;
import pe.edu.upc.soulstoryapi.service.ConversacionService;

import java.util.List;
import java.util.Optional;

@Service
public class ConversacionServiceImpl implements ConversacionService {

    private final AsignacionRepository asignacionRepository;
    private final MensajeRepository mensajeRepository;
    private final CuidadorRepository cuidadorRepository;
    private final AdultoMayorRepository adultoMayorRepository;
    private final UsuarioRepository usuarioRepository;

    public ConversacionServiceImpl(
            AsignacionRepository asignacionRepository,
            MensajeRepository mensajeRepository,
            CuidadorRepository cuidadorRepository,
            AdultoMayorRepository adultoMayorRepository,
            UsuarioRepository usuarioRepository) {

        this.asignacionRepository = asignacionRepository;
        this.mensajeRepository = mensajeRepository;
        this.cuidadorRepository = cuidadorRepository;
        this.adultoMayorRepository = adultoMayorRepository;
        this.usuarioRepository = usuarioRepository;
    }

    // HU-27
    @Override
    public List<ConversacionResumenDTO>
    listarConversacionesCuidador(Long idCuidador) {

        cuidadorRepository
                .findById(idCuidador)
                .orElseThrow(() ->
                        new CuidadorNoEncontradoException(
                                "El cuidador no existe"
                        )
                );

        return asignacionRepository
                .findByIdCuidadorAndEstadoIgnoreCaseOrderByFechaInicioDesc(
                        idCuidador,
                        "ACTIVA"
                )
                .stream()
                .map(this::convertirResumen)
                .toList();
    }

    private ConversacionResumenDTO convertirResumen(
            Asignacion asignacion) {

        AdultoMayor adultoMayor = adultoMayorRepository
                .findById(asignacion.getIdAdultoMayor())
                .orElseThrow(() ->
                        new AdultoMayorNoEncontradoException(
                                "El adulto mayor no existe"
                        )
                );

        Usuario usuario = usuarioRepository
                .findById(adultoMayor.getIdUsuario())
                .orElseThrow(() ->
                        new UsuarioNoEncontradoException(
                                "El usuario no existe"
                        )
                );

        Optional<Mensaje> ultimoMensaje = mensajeRepository
                .findFirstByIdAsignacionOrderByFechaHoraDesc(
                        asignacion.getIdAsignacion()
                );

        return new ConversacionResumenDTO(
                asignacion.getIdAsignacion(),
                adultoMayor.getIdAdultoMayor(),
                usuario.getNombreCompleto(),
                ultimoMensaje.map(Mensaje::getContenido).orElse(null),
                ultimoMensaje.map(Mensaje::getFechaHora).orElse(null)
        );
    }
}

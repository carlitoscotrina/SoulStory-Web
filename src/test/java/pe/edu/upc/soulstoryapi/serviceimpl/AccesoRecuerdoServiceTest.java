package pe.edu.upc.soulstoryapi.serviceimpl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pe.edu.upc.soulstoryapi.entity.AdultoMayor;
import pe.edu.upc.soulstoryapi.entity.AdultoMayorGrupo;
import pe.edu.upc.soulstoryapi.entity.Recuerdo;
import pe.edu.upc.soulstoryapi.entity.Usuario;
import pe.edu.upc.soulstoryapi.exception.AccesoNoAutorizadoException;
import pe.edu.upc.soulstoryapi.repository.AdultoMayorGrupoRepository;
import pe.edu.upc.soulstoryapi.repository.AdultoMayorRepository;
import pe.edu.upc.soulstoryapi.repository.AsignacionRepository;
import pe.edu.upc.soulstoryapi.repository.CuidadorRepository;
import pe.edu.upc.soulstoryapi.repository.RecuerdoGrupoRepository;
import pe.edu.upc.soulstoryapi.repository.RecuerdoRepository;
import pe.edu.upc.soulstoryapi.repository.UsuarioRepository;
import pe.edu.upc.soulstoryapi.service.AccesoRecuerdoService;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AccesoRecuerdoServiceTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private RecuerdoRepository recuerdoRepository;

    @Mock
    private AdultoMayorRepository adultoMayorRepository;

    @Mock
    private CuidadorRepository cuidadorRepository;

    @Mock
    private AsignacionRepository asignacionRepository;

    @Mock
    private AdultoMayorGrupoRepository adultoMayorGrupoRepository;

    @Mock
    private RecuerdoGrupoRepository recuerdoGrupoRepository;

    private AccesoRecuerdoService accesoRecuerdoService;

    @BeforeEach
    void prepararServicio() {

        accesoRecuerdoService = new AccesoRecuerdoServiceImpl(
                usuarioRepository,
                recuerdoRepository,
                adultoMayorRepository,
                cuidadorRepository,
                asignacionRepository,
                adultoMayorGrupoRepository,
                recuerdoGrupoRepository
        );
    }

    @Test
    void propietarioPuedeAccederASuRecuerdo() {

        Recuerdo recuerdo = crearRecuerdo(50L, 1L);

        prepararUsuarioYRecuerdo(recuerdo);
        when(adultoMayorRepository.findByIdUsuario(100L))
                .thenReturn(Optional.of(crearAdultoMayor(1L, 100L)));

        Recuerdo resultado = accesoRecuerdoService
                .validarAccesoUsuarioARecuerdo(100L, 50L);

        assertEquals(50L, resultado.getIdRecuerdo());
    }

    @Test
    void integranteDeGrupoAccedeARecuerdoCompartido() {

        Recuerdo recuerdo = crearRecuerdo(50L, 2L);
        AdultoMayorGrupo integrante = new AdultoMayorGrupo();
        integrante.setIdAdultoMayor(3L);
        integrante.setIdGrupo(8L);

        prepararUsuarioYRecuerdo(recuerdo);
        when(adultoMayorRepository.findByIdUsuario(100L))
                .thenReturn(Optional.of(crearAdultoMayor(3L, 100L)));
        when(cuidadorRepository.findByIdUsuario(100L))
                .thenReturn(Optional.empty());
        when(adultoMayorGrupoRepository.findByIdAdultoMayor(3L))
                .thenReturn(List.of(integrante));
        when(recuerdoGrupoRepository
                .existsByIdRecuerdoAndIdGrupo(50L, 8L))
                .thenReturn(true);

        Recuerdo resultado = accesoRecuerdoService
                .validarAccesoUsuarioARecuerdo(100L, 50L);

        assertEquals(50L, resultado.getIdRecuerdo());
    }

    @Test
    void usuarioSinAccesoEsDenegado() {

        Recuerdo recuerdo = crearRecuerdo(50L, 2L);

        prepararUsuarioYRecuerdo(recuerdo);
        when(adultoMayorRepository.findByIdUsuario(100L))
                .thenReturn(Optional.of(crearAdultoMayor(3L, 100L)));
        when(cuidadorRepository.findByIdUsuario(100L))
                .thenReturn(Optional.empty());
        when(adultoMayorGrupoRepository.findByIdAdultoMayor(3L))
                .thenReturn(List.of());

        assertThrows(
                AccesoNoAutorizadoException.class,
                () -> accesoRecuerdoService
                        .validarAccesoUsuarioARecuerdo(100L, 50L)
        );
    }

    private void prepararUsuarioYRecuerdo(Recuerdo recuerdo) {

        Usuario usuario = new Usuario();
        usuario.setIdUsuario(100L);

        when(usuarioRepository.findById(100L))
                .thenReturn(Optional.of(usuario));
        when(recuerdoRepository.findById(50L))
                .thenReturn(Optional.of(recuerdo));
    }

    private AdultoMayor crearAdultoMayor(
            Long idAdultoMayor,
            Long idUsuario) {

        AdultoMayor adultoMayor = new AdultoMayor();
        adultoMayor.setIdAdultoMayor(idAdultoMayor);
        adultoMayor.setIdUsuario(idUsuario);
        return adultoMayor;
    }

    private Recuerdo crearRecuerdo(
            Long idRecuerdo,
            Long idAdultoMayor) {

        Recuerdo recuerdo = new Recuerdo();
        recuerdo.setIdRecuerdo(idRecuerdo);
        recuerdo.setIdAdultoMayor(idAdultoMayor);
        return recuerdo;
    }
}

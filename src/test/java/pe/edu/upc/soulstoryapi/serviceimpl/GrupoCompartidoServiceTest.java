package pe.edu.upc.soulstoryapi.serviceimpl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pe.edu.upc.soulstoryapi.dto.AdultoMayorRespuestaDTO;
import pe.edu.upc.soulstoryapi.dto.RecuerdoRespuestaDTO;
import pe.edu.upc.soulstoryapi.entity.AdultoMayor;
import pe.edu.upc.soulstoryapi.entity.AdultoMayorGrupo;
import pe.edu.upc.soulstoryapi.entity.Grupo;
import pe.edu.upc.soulstoryapi.entity.Recuerdo;
import pe.edu.upc.soulstoryapi.entity.RecuerdoGrupo;
import pe.edu.upc.soulstoryapi.entity.Usuario;
import pe.edu.upc.soulstoryapi.exception.AccesoNoAutorizadoException;
import pe.edu.upc.soulstoryapi.exception.IntegranteGrupoDuplicadoException;
import pe.edu.upc.soulstoryapi.exception.RecuerdoGrupoDuplicadoException;
import pe.edu.upc.soulstoryapi.repository.AdultoMayorGrupoRepository;
import pe.edu.upc.soulstoryapi.repository.AdultoMayorRepository;
import pe.edu.upc.soulstoryapi.repository.GrupoRepository;
import pe.edu.upc.soulstoryapi.repository.RecuerdoGrupoRepository;
import pe.edu.upc.soulstoryapi.repository.UsuarioRepository;
import pe.edu.upc.soulstoryapi.service.GrupoCompartidoService;
import pe.edu.upc.soulstoryapi.service.RecuerdoService;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GrupoCompartidoServiceTest {

    @Mock
    private GrupoRepository grupoRepository;

    @Mock
    private AdultoMayorRepository adultoMayorRepository;

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private AdultoMayorGrupoRepository adultoMayorGrupoRepository;

    @Mock
    private RecuerdoGrupoRepository recuerdoGrupoRepository;

    @Mock
    private RecuerdoService recuerdoService;

    private GrupoCompartidoService grupoCompartidoService;

    @BeforeEach
    void prepararServicio() {

        grupoCompartidoService = new GrupoCompartidoServiceImpl(
                grupoRepository,
                adultoMayorRepository,
                usuarioRepository,
                adultoMayorGrupoRepository,
                recuerdoGrupoRepository,
                recuerdoService
        );
    }

    @Test
    void administradorAgregaIntegranteCorrectamente() {

        prepararAdministrador();

        AdultoMayor integrante = crearAdultoMayor(2L, 102L);
        when(adultoMayorRepository.findById(2L))
                .thenReturn(Optional.of(integrante));
        when(adultoMayorGrupoRepository
                .existsByIdAdultoMayorAndIdGrupo(2L, 10L))
                .thenReturn(false);
        when(usuarioRepository.findById(102L))
                .thenReturn(Optional.of(crearUsuario(102L)));

        AdultoMayorRespuestaDTO respuesta =
                grupoCompartidoService.agregarIntegrante(
                        1L,
                        10L,
                        2L
                );

        assertEquals(2L, respuesta.getIdAdultoMayor());
        assertEquals("Maria Gomez", respuesta.getNombreCompleto());
        verify(adultoMayorGrupoRepository)
                .save(any(AdultoMayorGrupo.class));
    }

    @Test
    void integranteDuplicadoLanzaExcepcion() {

        prepararAdministrador();
        when(adultoMayorRepository.findById(2L))
                .thenReturn(Optional.of(crearAdultoMayor(2L, 102L)));
        when(adultoMayorGrupoRepository
                .existsByIdAdultoMayorAndIdGrupo(2L, 10L))
                .thenReturn(true);

        assertThrows(
                IntegranteGrupoDuplicadoException.class,
                () -> grupoCompartidoService.agregarIntegrante(
                        1L,
                        10L,
                        2L
                )
        );
    }

    @Test
    void adultoQueNoAdministraGrupoNoPuedeAgregarIntegrante() {

        when(adultoMayorRepository.findById(2L))
                .thenReturn(Optional.of(crearAdultoMayor(2L, 102L)));
        when(grupoRepository.findById(10L))
                .thenReturn(Optional.of(crearGrupo(1L)));

        assertThrows(
                AccesoNoAutorizadoException.class,
                () -> grupoCompartidoService.agregarIntegrante(
                        2L,
                        10L,
                        3L
                )
        );
    }

    @Test
    void administradorAgregaRecuerdoCorrectamente() {

        prepararAdministrador();

        Recuerdo recuerdo = crearRecuerdo(30L, 2L);
        RecuerdoRespuestaDTO respuesta = crearRespuestaRecuerdo(30L);

        when(recuerdoService.obtenerEntidadRecuerdo(30L))
                .thenReturn(recuerdo);
        when(adultoMayorGrupoRepository
                .existsByIdAdultoMayorAndIdGrupo(2L, 10L))
                .thenReturn(true);
        when(recuerdoGrupoRepository
                .existsByIdRecuerdoAndIdGrupo(30L, 10L))
                .thenReturn(false);
        when(recuerdoService.convertirRespuesta(recuerdo))
                .thenReturn(respuesta);

        RecuerdoRespuestaDTO resultado =
                grupoCompartidoService.agregarRecuerdo(
                        1L,
                        10L,
                        30L
                );

        assertEquals(30L, resultado.getIdRecuerdo());
        verify(recuerdoGrupoRepository)
                .save(any(RecuerdoGrupo.class));
    }

    @Test
    void recuerdoDuplicadoLanzaExcepcion() {

        prepararAdministrador();

        Recuerdo recuerdo = crearRecuerdo(30L, 2L);
        when(recuerdoService.obtenerEntidadRecuerdo(30L))
                .thenReturn(recuerdo);
        when(adultoMayorGrupoRepository
                .existsByIdAdultoMayorAndIdGrupo(2L, 10L))
                .thenReturn(true);
        when(recuerdoGrupoRepository
                .existsByIdRecuerdoAndIdGrupo(30L, 10L))
                .thenReturn(true);

        assertThrows(
                RecuerdoGrupoDuplicadoException.class,
                () -> grupoCompartidoService.agregarRecuerdo(
                        1L,
                        10L,
                        30L
                )
        );
    }

    @Test
    void recuerdoDeAdultoNoIntegranteEsDenegado() {

        prepararAdministrador();

        Recuerdo recuerdo = crearRecuerdo(30L, 2L);
        when(recuerdoService.obtenerEntidadRecuerdo(30L))
                .thenReturn(recuerdo);
        when(adultoMayorGrupoRepository
                .existsByIdAdultoMayorAndIdGrupo(2L, 10L))
                .thenReturn(false);

        assertThrows(
                AccesoNoAutorizadoException.class,
                () -> grupoCompartidoService.agregarRecuerdo(
                        1L,
                        10L,
                        30L
                )
        );
    }

    @Test
    void integrantePuedeVerRecuerdosCompartidos() {

        Recuerdo recuerdo = crearRecuerdo(30L, 2L);
        RecuerdoRespuestaDTO respuesta = crearRespuestaRecuerdo(30L);
        RecuerdoGrupo relacion = new RecuerdoGrupo();
        relacion.setIdRecuerdo(30L);
        relacion.setIdGrupo(10L);

        when(adultoMayorRepository.findById(2L))
                .thenReturn(Optional.of(crearAdultoMayor(2L, 102L)));
        when(grupoRepository.findById(10L))
                .thenReturn(Optional.of(crearGrupo(1L)));
        when(adultoMayorGrupoRepository
                .existsByIdAdultoMayorAndIdGrupo(2L, 10L))
                .thenReturn(true);
        when(recuerdoGrupoRepository.findByIdGrupo(10L))
                .thenReturn(List.of(relacion));
        when(recuerdoService.obtenerEntidadRecuerdo(30L))
                .thenReturn(recuerdo);
        when(recuerdoService.convertirRespuesta(recuerdo))
                .thenReturn(respuesta);

        List<RecuerdoRespuestaDTO> recuerdos =
                grupoCompartidoService.listarRecuerdosCompartidos(
                        2L,
                        10L
                );

        assertEquals(1, recuerdos.size());
        assertEquals(30L, recuerdos.get(0).getIdRecuerdo());
    }

    @Test
    void noIntegranteNoPuedeVerRecuerdosCompartidos() {

        when(adultoMayorRepository.findById(2L))
                .thenReturn(Optional.of(crearAdultoMayor(2L, 102L)));
        when(grupoRepository.findById(10L))
                .thenReturn(Optional.of(crearGrupo(1L)));
        when(adultoMayorGrupoRepository
                .existsByIdAdultoMayorAndIdGrupo(2L, 10L))
                .thenReturn(false);

        assertThrows(
                AccesoNoAutorizadoException.class,
                () -> grupoCompartidoService
                        .listarRecuerdosCompartidos(2L, 10L)
        );
    }

    @Test
    void integranteDeGrupoSinRecuerdosRecibeListaVacia() {

        when(adultoMayorRepository.findById(2L))
                .thenReturn(Optional.of(crearAdultoMayor(2L, 102L)));
        when(grupoRepository.findById(10L))
                .thenReturn(Optional.of(crearGrupo(1L)));
        when(adultoMayorGrupoRepository
                .existsByIdAdultoMayorAndIdGrupo(2L, 10L))
                .thenReturn(true);
        when(recuerdoGrupoRepository.findByIdGrupo(10L))
                .thenReturn(List.of());

        List<RecuerdoRespuestaDTO> recuerdos =
                grupoCompartidoService.listarRecuerdosCompartidos(
                        2L,
                        10L
                );

        assertTrue(recuerdos.isEmpty());
    }

    private void prepararAdministrador() {

        when(adultoMayorRepository.findById(1L))
                .thenReturn(Optional.of(crearAdultoMayor(1L, 101L)));
        when(grupoRepository.findById(10L))
                .thenReturn(Optional.of(crearGrupo(1L)));
    }

    private AdultoMayor crearAdultoMayor(
            Long idAdultoMayor,
            Long idUsuario) {

        AdultoMayor adultoMayor = new AdultoMayor();
        adultoMayor.setIdAdultoMayor(idAdultoMayor);
        adultoMayor.setIdUsuario(idUsuario);
        return adultoMayor;
    }

    private Usuario crearUsuario(Long idUsuario) {

        Usuario usuario = new Usuario();
        usuario.setIdUsuario(idUsuario);
        usuario.setNombreCompleto("Maria Gomez");
        usuario.setEmail("maria@example.com");
        return usuario;
    }

    private Grupo crearGrupo(Long idAdministrador) {

        Grupo grupo = new Grupo();
        grupo.setIdGrupo(10L);
        grupo.setIdAdultoMayor(idAdministrador);
        grupo.setNombreGrupo("Familia");
        return grupo;
    }

    private Recuerdo crearRecuerdo(
            Long idRecuerdo,
            Long idAdultoMayor) {

        Recuerdo recuerdo = new Recuerdo();
        recuerdo.setIdRecuerdo(idRecuerdo);
        recuerdo.setIdAdultoMayor(idAdultoMayor);
        recuerdo.setTituloRecuerdo("Tarde familiar");
        return recuerdo;
    }

    private RecuerdoRespuestaDTO crearRespuestaRecuerdo(
            Long idRecuerdo) {

        RecuerdoRespuestaDTO respuesta = new RecuerdoRespuestaDTO();
        respuesta.setIdRecuerdo(idRecuerdo);
        respuesta.setTituloRecuerdo("Tarde familiar");
        return respuesta;
    }
}

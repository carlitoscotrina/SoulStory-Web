package pe.edu.upc.soulstoryapi.serviceimpl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pe.edu.upc.soulstoryapi.dto.ConfiguracionRespuestaDTO;
import pe.edu.upc.soulstoryapi.entity.Configuracion;
import pe.edu.upc.soulstoryapi.entity.Usuario;
import pe.edu.upc.soulstoryapi.repository.ConfiguracionRepository;
import pe.edu.upc.soulstoryapi.repository.UsuarioRepository;
import pe.edu.upc.soulstoryapi.service.ConfiguracionService;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ConfiguracionPreferenciasServiceTest {

    @Mock
    private ConfiguracionRepository configuracionRepository;

    @Mock
    private UsuarioRepository usuarioRepository;

    private ConfiguracionService configuracionService;

    @BeforeEach
    void prepararServicio() {

        configuracionService = new ConfiguracionServiceImpl(
                configuracionRepository,
                usuarioRepository
        );
    }

    @Test
    void activarTemaOscuroGuardaTemaOscuro() {

        prepararConfiguracion(
                ConfiguracionService.TAMANIO_MEDIANA,
                null,
                null
        );

        ConfiguracionRespuestaDTO respuesta = configuracionService
                .activarTemaOscuro(1L);

        assertEquals(ConfiguracionService.TEMA_OSCURO,
                respuesta.getTemaVisual());
    }

    @Test
    void activarTemaClaroGuardaTemaClaro() {

        prepararConfiguracion(
                ConfiguracionService.TAMANIO_MEDIANA,
                ConfiguracionService.TEMA_OSCURO,
                null
        );

        ConfiguracionRespuestaDTO respuesta = configuracionService
                .activarTemaClaro(1L);

        assertEquals(ConfiguracionService.TEMA_CLARO,
                respuesta.getTemaVisual());
    }

    @Test
    void cambiarTemaNoModificaTamanioFuente() {

        prepararConfiguracion(
                ConfiguracionService.TAMANIO_GRANDE,
                null,
                null
        );

        ConfiguracionRespuestaDTO respuesta = configuracionService
                .activarTemaOscuro(1L);

        assertEquals(ConfiguracionService.TAMANIO_GRANDE,
                respuesta.getTamanioFuente());
    }

    @Test
    void cambiarTemaNoModificaNotificaciones() {

        prepararConfiguracion(
                ConfiguracionService.TAMANIO_MEDIANA,
                null,
                true
        );

        ConfiguracionRespuestaDTO respuesta = configuracionService
                .activarTemaClaro(1L);

        assertTrue(respuesta.getNotificacionesSonido());
    }

    @Test
    void activarNotificacionesGuardaVerdadero() {

        prepararConfiguracion(
                ConfiguracionService.TAMANIO_MEDIANA,
                null,
                false
        );

        ConfiguracionRespuestaDTO respuesta = configuracionService
                .activarNotificaciones(1L);

        assertTrue(respuesta.getNotificacionesSonido());
    }

    @Test
    void desactivarNotificacionesGuardaFalso() {

        prepararConfiguracion(
                ConfiguracionService.TAMANIO_MEDIANA,
                null,
                true
        );

        ConfiguracionRespuestaDTO respuesta = configuracionService
                .desactivarNotificaciones(1L);

        assertFalse(respuesta.getNotificacionesSonido());
    }

    @Test
    void notificacionesNulasNoEstanHabilitadas() {

        prepararConfiguracionSoloConsulta(null);

        assertFalse(configuracionService
                .notificacionesHabilitadas(1L));
    }

    @Test
    void notificacionesFalsasNoEstanHabilitadas() {

        prepararConfiguracionSoloConsulta(false);

        assertFalse(configuracionService
                .notificacionesHabilitadas(1L));
    }

    @Test
    void notificacionesVerdaderasEstanHabilitadas() {

        prepararConfiguracionSoloConsulta(true);

        assertTrue(configuracionService
                .notificacionesHabilitadas(1L));
    }

    @Test
    void cambiarNotificacionesNoModificaTema() {

        prepararConfiguracion(
                ConfiguracionService.TAMANIO_MEDIANA,
                ConfiguracionService.TEMA_OSCURO,
                false
        );

        ConfiguracionRespuestaDTO respuesta = configuracionService
                .activarNotificaciones(1L);

        assertEquals(ConfiguracionService.TEMA_OSCURO,
                respuesta.getTemaVisual());
    }

    @Test
    void cambiarNotificacionesNoModificaFuente() {

        prepararConfiguracion(
                ConfiguracionService.TAMANIO_GRANDE,
                null,
                false
        );

        ConfiguracionRespuestaDTO respuesta = configuracionService
                .activarNotificaciones(1L);

        assertEquals(ConfiguracionService.TAMANIO_GRANDE,
                respuesta.getTamanioFuente());
    }

    private void prepararConfiguracion(
            String tamanioFuente,
            String temaVisual,
            Boolean notificacionesSonido) {

        Configuracion configuracion = crearConfiguracion(
                tamanioFuente,
                temaVisual,
                notificacionesSonido
        );

        prepararUsuario();
        when(configuracionRepository.findByIdUsuario(1L))
                .thenReturn(Optional.of(configuracion));
        when(configuracionRepository.save(configuracion))
                .thenReturn(configuracion);
    }

    private void prepararConfiguracionSoloConsulta(
            Boolean notificacionesSonido) {

        prepararUsuario();
        when(configuracionRepository.findByIdUsuario(1L))
                .thenReturn(Optional.of(crearConfiguracion(
                        ConfiguracionService.TAMANIO_MEDIANA,
                        null,
                        notificacionesSonido
                )));
    }

    private void prepararUsuario() {

        Usuario usuario = new Usuario();
        usuario.setIdUsuario(1L);

        when(usuarioRepository.findById(1L))
                .thenReturn(Optional.of(usuario));
    }

    private Configuracion crearConfiguracion(
            String tamanioFuente,
            String temaVisual,
            Boolean notificacionesSonido) {

        Configuracion configuracion = new Configuracion();
        configuracion.setIdConfiguracion(8L);
        configuracion.setIdUsuario(1L);
        configuracion.setTamanioFuente(tamanioFuente);
        configuracion.setTemaVisual(temaVisual);
        configuracion.setNotificacionesSonido(notificacionesSonido);
        return configuracion;
    }
}

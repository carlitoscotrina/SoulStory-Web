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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ConfiguracionServiceTest {

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
    void configuracionInexistenteCreaFuenteMediana() {

        prepararUsuario();
        when(configuracionRepository.findByIdUsuario(1L))
                .thenReturn(Optional.empty());
        when(configuracionRepository.save(any(Configuracion.class)))
                .thenAnswer(invocacion -> {
                    Configuracion configuracion = invocacion.getArgument(0);
                    configuracion.setIdConfiguracion(8L);
                    return configuracion;
                });

        ConfiguracionRespuestaDTO respuesta = configuracionService
                .obtenerConfiguracion(1L);

        assertEquals(8L, respuesta.getIdConfiguracion());
        assertEquals(ConfiguracionService.TAMANIO_MEDIANA,
                respuesta.getTamanioFuente());
    }

    @Test
    void fuentePequenaAumentaAMediana() {

        prepararConfiguracionExistente(
                ConfiguracionService.TAMANIO_PEQUENA
        );

        ConfiguracionRespuestaDTO respuesta = configuracionService
                .aumentarFuente(1L);

        assertEquals(ConfiguracionService.TAMANIO_MEDIANA,
                respuesta.getTamanioFuente());
    }

    @Test
    void fuenteMedianaAumentaAGrande() {

        prepararConfiguracionExistente(
                ConfiguracionService.TAMANIO_MEDIANA
        );

        ConfiguracionRespuestaDTO respuesta = configuracionService
                .aumentarFuente(1L);

        assertEquals(ConfiguracionService.TAMANIO_GRANDE,
                respuesta.getTamanioFuente());
    }

    @Test
    void fuenteGrandeAumentaYPermaneceGrande() {

        prepararConfiguracionExistente(
                ConfiguracionService.TAMANIO_GRANDE
        );

        ConfiguracionRespuestaDTO respuesta = configuracionService
                .aumentarFuente(1L);

        assertEquals(ConfiguracionService.TAMANIO_GRANDE,
                respuesta.getTamanioFuente());
    }

    @Test
    void fuenteGrandeDisminuyeAMediana() {

        prepararConfiguracionExistente(
                ConfiguracionService.TAMANIO_GRANDE
        );

        ConfiguracionRespuestaDTO respuesta = configuracionService
                .disminuirFuente(1L);

        assertEquals(ConfiguracionService.TAMANIO_MEDIANA,
                respuesta.getTamanioFuente());
    }

    @Test
    void fuenteMedianaDisminuyeAPequena() {

        prepararConfiguracionExistente(
                ConfiguracionService.TAMANIO_MEDIANA
        );

        ConfiguracionRespuestaDTO respuesta = configuracionService
                .disminuirFuente(1L);

        assertEquals(ConfiguracionService.TAMANIO_PEQUENA,
                respuesta.getTamanioFuente());
    }

    @Test
    void fuentePequenaDisminuyeYPermanecePequena() {

        prepararConfiguracionExistente(
                ConfiguracionService.TAMANIO_PEQUENA
        );

        ConfiguracionRespuestaDTO respuesta = configuracionService
                .disminuirFuente(1L);

        assertEquals(ConfiguracionService.TAMANIO_PEQUENA,
                respuesta.getTamanioFuente());
    }

    private void prepararUsuario() {

        Usuario usuario = new Usuario();
        usuario.setIdUsuario(1L);

        when(usuarioRepository.findById(1L))
                .thenReturn(Optional.of(usuario));
    }

    private void prepararConfiguracionExistente(
            String tamanioFuente) {

        prepararUsuario();

        Configuracion configuracion = new Configuracion();
        configuracion.setIdConfiguracion(8L);
        configuracion.setIdUsuario(1L);
        configuracion.setTamanioFuente(tamanioFuente);

        when(configuracionRepository.findByIdUsuario(1L))
                .thenReturn(Optional.of(configuracion));
        when(configuracionRepository.save(configuracion))
                .thenReturn(configuracion);
    }
}

package pe.edu.upc.soulstoryapi.serviceimpl;

import org.springframework.stereotype.Service;
import pe.edu.upc.soulstoryapi.dto.ConfiguracionRespuestaDTO;
import pe.edu.upc.soulstoryapi.entity.Configuracion;
import pe.edu.upc.soulstoryapi.exception.UsuarioNoEncontradoException;
import pe.edu.upc.soulstoryapi.repository.ConfiguracionRepository;
import pe.edu.upc.soulstoryapi.repository.UsuarioRepository;
import pe.edu.upc.soulstoryapi.service.ConfiguracionService;

@Service
public class ConfiguracionServiceImpl implements ConfiguracionService {

    private final ConfiguracionRepository configuracionRepository;
    private final UsuarioRepository usuarioRepository;

    public ConfiguracionServiceImpl(
            ConfiguracionRepository configuracionRepository,
            UsuarioRepository usuarioRepository) {

        this.configuracionRepository = configuracionRepository;
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    public ConfiguracionRespuestaDTO obtenerConfiguracion(
            Long idUsuario) {

        return convertirDTO(obtenerOCrearConfiguracion(idUsuario));
    }

    // HU-28
    @Override
    public ConfiguracionRespuestaDTO aumentarFuente(
            Long idUsuario) {

        Configuracion configuracion = obtenerOCrearConfiguracion(
                idUsuario
        );

        String tamanioActual = normalizarTamanio(
                configuracion.getTamanioFuente()
        );

        if (TAMANIO_PEQUENA.equals(tamanioActual)) {
            configuracion.setTamanioFuente(TAMANIO_MEDIANA);
        } else {
            configuracion.setTamanioFuente(TAMANIO_GRANDE);
        }

        return convertirDTO(configuracionRepository.save(configuracion));
    }

    @Override
    public ConfiguracionRespuestaDTO disminuirFuente(
            Long idUsuario) {

        Configuracion configuracion = obtenerOCrearConfiguracion(
                idUsuario
        );

        String tamanioActual = normalizarTamanio(
                configuracion.getTamanioFuente()
        );

        if (TAMANIO_GRANDE.equals(tamanioActual)) {
            configuracion.setTamanioFuente(TAMANIO_MEDIANA);
        } else {
            configuracion.setTamanioFuente(TAMANIO_PEQUENA);
        }

        return convertirDTO(configuracionRepository.save(configuracion));
    }

    // HU-29
    @Override
    public ConfiguracionRespuestaDTO activarTemaClaro(
            Long idUsuario) {

        return actualizarTema(
                idUsuario,
                TEMA_CLARO
        );
    }

    @Override
    public ConfiguracionRespuestaDTO activarTemaOscuro(
            Long idUsuario) {

        return actualizarTema(
                idUsuario,
                TEMA_OSCURO
        );
    }

    // HU-30
    @Override
    public ConfiguracionRespuestaDTO activarNotificaciones(
            Long idUsuario) {

        return actualizarNotificaciones(idUsuario, true);
    }

    @Override
    public ConfiguracionRespuestaDTO desactivarNotificaciones(
            Long idUsuario) {

        return actualizarNotificaciones(idUsuario, false);
    }

    @Override
    public boolean notificacionesHabilitadas(Long idUsuario) {

        Configuracion configuracion = obtenerOCrearConfiguracion(
                idUsuario
        );

        return Boolean.TRUE.equals(
                configuracion.getNotificacionesSonido()
        );
    }

    private ConfiguracionRespuestaDTO actualizarTema(
            Long idUsuario,
            String temaVisual) {

        Configuracion configuracion = obtenerOCrearConfiguracion(
                idUsuario
        );

        configuracion.setTemaVisual(temaVisual);

        return convertirDTO(configuracionRepository.save(configuracion));
    }

    private ConfiguracionRespuestaDTO actualizarNotificaciones(
            Long idUsuario,
            Boolean notificacionesSonido) {

        Configuracion configuracion = obtenerOCrearConfiguracion(
                idUsuario
        );

        configuracion.setNotificacionesSonido(notificacionesSonido);

        return convertirDTO(configuracionRepository.save(configuracion));
    }

    private Configuracion obtenerOCrearConfiguracion(
            Long idUsuario) {

        usuarioRepository
                .findById(idUsuario)
                .orElseThrow(() ->
                        new UsuarioNoEncontradoException(
                                "El usuario no existe"
                        )
                );

        return configuracionRepository
                .findByIdUsuario(idUsuario)
                .orElseGet(() -> crearConfiguracionPorDefecto(idUsuario));
    }

    private Configuracion crearConfiguracionPorDefecto(
            Long idUsuario) {

        Configuracion configuracion = new Configuracion();
        configuracion.setIdUsuario(idUsuario);
        configuracion.setTamanioFuente(TAMANIO_MEDIANA);
        configuracion.setIdioma(null);
        configuracion.setTemaVisual(null);
        configuracion.setNotificacionesSonido(null);

        return configuracionRepository.save(configuracion);
    }

    private String normalizarTamanio(String tamanioFuente) {

        if (TAMANIO_PEQUENA.equals(tamanioFuente)
                || TAMANIO_MEDIANA.equals(tamanioFuente)
                || TAMANIO_GRANDE.equals(tamanioFuente)) {

            return tamanioFuente;
        }

        return TAMANIO_MEDIANA;
    }

    private ConfiguracionRespuestaDTO convertirDTO(
            Configuracion configuracion) {

        return new ConfiguracionRespuestaDTO(
                configuracion.getIdConfiguracion(),
                configuracion.getIdUsuario(),
                configuracion.getIdioma(),
                configuracion.getTamanioFuente(),
                configuracion.getTemaVisual(),
                configuracion.getNotificacionesSonido()
        );
    }
}

package pe.edu.upc.soulstoryapi.service;

import pe.edu.upc.soulstoryapi.dto.ConfiguracionRespuestaDTO;

public interface ConfiguracionService {

    // Decision tecnica provisional para HU-28 hasta definir el diseno visual.

    public static final String TAMANIO_PEQUENA = "PEQUENA";
    public static final String TAMANIO_MEDIANA = "MEDIANA";
    public static final String TAMANIO_GRANDE = "GRANDE";
    public static final String TEMA_CLARO = "CLARO";
    public static final String TEMA_OSCURO = "OSCURO";

    public ConfiguracionRespuestaDTO obtenerConfiguracion(Long idUsuario);

    // HU-28
    public ConfiguracionRespuestaDTO aumentarFuente(Long idUsuario);

    public ConfiguracionRespuestaDTO disminuirFuente(Long idUsuario);

    // HU-29
    public ConfiguracionRespuestaDTO activarTemaClaro(Long idUsuario);

    public ConfiguracionRespuestaDTO activarTemaOscuro(Long idUsuario);

    // HU-30
    public ConfiguracionRespuestaDTO activarNotificaciones(Long idUsuario);

    public ConfiguracionRespuestaDTO desactivarNotificaciones(Long idUsuario);

    public boolean notificacionesHabilitadas(Long idUsuario);

}

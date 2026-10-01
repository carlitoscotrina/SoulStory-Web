package pe.edu.upc.soulstoryapi.serviceimpl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pe.edu.upc.soulstoryapi.dto.EnviarMensajeDTO;
import pe.edu.upc.soulstoryapi.dto.MensajeConversacionDTO;
import pe.edu.upc.soulstoryapi.entity.Asignacion;
import pe.edu.upc.soulstoryapi.entity.Mensaje;
import pe.edu.upc.soulstoryapi.exception.AccesoNoAutorizadoException;
import pe.edu.upc.soulstoryapi.repository.MensajeRepository;
import pe.edu.upc.soulstoryapi.service.AsignacionService;
import pe.edu.upc.soulstoryapi.service.MensajeService;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MensajeCuidadorServiceTest {

    @Mock
    private MensajeRepository mensajeRepository;

    @Mock
    private AsignacionService asignacionService;

    private MensajeService mensajeService;

    @BeforeEach
    void prepararServicio() {

        mensajeService = new MensajeServiceImpl(
                mensajeRepository,
                asignacionService
        );
    }

    @Test
    void cuidadorConAsignacionActivaPuedeEnviarMensaje() {

        when(asignacionService
                .obtenerAsignacionAutorizada(2L, 1L))
                .thenReturn(crearAsignacion(9L));
        when(mensajeRepository.save(any(Mensaje.class)))
                .thenAnswer(invocacion -> {
                    Mensaje mensaje = invocacion.getArgument(0);
                    mensaje.setIdMensaje(20L);
                    return mensaje;
                });

        MensajeConversacionDTO respuesta =
                mensajeService.enviarMensajeCuidador(
                        2L,
                        1L,
                        crearDTO("Buenos dias")
                );

        assertEquals(20L, respuesta.getIdMensaje());
        assertEquals("CUIDADOR", respuesta.getTipoRemitente());
    }

    @Test
    void mensajeCuidadorSeGuardaComoNoLeido() {

        when(asignacionService
                .obtenerAsignacionAutorizada(2L, 1L))
                .thenReturn(crearAsignacion(9L));
        when(mensajeRepository.save(any(Mensaje.class)))
                .thenAnswer(invocacion -> invocacion.getArgument(0));

        mensajeService.enviarMensajeCuidador(
                2L,
                1L,
                crearDTO("Como se siente hoy")
        );

        ArgumentCaptor<Mensaje> captor =
                ArgumentCaptor.forClass(Mensaje.class);

        verify(mensajeRepository).save(captor.capture());

        Mensaje mensaje = captor.getValue();
        assertEquals("CUIDADOR", mensaje.getTipoRemitente());
        assertFalse(mensaje.getLeido());
        assertEquals(9L, mensaje.getIdAsignacion());
    }

    @Test
    void mensajeCuidadorVacioEsInvalido() {

        when(asignacionService
                .obtenerAsignacionAutorizada(2L, 1L))
                .thenReturn(crearAsignacion(9L));

        assertThrows(
                IllegalArgumentException.class,
                () -> mensajeService.enviarMensajeCuidador(
                        2L,
                        1L,
                        crearDTO(" ")
                )
        );

        verifyNoInteractions(mensajeRepository);
    }

    @Test
    void cuidadorSinAsignacionNoPuedeEnviarMensaje() {

        when(asignacionService
                .obtenerAsignacionAutorizada(2L, 1L))
                .thenThrow(new AccesoNoAutorizadoException(
                        "El cuidador no tiene una asignacion activa con el adulto mayor"
                ));

        assertThrows(
                AccesoNoAutorizadoException.class,
                () -> mensajeService.enviarMensajeCuidador(
                        2L,
                        1L,
                        crearDTO("Hola")
                )
        );

        verifyNoInteractions(mensajeRepository);
    }

    @Test
    void conversacionDelAdultoIncluyeMensajeDelCuidador() {

        Mensaje mensaje = crearMensaje(20L, "CUIDADOR");

        when(asignacionService
                .obtenerAsignacionAutorizada(2L, 1L))
                .thenReturn(crearAsignacion(9L));
        when(mensajeRepository
                .findByIdAsignacionOrderByFechaHoraAsc(9L))
                .thenReturn(List.of(mensaje));

        List<MensajeConversacionDTO> conversacion =
                mensajeService.listarConversacion(1L, 2L);

        assertEquals(1, conversacion.size());
        assertEquals("CUIDADOR", conversacion.get(0).getTipoRemitente());
    }

    @Test
    void historialCuidadorDevuelveMensajesOrdenados() {

        Mensaje primero = crearMensaje(1L, "ADULTO_MAYOR");
        primero.setFechaHora(LocalDateTime.of(2026, 1, 1, 9, 0));
        Mensaje segundo = crearMensaje(2L, "CUIDADOR");
        segundo.setFechaHora(LocalDateTime.of(2026, 1, 1, 10, 0));

        when(asignacionService
                .obtenerAsignacionAutorizada(2L, 1L))
                .thenReturn(crearAsignacion(9L));
        when(mensajeRepository
                .findByIdAsignacionOrderByFechaHoraAsc(9L))
                .thenReturn(List.of(primero, segundo));

        List<MensajeConversacionDTO> historial = mensajeService
                .listarConversacionComoCuidador(2L, 1L);

        assertEquals(1L, historial.get(0).getIdMensaje());
        assertEquals(2L, historial.get(1).getIdMensaje());
    }

    @Test
    void cuidadorSinAsignacionNoPuedeConsultarHistorial() {

        when(asignacionService
                .obtenerAsignacionAutorizada(2L, 1L))
                .thenThrow(new AccesoNoAutorizadoException(
                        "El cuidador no tiene una asignacion activa con el adulto mayor"
                ));

        assertThrows(
                AccesoNoAutorizadoException.class,
                () -> mensajeService.listarConversacionComoCuidador(
                        2L,
                        1L
                )
        );

        verifyNoInteractions(mensajeRepository);
    }

    private EnviarMensajeDTO crearDTO(String contenido) {

        EnviarMensajeDTO dto = new EnviarMensajeDTO();
        dto.setContenido(contenido);
        return dto;
    }

    private Asignacion crearAsignacion(Long idAsignacion) {

        Asignacion asignacion = new Asignacion();
        asignacion.setIdAsignacion(idAsignacion);
        asignacion.setEstado("ACTIVA");
        return asignacion;
    }

    private Mensaje crearMensaje(
            Long idMensaje,
            String tipoRemitente) {

        Mensaje mensaje = new Mensaje();
        mensaje.setIdMensaje(idMensaje);
        mensaje.setContenido("Mensaje " + idMensaje);
        mensaje.setFechaHora(LocalDateTime.of(2026, 1, 1, 9, 0));
        mensaje.setLeido(false);
        mensaje.setTipoRemitente(tipoRemitente);
        mensaje.setIdAsignacion(9L);
        return mensaje;
    }
}

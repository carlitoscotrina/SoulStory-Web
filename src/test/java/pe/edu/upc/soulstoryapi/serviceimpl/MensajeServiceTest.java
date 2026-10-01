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
class MensajeServiceTest {

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
    void adultoConAsignacionActivaEnviaMensajeCorrectamente() {

        Asignacion asignacion = crearAsignacion(9L);
        when(asignacionService
                .obtenerAsignacionAutorizada(2L, 1L))
                .thenReturn(asignacion);
        when(mensajeRepository.save(any(Mensaje.class)))
                .thenAnswer(invocacion -> {
                    Mensaje mensaje = invocacion.getArgument(0);
                    mensaje.setIdMensaje(20L);
                    return mensaje;
                });

        MensajeConversacionDTO respuesta =
                mensajeService.enviarMensajeAdultoMayor(
                        1L,
                        2L,
                        crearDTO("Buenos dias")
                );

        ArgumentCaptor<Mensaje> captor =
                ArgumentCaptor.forClass(Mensaje.class);

        verify(mensajeRepository).save(captor.capture());

        Mensaje mensajeGuardado = captor.getValue();

        assertEquals(20L, respuesta.getIdMensaje());
        assertEquals("ADULTO_MAYOR", mensajeGuardado.getTipoRemitente());
        assertFalse(mensajeGuardado.getLeido());
        assertEquals(9L, mensajeGuardado.getIdAsignacion());
    }

    @Test
    void mensajeVacioEsInvalido() {

        when(asignacionService
                .obtenerAsignacionAutorizada(2L, 1L))
                .thenReturn(crearAsignacion(9L));

        assertThrows(
                IllegalArgumentException.class,
                () -> mensajeService.enviarMensajeAdultoMayor(
                        1L,
                        2L,
                        crearDTO(" ")
                )
        );

        verifyNoInteractions(mensajeRepository);
    }

    @Test
    void cuidadorNoRelacionadoNoPuedeRecibirMensaje() {

        when(asignacionService
                .obtenerAsignacionAutorizada(2L, 1L))
                .thenThrow(new AccesoNoAutorizadoException(
                        "El cuidador no tiene una asignacion activa con el adulto mayor"
                ));

        assertThrows(
                AccesoNoAutorizadoException.class,
                () -> mensajeService.enviarMensajeAdultoMayor(
                        1L,
                        2L,
                        crearDTO("Hola")
                )
        );

        verifyNoInteractions(mensajeRepository);
    }

    @Test
    void conversacionDevuelveMensajesOrdenadosCronologicamente() {

        Asignacion asignacion = crearAsignacion(9L);
        Mensaje primero = crearMensaje(
                1L,
                LocalDateTime.of(2026, 1, 1, 9, 0)
        );
        Mensaje segundo = crearMensaje(
                2L,
                LocalDateTime.of(2026, 1, 1, 10, 0)
        );

        when(asignacionService
                .obtenerAsignacionAutorizada(2L, 1L))
                .thenReturn(asignacion);
        when(mensajeRepository
                .findByIdAsignacionOrderByFechaHoraAsc(9L))
                .thenReturn(List.of(primero, segundo));

        List<MensajeConversacionDTO> conversacion =
                mensajeService.listarConversacion(1L, 2L);

        assertEquals(2, conversacion.size());
        assertEquals(1L, conversacion.get(0).getIdMensaje());
        assertEquals(2L, conversacion.get(1).getIdMensaje());
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
            LocalDateTime fechaHora) {

        Mensaje mensaje = new Mensaje();
        mensaje.setIdMensaje(idMensaje);
        mensaje.setContenido("Mensaje " + idMensaje);
        mensaje.setFechaHora(fechaHora);
        mensaje.setLeido(false);
        mensaje.setTipoRemitente("ADULTO_MAYOR");
        mensaje.setIdAsignacion(9L);
        return mensaje;
    }
}

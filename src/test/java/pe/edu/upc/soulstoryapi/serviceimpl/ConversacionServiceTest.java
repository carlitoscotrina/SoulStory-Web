package pe.edu.upc.soulstoryapi.serviceimpl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pe.edu.upc.soulstoryapi.dto.ConversacionResumenDTO;
import pe.edu.upc.soulstoryapi.entity.AdultoMayor;
import pe.edu.upc.soulstoryapi.entity.Asignacion;
import pe.edu.upc.soulstoryapi.entity.Cuidador;
import pe.edu.upc.soulstoryapi.entity.Mensaje;
import pe.edu.upc.soulstoryapi.entity.Usuario;
import pe.edu.upc.soulstoryapi.repository.AdultoMayorRepository;
import pe.edu.upc.soulstoryapi.repository.AsignacionRepository;
import pe.edu.upc.soulstoryapi.repository.CuidadorRepository;
import pe.edu.upc.soulstoryapi.repository.MensajeRepository;
import pe.edu.upc.soulstoryapi.repository.UsuarioRepository;
import pe.edu.upc.soulstoryapi.service.ConversacionService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ConversacionServiceTest {

    @Mock
    private AsignacionRepository asignacionRepository;

    @Mock
    private MensajeRepository mensajeRepository;

    @Mock
    private CuidadorRepository cuidadorRepository;

    @Mock
    private AdultoMayorRepository adultoMayorRepository;

    @Mock
    private UsuarioRepository usuarioRepository;

    private ConversacionService conversacionService;

    @BeforeEach
    void prepararServicio() {

        conversacionService = new ConversacionServiceImpl(
                asignacionRepository,
                mensajeRepository,
                cuidadorRepository,
                adultoMayorRepository,
                usuarioRepository
        );
    }

    @Test
    void cuidadorListaSusConversacionesActivas() {

        prepararConversacion(9L, 3L, Optional.empty());

        List<ConversacionResumenDTO> conversaciones =
                conversacionService.listarConversacionesCuidador(2L);

        assertEquals(1, conversaciones.size());
        assertEquals(9L, conversaciones.get(0).getIdAsignacion());
        assertEquals(3L, conversaciones.get(0).getIdAdultoMayor());
        assertEquals("Carlos Cotrina", conversaciones.get(0)
                .getNombreAdultoMayor());
    }

    @Test
    void conversacionConMensajesMuestraUltimoMensaje() {

        Mensaje ultimoMensaje = new Mensaje();
        ultimoMensaje.setContenido("Nos vemos manana");
        ultimoMensaje.setFechaHora(
                LocalDateTime.of(2026, 9, 28, 16, 30)
        );

        prepararConversacion(9L, 3L, Optional.of(ultimoMensaje));

        ConversacionResumenDTO resumen = conversacionService
                .listarConversacionesCuidador(2L)
                .get(0);

        assertEquals("Nos vemos manana", resumen.getUltimoMensaje());
        assertEquals(
                LocalDateTime.of(2026, 9, 28, 16, 30),
                resumen.getFechaUltimoMensaje()
        );
    }

    @Test
    void conversacionSinMensajesDevuelveUltimoMensajeNulo() {

        prepararConversacion(9L, 3L, Optional.empty());

        ConversacionResumenDTO resumen = conversacionService
                .listarConversacionesCuidador(2L)
                .get(0);

        assertNull(resumen.getUltimoMensaje());
        assertNull(resumen.getFechaUltimoMensaje());
    }

    @Test
    void cuidadorSinAsignacionesActivasRecibeListaVacia() {

        when(cuidadorRepository.findById(2L))
                .thenReturn(Optional.of(new Cuidador()));
        when(asignacionRepository
                .findByIdCuidadorAndEstadoIgnoreCaseOrderByFechaInicioDesc(
                        2L,
                        "ACTIVA"
                ))
                .thenReturn(List.of());

        List<ConversacionResumenDTO> conversaciones =
                conversacionService.listarConversacionesCuidador(2L);

        assertTrue(conversaciones.isEmpty());
    }

    private void prepararConversacion(
            Long idAsignacion,
            Long idAdultoMayor,
            Optional<Mensaje> ultimoMensaje) {

        Asignacion asignacion = new Asignacion();
        asignacion.setIdAsignacion(idAsignacion);
        asignacion.setIdAdultoMayor(idAdultoMayor);
        asignacion.setEstado("ACTIVA");

        AdultoMayor adultoMayor = new AdultoMayor();
        adultoMayor.setIdAdultoMayor(idAdultoMayor);
        adultoMayor.setIdUsuario(30L);

        Usuario usuario = new Usuario();
        usuario.setIdUsuario(30L);
        usuario.setNombreCompleto("Carlos Cotrina");

        when(cuidadorRepository.findById(2L))
                .thenReturn(Optional.of(new Cuidador()));
        when(asignacionRepository
                .findByIdCuidadorAndEstadoIgnoreCaseOrderByFechaInicioDesc(
                        2L,
                        "ACTIVA"
                ))
                .thenReturn(List.of(asignacion));
        when(adultoMayorRepository.findById(idAdultoMayor))
                .thenReturn(Optional.of(adultoMayor));
        when(usuarioRepository.findById(30L))
                .thenReturn(Optional.of(usuario));
        when(mensajeRepository
                .findFirstByIdAsignacionOrderByFechaHoraDesc(idAsignacion))
                .thenReturn(ultimoMensaje);
    }
}

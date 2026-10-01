package pe.edu.upc.soulstoryapi.serviceimpl;

import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pe.edu.upc.soulstoryapi.dto.EstadoSuscripcionDTO;
import pe.edu.upc.soulstoryapi.dto.PagoRespuestaDTO;
import pe.edu.upc.soulstoryapi.dto.ProcesarPagoDTO;
import pe.edu.upc.soulstoryapi.dto.SuscripcionRespuestaDTO;
import pe.edu.upc.soulstoryapi.entity.AdultoMayor;
import pe.edu.upc.soulstoryapi.entity.Pago;
import pe.edu.upc.soulstoryapi.entity.Plan;
import pe.edu.upc.soulstoryapi.entity.Suscripcion;
import pe.edu.upc.soulstoryapi.entity.Usuario;
import pe.edu.upc.soulstoryapi.exception.AccesoNoAutorizadoException;
import pe.edu.upc.soulstoryapi.exception.AdultoMayorNoEncontradoException;
import pe.edu.upc.soulstoryapi.exception.PagoRechazadoException;
import pe.edu.upc.soulstoryapi.exception.PlanNoEncontradoException;
import pe.edu.upc.soulstoryapi.exception.ServicioPagoNoDisponibleException;
import pe.edu.upc.soulstoryapi.exception.SuscripcionYaActivaException;
import pe.edu.upc.soulstoryapi.repository.AdultoMayorRepository;
import pe.edu.upc.soulstoryapi.repository.PagoRepository;
import pe.edu.upc.soulstoryapi.repository.PlanRepository;
import pe.edu.upc.soulstoryapi.repository.SuscripcionRepository;
import pe.edu.upc.soulstoryapi.repository.UsuarioRepository;
import pe.edu.upc.soulstoryapi.service.PasarelaPagoService;
import pe.edu.upc.soulstoryapi.service.SuscripcionService;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SuscripcionServiceTest {

    @Mock
    private SuscripcionRepository suscripcionRepository;

    @Mock
    private PagoRepository pagoRepository;

    @Mock
    private PlanRepository planRepository;

    @Mock
    private AdultoMayorRepository adultoMayorRepository;

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private PasarelaPagoService pasarelaPagoService;

    private SuscripcionService suscripcionService;

    @BeforeEach
    void prepararServicio() {

        suscripcionService = new SuscripcionServiceImpl(
                suscripcionRepository,
                pagoRepository,
                planRepository,
                adultoMayorRepository,
                usuarioRepository,
                pasarelaPagoService
        );
    }

    @Test
    void seleccionarPlanCreaSuscripcionPendiente() {

        prepararAdultoMayor();
        when(planRepository.findById(2L))
                .thenReturn(Optional.of(crearPlan(2L, 25.0)));
        when(suscripcionRepository
                .findFirstByIdAdultoMayorAndIdPlanAndEstadoTrueOrderByIdSuscripcionDesc(
                        1L,
                        2L
                ))
                .thenReturn(Optional.empty());
        when(suscripcionRepository.save(any(Suscripcion.class)))
                .thenAnswer(invocacion -> {
                    Suscripcion suscripcion = invocacion.getArgument(0);
                    suscripcion.setIdSuscripcion(8);
                    return suscripcion;
                });

        SuscripcionRespuestaDTO respuesta = suscripcionService
                .seleccionarPlan(1L, 2L);

        assertEquals(8, respuesta.getIdSuscripcion());
        assertFalse(respuesta.getEstado());
        assertNull(respuesta.getFechaInicio());
        assertNull(respuesta.getFechaFin());
        assertNull(respuesta.getIdPago());
        assertEquals("Plan de prueba", respuesta.getNombrePlan());
    }

    @Test
    void adultoMayorInexistenteNoPuedeSeleccionarPlan() {

        when(adultoMayorRepository.findById(1L))
                .thenReturn(Optional.empty());

        assertThrows(
                AdultoMayorNoEncontradoException.class,
                () -> suscripcionService.seleccionarPlan(1L, 2L)
        );
    }

    @Test
    void planInexistenteNoPuedeSeleccionarse() {

        prepararAdultoMayor();
        when(planRepository.findById(2L))
                .thenReturn(Optional.empty());

        assertThrows(
                PlanNoEncontradoException.class,
                () -> suscripcionService.seleccionarPlan(1L, 2L)
        );
    }

    @Test
    void planActivoDuplicadoEsRechazado() {

        prepararAdultoMayor();
        when(planRepository.findById(2L))
                .thenReturn(Optional.of(crearPlan(2L, 25.0)));
        when(suscripcionRepository
                .findFirstByIdAdultoMayorAndIdPlanAndEstadoTrueOrderByIdSuscripcionDesc(
                        1L,
                        2L
                ))
                .thenReturn(Optional.of(crearSuscripcion(
                        8,
                        1L,
                        2L,
                        true,
                        LocalDateTime.now(),
                        LocalDateTime.now().plusMonths(1),
                        20L
                )));

        assertThrows(
                SuscripcionYaActivaException.class,
                () -> suscripcionService.seleccionarPlan(1L, 2L)
        );
    }

    @Test
    void pagoAprobadoCreaPagoConMontoDelPlan() {

        Suscripcion suscripcion = prepararPagoPendiente(25.0);
        prepararGuardadoPagoExitoso();

        PagoRespuestaDTO respuesta = suscripcionService.procesarPago(
                1L,
                8,
                crearPagoDTO("TARJETA")
        );

        ArgumentCaptor<Pago> captor = ArgumentCaptor.forClass(Pago.class);
        verify(pagoRepository).save(captor.capture());

        Pago pago = captor.getValue();
        assertEquals(25.0, pago.getMonto());
        assertEquals(10L, pago.getIdUsuario());
        assertTrue(pago.getEstado());
        assertEquals(12L, respuesta.getIdPago());
        assertEquals(25.0, respuesta.getMonto());
        assertTrue(suscripcion.getEstado());
    }

    @Test
    void pagoAprobadoActivaSuscripcion() {

        Suscripcion suscripcion = prepararPagoPendiente(35.0);
        prepararGuardadoPagoExitoso();

        PagoRespuestaDTO respuesta = suscripcionService.procesarPago(
                1L,
                8,
                crearPagoDTO("TARJETA")
        );

        ArgumentCaptor<Suscripcion> captor =
                ArgumentCaptor.forClass(Suscripcion.class);
        verify(suscripcionRepository).save(captor.capture());

        Suscripcion suscripcionGuardada = captor.getValue();
        assertTrue(suscripcionGuardada.getEstado());
        assertEquals(12L, suscripcionGuardada.getIdPago());
        assertEquals(
                suscripcionGuardada.getFechaInicio()
                        .plusMonths(SuscripcionService.DURACION_SUSCRIPCION_MESES),
                suscripcionGuardada.getFechaFin()
        );
        assertTrue(respuesta.getSuscripcionActiva());
    }

    @Test
    void montoNoProvieneDelClienteSinoDelPlan() {

        prepararPagoPendiente(99.5);
        prepararGuardadoPagoExitoso();

        PagoRespuestaDTO respuesta = suscripcionService.procesarPago(
                1L,
                8,
                crearPagoDTO("TARJETA")
        );

        assertEquals(99.5, respuesta.getMonto());
    }

    @Test
    void metodoPagoVacioEsInvalido() {

        ProcesarPagoDTO dto = crearPagoDTO(" ");

        try (ValidatorFactory fabrica =
                     Validation.buildDefaultValidatorFactory()) {

            Validator validador = fabrica.getValidator();

            assertFalse(validador.validate(dto).isEmpty());
        }
    }

    @Test
    void pagoRechazadoNoActivaSuscripcionNiGuardaPago() {

        Suscripcion suscripcion = prepararPagoPendiente(25.0);
        doThrow(new PagoRechazadoException("El pago fue rechazado"))
                .when(pasarelaPagoService)
                .procesarPago("RECHAZADO");

        assertThrows(
                PagoRechazadoException.class,
                () -> suscripcionService.procesarPago(
                        1L,
                        8,
                        crearPagoDTO("RECHAZADO")
                )
        );

        assertFalse(suscripcion.getEstado());
        assertNull(suscripcion.getIdPago());
        verifyNoInteractions(pagoRepository);
    }

    @Test
    void servicioPagoCaidoNoActivaSuscripcion() {

        Suscripcion suscripcion = prepararPagoPendiente(25.0);
        doThrow(new ServicioPagoNoDisponibleException(
                "El servicio de pago no está disponible"
        )).when(pasarelaPagoService).procesarPago("SERVICIO_CAIDO");

        assertThrows(
                ServicioPagoNoDisponibleException.class,
                () -> suscripcionService.procesarPago(
                        1L,
                        8,
                        crearPagoDTO("SERVICIO_CAIDO")
                )
        );

        assertFalse(suscripcion.getEstado());
        verifyNoInteractions(pagoRepository);
    }

    @Test
    void suscripcionDeOtroAdultoNoPuedePagarse() {

        prepararAdultoMayor();
        when(suscripcionRepository.findById(8))
                .thenReturn(Optional.of(crearSuscripcion(
                        8,
                        2L,
                        2L,
                        false,
                        null,
                        null,
                        null
                )));

        assertThrows(
                AccesoNoAutorizadoException.class,
                () -> suscripcionService.procesarPago(
                        1L,
                        8,
                        crearPagoDTO("TARJETA")
                )
        );

        verifyNoInteractions(pagoRepository, pasarelaPagoService);
    }

    @Test
    void suscripcionActivaNoPuedePagarseNuevamente() {

        prepararAdultoMayor();
        when(suscripcionRepository.findById(8))
                .thenReturn(Optional.of(crearSuscripcion(
                        8,
                        1L,
                        2L,
                        true,
                        LocalDateTime.now(),
                        LocalDateTime.now().plusMonths(1),
                        12L
                )));

        assertThrows(
                SuscripcionYaActivaException.class,
                () -> suscripcionService.procesarPago(
                        1L,
                        8,
                        crearPagoDTO("TARJETA")
                )
        );

        verifyNoInteractions(pagoRepository, pasarelaPagoService);
    }

    @Test
    void suscripcionConFechaFuturaEstaActiva() {

        prepararEstado(crearSuscripcion(
                8,
                1L,
                2L,
                true,
                LocalDateTime.now().minusDays(1),
                LocalDateTime.now().plusDays(1),
                12L
        ));

        EstadoSuscripcionDTO respuesta = suscripcionService
                .consultarEstado(1L);

        assertEquals("ACTIVA", respuesta.getEstado());
        assertEquals("Plan de prueba", respuesta.getNombrePlan());
    }

    @Test
    void suscripcionConFechaPasadaEstaVencida() {

        prepararEstado(crearSuscripcion(
                8,
                1L,
                2L,
                true,
                LocalDateTime.now().minusMonths(2),
                LocalDateTime.now().minusMinutes(1),
                12L
        ));

        assertEquals(
                "VENCIDA",
                suscripcionService.consultarEstado(1L).getEstado()
        );
    }

    @Test
    void suscripcionInactivaConPagoPrevioEstaCancelada() {

        prepararEstado(crearSuscripcion(
                8,
                1L,
                2L,
                false,
                LocalDateTime.now().minusDays(1),
                LocalDateTime.now().plusDays(29),
                12L
        ));

        assertEquals(
                "CANCELADA",
                suscripcionService.consultarEstado(1L).getEstado()
        );
    }

    @Test
    void suscripcionSinPagoEstaPendiente() {

        prepararEstado(crearSuscripcion(
                8,
                1L,
                2L,
                false,
                null,
                null,
                null
        ));

        assertEquals(
                "PENDIENTE",
                suscripcionService.consultarEstado(1L).getEstado()
        );
    }

    @Test
    void adultoSinSuscripcionObtieneEstadoSinSuscripcion() {

        prepararAdultoMayor();
        when(suscripcionRepository
                .findFirstByIdAdultoMayorOrderByIdSuscripcionDesc(1L))
                .thenReturn(Optional.empty());

        EstadoSuscripcionDTO respuesta = suscripcionService
                .consultarEstado(1L);

        assertEquals("SIN_SUSCRIPCION", respuesta.getEstado());
        assertNull(respuesta.getIdSuscripcion());
        assertNull(respuesta.getIdPlan());
    }

    private Suscripcion prepararPagoPendiente(Double precioPlan) {

        prepararAdultoMayor();
        prepararUsuario();

        Suscripcion suscripcion = crearSuscripcion(
                8,
                1L,
                2L,
                false,
                null,
                null,
                null
        );

        when(suscripcionRepository.findById(8))
                .thenReturn(Optional.of(suscripcion));
        when(planRepository.findById(2L))
                .thenReturn(Optional.of(crearPlan(2L, precioPlan)));

        return suscripcion;
    }

    private void prepararGuardadoPagoExitoso() {

        when(pagoRepository.save(any(Pago.class)))
                .thenAnswer(invocacion -> {
                    Pago pago = invocacion.getArgument(0);
                    pago.setIdPago(12L);
                    return pago;
                });
        when(suscripcionRepository.save(any(Suscripcion.class)))
                .thenAnswer(invocacion -> invocacion.getArgument(0));
    }

    private void prepararEstado(Suscripcion suscripcion) {

        prepararAdultoMayor();
        when(suscripcionRepository
                .findFirstByIdAdultoMayorOrderByIdSuscripcionDesc(1L))
                .thenReturn(Optional.of(suscripcion));
        when(planRepository.findById(2L))
                .thenReturn(Optional.of(crearPlan(2L, 25.0)));
    }

    private void prepararAdultoMayor() {

        AdultoMayor adultoMayor = new AdultoMayor();
        adultoMayor.setIdAdultoMayor(1L);
        adultoMayor.setIdUsuario(10L);

        when(adultoMayorRepository.findById(1L))
                .thenReturn(Optional.of(adultoMayor));
    }

    private void prepararUsuario() {

        Usuario usuario = new Usuario();
        usuario.setIdUsuario(10L);

        when(usuarioRepository.findById(10L))
                .thenReturn(Optional.of(usuario));
    }

    private Plan crearPlan(Long idPlan, Double precio) {

        Plan plan = new Plan();
        plan.setIdPlan(idPlan);
        plan.setNombrePlan("Plan de prueba");
        plan.setPrecio(precio);
        return plan;
    }

    private Suscripcion crearSuscripcion(
            Integer idSuscripcion,
            Long idAdultoMayor,
            Long idPlan,
            Boolean estado,
            LocalDateTime fechaInicio,
            LocalDateTime fechaFin,
            Long idPago) {

        Suscripcion suscripcion = new Suscripcion();
        suscripcion.setIdSuscripcion(idSuscripcion);
        suscripcion.setIdAdultoMayor(idAdultoMayor);
        suscripcion.setIdPlan(idPlan);
        suscripcion.setEstado(estado);
        suscripcion.setFechaInicio(fechaInicio);
        suscripcion.setFechaFin(fechaFin);
        suscripcion.setIdPago(idPago);
        return suscripcion;
    }

    private ProcesarPagoDTO crearPagoDTO(String metodoPago) {

        ProcesarPagoDTO dto = new ProcesarPagoDTO();
        dto.setMetodoPago(metodoPago);
        return dto;
    }
}

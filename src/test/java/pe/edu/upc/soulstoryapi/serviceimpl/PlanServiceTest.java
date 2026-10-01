package pe.edu.upc.soulstoryapi.serviceimpl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pe.edu.upc.soulstoryapi.dto.PlanRespuestaDTO;
import pe.edu.upc.soulstoryapi.entity.AdultoMayor;
import pe.edu.upc.soulstoryapi.entity.Plan;
import pe.edu.upc.soulstoryapi.exception.AdultoMayorNoEncontradoException;
import pe.edu.upc.soulstoryapi.exception.PlanNoEncontradoException;
import pe.edu.upc.soulstoryapi.repository.AdultoMayorRepository;
import pe.edu.upc.soulstoryapi.repository.PlanRepository;
import pe.edu.upc.soulstoryapi.service.PlanService;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PlanServiceTest {

    @Mock
    private PlanRepository planRepository;

    @Mock
    private AdultoMayorRepository adultoMayorRepository;

    private PlanService planService;

    @BeforeEach
    void prepararServicio() {

        planService = new PlanServiceImpl(
                planRepository,
                adultoMayorRepository
        );
    }

    @Test
    void adultoExistentePuedeListarPlanes() {

        prepararAdultoMayor();
        when(planRepository.findAll())
                .thenReturn(List.of(crearPlan(1L, 19.9)));

        List<PlanRespuestaDTO> planes = planService.listarPlanes(1L);

        assertEquals(1, planes.size());
        assertEquals(1L, planes.get(0).getIdPlan());
    }

    @Test
    void catalogoVacioDevuelveListaVacia() {

        prepararAdultoMayor();
        when(planRepository.findAll()).thenReturn(List.of());

        List<PlanRespuestaDTO> planes = planService.listarPlanes(1L);

        assertTrue(planes.isEmpty());
    }

    @Test
    void dtoDevuelvePrecioDelPlan() {

        prepararAdultoMayor();
        when(planRepository.findAll())
                .thenReturn(List.of(crearPlan(1L, 19.9)));

        PlanRespuestaDTO respuesta = planService
                .listarPlanes(1L)
                .get(0);

        assertEquals(19.9, respuesta.getPrecio());
    }

    @Test
    void detalleDevuelvePlanSolicitado() {

        prepararAdultoMayor();
        when(planRepository.findById(1L))
                .thenReturn(Optional.of(crearPlan(1L, 19.9)));

        PlanRespuestaDTO respuesta = planService.obtenerPlan(1L, 1L);

        assertEquals("Plan de prueba", respuesta.getNombrePlan());
        assertEquals(100, respuesta.getLimiteRecuerdos());
    }

    @Test
    void planInexistenteLanzaExcepcion() {

        prepararAdultoMayor();
        when(planRepository.findById(9L))
                .thenReturn(Optional.empty());

        assertThrows(
                PlanNoEncontradoException.class,
                () -> planService.obtenerPlan(1L, 9L)
        );
    }

    @Test
    void adultoMayorInexistenteLanzaExcepcion() {

        when(adultoMayorRepository.findById(1L))
                .thenReturn(Optional.empty());

        assertThrows(
                AdultoMayorNoEncontradoException.class,
                () -> planService.listarPlanes(1L)
        );
    }

    private void prepararAdultoMayor() {

        when(adultoMayorRepository.findById(1L))
                .thenReturn(Optional.of(new AdultoMayor()));
    }

    private Plan crearPlan(Long idPlan, Double precio) {

        Plan plan = new Plan();
        plan.setIdPlan(idPlan);
        plan.setNombrePlan("Plan de prueba");
        plan.setPrecio(precio);
        plan.setLimiteRecuerdos(100);
        plan.setDescripcion("Datos usados solo por la prueba");
        return plan;
    }
}

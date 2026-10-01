package pe.edu.upc.soulstoryapi.serviceimpl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pe.edu.upc.soulstoryapi.dto.CrearRecuerdoTextoDTO;
import pe.edu.upc.soulstoryapi.dto.RecuerdoRespuestaDTO;
import pe.edu.upc.soulstoryapi.entity.AdultoMayor;
import pe.edu.upc.soulstoryapi.entity.Recuerdo;
import pe.edu.upc.soulstoryapi.exception.AccesoNoAutorizadoException;
import pe.edu.upc.soulstoryapi.repository.AdultoMayorRepository;
import pe.edu.upc.soulstoryapi.repository.RecuerdoRepository;
import pe.edu.upc.soulstoryapi.service.ArchivoRecuerdoService;
import pe.edu.upc.soulstoryapi.service.AsignacionService;
import pe.edu.upc.soulstoryapi.service.RecuerdoService;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RecuerdoServiceTest {

    @Mock
    private RecuerdoRepository recuerdoRepository;

    @Mock
    private AdultoMayorRepository adultoMayorRepository;

    @Mock
    private ArchivoRecuerdoService archivoRecuerdoService;

    @Mock
    private AsignacionService asignacionService;

    private RecuerdoService recuerdoService;

    @BeforeEach
    void prepararServicio() {

        recuerdoService = new RecuerdoServiceImpl(
                recuerdoRepository,
                adultoMayorRepository,
                archivoRecuerdoService,
                asignacionService
        );
    }

    @Test
    void cuidadorNoAsignadoNoPuedeCrearRecuerdo() {

        CrearRecuerdoTextoDTO dto = new CrearRecuerdoTextoDTO();
        dto.setTituloRecuerdo("Una historia");
        dto.setContenido("Contenido del recuerdo");

        when(asignacionService.obtenerAsignacionAutorizada(1L, 2L))
                .thenThrow(new AccesoNoAutorizadoException(
                        "El cuidador no tiene una asignación activa con el adulto mayor"
                ));

        assertThrows(
                AccesoNoAutorizadoException.class,
                () -> recuerdoService.crearRecuerdoTextoComoCuidador(
                        1L,
                        2L,
                        dto
                )
        );

        verifyNoInteractions(recuerdoRepository);
    }

    @Test
    void alternarFavoritoCambiaDeFalsoAVerdadero() {

        Recuerdo recuerdo = crearRecuerdo(false);

        when(adultoMayorRepository.findById(1L))
                .thenReturn(Optional.of(new AdultoMayor()));
        when(recuerdoRepository.findById(10L))
                .thenReturn(Optional.of(recuerdo));
        when(recuerdoRepository.save(recuerdo))
                .thenReturn(recuerdo);

        RecuerdoRespuestaDTO respuesta =
                recuerdoService.alternarFavoritoComoAdultoMayor(
                        1L,
                        10L
                );

        assertTrue(respuesta.getFavorito());
    }

    @Test
    void alternarFavoritoCambiaDeVerdaderoAFalso() {

        Recuerdo recuerdo = crearRecuerdo(true);

        when(adultoMayorRepository.findById(1L))
                .thenReturn(Optional.of(new AdultoMayor()));
        when(recuerdoRepository.findById(10L))
                .thenReturn(Optional.of(recuerdo));
        when(recuerdoRepository.save(recuerdo))
                .thenReturn(recuerdo);

        RecuerdoRespuestaDTO respuesta =
                recuerdoService.alternarFavoritoComoAdultoMayor(
                        1L,
                        10L
                );

        assertFalse(respuesta.getFavorito());
    }

    private Recuerdo crearRecuerdo(Boolean favorito) {

        Recuerdo recuerdo = new Recuerdo();

        recuerdo.setIdRecuerdo(10L);
        recuerdo.setTituloRecuerdo("Mi recuerdo");
        recuerdo.setTipoRecuerdo("TEXTO");
        recuerdo.setContenido("Contenido");
        recuerdo.setFormato("text/plain");
        recuerdo.setFavorito(favorito);
        recuerdo.setIdAdultoMayor(1L);

        return recuerdo;
    }
}

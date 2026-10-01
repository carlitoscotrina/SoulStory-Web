package pe.edu.upc.soulstoryapi.serviceimpl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pe.edu.upc.soulstoryapi.dto.CrearGrupoDTO;
import pe.edu.upc.soulstoryapi.dto.GrupoRespuestaDTO;
import pe.edu.upc.soulstoryapi.entity.AdultoMayor;
import pe.edu.upc.soulstoryapi.entity.Grupo;
import pe.edu.upc.soulstoryapi.exception.AccesoNoAutorizadoException;
import pe.edu.upc.soulstoryapi.repository.AdultoMayorRepository;
import pe.edu.upc.soulstoryapi.repository.GrupoRepository;
import pe.edu.upc.soulstoryapi.service.GrupoService;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GrupoServiceTest {

    @Mock
    private GrupoRepository grupoRepository;

    @Mock
    private AdultoMayorRepository adultoMayorRepository;

    private GrupoService grupoService;

    @BeforeEach
    void prepararServicio() {

        grupoService = new GrupoServiceImpl(
                grupoRepository,
                adultoMayorRepository
        );
    }

    @Test
    void crearGrupoGuardaYDevuelveGrupoCorrecto() {

        CrearGrupoDTO dto = new CrearGrupoDTO();
        dto.setNombreGrupo("Familia");
        dto.setDescripcion("Recuerdos familiares");

        when(adultoMayorRepository.findById(1L))
                .thenReturn(Optional.of(new AdultoMayor()));
        when(grupoRepository.save(any(Grupo.class)))
                .thenAnswer(invocacion -> {
                    Grupo grupo = invocacion.getArgument(0);
                    grupo.setIdGrupo(8L);
                    return grupo;
                });

        GrupoRespuestaDTO respuesta = grupoService.crearGrupo(
                1L,
                dto
        );

        assertEquals(8L, respuesta.getIdGrupo());
        assertEquals("Familia", respuesta.getNombreGrupo());
        assertEquals("Recuerdos familiares", respuesta.getDescripcion());
        assertEquals(1L, respuesta.getIdAdultoMayor());
    }

    @Test
    void otroAdultoMayorNoPuedeConsultarGrupoAjeno() {

        Grupo grupo = new Grupo();
        grupo.setIdGrupo(8L);
        grupo.setNombreGrupo("Familia");
        grupo.setIdAdultoMayor(1L);

        when(adultoMayorRepository.findById(2L))
                .thenReturn(Optional.of(new AdultoMayor()));
        when(grupoRepository.findById(8L))
                .thenReturn(Optional.of(grupo));

        assertThrows(
                AccesoNoAutorizadoException.class,
                () -> grupoService.obtenerGrupo(2L, 8L)
        );
    }
}

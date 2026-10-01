package pe.edu.upc.soulstoryapi.serviceimpl;

import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pe.edu.upc.soulstoryapi.dto.GenerarImagenIaDTO;
import pe.edu.upc.soulstoryapi.dto.ImagenIaRespuestaDTO;
import pe.edu.upc.soulstoryapi.dto.RecuerdoRespuestaDTO;
import pe.edu.upc.soulstoryapi.entity.GaleriaIa;
import pe.edu.upc.soulstoryapi.exception.ImagenIaPendienteNoEncontradaException;
import pe.edu.upc.soulstoryapi.exception.ServicioIaNoDisponibleException;
import pe.edu.upc.soulstoryapi.repository.GaleriaIaRepository;
import pe.edu.upc.soulstoryapi.service.ArchivoRecuerdoService;
import pe.edu.upc.soulstoryapi.service.AsignacionService;
import pe.edu.upc.soulstoryapi.service.ImagenIaService;
import pe.edu.upc.soulstoryapi.service.OpenAiImageService;
import pe.edu.upc.soulstoryapi.service.RecuerdoService;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ImagenIaServiceTest {

    @Mock
    private OpenAiImageService openAiImageService;

    @Mock
    private ArchivoRecuerdoService archivoRecuerdoService;

    @Mock
    private RecuerdoService recuerdoService;

    @Mock
    private AsignacionService asignacionService;

    @Mock
    private GaleriaIaRepository galeriaIaRepository;

    private ImagenIaService imagenIaService;

    @BeforeEach
    void prepararServicio() {

        imagenIaService = new ImagenIaServiceImpl(
                openAiImageService,
                archivoRecuerdoService,
                recuerdoService,
                asignacionService,
                galeriaIaRepository
        );
    }

    @Test
    void openAiSinApiKeyLanzaExcepcionControlada() {

        OpenAiImageService servicio = new OpenAiImageServiceImpl(
                "https://api.openai.com/v1",
                "",
                "gpt-image-2.5-flare"
        );

        assertThrows(
                ServicioIaNoDisponibleException.class,
                () -> servicio.generarImagen("Una familia frente al mar")
        );
    }

    @Test
    void respuestaValidaDeIaCreaImagenPendiente() {

        GenerarImagenIaDTO dto = crearSolicitud();
        byte[] imagenGenerada = new byte[]{1, 2, 3};

        when(openAiImageService.generarImagen(dto.getPrompt()))
                .thenReturn(imagenGenerada);
        when(archivoRecuerdoService.guardarImagenIa(
                imagenGenerada
        )).thenReturn("ia/imagen.png");

        ImagenIaRespuestaDTO respuesta = imagenIaService
                .generarImagen(dto);

        assertNotNull(respuesta.getToken());
        assertEquals(dto.getTitulo(), respuesta.getTitulo());
        assertEquals(dto.getPrompt(), respuesta.getPrompt());
        assertEquals(
                "/api/ia/imagenes/" + respuesta.getToken(),
                respuesta.getUrlImagen()
        );
    }

    @Test
    void confirmarCreaRecuerdoYGaleriaIa() {

        GenerarImagenIaDTO dto = crearSolicitud();
        byte[] imagenGenerada = new byte[]{1, 2, 3};

        when(openAiImageService.generarImagen(dto.getPrompt()))
                .thenReturn(imagenGenerada);
        when(archivoRecuerdoService.guardarImagenIa(
                imagenGenerada
        )).thenReturn("ia/imagen.png");

        RecuerdoRespuestaDTO recuerdo = new RecuerdoRespuestaDTO();
        recuerdo.setIdRecuerdo(20L);

        when(recuerdoService.crearRecuerdoImagenIa(
                1L,
                null,
                dto.getTitulo(),
                "ia/imagen.png"
        )).thenReturn(recuerdo);

        ImagenIaRespuestaDTO pendiente = imagenIaService
                .generarImagen(dto);

        RecuerdoRespuestaDTO respuesta = imagenIaService
                .confirmarComoAdultoMayor(
                        1L,
                        pendiente.getToken()
                );

        ArgumentCaptor<GaleriaIa> captor =
                ArgumentCaptor.forClass(GaleriaIa.class);

        verify(galeriaIaRepository).save(captor.capture());

        GaleriaIa galeriaIa = captor.getValue();

        assertEquals(20L, respuesta.getIdRecuerdo());
        assertEquals(dto.getPrompt(), galeriaIa.getPromptDescripcion());
        assertEquals(dto.getTitulo(), galeriaIa.getTitulo());
        assertEquals("ia/imagen.png", galeriaIa.getUrlImagen());
        assertEquals(20L, galeriaIa.getIdRecuerdo());
    }

    @Test
    void tokenInexistenteLanzaExcepcionControlada() {

        assertThrows(
                ImagenIaPendienteNoEncontradaException.class,
                () -> imagenIaService.obtenerPreview("token-inexistente")
        );
    }

    @Test
    void promptVacioODemasiadoLargoEsInvalido() {

        Validator validador = Validation
                .buildDefaultValidatorFactory()
                .getValidator();

        GenerarImagenIaDTO promptVacio = new GenerarImagenIaDTO();
        promptVacio.setTitulo("Mi recuerdo");
        promptVacio.setPrompt(" ");

        GenerarImagenIaDTO promptExtenso = new GenerarImagenIaDTO();
        promptExtenso.setTitulo("Mi recuerdo");
        promptExtenso.setPrompt("a".repeat(
                GenerarImagenIaDTO.MAX_PROMPT_IA + 1
        ));

        assertFalse(validador.validate(promptVacio).isEmpty());
        assertFalse(validador.validate(promptExtenso).isEmpty());
    }

    private GenerarImagenIaDTO crearSolicitud() {

        GenerarImagenIaDTO dto = new GenerarImagenIaDTO();

        dto.setTitulo("Una tarde en familia");
        dto.setPrompt("Una familia frente al mar al atardecer");

        return dto;
    }
}

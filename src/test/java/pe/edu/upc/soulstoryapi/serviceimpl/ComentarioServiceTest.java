package pe.edu.upc.soulstoryapi.serviceimpl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pe.edu.upc.soulstoryapi.dto.ComentarioRespuestaDTO;
import pe.edu.upc.soulstoryapi.dto.CrearComentarioDTO;
import pe.edu.upc.soulstoryapi.entity.Comentario;
import pe.edu.upc.soulstoryapi.entity.Recuerdo;
import pe.edu.upc.soulstoryapi.entity.Usuario;
import pe.edu.upc.soulstoryapi.exception.AccesoNoAutorizadoException;
import pe.edu.upc.soulstoryapi.repository.ComentarioRepository;
import pe.edu.upc.soulstoryapi.repository.UsuarioRepository;
import pe.edu.upc.soulstoryapi.service.AccesoRecuerdoService;
import pe.edu.upc.soulstoryapi.service.ComentarioService;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ComentarioServiceTest {

    @Mock
    private ComentarioRepository comentarioRepository;

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private AccesoRecuerdoService accesoRecuerdoService;

    private ComentarioService comentarioService;

    @BeforeEach
    void prepararServicio() {

        comentarioService = new ComentarioServiceImpl(
                comentarioRepository,
                usuarioRepository,
                accesoRecuerdoService
        );
    }

    @Test
    void usuarioAutorizadoPuedeComentar() {

        CrearComentarioDTO dto = crearDTO("Que bonito recuerdo");
        Usuario usuario = crearUsuario(100L, "Maria Perez");

        when(accesoRecuerdoService
                .validarAccesoUsuarioARecuerdo(100L, 50L))
                .thenReturn(new Recuerdo());
        when(comentarioRepository.save(any(Comentario.class)))
                .thenAnswer(invocacion -> {
                    Comentario comentario = invocacion.getArgument(0);
                    comentario.setIdComentario(8L);
                    return comentario;
                });
        when(usuarioRepository.findById(100L))
                .thenReturn(Optional.of(usuario));

        ComentarioRespuestaDTO respuesta =
                comentarioService.crearComentario(100L, 50L, dto);

        assertEquals(8L, respuesta.getIdComentario());
        assertEquals("Que bonito recuerdo", respuesta.getTextoComentario());
        assertEquals("Maria Perez", respuesta.getNombreCompleto());
    }

    @Test
    void comentarioVacioEsInvalido() {

        when(accesoRecuerdoService
                .validarAccesoUsuarioARecuerdo(100L, 50L))
                .thenReturn(new Recuerdo());

        assertThrows(
                IllegalArgumentException.class,
                () -> comentarioService.crearComentario(
                        100L,
                        50L,
                        crearDTO(" ")
                )
        );

        verifyNoInteractions(comentarioRepository);
    }

    @Test
    void usuarioAutorizadoPuedeListarComentarios() {

        Comentario comentario = crearComentario(8L, 200L, 50L);

        when(accesoRecuerdoService
                .validarAccesoUsuarioARecuerdo(100L, 50L))
                .thenReturn(new Recuerdo());
        when(comentarioRepository
                .findByIdRecuerdoOrderByIdComentarioAsc(50L))
                .thenReturn(List.of(comentario));
        when(usuarioRepository.findById(200L))
                .thenReturn(Optional.of(crearUsuario(200L, "Jose Diaz")));

        List<ComentarioRespuestaDTO> respuesta =
                comentarioService.listarComentarios(100L, 50L);

        assertEquals(1, respuesta.size());
        assertEquals("Jose Diaz", respuesta.get(0).getNombreCompleto());
    }

    @Test
    void recuerdoSinComentariosDevuelveListaVacia() {

        when(accesoRecuerdoService
                .validarAccesoUsuarioARecuerdo(100L, 50L))
                .thenReturn(new Recuerdo());
        when(comentarioRepository
                .findByIdRecuerdoOrderByIdComentarioAsc(50L))
                .thenReturn(List.of());

        List<ComentarioRespuestaDTO> respuesta =
                comentarioService.listarComentarios(100L, 50L);

        assertTrue(respuesta.isEmpty());
    }

    @Test
    void usuarioSinAccesoNoPuedeLeerComentarios() {

        when(accesoRecuerdoService
                .validarAccesoUsuarioARecuerdo(100L, 50L))
                .thenThrow(new AccesoNoAutorizadoException(
                        "El usuario no tiene acceso a este recuerdo"
                ));

        assertThrows(
                AccesoNoAutorizadoException.class,
                () -> comentarioService.listarComentarios(100L, 50L)
        );

        verifyNoInteractions(comentarioRepository);
    }

    private CrearComentarioDTO crearDTO(String textoComentario) {

        CrearComentarioDTO dto = new CrearComentarioDTO();
        dto.setTextoComentario(textoComentario);
        return dto;
    }

    private Comentario crearComentario(
            Long idComentario,
            Long idUsuario,
            Long idRecuerdo) {

        Comentario comentario = new Comentario();
        comentario.setIdComentario(idComentario);
        comentario.setTextoComentario("Hermoso recuerdo");
        comentario.setIdUsuario(idUsuario);
        comentario.setIdRecuerdo(idRecuerdo);
        return comentario;
    }

    private Usuario crearUsuario(
            Long idUsuario,
            String nombreCompleto) {

        Usuario usuario = new Usuario();
        usuario.setIdUsuario(idUsuario);
        usuario.setNombreCompleto(nombreCompleto);
        return usuario;
    }
}

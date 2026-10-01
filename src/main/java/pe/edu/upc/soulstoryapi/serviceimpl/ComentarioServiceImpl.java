package pe.edu.upc.soulstoryapi.serviceimpl;

import org.springframework.stereotype.Service;
import pe.edu.upc.soulstoryapi.dto.ComentarioRespuestaDTO;
import pe.edu.upc.soulstoryapi.dto.CrearComentarioDTO;
import pe.edu.upc.soulstoryapi.entity.Comentario;
import pe.edu.upc.soulstoryapi.entity.Usuario;
import pe.edu.upc.soulstoryapi.exception.UsuarioNoEncontradoException;
import pe.edu.upc.soulstoryapi.repository.ComentarioRepository;
import pe.edu.upc.soulstoryapi.repository.UsuarioRepository;
import pe.edu.upc.soulstoryapi.service.AccesoRecuerdoService;
import pe.edu.upc.soulstoryapi.service.ComentarioService;

import java.util.List;

@Service
public class ComentarioServiceImpl implements ComentarioService {

    private final ComentarioRepository comentarioRepository;
    private final UsuarioRepository usuarioRepository;
    private final AccesoRecuerdoService accesoRecuerdoService;

    public ComentarioServiceImpl(
            ComentarioRepository comentarioRepository,
            UsuarioRepository usuarioRepository,
            AccesoRecuerdoService accesoRecuerdoService) {

        this.comentarioRepository = comentarioRepository;
        this.usuarioRepository = usuarioRepository;
        this.accesoRecuerdoService = accesoRecuerdoService;
    }

    // HU-23
    @Override
    public ComentarioRespuestaDTO crearComentario(
            Long idUsuario,
            Long idRecuerdo,
            CrearComentarioDTO dto) {

        accesoRecuerdoService.validarAccesoUsuarioARecuerdo(
                idUsuario,
                idRecuerdo
        );

        validarTextoComentario(dto);

        Comentario comentario = new Comentario();
        comentario.setTextoComentario(dto.getTextoComentario());
        comentario.setIdUsuario(idUsuario);
        comentario.setIdRecuerdo(idRecuerdo);

        return convertirDTO(
                comentarioRepository.save(comentario)
        );
    }

    // HU-24
    @Override
    public List<ComentarioRespuestaDTO> listarComentarios(
            Long idUsuario,
            Long idRecuerdo) {

        accesoRecuerdoService.validarAccesoUsuarioARecuerdo(
                idUsuario,
                idRecuerdo
        );

        return comentarioRepository
                .findByIdRecuerdoOrderByIdComentarioAsc(idRecuerdo)
                .stream()
                .map(this::convertirDTO)
                .toList();
    }

    private void validarTextoComentario(
            CrearComentarioDTO dto) {

        if (dto == null
                || dto.getTextoComentario() == null
                || dto.getTextoComentario().isBlank()) {

            throw new IllegalArgumentException(
                    "El comentario no puede estar vacío"
            );
        }
    }

    private ComentarioRespuestaDTO convertirDTO(
            Comentario comentario) {

        Usuario usuario = usuarioRepository
                .findById(comentario.getIdUsuario())
                .orElseThrow(() ->
                        new UsuarioNoEncontradoException(
                                "El usuario no existe"
                        )
                );

        return new ComentarioRespuestaDTO(
                comentario.getIdComentario(),
                comentario.getTextoComentario(),
                comentario.getIdUsuario(),
                usuario.getNombreCompleto(),
                comentario.getIdRecuerdo()
        );
    }
}

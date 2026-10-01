package pe.edu.upc.soulstoryapi.serviceimpl;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import pe.edu.upc.soulstoryapi.dto.RegistroUsuarioDTO;
import pe.edu.upc.soulstoryapi.dto.UsuarioRespuestaDTO;
import pe.edu.upc.soulstoryapi.entity.Usuario;
import pe.edu.upc.soulstoryapi.exception.CorreoRegistradoException;
import pe.edu.upc.soulstoryapi.repository.UsuarioRepository;
import pe.edu.upc.soulstoryapi.security.JwtUtilService;
import pe.edu.upc.soulstoryapi.security.UsuarioSecurity;
import pe.edu.upc.soulstoryapi.dto.LoginUsuarioDTO;
import pe.edu.upc.soulstoryapi.dto.LoginRespuestaDTO;
import pe.edu.upc.soulstoryapi.exception.CredencialesInvalidasException;
import pe.edu.upc.soulstoryapi.dto.ActualizarPerfilDTO;
import pe.edu.upc.soulstoryapi.exception.UsuarioNoEncontradoException;
import pe.edu.upc.soulstoryapi.service.UsuarioService;

import java.util.Optional;

@Service
public class UsuarioServiceImpl implements UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtilService jwtUtilService;

    public UsuarioServiceImpl(UsuarioRepository usuarioRepository,
                          PasswordEncoder passwordEncoder,
                          JwtUtilService jwtUtilService) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtilService = jwtUtilService;
    }

    @Override
    public UsuarioRespuestaDTO registrarUsuario(RegistroUsuarioDTO dto) {

        if (usuarioRepository.existsByEmail(dto.getEmail())) {
            throw new CorreoRegistradoException(
                    "El correo ya está registrado"
            );
        }

        Usuario usuario = new Usuario();

        usuario.setNombreCompleto(dto.getNombreCompleto());
        usuario.setEmail(dto.getEmail());

        usuario.setContrasena(
                passwordEncoder.encode(dto.getContrasena())
        );

        usuario.setRol(dto.getRol());

        Usuario usuarioGuardado =
                usuarioRepository.save(usuario);

        return new UsuarioRespuestaDTO(
                usuarioGuardado.getIdUsuario(),
                usuarioGuardado.getNombreCompleto(),
                usuarioGuardado.getEmail(),
                usuarioGuardado.getRol(),
                "Registro exitoso"
        );
    }

    @Override
    public LoginRespuestaDTO iniciarSesion(LoginUsuarioDTO dto) {

        Usuario usuario = usuarioRepository.findByEmail(dto.getEmail())
                .orElseThrow(() ->
                        new CredencialesInvalidasException(
                                "Las credenciales no son válidas"
                        )
                );

        if (!passwordEncoder.matches(
                dto.getContrasena(),
                usuario.getContrasena())) {

            throw new CredencialesInvalidasException(
                    "Las credenciales son incorrectas"
            );
        }

        String token = jwtUtilService.generateToken(
                new UsuarioSecurity(usuario)
        );

        return new LoginRespuestaDTO(
                usuario.getIdUsuario(),
                usuario.getNombreCompleto(),
                usuario.getEmail(),
                usuario.getRol(),
                "Inicio de sesión exitoso",
                token
        );
    }

    @Override
    public UsuarioRespuestaDTO obtenerPerfil(Long idUsuario) {

        Usuario usuario = usuarioRepository.findById(idUsuario)
                .orElseThrow(() ->
                        new UsuarioNoEncontradoException(
                                "El usuario no existe"
                        )
                );

        return new UsuarioRespuestaDTO(
                usuario.getIdUsuario(),
                usuario.getNombreCompleto(),
                usuario.getEmail(),
                usuario.getRol(),
                "Perfil obtenido correctamente"
        );
    }

    @Override
    public UsuarioRespuestaDTO actualizarPerfil(
            Long idUsuario,
            ActualizarPerfilDTO dto) {

        Usuario usuario = usuarioRepository.findById(idUsuario)
                .orElseThrow(() ->
                        new UsuarioNoEncontradoException(
                                "El usuario no existe"
                        )
                );

        Optional<Usuario> usuarioConEmail =
                usuarioRepository.findByEmail(dto.getEmail());

        if (usuarioConEmail.isPresent()
                && !usuarioConEmail.get()
                .getIdUsuario()
                .equals(idUsuario)) {

            throw new CorreoRegistradoException(
                    "El correo ya está registrado"
            );
        }

        usuario.setNombreCompleto(dto.getNombreCompleto());
        usuario.setEmail(dto.getEmail());

        Usuario usuarioActualizado =
                usuarioRepository.save(usuario);

        return new UsuarioRespuestaDTO(
                usuarioActualizado.getIdUsuario(),
                usuarioActualizado.getNombreCompleto(),
                usuarioActualizado.getEmail(),
                usuarioActualizado.getRol(),
                "Perfil actualizado correctamente"
        );
    }
}
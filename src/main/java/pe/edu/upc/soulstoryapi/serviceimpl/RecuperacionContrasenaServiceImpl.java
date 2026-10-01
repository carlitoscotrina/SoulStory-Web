package pe.edu.upc.soulstoryapi.serviceimpl;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import pe.edu.upc.soulstoryapi.dto.RecuperarContrasenaDTO;
import pe.edu.upc.soulstoryapi.dto.RestablecerContrasenaDTO;
import pe.edu.upc.soulstoryapi.entity.Usuario;
import pe.edu.upc.soulstoryapi.exception.CorreoNoRegistradoException;
import pe.edu.upc.soulstoryapi.exception.TokenRecuperacionException;
import pe.edu.upc.soulstoryapi.exception.UsuarioNoEncontradoException;
import pe.edu.upc.soulstoryapi.repository.UsuarioRepository;
import pe.edu.upc.soulstoryapi.service.EmailService;
import pe.edu.upc.soulstoryapi.service.RecuperacionContrasenaService;
import pe.edu.upc.soulstoryapi.service.TokenRecuperacion;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class RecuperacionContrasenaServiceImpl implements RecuperacionContrasenaService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;

    private final Map<String, TokenRecuperacion> tokens =
            new ConcurrentHashMap<>();

    @Value("${app.frontend.url}")
    private String frontendUrl;

    public RecuperacionContrasenaServiceImpl(
            UsuarioRepository usuarioRepository,
            PasswordEncoder passwordEncoder,
            EmailService emailService) {

        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
        this.emailService = emailService;
    }

    @Override
    public void solicitarRecuperacion(
            RecuperarContrasenaDTO dto) {

        Usuario usuario = usuarioRepository
                .findByEmail(dto.getEmail())
                .orElseThrow(() ->
                        new CorreoNoRegistradoException(
                                "No existe una cuenta asociada al correo ingresado"
                        )
                );

        String token = UUID.randomUUID().toString();

        LocalDateTime expiracion =
                LocalDateTime.now().plusMinutes(15);

        TokenRecuperacion tokenRecuperacion =
                new TokenRecuperacion(
                        usuario.getIdUsuario(),
                        expiracion
                );

        tokens.put(token, tokenRecuperacion);

        String enlace =
                frontendUrl +
                        "/restablecer-contrasena?token=" +
                        token;

        emailService.enviarRecuperacion(
                usuario.getEmail(),
                enlace
        );
    }

    @Override
    public void restablecerContrasena(
            RestablecerContrasenaDTO dto) {

        TokenRecuperacion tokenRecuperacion =
                tokens.get(dto.getToken());

        if (tokenRecuperacion == null) {

            throw new TokenRecuperacionException(
                    "El enlace de recuperación no es válido"
            );
        }

        if (LocalDateTime.now()
                .isAfter(tokenRecuperacion.getFechaExpiracion())) {

            tokens.remove(dto.getToken());

            throw new TokenRecuperacionException(
                    "El enlace de recuperación ha expirado"
            );
        }

        Usuario usuario = usuarioRepository
                .findById(tokenRecuperacion.getIdUsuario())
                .orElseThrow(() ->
                        new UsuarioNoEncontradoException(
                                "El usuario no existe"
                        )
                );

        String nuevaContrasenaCodificada =
                passwordEncoder.encode(
                        dto.getNuevaContrasena()
                );

        usuario.setContrasena(
                nuevaContrasenaCodificada
        );

        usuarioRepository.save(usuario);

        // Un token solamente puede utilizarse una vez.
        tokens.remove(dto.getToken());
    }
}
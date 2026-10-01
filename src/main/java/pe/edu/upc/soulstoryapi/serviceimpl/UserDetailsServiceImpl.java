package pe.edu.upc.soulstoryapi.serviceimpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import pe.edu.upc.soulstoryapi.entity.Usuario;
import pe.edu.upc.soulstoryapi.repository.UsuarioRepository;
import pe.edu.upc.soulstoryapi.security.UsuarioSecurity;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    // Se usa el repository (y no UsuarioService) para evitar una dependencia circular:
    // UsuarioServiceImpl -> PasswordEncoder -> SecurityConfiguration -> JwtRequestFilter -> UserDetailsService
    @Autowired
    private UsuarioRepository usuarioRepository;

    // El "username" de SoulStory es el correo electrónico
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("El usuario no existe"));

        return new UsuarioSecurity(usuario);
    }

}

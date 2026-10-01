package pe.edu.upc.soulstoryapi.security;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuthoritySecurity implements GrantedAuthority {

    // En SoulStory el rol vive como texto dentro de Usuario (columna Rol)
    private String rol;

    @Override
    public String getAuthority() {
        return rol;
    }

}

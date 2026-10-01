package pe.edu.upc.soulstoryapi.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UsuarioRespuestaDTO {

    private Long idUsuario;

    private String nombreCompleto;

    private String email;

    private String rol;

    private String mensaje;
}

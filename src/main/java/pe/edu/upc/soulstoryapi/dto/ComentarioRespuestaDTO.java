package pe.edu.upc.soulstoryapi.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ComentarioRespuestaDTO {

    private Long idComentario;

    private String textoComentario;

    private Long idUsuario;

    private String nombreCompleto;

    private Long idRecuerdo;
}

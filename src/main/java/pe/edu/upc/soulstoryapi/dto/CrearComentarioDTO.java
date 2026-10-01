package pe.edu.upc.soulstoryapi.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CrearComentarioDTO {

    @NotBlank(message = "El comentario no puede estar vacío")
    private String textoComentario;
}

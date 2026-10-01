package pe.edu.upc.soulstoryapi.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GenerarImagenIaDTO {

    /*
     * Regla técnica provisional: el documento funcional no define
     * el máximo de caracteres para la descripción de IA.
     */
    public static final int MAX_PROMPT_IA = 1000;

    @NotBlank(message = "El título es obligatorio")
    private String titulo;

    @NotBlank(message = "La descripción es obligatoria")
    @Size(
            max = MAX_PROMPT_IA,
            message = "La descripción excede la longitud máxima permitida"
    )
    private String prompt;
}

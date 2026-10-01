package pe.edu.upc.soulstoryapi.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CrearRecuerdoTextoDTO {

    /*
     * Regla técnica provisional: el documento funcional no define
     * el máximo de caracteres para los recuerdos de texto.
     */
    public static final int MAX_CONTENIDO_TEXTO = 5000;

    @NotBlank(message = "El título del recuerdo es obligatorio")
    private String tituloRecuerdo;

    @NotBlank(message = "El contenido del recuerdo es obligatorio")
    @Size(
            max = MAX_CONTENIDO_TEXTO,
            message = "El contenido del recuerdo no puede superar los 5000 caracteres"
    )
    private String contenido;
}

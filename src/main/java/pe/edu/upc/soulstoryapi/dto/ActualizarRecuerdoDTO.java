package pe.edu.upc.soulstoryapi.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ActualizarRecuerdoDTO {

    @NotBlank(message = "El título del recuerdo es obligatorio")
    private String tituloRecuerdo;

    private String contenido;
}

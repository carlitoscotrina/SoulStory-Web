package pe.edu.upc.soulstoryapi.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EnviarMensajeDTO {

    @NotBlank(message = "El mensaje no puede estar vacío")
    private String contenido;
}

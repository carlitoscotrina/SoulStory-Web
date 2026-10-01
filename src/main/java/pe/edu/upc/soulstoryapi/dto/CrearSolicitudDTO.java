package pe.edu.upc.soulstoryapi.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CrearSolicitudDTO {

    @NotNull(message = "Debe seleccionar un cuidador")
    private Long idCuidador;

    private String mensaje;
}

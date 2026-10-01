package pe.edu.upc.soulstoryapi.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CrearGrupoDTO {

    @NotBlank(message = "El nombre del grupo es obligatorio")
    private String nombreGrupo;

    private String descripcion;
}

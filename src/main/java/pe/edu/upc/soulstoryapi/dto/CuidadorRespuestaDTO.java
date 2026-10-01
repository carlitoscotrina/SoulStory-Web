package pe.edu.upc.soulstoryapi.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CuidadorRespuestaDTO {

    private Long idCuidador;

    private String nombreCompleto;

    private String email;

    private String telefono;

    private String especialidad;

    private String biografia;

    private Boolean notificacionEncendida;
}

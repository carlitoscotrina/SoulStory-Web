package pe.edu.upc.soulstoryapi.dto;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AdultoMayorRespuestaDTO {

    private Long idAdultoMayor;

    private String nombreCompleto;

    private String email;

    private LocalDate fechaNacimiento;

    private String direccion;
}

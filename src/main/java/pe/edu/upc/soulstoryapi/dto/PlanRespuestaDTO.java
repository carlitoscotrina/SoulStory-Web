package pe.edu.upc.soulstoryapi.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PlanRespuestaDTO {

    private Long idPlan;

    private String nombrePlan;

    private Double precio;

    private Integer limiteRecuerdos;

    private String descripcion;
}

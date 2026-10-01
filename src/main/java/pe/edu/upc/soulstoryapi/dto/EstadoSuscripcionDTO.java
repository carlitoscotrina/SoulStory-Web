package pe.edu.upc.soulstoryapi.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EstadoSuscripcionDTO {

    private String estado;

    private Integer idSuscripcion;

    private Long idPlan;

    private String nombrePlan;

    private LocalDateTime fechaInicio;

    private LocalDateTime fechaFin;
}

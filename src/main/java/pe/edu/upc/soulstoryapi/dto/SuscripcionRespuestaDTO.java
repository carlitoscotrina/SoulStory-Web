package pe.edu.upc.soulstoryapi.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SuscripcionRespuestaDTO {

    private Integer idSuscripcion;

    private LocalDateTime fechaInicio;

    private LocalDateTime fechaFin;

    private Boolean estado;

    private Long idAdultoMayor;

    private Long idPlan;

    private Long idPago;

    private String nombrePlan;
}

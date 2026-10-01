package pe.edu.upc.soulstoryapi.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PagoRespuestaDTO {

    private Long idPago;

    private Double monto;

    private LocalDateTime fechaPago;

    private String metodoPago;

    private Boolean estado;

    private Integer idSuscripcion;

    private Boolean suscripcionActiva;

    private String mensaje;
}

package pe.edu.upc.soulstoryapi.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SolicitudRespuestaDTO {

    private Long idSolicitud;

    private String iniciadoPor;

    private String estado;

    private LocalDateTime fechaCreacion;

    private String mensaje;

    private Long idCuidador;

    private Long idAdultoMayor;
}

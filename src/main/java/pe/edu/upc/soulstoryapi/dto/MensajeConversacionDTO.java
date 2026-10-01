package pe.edu.upc.soulstoryapi.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MensajeConversacionDTO {

    private Long idMensaje;

    private String contenido;

    private LocalDateTime fechaHora;

    private Boolean leido;

    private String tipoRemitente;

    private Long idAsignacion;
}

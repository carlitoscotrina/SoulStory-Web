package pe.edu.upc.soulstoryapi.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ConversacionResumenDTO {

    private Long idAsignacion;

    private Long idAdultoMayor;

    private String nombreAdultoMayor;

    private String ultimoMensaje;

    private LocalDateTime fechaUltimoMensaje;
}

package pe.edu.upc.soulstoryapi.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RecuerdoRespuestaDTO {

    private Long idRecuerdo;

    private String tituloRecuerdo;

    private String tipoRecuerdo;

    private String contenido;

    private String formato;

    private Boolean favorito;

    private LocalDateTime fechaCreacion;

    private Long idAdultoMayor;

    private Long idAsignacion;
}

package pe.edu.upc.soulstoryapi.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GrupoRespuestaDTO {

    private Long idGrupo;

    private String nombreGrupo;

    private String descripcion;

    private Long idAdultoMayor;
}

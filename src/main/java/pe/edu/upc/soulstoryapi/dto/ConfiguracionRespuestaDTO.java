package pe.edu.upc.soulstoryapi.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ConfiguracionRespuestaDTO {

    private Long idConfiguracion;

    private Long idUsuario;

    private String idioma;

    private String tamanioFuente;

    private String temaVisual;

    private Boolean notificacionesSonido;
}

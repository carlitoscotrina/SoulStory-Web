package pe.edu.upc.soulstoryapi.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ImagenIaRespuestaDTO {

    private String token;

    private String titulo;

    private String prompt;

    private String urlImagen;

    private String mensaje;
}

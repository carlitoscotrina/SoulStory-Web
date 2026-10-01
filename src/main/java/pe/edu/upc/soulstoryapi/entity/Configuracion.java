package pe.edu.upc.soulstoryapi.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "configuracion", schema = "dbo")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Configuracion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idConfiguracion")
    private Long idConfiguracion;

    @Column(name = "idioma")
    private String idioma;

    @Column(name = "tamanioFuente")
    private String tamanioFuente;

    @Column(name = "temaVisual")
    private String temaVisual;

    @Column(name = "notificacionesSonido")
    private Boolean notificacionesSonido;

    @Column(name = "idUsuario")
    private Long idUsuario;
}

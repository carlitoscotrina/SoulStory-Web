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

import java.time.LocalDateTime;

@Entity
@Table(name = "galeria_ia", schema = "dbo")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class GaleriaIa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idRetratoIa")
    private Long idRetratoIa;

    @Column(name = "prompt_descripcion")
    private String promptDescripcion;

    @Column(name = "titulo")
    private String titulo;

    @Column(name = "url_imagen")
    private String urlImagen;

    @Column(name = "fecha_creacion")
    private LocalDateTime fechaCreacion;

    @Column(name = "id_recuerdo")
    private Long idRecuerdo;
}

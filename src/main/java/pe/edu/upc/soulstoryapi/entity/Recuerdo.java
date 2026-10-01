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
@Table(name = "recuerdo", schema = "dbo")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Recuerdo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_recuerdo")
    private Long idRecuerdo;

    @Column(name = "titulo_recuerdo")
    private String tituloRecuerdo;

    @Column(name = "tipo_recuerdo")
    private String tipoRecuerdo;

    @Column(name = "contenido")
    private String contenido;

    @Column(name = "formato")
    private String formato;

    @Column(name = "favorito")
    private Boolean favorito;

    @Column(name = "fechaCreacion")
    private LocalDateTime fechaCreacion;

    @Column(name = "idAdultoMayor")
    private Long idAdultoMayor;

    @Column(name = "idAsignacion")
    private Long idAsignacion;
}

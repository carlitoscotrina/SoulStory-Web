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
@Table(name = "grupo", schema = "dbo")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Grupo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idGrupo")
    private Long idGrupo;

    @Column(name = "nombre_grupo")
    private String nombreGrupo;

    @Column(name = "descripcion")
    private String descripcion;

    @Column(name = "idAdultoMayor")
    private Long idAdultoMayor;
}

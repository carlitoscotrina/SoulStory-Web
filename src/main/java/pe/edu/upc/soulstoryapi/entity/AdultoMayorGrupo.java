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
@Table(name = "`adulto_,mayor_grupo`", schema = "dbo")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AdultoMayorGrupo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idAdultoMayorGrupo")
    private Long idAdultoMayorGrupo;

    @Column(name = "idAdultoMayor")
    private Long idAdultoMayor;

    @Column(name = "idGrupo")
    private Long idGrupo;
}

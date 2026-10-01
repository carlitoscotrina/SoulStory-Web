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
@Table(name = "recuerdo_grupo", schema = "dbo")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RecuerdoGrupo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idRecuerdoGrupo")
    private Long idRecuerdoGrupo;

    @Column(name = "id_recuerdo")
    private Long idRecuerdo;

    @Column(name = "idGrupo")
    private Long idGrupo;
}

package pe.edu.upc.soulstoryapi.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "`plan`", schema = "dbo")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Plan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_plan")
    private Long idPlan;

    @Column(name = "nombre_plan")
    private String nombrePlan;

    @Column(name = "precio")
    private Double precio;

    @Column(name = "limite_recuerdos")
    private Integer limiteRecuerdos;

    @Column(name = "descripcion")
    private String descripcion;
}

package pe.edu.upc.soulstoryapi.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "cuidador", schema = "dbo")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Cuidador {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idCuidador")
    private Long idCuidador;

    @Column(name = "telefono")
    private String telefono;

    @Column(name = "especialidad")
    private String especialidad;

    @Column(name = "biografia")
    private String biografia;

    @Column(name = "notificacionEncendida")
    private Boolean notificacionEncendida;

    @Column(name = "idUsuario")
    private Long idUsuario;
}

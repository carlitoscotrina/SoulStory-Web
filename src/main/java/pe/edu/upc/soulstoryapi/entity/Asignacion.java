package pe.edu.upc.soulstoryapi.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "asignacion", schema = "dbo")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Asignacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idAsignacion")
    private Long idAsignacion;

    @Column(name = "fechaInicio")
    private LocalDateTime fechaInicio;

    @Column(name = "fechaFin")
    private LocalDateTime fechaFin;

    @Column(name = "estado")
    private String estado;

    @Column(name = "idSolicitud")
    private Long idSolicitud;

    @Column(name = "idCuidador")
    private Long idCuidador;

    @Column(name = "idAdultoMayor")
    private Long idAdultoMayor;
}

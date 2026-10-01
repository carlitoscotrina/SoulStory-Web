package pe.edu.upc.soulstoryapi.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "solicitud", schema = "dbo")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Solicitud {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idSolicitud")
    private Long idSolicitud;

    @Column(name = "iniciadoPor")
    private String iniciadoPor;

    @Column(name = "estado")
    private String estado;

    @Column(name = "fechaCreacion")
    private LocalDateTime fechaCreacion;

    @Column(name = "mensaje")
    private String mensaje;

    @Column(name = "idCuidador")
    private Long idCuidador;

    @Column(name = "idAdultoMayor")
    private Long idAdultoMayor;
}

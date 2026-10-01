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
@Table(name = "mensaje", schema = "dbo")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Mensaje {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_mensaje")
    private Long idMensaje;

    @Column(name = "contenido")
    private String contenido;

    @Column(name = "fecha_hora")
    private LocalDateTime fechaHora;

    @Column(name = "leido")
    private Boolean leido;

    @Column(name = "tipo_remitente")
    private String tipoRemitente;

    @Column(name = "idAsignacion")
    private Long idAsignacion;
}

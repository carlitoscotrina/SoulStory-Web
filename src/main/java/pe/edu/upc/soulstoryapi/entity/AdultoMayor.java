package pe.edu.upc.soulstoryapi.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "adulto_mayor", schema = "dbo")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AdultoMayor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idAdultoMayor")
    private Long idAdultoMayor;

    @Column(name = "fechanacimiento")
    private LocalDate fechaNacimiento;

    @Column(name = "direccion")
    private String direccion;

    @Column(name = "idUsuario")
    private Long idUsuario;
}

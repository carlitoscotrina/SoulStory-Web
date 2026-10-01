package pe.edu.upc.soulstoryapi.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Table(name = "usuario", schema = "dbo")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idUsuario")
    private Long idUsuario;

    @Column(name = "nombreCompleto")
    private String nombreCompleto;

    @Column(name = "email")
    private String email;

    @ToString.Exclude
    @Column(name = "contraseña")
    private String contrasena;

    @Column(name = "Rol")
    private String rol;
}

package pe.edu.upc.soulstoryapi.service;

import java.time.LocalDateTime;

public class TokenRecuperacion {

    private Long idUsuario;
    private LocalDateTime fechaExpiracion;

    public TokenRecuperacion() {
    }

    public TokenRecuperacion(
            Long idUsuario,
            LocalDateTime fechaExpiracion) {

        this.idUsuario = idUsuario;
        this.fechaExpiracion = fechaExpiracion;
    }

    public Long getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(Long idUsuario) {
        this.idUsuario = idUsuario;
    }

    public LocalDateTime getFechaExpiracion() {
        return fechaExpiracion;
    }

    public void setFechaExpiracion(LocalDateTime fechaExpiracion) {
        this.fechaExpiracion = fechaExpiracion;
    }
}
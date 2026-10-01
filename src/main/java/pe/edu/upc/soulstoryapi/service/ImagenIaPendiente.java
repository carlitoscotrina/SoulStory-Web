package pe.edu.upc.soulstoryapi.service;

import java.time.LocalDateTime;

public class ImagenIaPendiente {

    private String token;
    private String titulo;
    private String prompt;
    private String rutaRelativa;
    private String formato;
    private LocalDateTime fechaCreacion;

    public ImagenIaPendiente() {
    }

    public ImagenIaPendiente(
            String token,
            String titulo,
            String prompt,
            String rutaRelativa,
            String formato,
            LocalDateTime fechaCreacion) {

        this.token = token;
        this.titulo = titulo;
        this.prompt = prompt;
        this.rutaRelativa = rutaRelativa;
        this.formato = formato;
        this.fechaCreacion = fechaCreacion;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getPrompt() {
        return prompt;
    }

    public void setPrompt(String prompt) {
        this.prompt = prompt;
    }

    public String getRutaRelativa() {
        return rutaRelativa;
    }

    public void setRutaRelativa(String rutaRelativa) {
        this.rutaRelativa = rutaRelativa;
    }

    public String getFormato() {
        return formato;
    }

    public void setFormato(String formato) {
        this.formato = formato;
    }

    public LocalDateTime getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(LocalDateTime fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }
}

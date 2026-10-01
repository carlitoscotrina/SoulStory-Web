package pe.edu.upc.soulstoryapi.exception;

public class CorreoNoRegistradoException extends RuntimeException {

    public CorreoNoRegistradoException(String mensaje) {
        super(mensaje);
    }
}
package pe.edu.upc.soulstoryapi.exception;

public class SolicitudNoEncontradaException
        extends RuntimeException {

    public SolicitudNoEncontradaException(String mensaje) {
        super(mensaje);
    }
}
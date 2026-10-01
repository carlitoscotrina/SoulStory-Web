package pe.edu.upc.soulstoryapi.exception;

public class SolicitudDuplicadaException
        extends RuntimeException {

    public SolicitudDuplicadaException(String mensaje) {
        super(mensaje);
    }
}
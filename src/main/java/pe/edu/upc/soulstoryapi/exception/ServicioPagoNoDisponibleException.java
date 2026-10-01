package pe.edu.upc.soulstoryapi.exception;

public class ServicioPagoNoDisponibleException
        extends RuntimeException {

    public ServicioPagoNoDisponibleException(String mensaje) {
        super(mensaje);
    }
}

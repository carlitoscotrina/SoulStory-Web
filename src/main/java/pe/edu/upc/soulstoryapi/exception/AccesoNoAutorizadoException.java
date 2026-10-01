package pe.edu.upc.soulstoryapi.exception;

public class AccesoNoAutorizadoException
        extends RuntimeException {

    public AccesoNoAutorizadoException(
            String mensaje) {

        super(mensaje);
    }
}
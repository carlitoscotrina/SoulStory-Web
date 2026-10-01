package pe.edu.upc.soulstoryapi.exception;

public class AsignacionNoEncontradaException
        extends RuntimeException {

    public AsignacionNoEncontradaException(
            String mensaje) {

        super(mensaje);
    }
}
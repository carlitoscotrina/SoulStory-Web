package pe.edu.upc.soulstoryapi.exception;

public class AdultoMayorNoEncontradoException
        extends RuntimeException {

    public AdultoMayorNoEncontradoException(
            String mensaje) {

        super(mensaje);
    }
}
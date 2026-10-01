package pe.edu.upc.soulstoryapi.exception;

public class CuidadorNoEncontradoException
        extends RuntimeException {

    public CuidadorNoEncontradoException(
            String mensaje) {

        super(mensaje);
    }
}
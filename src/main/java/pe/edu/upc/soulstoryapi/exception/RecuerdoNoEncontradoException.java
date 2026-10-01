package pe.edu.upc.soulstoryapi.exception;

public class RecuerdoNoEncontradoException
        extends RuntimeException {

    public RecuerdoNoEncontradoException(
            String mensaje) {

        super(mensaje);
    }
}

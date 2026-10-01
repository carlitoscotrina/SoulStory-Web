package pe.edu.upc.soulstoryapi.exception;

public class RecuerdoGrupoDuplicadoException
        extends RuntimeException {

    public RecuerdoGrupoDuplicadoException(
            String mensaje) {

        super(mensaje);
    }
}

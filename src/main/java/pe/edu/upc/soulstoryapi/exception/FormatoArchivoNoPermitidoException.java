package pe.edu.upc.soulstoryapi.exception;

public class FormatoArchivoNoPermitidoException
        extends RuntimeException {

    public FormatoArchivoNoPermitidoException(
            String mensaje) {

        super(mensaje);
    }
}

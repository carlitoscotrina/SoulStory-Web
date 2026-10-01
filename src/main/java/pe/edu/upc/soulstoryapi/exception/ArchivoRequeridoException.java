package pe.edu.upc.soulstoryapi.exception;

public class ArchivoRequeridoException
        extends RuntimeException {

    public ArchivoRequeridoException(
            String mensaje) {

        super(mensaje);
    }
}

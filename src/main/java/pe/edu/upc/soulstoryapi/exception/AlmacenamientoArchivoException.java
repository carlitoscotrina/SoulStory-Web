package pe.edu.upc.soulstoryapi.exception;

public class AlmacenamientoArchivoException
        extends RuntimeException {

    public AlmacenamientoArchivoException(
            String mensaje) {

        super(mensaje);
    }

    public AlmacenamientoArchivoException(
            String mensaje,
            Throwable causa) {

        super(mensaje, causa);
    }
}

package pe.edu.upc.soulstoryapi.exception;

public class ImagenIaPendienteNoEncontradaException
        extends RuntimeException {

    public ImagenIaPendienteNoEncontradaException(
            String mensaje) {

        super(mensaje);
    }
}

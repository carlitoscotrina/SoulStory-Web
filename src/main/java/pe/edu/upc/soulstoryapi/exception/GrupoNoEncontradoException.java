package pe.edu.upc.soulstoryapi.exception;

public class GrupoNoEncontradoException
        extends RuntimeException {

    public GrupoNoEncontradoException(
            String mensaje) {

        super(mensaje);
    }
}

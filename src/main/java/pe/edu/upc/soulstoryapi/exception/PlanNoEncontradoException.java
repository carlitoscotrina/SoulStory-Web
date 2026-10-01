package pe.edu.upc.soulstoryapi.exception;

public class PlanNoEncontradoException
        extends RuntimeException {

    public PlanNoEncontradoException(String mensaje) {
        super(mensaje);
    }
}
package pe.edu.upc.soulstoryapi.service;

public interface PasarelaPagoService {

    public static final String PROVEEDOR_SIMULADO = "SIMULATED";

    /*
     * Decision tecnica de desarrollo para HU-33. Esta clase se puede
     * sustituir posteriormente por una integracion con una pasarela real.
     */
    public void procesarPago(String metodoPago);

}

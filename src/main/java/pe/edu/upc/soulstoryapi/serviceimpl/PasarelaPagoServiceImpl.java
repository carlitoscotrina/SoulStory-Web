package pe.edu.upc.soulstoryapi.serviceimpl;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import pe.edu.upc.soulstoryapi.exception.PagoRechazadoException;
import pe.edu.upc.soulstoryapi.exception.ServicioPagoNoDisponibleException;
import pe.edu.upc.soulstoryapi.service.PasarelaPagoService;

@Service
public class PasarelaPagoServiceImpl implements PasarelaPagoService {

    private final String proveedor;

    public PasarelaPagoServiceImpl(
            @Value("${app.payment.provider:SIMULATED}")
            String proveedor) {

        this.proveedor = proveedor;
    }

    /*
     * Decision tecnica de desarrollo para HU-33. Esta clase se puede
     * sustituir posteriormente por una integracion con una pasarela real.
     */
    @Override
    public void procesarPago(String metodoPago) {

        if (!PROVEEDOR_SIMULADO.equalsIgnoreCase(proveedor)) {
            throw new ServicioPagoNoDisponibleException(
                    "El servicio de pago no está disponible"
            );
        }

        String metodoNormalizado = metodoPago.trim();

        if ("SERVICIO_CAIDO".equalsIgnoreCase(metodoNormalizado)) {
            throw new ServicioPagoNoDisponibleException(
                    "El servicio de pago no está disponible"
            );
        }

        if ("RECHAZADO".equalsIgnoreCase(metodoNormalizado)) {
            throw new PagoRechazadoException(
                    "El pago fue rechazado"
            );
        }
    }
}

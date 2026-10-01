package pe.edu.upc.soulstoryapi.serviceimpl;

import org.junit.jupiter.api.Test;
import pe.edu.upc.soulstoryapi.exception.PagoRechazadoException;
import pe.edu.upc.soulstoryapi.exception.ServicioPagoNoDisponibleException;
import pe.edu.upc.soulstoryapi.service.PasarelaPagoService;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

class PasarelaPagoServiceTest {

    @Test
    void proveedorSimuladoApruebaUnMetodoValido() {

        PasarelaPagoService pasarela = new PasarelaPagoServiceImpl(
                PasarelaPagoService.PROVEEDOR_SIMULADO
        );

        assertDoesNotThrow(() -> pasarela.procesarPago("TARJETA"));
    }

    @Test
    void proveedorSimuladoRechazaElMetodoRechazado() {

        PasarelaPagoService pasarela = new PasarelaPagoServiceImpl(
                PasarelaPagoService.PROVEEDOR_SIMULADO
        );

        assertThrows(
                PagoRechazadoException.class,
                () -> pasarela.procesarPago("RECHAZADO")
        );
    }

    @Test
    void proveedorSimuladoInformaServicioCaido() {

        PasarelaPagoService pasarela = new PasarelaPagoServiceImpl(
                PasarelaPagoService.PROVEEDOR_SIMULADO
        );

        assertThrows(
                ServicioPagoNoDisponibleException.class,
                () -> pasarela.procesarPago("SERVICIO_CAIDO")
        );
    }
}

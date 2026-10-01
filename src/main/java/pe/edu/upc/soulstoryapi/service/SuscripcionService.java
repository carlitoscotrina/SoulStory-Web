package pe.edu.upc.soulstoryapi.service;

import pe.edu.upc.soulstoryapi.dto.EstadoSuscripcionDTO;
import pe.edu.upc.soulstoryapi.dto.PagoRespuestaDTO;
import pe.edu.upc.soulstoryapi.dto.ProcesarPagoDTO;
import pe.edu.upc.soulstoryapi.dto.SuscripcionRespuestaDTO;

public interface SuscripcionService {

    /*
     * Decision tecnica provisional: el documento no establece la duracion
     * de cada plan, por lo que una suscripcion aprobada dura un mes.
     */

    public static final long DURACION_SUSCRIPCION_MESES = 1;

    // HU-32
    public SuscripcionRespuestaDTO seleccionarPlan(Long idAdultoMayor, Long idPlan);

    // HU-33
    public PagoRespuestaDTO procesarPago(Long idAdultoMayor, Integer idSuscripcion, ProcesarPagoDTO dto);

    // HU-34
    public EstadoSuscripcionDTO consultarEstado(Long idAdultoMayor);

}

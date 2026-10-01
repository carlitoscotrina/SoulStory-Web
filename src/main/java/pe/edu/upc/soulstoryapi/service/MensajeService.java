package pe.edu.upc.soulstoryapi.service;

import pe.edu.upc.soulstoryapi.dto.EnviarMensajeDTO;
import pe.edu.upc.soulstoryapi.dto.MensajeConversacionDTO;

import java.util.List;

public interface MensajeService {

    // HU-25
    public MensajeConversacionDTO enviarMensajeAdultoMayor(
            Long idAdultoMayor,
            Long idCuidador,
            EnviarMensajeDTO dto
    );

    // HU-26
    public MensajeConversacionDTO enviarMensajeCuidador(
            Long idCuidador,
            Long idAdultoMayor,
            EnviarMensajeDTO dto
    );

    public List<MensajeConversacionDTO> listarConversacion(Long idAdultoMayor, Long idCuidador);

    // HU-26 y HU-27
    public List<MensajeConversacionDTO> listarConversacionComoCuidador(Long idCuidador, Long idAdultoMayor);

}

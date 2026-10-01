package pe.edu.upc.soulstoryapi.service;

import pe.edu.upc.soulstoryapi.dto.ConversacionResumenDTO;

import java.util.List;

public interface ConversacionService {

    // HU-27
    public List<ConversacionResumenDTO> listarConversacionesCuidador(Long idCuidador);

}

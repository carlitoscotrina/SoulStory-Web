package pe.edu.upc.soulstoryapi.service;

import pe.edu.upc.soulstoryapi.entity.Recuerdo;

public interface AccesoRecuerdoService {

    public Recuerdo validarAccesoUsuarioARecuerdo(Long idUsuario, Long idRecuerdo);

}

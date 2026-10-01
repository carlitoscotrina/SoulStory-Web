package pe.edu.upc.soulstoryapi.service;

import pe.edu.upc.soulstoryapi.dto.RecuperarContrasenaDTO;
import pe.edu.upc.soulstoryapi.dto.RestablecerContrasenaDTO;

public interface RecuperacionContrasenaService {

    public void solicitarRecuperacion(RecuperarContrasenaDTO dto);

    public void restablecerContrasena(RestablecerContrasenaDTO dto);

}

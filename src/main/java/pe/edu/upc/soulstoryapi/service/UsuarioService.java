package pe.edu.upc.soulstoryapi.service;

import pe.edu.upc.soulstoryapi.dto.RegistroUsuarioDTO;
import pe.edu.upc.soulstoryapi.dto.UsuarioRespuestaDTO;
import pe.edu.upc.soulstoryapi.dto.LoginUsuarioDTO;
import pe.edu.upc.soulstoryapi.dto.LoginRespuestaDTO;
import pe.edu.upc.soulstoryapi.dto.ActualizarPerfilDTO;

public interface UsuarioService {

    public UsuarioRespuestaDTO registrarUsuario(RegistroUsuarioDTO dto);

    public LoginRespuestaDTO iniciarSesion(LoginUsuarioDTO dto);

    public UsuarioRespuestaDTO obtenerPerfil(Long idUsuario);

    public UsuarioRespuestaDTO actualizarPerfil(Long idUsuario, ActualizarPerfilDTO dto);

}

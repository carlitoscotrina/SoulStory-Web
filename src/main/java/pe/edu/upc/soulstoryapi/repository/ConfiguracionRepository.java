package pe.edu.upc.soulstoryapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pe.edu.upc.soulstoryapi.entity.Configuracion;

import java.util.Optional;

public interface ConfiguracionRepository
        extends JpaRepository<Configuracion, Long> {

    Optional<Configuracion> findByIdUsuario(Long idUsuario);
}

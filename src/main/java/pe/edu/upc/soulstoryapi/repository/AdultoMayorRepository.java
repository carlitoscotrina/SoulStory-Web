package pe.edu.upc.soulstoryapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pe.edu.upc.soulstoryapi.entity.AdultoMayor;

import java.util.Optional;

public interface AdultoMayorRepository
        extends JpaRepository<AdultoMayor, Long> {

    Optional<AdultoMayor> findByIdUsuario(Long idUsuario);
}
package pe.edu.upc.soulstoryapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pe.edu.upc.soulstoryapi.entity.Cuidador;

import java.util.Optional;

public interface CuidadorRepository
        extends JpaRepository<Cuidador, Long> {

    Optional<Cuidador> findByIdUsuario(Long idUsuario);
}
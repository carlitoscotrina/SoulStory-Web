package pe.edu.upc.soulstoryapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pe.edu.upc.soulstoryapi.entity.GaleriaIa;

import java.util.List;

public interface GaleriaIaRepository
        extends JpaRepository<GaleriaIa, Long> {

    List<GaleriaIa> findByIdRecuerdo(Long idRecuerdo);
}

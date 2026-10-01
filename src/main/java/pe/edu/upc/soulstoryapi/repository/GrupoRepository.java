package pe.edu.upc.soulstoryapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pe.edu.upc.soulstoryapi.entity.Grupo;

import java.util.List;

public interface GrupoRepository
        extends JpaRepository<Grupo, Long> {

    List<Grupo>
    findByIdAdultoMayorOrderByIdGrupoDesc(
            Long idAdultoMayor
    );
}

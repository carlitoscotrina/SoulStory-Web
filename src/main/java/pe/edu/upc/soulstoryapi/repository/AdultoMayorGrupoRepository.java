package pe.edu.upc.soulstoryapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pe.edu.upc.soulstoryapi.entity.AdultoMayorGrupo;

import java.util.List;
import java.util.Optional;

public interface AdultoMayorGrupoRepository
        extends JpaRepository<AdultoMayorGrupo, Long> {

    boolean existsByIdAdultoMayorAndIdGrupo(
            Long idAdultoMayor,
            Long idGrupo
    );

    List<AdultoMayorGrupo> findByIdGrupo(Long idGrupo);

    List<AdultoMayorGrupo> findByIdAdultoMayor(
            Long idAdultoMayor
    );

    Optional<AdultoMayorGrupo>
    findByIdAdultoMayorAndIdGrupo(
            Long idAdultoMayor,
            Long idGrupo
    );
}

package pe.edu.upc.soulstoryapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pe.edu.upc.soulstoryapi.entity.RecuerdoGrupo;

import java.util.List;
import java.util.Optional;

public interface RecuerdoGrupoRepository
        extends JpaRepository<RecuerdoGrupo, Long> {

    boolean existsByIdRecuerdoAndIdGrupo(
            Long idRecuerdo,
            Long idGrupo
    );

    List<RecuerdoGrupo> findByIdGrupo(Long idGrupo);

    Optional<RecuerdoGrupo> findByIdRecuerdoAndIdGrupo(
            Long idRecuerdo,
            Long idGrupo
    );
}

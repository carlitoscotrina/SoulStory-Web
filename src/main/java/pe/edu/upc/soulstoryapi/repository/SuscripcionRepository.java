package pe.edu.upc.soulstoryapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pe.edu.upc.soulstoryapi.entity.Suscripcion;

import java.util.List;
import java.util.Optional;

public interface SuscripcionRepository
        extends JpaRepository<Suscripcion, Integer> {

    List<Suscripcion>
    findByIdAdultoMayorOrderByIdSuscripcionDesc(
            Long idAdultoMayor
    );

    Optional<Suscripcion>
    findFirstByIdAdultoMayorAndIdPlanAndEstadoTrueOrderByIdSuscripcionDesc(
            Long idAdultoMayor,
            Long idPlan
    );

    Optional<Suscripcion>
    findFirstByIdAdultoMayorOrderByIdSuscripcionDesc(
            Long idAdultoMayor
    );

    Optional<Suscripcion>
    findFirstByIdAdultoMayorAndEstadoTrueOrderByIdSuscripcionDesc(
            Long idAdultoMayor
    );
}

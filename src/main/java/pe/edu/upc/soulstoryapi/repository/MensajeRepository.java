package pe.edu.upc.soulstoryapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pe.edu.upc.soulstoryapi.entity.Mensaje;

import java.util.List;
import java.util.Optional;

public interface MensajeRepository
        extends JpaRepository<Mensaje, Long> {

    List<Mensaje>
    findByIdAsignacionOrderByFechaHoraAsc(
            Long idAsignacion
    );

    Optional<Mensaje>
    findFirstByIdAsignacionOrderByFechaHoraDesc(
            Long idAsignacion
    );
}

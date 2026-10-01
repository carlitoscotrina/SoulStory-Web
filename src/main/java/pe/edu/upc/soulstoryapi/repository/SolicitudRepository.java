package pe.edu.upc.soulstoryapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pe.edu.upc.soulstoryapi.entity.Solicitud;

import java.util.List;
import java.util.Optional;

public interface SolicitudRepository
        extends JpaRepository<Solicitud, Long> {

    boolean existsByIdAdultoMayorAndIdCuidadorAndEstado(
            Long idAdultoMayor,
            Long idCuidador,
            String estado
    );

    List<Solicitud>
    findByIdAdultoMayorOrderByFechaCreacionDesc(
            Long idAdultoMayor
    );

    List<Solicitud>
    findByIdCuidadorOrderByFechaCreacionDesc(
            Long idCuidador
    );

    Optional<Solicitud>
    findByIdSolicitudAndIdAdultoMayor(
            Long idSolicitud,
            Long idAdultoMayor
    );

    Optional<Solicitud>
    findByIdSolicitudAndIdCuidador(
            Long idSolicitud,
            Long idCuidador
    );
}
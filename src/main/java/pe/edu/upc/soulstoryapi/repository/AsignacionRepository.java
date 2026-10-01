package pe.edu.upc.soulstoryapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pe.edu.upc.soulstoryapi.entity.Asignacion;

import java.util.List;
import java.util.Optional;

public interface AsignacionRepository
        extends JpaRepository<Asignacion, Long> {

    List<Asignacion> findByIdCuidador(Long idCuidador);

    List<Asignacion> findByIdAdultoMayor(Long idAdultoMayor);

    Optional<Asignacion>
    findFirstByIdAdultoMayorOrderByFechaInicioDesc(
            Long idAdultoMayor
    );

    Optional<Asignacion>
    findFirstByIdCuidadorAndIdAdultoMayorAndEstadoIgnoreCaseOrderByFechaInicioDesc(
            Long idCuidador,
            Long idAdultoMayor,
            String estado
    );

    List<Asignacion>
    findByIdCuidadorAndEstadoIgnoreCaseOrderByFechaInicioDesc(
            Long idCuidador,
            String estado
    );

    boolean existsByIdCuidadorAndIdAdultoMayor(
            Long idCuidador,
            Long idAdultoMayor
    );

    boolean existsByIdSolicitud(Long idSolicitud);
}

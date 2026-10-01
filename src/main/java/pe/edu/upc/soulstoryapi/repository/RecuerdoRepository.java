package pe.edu.upc.soulstoryapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pe.edu.upc.soulstoryapi.entity.Recuerdo;

import java.util.List;

public interface RecuerdoRepository
        extends JpaRepository<Recuerdo, Long> {

    List<Recuerdo>
    findByIdAdultoMayorOrderByFechaCreacionDesc(
            Long idAdultoMayor
    );

    List<Recuerdo>
    findByIdAdultoMayorAndFavoritoTrueOrderByFechaCreacionDesc(
            Long idAdultoMayor
    );
}

package pe.edu.upc.soulstoryapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pe.edu.upc.soulstoryapi.entity.Pago;

public interface PagoRepository extends JpaRepository<Pago, Long> {
}

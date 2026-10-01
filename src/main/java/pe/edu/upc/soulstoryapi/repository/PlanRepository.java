package pe.edu.upc.soulstoryapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pe.edu.upc.soulstoryapi.entity.Plan;

public interface PlanRepository
        extends JpaRepository<Plan, Long> {
}
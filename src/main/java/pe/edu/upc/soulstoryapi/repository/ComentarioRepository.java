package pe.edu.upc.soulstoryapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pe.edu.upc.soulstoryapi.entity.Comentario;

import java.util.List;

public interface ComentarioRepository
        extends JpaRepository<Comentario, Long> {

    List<Comentario>
    findByIdRecuerdoOrderByIdComentarioAsc(
            Long idRecuerdo
    );
}

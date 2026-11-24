package cl.duoc.backend.repository;

import cl.duoc.backend.model.OportunidadModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OportunidadSpringRepository extends JpaRepository<OportunidadModel, Long> {
}

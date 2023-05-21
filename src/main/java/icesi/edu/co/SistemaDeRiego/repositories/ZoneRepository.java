package icesi.edu.co.SistemaDeRiego.repositories;

import icesi.edu.co.SistemaDeRiego.entities.Zone;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface ZoneRepository extends CrudRepository<Zone, Long> {
    @Query("SELECT COUNT(z) > 0 FROM Zone z WHERE z.name = :name")
    boolean existsByName(@Param("name") String name);
}

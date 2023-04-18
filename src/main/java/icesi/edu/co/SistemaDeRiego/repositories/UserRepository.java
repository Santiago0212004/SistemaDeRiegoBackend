package icesi.edu.co.SistemaDeRiego.repositories;

import icesi.edu.co.SistemaDeRiego.entities.User;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, Long> {
}

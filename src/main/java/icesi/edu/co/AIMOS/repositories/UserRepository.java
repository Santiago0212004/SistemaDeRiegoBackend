package icesi.edu.co.AIMOS.repositories;

import icesi.edu.co.AIMOS.entities.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface UserRepository extends CrudRepository<User, String> {
    @Query("SELECT COUNT(u) > 0 FROM User u WHERE u.username = :username")
    boolean existsByUsername(@Param("username") String username);

    @Query("SELECT COUNT(u) > 0 FROM User u WHERE u.identification = :identification")
    boolean existsByIdentification(@Param("identification") String identification);

    @Query("SELECT u FROM User u WHERE u.identification = :identification")
    User findByIdentification(@Param("identification") String identification);

    @Query("SELECT COUNT(u) > 0 FROM User u WHERE u.authorization.value = :authorizationValue")
    boolean existsByAuthorizationValue(@Param("authorizationValue") String authorizationValue);

}

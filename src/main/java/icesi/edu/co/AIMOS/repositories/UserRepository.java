package icesi.edu.co.AIMOS.repositories;

import icesi.edu.co.AIMOS.entities.User;
import icesi.edu.co.AIMOS.entities.Zone;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface UserRepository extends CrudRepository<User, String> {
    @Query("SELECT COUNT(u) > 0 FROM User u WHERE u.username = :username")
    boolean existsByUsername(@Param("username") String username);

    @Query("SELECT COUNT(u) > 0 FROM User u WHERE u.identification = :identification")
    boolean existsByIdentification(@Param("identification") String identification);

    @Query("SELECT u FROM User u WHERE u.identification = :identification")
    User findByIdentification(@Param("identification") String identification);

    @Query("SELECT COUNT(u) > 0 FROM User u WHERE u.authorization.value = :authorizationValue")
    boolean existsByAuthorizationValue(@Param("authorizationValue") String authorizationValue);

    @Query("SELECT u FROM User u WHERE u.authorization.type <> 'MASTER'")
    List<User> findAllNonMasterUsers();

    @Query("SELECT u FROM User u INNER JOIN u.zones z WHERE z.id <> :zoneId")
    List<User> findUsersNotLinkedToZone(@Param("zoneId") Long zoneId);
}

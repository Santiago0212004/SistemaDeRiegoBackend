package icesi.edu.co.AIMOS.repositories;

import icesi.edu.co.AIMOS.entities.Authorization;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface AuthorizationRepository extends CrudRepository<Authorization, String> {
    @Query("SELECT a FROM Authorization a WHERE a.type <> 'MASTER'")
    List<Authorization> findAllNonMasterAuthorizations();
}

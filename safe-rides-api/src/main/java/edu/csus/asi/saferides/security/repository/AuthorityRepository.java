package edu.csus.asi.saferides.security.repository;

import edu.csus.asi.saferides.security.model.Authority;
import edu.csus.asi.saferides.security.model.AuthorityName;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface AuthorityRepository extends CrudRepository<Authority, Long> {

    /*
     * Other query methods can be defined by simply declaring the method signature.
     * Spring Data JPA will automagically create an implementation on the fly.
     * */
    List<Authority> findAll();

    Authority findByName(AuthorityName name);

}
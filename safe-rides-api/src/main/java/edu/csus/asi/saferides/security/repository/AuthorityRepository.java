package edu.csus.asi.saferides.security.repository;

import edu.csus.asi.saferides.security.model.Authority;
import edu.csus.asi.saferides.security.model.AuthorityName;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

/**
 * Datastore interface for authorities (roles).
 */
public interface AuthorityRepository extends CrudRepository<Authority, Long> {

    /**
     * Get all authorities
     *
     * @return all authorities
     */
    List<Authority> findAll();

    /**
     * Get an authority
     *
     * @param name of the authority
     * @return authority
     */
    Authority findByName(AuthorityName name);

}
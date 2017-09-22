package edu.csus.asi.saferides.repository;

import edu.csus.asi.saferides.model.Authority;
import edu.csus.asi.saferides.model.AuthorityName;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

/**
 * Provides methods for working with the Authority persistence
 * <p>
 * By extending CrudRepository, AuthorityRepository inherits several methods for working with Authority persistence,
 * including methods for saving, deleting, and finding Authority entities
 *
 * @see <a href="http://tinyurl.com/hxz23zt">CrudRepository</a>
 * <p>
 * Other query methods can be defined by simply declaring the method signature.
 * Spring Data JPA will automatically create an implementation on the fly.
 * See <a href="https://docs.spring.io/spring-data/jpa/docs/current/reference/html/#jpa.query-methods" target="_blank">Query Methods</a>
 */
public interface AuthorityRepository extends CrudRepository<Authority, Long> {

    /**
     * Find all authorities
     *
     * @return list of all authorities
     */
    List<Authority> findAll();

    /**
     * Find authority with the specified name
     * Returns null if no authority is associated with the specified name
     *
     * @param name the name of the authority to search by
     * @return authority with the specified name
     */
    Authority findByName(AuthorityName name);

    /**
     * Find list of authorities with given authority names.
     *
     * @param names authority names to search by
     * @return list of authorities with specified authority names
     */
    List<Authority> findByNameIn(List<AuthorityName> names);

}
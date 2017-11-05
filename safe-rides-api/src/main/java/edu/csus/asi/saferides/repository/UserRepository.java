package edu.csus.asi.saferides.repository;

import edu.csus.asi.saferides.model.User;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

/**
 * Provides methods for working with the User persistence
 * <p>
 * By extending CrudRepository, UserRepository inherits several methods for working with User persistence,
 * including methods for saving, deleting, and finding User entities
 *
 * @see <a href="http://tinyurl.com/hxz23zt">CrudRepository</a>
 * <p>
 * Other query methods can be defined by simply declaring the method signature.
 * Spring Data JPA will automatically create an implementation on the fly.
 * See <a href="https://docs.spring.io/spring-data/jpa/docs/current/reference/html/#jpa.query-methods" target="_blank">Query Methods</a>
 */
public interface UserRepository extends CrudRepository<User, Long> {

    /**
     * Finds all users
     *
     * @return list of all users
     */
	@Cacheable("users")
    List<User> findAll();

    /**
     * Finds user with the specified username
     * Returns null if no user is associated with the username
     *
     * @param username the username of the user to search by
     * @return user with the specified username
     */
	@Cacheable("users")
    User findByUsernameIgnoreCase(String username);

	@Cacheable("users")
    List<User> findByActive(boolean active);
}

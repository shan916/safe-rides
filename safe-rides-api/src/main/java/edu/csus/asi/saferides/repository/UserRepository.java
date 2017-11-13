package edu.csus.asi.saferides.repository;

import edu.csus.asi.saferides.model.AuthorityName;
import edu.csus.asi.saferides.model.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
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
    List<User> findAll();

    /**
     * Finds user with the specified username
     * Returns null if no user is associated with the username
     *
     * @param username the username of the user to search by
     * @return user with the specified username
     */
    User findByUsernameIgnoreCase(String username);

    /**
     * Finds all users by their active flag
     *
     * @param active whether they are active (enabled)
     * @return list of users
     */
    List<User> findByActive(boolean active);


    /**
     * Delete users that have the specified authority level and the token valid date is before the specified date
     *
     * @param dateTime       the date which prior user records should be deleted (exclusive)
     * @param authorityLevel the authority level
     */
    @Transactional
    void deleteByTokenValidFromBeforeAndAuthorityLevel(LocalDateTime dateTime, AuthorityName authorityLevel);
}

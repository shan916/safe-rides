package edu.csus.asi.saferides.repository;

import edu.csus.asi.saferides.model.Driver;
import edu.csus.asi.saferides.security.model.User;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

/**
 * Provides methods for working with the Driver persistence
 * <p>
 * By extending CrudRepository, DriverRepository inherits several methods for working with Driver persistence,
 * including methods for saving, deleting, and finding Driver entities
 *
 * @see <a href="http://tinyurl.com/hxz23zt">CrudRepository</a>
 * <p>
 * Other query methods can be defined by simply declaring the method signature.
 * Spring Data JPA will automatically create an implementation on the fly.
 * See <a href="https://docs.spring.io/spring-data/jpa/docs/current/reference/html/#jpa.query-methods" target="_blank">Query Methods</a>
 */
public interface DriverRepository extends CrudRepository<Driver, Long> {

    /**
     * Finds all drivers ordered by modified date in descending order
     *
     * @return list of all drivers ordered by modified date in descending order
     */
    List<Driver> findAllByOrderByModifiedDateDesc();

    /**
     * Finds the driver associated with the specified user
     * Returns null if no driver is associated with the specified user
     *
     * @param user the user associated with the driver
     * @return the driver associated with the specified user
     */
    Driver findByUser(User user);

}

package edu.csus.asi.saferides.repository;

import edu.csus.asi.saferides.model.Driver;
import edu.csus.asi.saferides.model.DriverLocation;
import org.springframework.data.repository.CrudRepository;

/**
 * Provides methods for working with the DriverLocation persistence
 * <p>
 * By extending CrudRepository, DriverLocationRepository inherits several methods for working with DriverLocation persistence,
 * including methods for saving, deleting, and finding DriverLocation entities
 *
 * @see <a href="http://tinyurl.com/hxz23zt">CrudRepository</a>
 * <p>
 * Other query methods can be defined by simply declaring the method signature.
 * Spring Data JPA will automatically create an implementation on the fly.
 * See <a href="https://docs.spring.io/spring-data/jpa/docs/current/reference/html/#jpa.query-methods" target="_blank">Query Methods</a>
 */
public interface DriverLocationRepository extends CrudRepository<DriverLocation, Long> {

    /**
     * Returns the most recently created driver location for a driver
     *
     * @param driver the driver that is mapped to the driver location
     * @return the latest driver location object mapped to the input driver object
     */
    DriverLocation findTop1ByDriverOrderByCreatedDateDesc(Driver driver);
}

package edu.csus.asi.saferides.repository;

import edu.csus.asi.saferides.model.Driver;
import edu.csus.asi.saferides.model.DriverLocation;
import org.springframework.data.repository.CrudRepository;

/**
 * Datastore interface for driver location
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

package edu.csus.asi.saferides.repository;

import edu.csus.asi.saferides.model.Driver;
import edu.csus.asi.saferides.model.DriverLocation;
import org.springframework.data.repository.CrudRepository;

// Contains the locations of all drivers.
public interface DriverLocationRepository extends CrudRepository<DriverLocation, Long> {

    /*
     * Other query methods can be defined by simply declaring the method signature.
     * Spring Data JPA will automagically create an implementation on the fly.
     * */
	
	// Returns the most recently created driver
    DriverLocation findTop1ByDriverOrderByCreatedDateDesc(Driver driver);
}

package edu.csus.asi.saferides.repository;

import edu.csus.asi.saferides.model.Driver;
import edu.csus.asi.saferides.model.DriverLocation;
import org.springframework.data.repository.CrudRepository;

public interface DriverLocationRepository extends CrudRepository<DriverLocation, Long> {

    /*
     * Other query methods can be defined by simply declaring the method signature.
     * Spring Data JPA will automagically create an implementation on the fly.
     * */
    DriverLocation findTop1ByDriverOrderByCreatedDateDesc(Driver driver);
}

package edu.csus.asi.saferides.repository;

import edu.csus.asi.saferides.model.Driver;
import edu.csus.asi.saferides.security.model.User;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

/*
 * @author Zeeshan Khaliq
 * 
 * By extending CrudRepository, DriverRepository inherits several methods for working with Driver persistence, 
 * including methods for saving, deleting, and finding Driver entities
 * @see <a href="http://tinyurl.com/hxz23zt">CrudRepository</a>
 * 
 * */
public interface DriverRepository extends CrudRepository<Driver, Long> {
    /*
     * Other query methods can be defined by simply declaring the method signature.
	 * Spring Data JPA will automagically create an implementation on the fly.
	 * */

    // Returns a list of Drivers by status=active
    List<Driver> findByActive(Boolean active);

    // Returns a list of Drivers by status=active, ordered by modified date
    List<Driver> findByActiveTrueOrderByModifiedDateDesc();

    // Returns a list of Drivers by status=active, ordered in reverse by modified date.
    List<Driver> findByActiveFalseOrderByModifiedDateDesc();

    // Returns a list of all drivers ordered by modified date.
    List<Driver> findAllByOrderByModifiedDateDesc();

    //Returns a driver with the specified user name.
    Driver findByUser(User user);
}

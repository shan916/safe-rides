package edu.csus.asi.saferides.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import edu.csus.asi.saferides.model.Driver;

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
	List<Driver> findByActive(Boolean active);
	
}

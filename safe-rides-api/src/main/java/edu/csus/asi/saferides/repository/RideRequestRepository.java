package edu.csus.asi.saferides.repository;

import edu.csus.asi.saferides.model.RideRequest;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface RideRequestRepository extends CrudRepository<RideRequest, Long> {

	/*
	 * Other query methods can be defined by simply declaring the method signature.
	 * Spring Data JPA will automagically create an implementation on the fly.
	 * */
	List<RideRequest> findByStatus(int status);
	
}
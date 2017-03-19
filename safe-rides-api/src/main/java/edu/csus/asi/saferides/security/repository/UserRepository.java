package edu.csus.asi.saferides.security.repository;

import edu.csus.asi.saferides.security.model.User;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface UserRepository extends CrudRepository<User, Long> {

	/*
	 * Other query methods can be defined by simply declaring the method signature.
	 * Spring Data JPA will automagically create an implementation on the fly.
	 * */
	List<User> findAll();
	
	User findByUsername(String username);
	
}
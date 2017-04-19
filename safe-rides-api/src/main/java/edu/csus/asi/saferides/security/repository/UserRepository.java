package edu.csus.asi.saferides.security.repository;

import edu.csus.asi.saferides.security.model.User;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

/**
 * Datastore interface for application users.
 */
public interface UserRepository extends CrudRepository<User, Long> {

    /**
     * Get all users
     *
     * @return all users
     */
    List<User> findAll();

    /**
     * Get a user
     *
     * @param username of the user
     * @return user
     */
    User findByUsername(String username);

}
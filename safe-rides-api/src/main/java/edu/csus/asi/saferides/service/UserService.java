package edu.csus.asi.saferides.service;

import edu.csus.asi.saferides.model.AuthorityName;
import edu.csus.asi.saferides.model.User;
import edu.csus.asi.saferides.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Provides methods containing logic for CRUD operations for Users
 */
@Service
public class UserService {

    private final UserRepository userRepository;

    /**
     * Dependency Injection
     *
     * @param userRepository singleton for UserRepository
     */
    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Returns list of users with given criteria
     *
     * @param active whether users are active or not
     * @param role   role to search by
     * @return list of users with given criteria
     */
    public List<User> getUsers(Boolean active, AuthorityName role) {

        List<User> users;

        if (active != null) {
            users = userRepository.findByActive(active);
        } else {
            users = userRepository.findAll();
        }

        if (role != null) {
            // get all drivers
            if (role == AuthorityName.ROLE_DRIVER) {
                // remove anyone not a driver
                users.removeIf(u -> u.getAuthorityLevel() != AuthorityName.ROLE_DRIVER);
            }   // get all coordinators
            else if (role == AuthorityName.ROLE_COORDINATOR) {
                // remove anyone not a coordinator
                users.removeIf(u -> u.getAuthorityLevel() != (AuthorityName.ROLE_COORDINATOR));
            } // get all admins
            else if (role == AuthorityName.ROLE_ADMIN) {
                // remove anyone not an admin
                users.removeIf(u -> u.getAuthorityLevel() != AuthorityName.ROLE_ADMIN);
            }
        }

        return users;
    }

    /**
     * Returns list of users with given criteria
     *
     * @param active  whether users are active or not
     * @param notRole role to search by
     * @return list of users with given criteria
     */
    public List<User> getUsersNotInRole(Boolean active, AuthorityName notRole) {

        List<User> users;

        if (active != null) {
            if (active) {
                users = userRepository.findByActive(true);
            } else {
                users = userRepository.findByActive(false);
            }
        } else {
            users = userRepository.findAll();
        }

        if (notRole != null) {
            // get all non-drivers
            if (notRole == AuthorityName.ROLE_DRIVER) {
                users.removeIf(u -> u.getAuthorityLevel() == AuthorityName.ROLE_DRIVER);
            }   // get all non-coordinators
            else if (notRole == AuthorityName.ROLE_COORDINATOR) {
                users.removeIf(u -> u.getAuthorityLevel() == (AuthorityName.ROLE_COORDINATOR));
            } // get all non-admins
            else if (notRole == AuthorityName.ROLE_ADMIN) {
                users.removeIf(u -> u.getAuthorityLevel() == (AuthorityName.ROLE_ADMIN));
            }
        }

        return users;
    }

    public User getUserById(Long id) {
        return userRepository.findOne(id);
    }
}

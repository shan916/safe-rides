package edu.csus.asi.saferides.service;

import edu.csus.asi.saferides.mapper.UserMapper;
import edu.csus.asi.saferides.model.Authority;
import edu.csus.asi.saferides.model.AuthorityName;
import edu.csus.asi.saferides.model.User;
import edu.csus.asi.saferides.model.dto.DriverDto;
import edu.csus.asi.saferides.model.dto.UserDto;
import edu.csus.asi.saferides.repository.AuthorityRepository;
import edu.csus.asi.saferides.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

/**
 * Provides methods containing logic for CRUD operations for Users
 */
@Service
public class UserService {

    private UserRepository userRepository;
    private UserMapper userMapper;
    private AuthorityRepository authorityRepository;

    /**
     * Inject dependencies
     *
     * @param userRepository      singleton for UserRepository
     * @param userMapper          singleton for UserMapper
     * @param authorityRepository singleton for AuthorityRepository
     */
    @Autowired
    public UserService(UserRepository userRepository, UserMapper userMapper, AuthorityRepository authorityRepository) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.authorityRepository = authorityRepository;
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
            if (active) {
                users = userRepository.findByActive(true);
            } else {
                users = userRepository.findByActive(false);
            }
        } else {
            users = userRepository.findAll();
        }

        if (role != null) {
            // get all drivers
            if (role == AuthorityName.ROLE_DRIVER) {
                // remove anyone not a driver (coordinator and higher)
                Authority coord = authorityRepository.findByName(AuthorityName.ROLE_COORDINATOR);
                users.removeIf(u -> u.getAuthorities().contains(coord));
            }   // get all coordinators
            else if (role == AuthorityName.ROLE_COORDINATOR) {
                // remove anyone not a coordinator
                Authority coord = authorityRepository.findByName(AuthorityName.ROLE_COORDINATOR);
                Authority admin = authorityRepository.findByName(AuthorityName.ROLE_ADMIN);
                users.removeIf(u -> !u.getAuthorities().contains(coord) || u.getAuthorities().contains(admin)); // remove if not coord or is admin
            } // get all admins
            else if (role == AuthorityName.ROLE_ADMIN) {
                // remove anyone not an admin
                Authority admin = authorityRepository.findByName(AuthorityName.ROLE_ADMIN);
                users.removeIf(u -> !u.getAuthorities().contains(admin));
            }
        }

        return users;

    }

    /**
     * Returns list of users with given criteria
     *
     * @param active whether users are active or not
     * @param notRole   role to search by
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
                // remove anyone that is just a driver
                Authority driver = authorityRepository.findByName(AuthorityName.ROLE_DRIVER);
                Authority coord = authorityRepository.findByName(AuthorityName.ROLE_COORDINATOR);
                users.removeIf(u -> u.getAuthorities().contains(driver) && !u.getAuthorities().contains(coord));
            }   // get all non-coordinators
            else if (notRole == AuthorityName.ROLE_COORDINATOR) {
                // remove anyone not a coordinator
                Authority coord = authorityRepository.findByName(AuthorityName.ROLE_COORDINATOR);
                Authority admin = authorityRepository.findByName(AuthorityName.ROLE_ADMIN);
                users.removeIf(u -> u.getAuthorities().contains(coord) && !u.getAuthorities().contains(admin)); // remove if coord and is not admin
            } // get all non-admins
            else if (notRole == AuthorityName.ROLE_ADMIN) {
                // remove anyone that is an admin
                Authority admin = authorityRepository.findByName(AuthorityName.ROLE_ADMIN);
                users.removeIf(u -> u.getAuthorities().contains(admin));
            }
        }

        return users;

    }

    public User getUserById(Long id) {
        return userRepository.findOne(id);
    }

    /**
     * Creates the given user as a coordinator in the User persistence
     *
     * @param userDto the user to save
     * @return the created coordinator user
     */
    public User createCoordinatorUser(UserDto userDto) {
        User user = userMapper.map(userDto, User.class);

        List<Authority> authorities = authorityRepository.findByNameIn(Arrays.asList(AuthorityName.ROLE_COORDINATOR, AuthorityName.ROLE_DRIVER, AuthorityName.ROLE_RIDER));
        user.setAuthorities(authorities);

        return userRepository.save(user);
    }

    /**
     * Updates the given user as a coordinator in the User persistence
     *
     * @param userDto the user to update
     * @return the updated coordinator user
     */
    public User updateCoordinatorUser(UserDto userDto) {
        User existingUser = userRepository.findOne(userDto.getId());

        User newUser = userMapper.map(userDto, User.class);
        newUser.setAuthorities(existingUser.getAuthorities());

        return userRepository.save(newUser);
    }

    /**
     * Deletes the coordinator with the given id
     *
     * @param id id of coordinator to delete
     * @return true if deleted, false if user with id doesn't exist
     */
    public boolean deleteCoordinator(Long id) {
        if (userRepository.findOne(id) == null) {
            return false;
        } else {
            userRepository.delete(id);
            return true;
        }
    }

    /**
     * Creates a driver user from the given DriverDto
     *
     * @param driverDto the driverDto containing info for the driver
     * @return the created user
     */
    public User createDriverUser(DriverDto driverDto) {
        User user = new User(driverDto.getUsername(), driverDto.getDriverFirstName(), driverDto.getDriverLastName());

        List<Authority> authorities = authorityRepository.findByNameIn(Arrays.asList(AuthorityName.ROLE_DRIVER, AuthorityName.ROLE_RIDER));
        user.setAuthorities(authorities);

        return userRepository.save(user);
    }
}

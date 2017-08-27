package edu.csus.asi.saferides.security.service;

import edu.csus.asi.saferides.model.dto.DriverDto;
import edu.csus.asi.saferides.security.ArgonPasswordEncoder;
import edu.csus.asi.saferides.security.dto.UserDto;
import edu.csus.asi.saferides.security.mapper.UserMapper;
import edu.csus.asi.saferides.security.model.Authority;
import edu.csus.asi.saferides.security.model.AuthorityName;
import edu.csus.asi.saferides.security.model.User;
import edu.csus.asi.saferides.security.repository.AuthorityRepository;
import edu.csus.asi.saferides.security.repository.UserRepository;
import edu.csus.asi.saferides.utility.Util;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
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
    private ArgonPasswordEncoder argonPasswordEncoder;

    /**
     * Inject dependencies
     *
     * @param userRepository       singleton for UserRepository
     * @param userMapper           singleton for UserMapper
     * @param authorityRepository  singleton for AuthorityRepository
     * @param argonPasswordEncoder singleton for ArgonPasswordEncoder
     */
    @Autowired
    public UserService(UserRepository userRepository, UserMapper userMapper, AuthorityRepository authorityRepository,
                       ArgonPasswordEncoder argonPasswordEncoder) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.authorityRepository = authorityRepository;
        this.argonPasswordEncoder = argonPasswordEncoder;
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

        user.setPassword(this.hashPassword(userDto.getPassword()));

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

        // TODO - blacklist passwords
        if (StringUtils.isEmpty(newUser.getPassword())) {
            newUser.setPassword(existingUser.getPassword());
        } else {
            newUser.setPassword(argonPasswordEncoder.encode(newUser.getPassword()));
            newUser.setLastPasswordResetDate(LocalDateTime.now(ZoneId.of(Util.APPLICATION_TIME_ZONE))); // invalidate existing token
        }

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
        User user = new User(driverDto.getOneCardId(), driverDto.getDriverFirstName(), driverDto.getDriverLastName(), hashPassword(driverDto.getPassword()));

        List<Authority> authorities = authorityRepository.findByNameIn(Arrays.asList(AuthorityName.ROLE_DRIVER, AuthorityName.ROLE_RIDER));
        user.setAuthorities(authorities);

        return userRepository.save(user);
    }

    public User updateDriverUser(DriverDto driverDto) {
        User user = userRepository.findByUsernameIgnoreCase(driverDto.getOneCardId());
        user.setFirstName(driverDto.getDriverFirstName());
        user.setLastName(driverDto.getDriverLastName());
        user.setPassword(hashPassword(driverDto.getPassword()));
        return userRepository.save(user);
    }

    /**
     * Hashes the given password using the ArgonPasswordEncoder
     *
     * @param password the password to hash
     * @return the hashed password
     */
    private String hashPassword(String password) {
        return argonPasswordEncoder.encode(password);
    }

}

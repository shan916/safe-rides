package edu.csus.asi.saferides.security.service;

import edu.csus.asi.saferides.model.AuthorityName;
import edu.csus.asi.saferides.model.User;
import edu.csus.asi.saferides.repository.UserRepository;
import edu.csus.asi.saferides.security.JwtUserFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * Provides two separate authentication methods (Real user, rider).
 */
@Service
public class JwtUserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    /**
     * Dependency injection
     *
     * @param userRepository User Repository
     */
    @Autowired
    public JwtUserDetailsServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Get UserDetail object for a User
     *
     * @param username the username of the User
     * @return UserDetail object for user
     * @throws UsernameNotFoundException error if username not found in the datastore (database - users table)
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsernameIgnoreCase(username);

        if (user == null) {
            throw new UsernameNotFoundException(String.format("No user found with username '%s'.", username));
        } else {

            if (!user.isActive()) {
                user.setAuthorityLevel(AuthorityName.ROLE_RIDER);
            }

            return JwtUserFactory.create(user);
        }
    }
}

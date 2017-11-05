package edu.csus.asi.saferides.security.service;

import edu.csus.asi.saferides.model.Authority;
import edu.csus.asi.saferides.model.AuthorityName;
import edu.csus.asi.saferides.model.User;
import edu.csus.asi.saferides.repository.AuthorityRepository;
import edu.csus.asi.saferides.repository.UserRepository;
import edu.csus.asi.saferides.security.JwtUserFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

/**
 * Provides two separate authentication methods (Real user, rider).
 */
@Service
public class JwtUserDetailsServiceImpl implements UserDetailsService {

    private UserRepository userRepository;
    private AuthorityRepository authorityRepository;

    /**
     * Dependency injection
     *
     * @param userRepository      User Repository
     * @param authorityRepository Authority Repository
     */
    @Autowired
    public JwtUserDetailsServiceImpl(UserRepository userRepository, AuthorityRepository authorityRepository) {
        this.userRepository = userRepository;
        this.authorityRepository = authorityRepository;
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
                Authority authority = authorityRepository.findByName(AuthorityName.ROLE_RIDER);
                ArrayList authorityList = new ArrayList<Authority>();
                authorityList.add(authority);
                user.setAuthorities(authorityList);
            }

            return JwtUserFactory.create(user);
        }
    }
}

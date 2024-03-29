package edu.csus.asi.saferides.security.service;

import edu.csus.asi.saferides.model.RideRequest;
import edu.csus.asi.saferides.repository.RideRequestRepository;
import edu.csus.asi.saferides.security.JwtUserFactory;
import edu.csus.asi.saferides.security.model.Authority;
import edu.csus.asi.saferides.security.model.AuthorityName;
import edu.csus.asi.saferides.security.model.User;
import edu.csus.asi.saferides.security.repository.AuthorityRepository;
import edu.csus.asi.saferides.security.repository.UserRepository;
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

    // dependency injections
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RideRequestRepository rideRequestRepository;
    @Autowired
    private AuthorityRepository authorityRepository;

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
            return JwtUserFactory.create(user);
        }
    }

    /**
     * Get UserDetail object for a User
     *
     * @param oneCardId the onecard id of the rider
     * @return UserDetail object for rider
     * @throws UsernameNotFoundException error if username not found in the datastore (database - users table)
     */
    public UserDetails loadRiderByOnecard(String oneCardId) throws UsernameNotFoundException {
        // find user in rides table
        RideRequest rideRequest = rideRequestRepository.findTop1ByOneCardIdOrderByRequestDateDesc(oneCardId);

        if (rideRequest == null) {
            throw new UsernameNotFoundException(String.format("No rider found with OneCard ID '%s'.", oneCardId));
        } else {
            // return user with the requestor name and rider role
            User riderUser = new User(rideRequest.getOneCardId(), rideRequest.getRequestorFirstName(), rideRequest.getRequestorLastName());

            ArrayList<Authority> authorityList = new ArrayList<Authority>();
            authorityList.add(authorityRepository.findByName(AuthorityName.ROLE_RIDER));
            riderUser.setAuthorities(authorityList);

            return JwtUserFactory.create(riderUser);
        }
    }
}

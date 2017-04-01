package edu.csus.asi.saferides.service;

import edu.csus.asi.saferides.model.ResponseMessage;
import edu.csus.asi.saferides.security.*;
import edu.csus.asi.saferides.security.model.Authority;
import edu.csus.asi.saferides.security.model.AuthorityName;
import edu.csus.asi.saferides.security.model.User;
import edu.csus.asi.saferides.security.repository.AuthorityRepository;
import edu.csus.asi.saferides.security.repository.UserRepository;
import edu.csus.asi.saferides.security.service.JwtAuthenticationResponse;
import edu.csus.asi.saferides.security.service.JwtUserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import java.net.URI;
import java.util.ArrayList;

/*
 * Rest API controller for the User resource 
 * */
@RestController
@CrossOrigin(origins = {"http://localhost:9000", "https://codeteam6.io"})
@RequestMapping("/users")
public class UserController {

    @Value("${jwt.header}")
    private String tokenHeader;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private JwtUserDetailsServiceImpl userDetailsService;

    // this creates a singleton for UserRepository
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AuthorityRepository authorityRepository;


	/*
     * GET "/users"
	 * 
	 * @return list users
	 
	@RequestMapping(method = RequestMethod.GET)
	public Iterable<User> retrieveAll() {
			return userRepository.findAll();
	}
	* */

	/*
     * GET "/users"
	 *
	 * @return JwtUser object of the current authenticated user
	 */

    @RequestMapping(value = "/me", method = RequestMethod.GET)
    public JwtUser getAuthenticatedUser(HttpServletRequest request) {
        String token = request.getHeader(tokenHeader);
        String username = jwtTokenUtil.getUsernameFromToken(token);
        ArrayList<AuthorityName> authoritiesFromToken = jwtTokenUtil.getAuthoritiesFromToken(token);

        JwtUser user;

        // if rider
        if (authoritiesFromToken.size() == 1 && authoritiesFromToken.get(0).equals(AuthorityName.ROLE_RIDER)) {
            try {
                // try to load from riderequests
                user = (JwtUser) userDetailsService.loadRiderByOnecard(username);
            } catch (Exception e) {
                // else create a new 'user'
                User riderUser = new User(username, "anon_fname", "anon_lname");

                ArrayList<Authority> authorityList = new ArrayList<Authority>();
                authorityList.add(authorityRepository.findByName(AuthorityName.ROLE_RIDER));
                riderUser.setAuthorities(authorityList);

                user = JwtUserFactory.create(riderUser);
            }
        } else {
            user = (JwtUser) userDetailsService.loadUserByUsername(username);
        }

        return user;
    }

	/*
     * GET "/users/{id}
	 * 
	 * @param id - the id of the user to find
	 * 
	 * @return user with specified id else not found
	 
	@RequestMapping(method = RequestMethod.GET, value="/{id}")
	public ResponseEntity<?> retrieve(@PathVariable Long id) {
		User result = userRepository.findOne(id);
		
		if (result == null) {
			return ResponseEntity.notFound().build();
		} else {
			return ResponseEntity.ok(result);
		}
	}
	* */

	/*
     * GET "/users/byUsername/{username}
	 * 
	 * @param username - the username of the user to find
	 * 
	 * @return user with specified username else not found
	 
	@RequestMapping(method = RequestMethod.GET, value="/byUsername/{username}")
	public ResponseEntity<?> retrieve(@PathVariable String username) {
		User result = userRepository.findByUsername(username);
		
		if (result == null) {
			return ResponseEntity.notFound().build();
		} else {
			return ResponseEntity.ok(result);
		}
	}
	* */


    /*
     * POST "/users"
     *
     * Creates the given user
     *
     * @param user - the user to be created in the database
     *
     * @return HTTP response containing saved entity with status of "created"
     *  	   and the location header set to location of the entity
     * */
    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<?> save(@RequestBody User user) {
        User result = userRepository.save(user);

        // create URI of where the user was created
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest().path("/{username}")
                .buildAndExpand(result.getUsername()).toUri();

        return ResponseEntity.created(location).body(result);
    }


    /*
     * POST "/users/auth"
     *
     * Authenticates the given user
     *
     * @param user - the user to be authenticated in the database
     *
     * */
    @RequestMapping(method = RequestMethod.POST, value = "/auth")
    public ResponseEntity<?> authenticate(@RequestBody JwtAuthenticationRequest authenticationRequest) throws AuthenticationException {
        // Perform the security
        try {
            final Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            authenticationRequest.getUsername(),
                            authenticationRequest.getPassword()
                    )
            );
            SecurityContextHolder.getContext().setAuthentication(authentication);

            // Reload password post-security so we can generate token
            final UserDetails userDetails = userDetailsService.loadUserByUsername(authenticationRequest.getUsername());
            final String token = jwtTokenUtil.generateToken(userDetails);

            // Return the token
            return ResponseEntity.ok(new JwtAuthenticationResponse(token));
        } catch (AuthenticationException exception) {
            return ResponseEntity.status(422).body(new ResponseMessage("Bad credentials"));
        }
    }

    /*
     * POST "/users/authrider"
     *
     * Authenticates the rider
     *
     * @param authenticationRequest - the rider's onecard to be authenticated
     *
     * */
    @RequestMapping(method = RequestMethod.POST, value = "/authrider")
    public ResponseEntity<?> authenticateRider(@RequestBody JwtRiderAuthenticationRequest riderAuthenticationRequest) throws AuthenticationException {
        // validate onecard (not null)
        if (riderAuthenticationRequest.getOneCardId() == null) {
            return ResponseEntity.status(422).body(new ResponseMessage("Bad credentials"));
        }

        User riderUser = new User("" + riderAuthenticationRequest.getOneCardId(), "anon_fname", "anon_lname");

        ArrayList<Authority> authorityList = new ArrayList<Authority>();
        authorityList.add(authorityRepository.findByName(AuthorityName.ROLE_RIDER));
        riderUser.setAuthorities(authorityList);

        UserDetails userDetails = JwtUserFactory.create(riderUser);

        final String token = jwtTokenUtil.generateToken(userDetails);

        // Return the token
        return ResponseEntity.ok(new JwtAuthenticationResponse(token));
    }


    @RequestMapping(value = "/refresh", method = RequestMethod.GET)
    public ResponseEntity<?> refreshAndGetAuthenticationToken(HttpServletRequest request) {
        String token = request.getHeader(tokenHeader);
        String username = jwtTokenUtil.getUsernameFromToken(token);
        JwtUser user = (JwtUser) userDetailsService.loadUserByUsername(username);

        if (jwtTokenUtil.canTokenBeRefreshed(token, user.getLastPasswordResetDate())) {
            String refreshedToken = jwtTokenUtil.refreshToken(token);
            return ResponseEntity.ok(new JwtAuthenticationResponse(refreshedToken));
        } else {
            return ResponseEntity.badRequest().body(null);
        }
    }

    /*
     * PUT "/users/{id}"
     *
     * Updates the given user
     *
     * @param user - the user to be updated in the database
     *
     * @return HTTP response containing saved entity with status of "ok"
     * */
    @RequestMapping(method = RequestMethod.PUT, value = "/{username}")
    public ResponseEntity<?> save(@PathVariable String username, @RequestBody User user) {
        User result = userRepository.save(user);

        return ResponseEntity.ok(result);
    }
	
	
	/*
	 * DELETE "/users/{id} 
	 * 
	 * @param id - the id of the user to delete
	 * 
	 * @return HTTP response with status of "no content" if deleted successfully
	 * 		   else response with status of "not found"
	 
	@RequestMapping(method = RequestMethod.DELETE, value = "/{id}")
	public ResponseEntity<?> delete(@PathVariable Long id) {
		if (userRepository.findOne(id) == null) {
			return ResponseEntity.notFound().build();
		} else {
			userRepository.delete(id);
			return ResponseEntity.noContent().build();
		}
	}
	* */
}

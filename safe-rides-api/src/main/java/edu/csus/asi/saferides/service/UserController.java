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
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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

/**
 * Rest API controller for the User resource
 */
@RestController
@CrossOrigin(origins = {"http://localhost:9000", "https://codeteam6.io"})
@RequestMapping("/users")
public class UserController {

    @Value("${jwt.header}")
    private String tokenHeader;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUserDetailsServiceImpl userDetailsService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AuthorityRepository authorityRepository;


    /**
     * Get information on authenticated user
     *
     * @param request
     * @return JWTUser object
     */
    @RequestMapping(value = "/me", method = RequestMethod.GET)
    @PreAuthorize("hasRole('RIDER')")
    @ApiOperation(value = "myUserInfo", nickname = "My User Information", notes = "Returns the authenticated user's information")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success", response = JwtUser.class),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 403, message = "Forbidden"),
            @ApiResponse(code = 500, message = "Failure")})
    public JwtUser myUserInfo(HttpServletRequest request) {
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

    /**
     * Create a new user
     *
     * @param user to create
     * @return result
     */
    @RequestMapping(method = RequestMethod.POST)
    @PreAuthorize("hasRole('ADMIN')")
    @ApiOperation(value = "createUser", nickname = "Create User", notes = "Creates a new user")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success", response = ResponseEntity.class),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 403, message = "Forbidden"),
            @ApiResponse(code = 500, message = "Failure")})
    public ResponseEntity<?> createUser(@RequestBody User user) {
        user.setUsername(user.getUsername().toLowerCase());
        User result = userRepository.save(user);

        // create URI of where the user was created
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest().path("/{username}")
                .buildAndExpand(result.getUsername()).toUri();

        return ResponseEntity.created(location).body(result);
    }

    /**
     * Processes authentication request for application users
     *
     * @param authenticationRequest POSTed authentication request
     * @return JWT
     * @throws AuthenticationException
     */
    @RequestMapping(method = RequestMethod.POST, value = "/auth")
    @ApiOperation(value = "authenticate", nickname = "Authenticate", notes = "User's authentication - Admin, Coordinator, Driver. Returns a JWT")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success", response = ResponseEntity.class),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 403, message = "Forbidden"),
            @ApiResponse(code = 500, message = "Failure")})
    public ResponseEntity<?> authenticate(@RequestBody JwtAuthenticationRequest authenticationRequest) throws AuthenticationException {
        // Perform the security
        try {
            final Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            authenticationRequest.getUsername().toLowerCase(),
                            authenticationRequest.getPassword()
                    )
            );
            SecurityContextHolder.getContext().setAuthentication(authentication);

            // Reload password post-security so we can generate token
            final UserDetails userDetails = userDetailsService.loadUserByUsername(authenticationRequest.getUsername().toLowerCase());
            final String token = jwtTokenUtil.generateToken(userDetails);

            // Return the token
            return ResponseEntity.ok(new JwtAuthenticationResponse(token));
        } catch (AuthenticationException exception) {
            return ResponseEntity.status(422).body(new ResponseMessage("Bad credentials"));
        }
    }

    /**
     * Processes authentication request for riders
     *
     * @param riderAuthenticationRequest POSTed authentication request
     * @return JWT
     * @throws AuthenticationException
     */
    @RequestMapping(method = RequestMethod.POST, value = "/authrider")
    @ApiOperation(value = "authenticateRider", nickname = "Authenticate Rider", notes = "User's authentication - Rider. Returns a JWT")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success", response = ResponseEntity.class),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 403, message = "Forbidden"),
            @ApiResponse(code = 500, message = "Failure")})
    public ResponseEntity<?> authenticateRider(@RequestBody JwtRiderAuthenticationRequest riderAuthenticationRequest) throws AuthenticationException {
        // validate onecard (not null)
        if (riderAuthenticationRequest.getOneCardId() == null) {
            return ResponseEntity.status(422).body(new ResponseMessage("Bad credentials"));
        }

        // create a new user object for a rider
        User riderUser = new User(riderAuthenticationRequest.getOneCardId().toLowerCase(), "anon_fname", "anon_lname");

        // set roles for the new user to rider only
        ArrayList<Authority> authorityList = new ArrayList<Authority>();
        authorityList.add(authorityRepository.findByName(AuthorityName.ROLE_RIDER));
        riderUser.setAuthorities(authorityList);

        // create a token for the new user
        UserDetails userDetails = JwtUserFactory.create(riderUser);
        final String token = jwtTokenUtil.generateToken(userDetails);

        // Return the token
        return ResponseEntity.ok(new JwtAuthenticationResponse(token));
    }

    /**
     * Update a user
     *
     * @param username of user to update
     * @param user     details to set
     * @return status
     */
    @RequestMapping(method = RequestMethod.PUT, value = "/{username}")
    @PreAuthorize("hasRole('ADMIN')")
    @ApiOperation(value = "updateUser", nickname = "Update User", notes = "Updates a user")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success", response = ResponseEntity.class),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 403, message = "Forbidden"),
            @ApiResponse(code = 500, message = "Failure")})
    public ResponseEntity<?> updateUser(@PathVariable String username, @RequestBody User user) {
        if (userRepository.findByUsername(username.toLowerCase()).getUsername() != user.getUsername().toLowerCase()) {
            return ResponseEntity.badRequest().body(new ResponseMessage("Username mismatch"));
        }
        user.setUsername(user.getUsername().toLowerCase());
        User result = userRepository.save(user);

        return ResponseEntity.ok(result);
    }
}

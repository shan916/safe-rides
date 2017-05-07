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
import edu.csus.asi.saferides.utility.Util;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.apache.commons.lang3.StringUtils;
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
import java.util.List;

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

    @RequestMapping(method = RequestMethod.GET)
    @PreAuthorize("hasRole('ADMIN')")
    @ApiOperation(value = "retrieveAll", nickname = "retrieveAll", notes = "Returns a list of users...")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success", response = User.class, responseContainer = "List"),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 403, message = "Forbidden"),
            @ApiResponse(code = 500, message = "Failure")})
    public List<User> retrieveAll(@RequestParam(value = "active", required = false) Boolean active,
                                  @RequestParam(value = "role", required = false) AuthorityName role) {
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
     * Get information on authenticated user
     *
     * @param request HTTP servlet request
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
     * Processes authentication request for application users
     *
     * @param authenticationRequest POSTed authentication request
     * @return JWT
     * @throws AuthenticationException authentication exception
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
     * @throws AuthenticationException authentication exception
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
     * Creates a new user
     * <p>
     * Returns HTTP status 400 if password does not meet security requirements
     *
     * @param user Request body containing user to create
     * @return newly created user and location
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
        if (user.getPassword() == null || !Util.isPasswordValid(user.getPassword())) {
            return ResponseEntity.badRequest().body(new ResponseMessage("Password does not meet security requirements"));
        }

        User result = userRepository.save(user);

        // create URI of where the user was created
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest().path("/{username}")
                .buildAndExpand(result.getUsername()).toUri();

        return ResponseEntity.created(location).body(result);
    }

    /**
     * Updates the given user
     * <p>
     * Returns HTTP status 400 under the following conditions:
     * <ul>
     * <li>
     * Username in path does not match username in request body
     * </li>
     * <li>
     * New password does not meet security requirements
     * </li>
     * </ul>
     *
     * @param username path parameter of username to update
     * @param user     request body containing user to update
     * @return updated user or error if any
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
        User existingUser = userRepository.findByUsernameIgnoreCase(username);

        // update user if username in path and username of user object are equal
        if (existingUser != null && existingUser.getUsername().equalsIgnoreCase(user.getUsername())) {

            // if new password is empty or null, keep existing password
            if (StringUtils.isEmpty(user.getPassword())) {
                user.setPassword(existingUser.getPassword());
            } else if (!Util.isPasswordValid(user.getPassword())) { // else new password must meet security requirements
                return ResponseEntity.badRequest().body(new ResponseMessage("New password does not meet security requirements"));
            }

            User result = userRepository.save(user);
            return ResponseEntity.ok(result);
        } else {
            return ResponseEntity.badRequest().body(new ResponseMessage("Username mismatch"));
        }
    }

}
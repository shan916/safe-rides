package edu.csus.asi.saferides.controller;

import edu.csus.asi.saferides.mapper.UserMapper;
import edu.csus.asi.saferides.model.AuthorityName;
import edu.csus.asi.saferides.model.ResponseMessage;
import edu.csus.asi.saferides.model.User;
import edu.csus.asi.saferides.model.dto.UserDto;
import edu.csus.asi.saferides.repository.AuthorityRepository;
import edu.csus.asi.saferides.repository.UserRepository;
import edu.csus.asi.saferides.security.JwtTokenUtil;
import edu.csus.asi.saferides.security.JwtUser;
import edu.csus.asi.saferides.security.service.JwtUserDetailsServiceImpl;
import edu.csus.asi.saferides.service.UserService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.validation.annotation.Validated;
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

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private UserService userService;

    @RequestMapping(method = RequestMethod.GET)
    @PreAuthorize("hasRole('ADMIN')")
    @ApiOperation(value = "retrieveAll", nickname = "retrieveAll", notes = "Returns a list of users...")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success", response = User.class, responseContainer = "List"),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 403, message = "Forbidden"),
            @ApiResponse(code = 500, message = "Failure")})
    public List<UserDto> retrieveAll(@RequestParam(value = "active", required = false) Boolean active,
                                     @RequestParam(value = "role", required = false) AuthorityName role) {

        List<User> users = userService.getUsers(active, role);

        ArrayList<UserDto> userDtos = new ArrayList<>();
        for (User user : users) {
            userDtos.add(userMapper.map(user, UserDto.class));
        }

        return userDtos;
    }

    /**
     * Returns user with given id
     *
     * @param id - id of user to find
     * @return user with given id
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    @PreAuthorize("hasRole('ADMIN')")
    @ApiOperation(value = "retrieve", nickname = "retrieve", notes = "Returns a user with the given id")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success", response = User.class),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 403, message = "Forbidden"),
            @ApiResponse(code = 500, message = "Failure")})
    public ResponseEntity<?> retrieve(@PathVariable Long id) {
        User user = userService.getUserById(id);

        if (user == null) {
            return ResponseEntity.notFound().build();
        } else {
            return ResponseEntity.ok(userMapper.map(user, UserDto.class));
        }
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

        JwtUser user = (JwtUser) userDetailsService.loadUserByUsername(username);

        return user;
    }

    /**
     * Creates a new user
     * <p>
     * Returns HTTP status 400 if password does not meet security requirements
     *
     * @param userDto Request body containing user to create
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
    public ResponseEntity<?> createUser(@Validated @RequestBody UserDto userDto) {
        User result = userService.createCoordinatorUser(userDto);

        // create URI of where the user was created
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest().path("/{username}")
                .buildAndExpand(result.getUsername()).toUri();

        return ResponseEntity.created(location).body(userMapper.map(result, UserDto.class));
    }

    /**
     * Updates the user with the given id
     * <p>
     * Returns HTTP status 400 under the following conditions:
     * <ul>
     * <li>
     * Id in path does not match id in request body
     * </li>
     * <li>
     * New password does not meet security requirements
     * </li>
     * </ul>
     *
     * @param id      path parameter of id of user to update
     * @param userDto request body containing user to update
     * @return updated user or error if any
     */
    @RequestMapping(method = RequestMethod.PUT, value = "/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @ApiOperation(value = "updateUser", nickname = "Update User", notes = "Updates a user")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success", response = ResponseEntity.class),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 403, message = "Forbidden"),
            @ApiResponse(code = 500, message = "Failure")})
    public ResponseEntity<?> updateUser(@PathVariable Long id, @Validated @RequestBody UserDto userDto) {

        if (!userDto.getId().equals(id)) {
            return ResponseEntity.badRequest().body(new ResponseMessage("Id mismatch"));
        }

        User existingUser = userRepository.findOne(id);

        if (existingUser == null) {
            return ResponseEntity.badRequest().body(new ResponseMessage("User does not exist"));
        }

        if (!existingUser.getUsername().equalsIgnoreCase(userDto.getUsername())) {
            return ResponseEntity.badRequest().body(new ResponseMessage("Username is not allowed to be modified"));
        }

        User result = userService.updateCoordinatorUser(userDto);

        return ResponseEntity.ok(userMapper.map(result, UserDto.class));
    }

    /**
     * Deletes the user with the given id
     *
     * @param id path parameter of id of user to delete
     * @return HTTP 204 if deleted, 400 if user with id doesn't exist
     */
    @RequestMapping(method = RequestMethod.DELETE, value = "/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @ApiOperation(value = "deleteUser", nickname = "Delete User", notes = "Deletes a user")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success", response = ResponseEntity.class),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 403, message = "Forbidden"),
            @ApiResponse(code = 500, message = "Failure")})
    public ResponseEntity<?> deleteUser(@PathVariable Long id) {
        if (userService.deleteCoordinator(id)) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.badRequest().body(new ResponseMessage("User does not exist"));
        }
    }

}
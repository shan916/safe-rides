package edu.csus.asi.saferides.controller;

import edu.csus.asi.saferides.mapper.UserMapper;
import edu.csus.asi.saferides.model.*;
import edu.csus.asi.saferides.model.dto.UserDto;
import edu.csus.asi.saferides.repository.DriverRepository;
import edu.csus.asi.saferides.repository.UserRepository;
import edu.csus.asi.saferides.security.JwtTokenUtil;
import edu.csus.asi.saferides.security.JwtUser;
import edu.csus.asi.saferides.security.service.JwtUserDetailsServiceImpl;
import edu.csus.asi.saferides.service.UserService;
import edu.csus.asi.saferides.utility.Util;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

/**
 * Rest API controller for the User resource
 */
@RestController
@CrossOrigin(origins = {"http://localhost:9000", "https://codeteam6.io"})
@RequestMapping("/users")
class UserController {

    private final String tokenHeader;
    private final JwtTokenUtil jwtTokenUtil;
    private final JwtUserDetailsServiceImpl userDetailsService;
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final UserService userService;
    private final DriverRepository driverRepository;

    /**
     * User controller constructor with dependency injection
     *
     * @param jwtTokenUtil        JWT Token Util
     * @param userDetailsService  User Details Service
     * @param userRepository      User Repository
     * @param userMapper          User Mapper
     * @param userService         User Service
     * @param driverRepository    Driver Repository
     * @param tokenHeader         HTTP header that stores the JWT, defined in application.yaml
     */
    @Autowired
    public UserController(JwtTokenUtil jwtTokenUtil, JwtUserDetailsServiceImpl userDetailsService,
                          UserRepository userRepository, UserMapper userMapper, UserService userService,
                          DriverRepository driverRepository, @Value("#{@environment['jwt.header'] ?: \"\" }") String tokenHeader) {
        this.jwtTokenUtil = jwtTokenUtil;
        this.userDetailsService = userDetailsService;
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.userService = userService;
        this.driverRepository = driverRepository;
        this.tokenHeader = tokenHeader;
    }

    /**
     * Get a list of users
     *
     * @param active  filter by active flag
     * @param role    filter to users that are in the specified role
     * @param notRole filter to users that are not in the specified role
     * @return List of users
     */
    @RequestMapping(method = RequestMethod.GET)
    @PreAuthorize("hasRole('COORDINATOR')")
    @ApiOperation(value = " ", nickname = "retrieveAll", notes = "Returns a list of users")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success", response = User.class, responseContainer = "List"),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 403, message = "Forbidden"),
            @ApiResponse(code = 500, message = "Failure")})
    public ResponseEntity<List<UserDto>> retrieveAll(@RequestParam(value = "active", required = false) Boolean active,
                                                     @RequestParam(value = "role", required = false) AuthorityName role,
                                                     @RequestParam(value = "!role", required = false) AuthorityName notRole) {

        List<User> users;
        if (notRole != null) {
            users = userService.getUsersNotInRole(active, notRole);
        } else {
            users = userService.getUsers(active, role);
        }

        ArrayList<UserDto> userDtos = new ArrayList<>();
        for (User user : users) {
            userDtos.add(userMapper.map(user, UserDto.class));
        }

        return ResponseEntity.ok(userDtos);
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
    public ResponseEntity<UserDto> retrieve(@PathVariable Long id) {
        User user = userService.getUserById(id);

        if (user == null) {
            return ResponseEntity.notFound().build();
        } else {
            return ResponseEntity.ok(userMapper.map(user, UserDto.class));
        }
    }

    /**
     * Deletes coordinator user with given id
     *
     * @param id - id of user to find
     * @return user with given id if not deleted, empty body if deleted
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    @PreAuthorize("hasRole('ADMIN')")
    @ApiOperation(value = "retrieve", nickname = "retrieve", notes = "Deletes a coordinator user with the given id")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success", response = User.class),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 403, message = "Forbidden"),
            @ApiResponse(code = 500, message = "Failure")})
    public ResponseEntity<UserDto> deleteCoordinator(@PathVariable Long id) {
        User user = userService.getUserById(id);

        if (user == null) {
            return ResponseEntity.notFound().build();
        } else {
            if (user.getAuthorityLevel() == AuthorityName.ROLE_COORDINATOR) {
                userRepository.delete(user);
                return ResponseEntity.noContent().build();
            }

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
    public ResponseEntity<JwtUser> myUserInfo(HttpServletRequest request) {
        String token = request.getHeader(tokenHeader);
        String username = jwtTokenUtil.getUsernameFromToken(token);
        ArrayList<AuthorityName> authoritiesFromToken = jwtTokenUtil.getAuthoritiesFromToken(token);

        JwtUser user = (JwtUser) userDetailsService.loadUserByUsername(username);

        return ResponseEntity.ok(user);
    }

    /**
     * Updates the user with the given id
     * <p>
     * Returns HTTP status 400 under the following conditions:
     * <ul>
     * <li>
     * Id in path does not match id in request body
     * </li>
     * </ul>
     *
     * @param request Http Request
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
    public ResponseEntity<?> updateUser(HttpServletRequest request, @PathVariable Long id, @Validated @RequestBody UserDto userDto) {
        String token = request.getHeader(tokenHeader);
        String username = jwtTokenUtil.getUsernameFromToken(token);
        if (username.equals(userDto.getUsername())) {
            return ResponseEntity.badRequest().body(new ResponseMessage("You cannot modify yourself"));
        }

        if (!userDto.getId().equals(id)) {
            return ResponseEntity.badRequest().body(new ResponseMessage("Id mismatch"));
        }

        User existingUser = userRepository.findOne(id);

        if (existingUser == null) {
            return ResponseEntity.badRequest().body(new ResponseMessage("User does not exist"));
        }

        Driver driver = driverRepository.findByUser(existingUser);

        if (driver != null && driver.getUser().isActive()) {
            return ResponseEntity.badRequest().body(new ResponseMessage("Cannot modify an active driver as a coordinator!"));
        } else if (driver != null) {
            // todo: save driver statistics for reporting
            driverRepository.delete(driver);
        }

        if (!existingUser.getUsername().equalsIgnoreCase(userDto.getUsername())) {
            return ResponseEntity.badRequest().body(new ResponseMessage("Username is not allowed to be modified"));
        }

        existingUser.setFirstName(userDto.getFirstName());
        existingUser.setLastName(userDto.getLastName());
        existingUser.setTokenValidFrom(LocalDateTime.now(ZoneId.of(Util.APPLICATION_TIME_ZONE)));
        existingUser.setActive(userDto.isActive());

        existingUser.setAuthorityLevel(AuthorityName.ROLE_COORDINATOR);

        User result = userRepository.save(existingUser);

        return ResponseEntity.ok(userMapper.map(result, UserDto.class));
    }
}

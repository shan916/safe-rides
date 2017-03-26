package edu.csus.asi.saferides.service;

import edu.csus.asi.saferides.model.RideRequest;
import edu.csus.asi.saferides.model.RideRequestStatus;
import edu.csus.asi.saferides.repository.RideRequestRepository;
import edu.csus.asi.saferides.security.JwtTokenUtil;
import edu.csus.asi.saferides.security.JwtUserFactory;
import edu.csus.asi.saferides.security.model.Authority;
import edu.csus.asi.saferides.security.model.AuthorityName;
import edu.csus.asi.saferides.security.model.User;
import edu.csus.asi.saferides.security.repository.AuthorityRepository;
import edu.csus.asi.saferides.security.repository.UserRepository;
import edu.csus.asi.saferides.security.service.JwtAuthenticationResponse;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.mobile.device.Device;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/*
 * @author Ryan Long
 * 
 * Rest API controller for the RideRequest resource 
 * */
@RestController
@CrossOrigin(origins = {"http://localhost:9000", "https://codeteam6.io"})
@RequestMapping("/rides")
@PreAuthorize("hasRole('COORDINATOR')")
public class RideRequestController {

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    // this creates a singleton for RideRequestRepository
    @Autowired
    private RideRequestRepository rideRequestRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AuthorityRepository authorityRepository;

    /*
     * GET /rides
     */
    @RequestMapping(method = RequestMethod.GET)
    @ApiOperation(value = "retrieveAll", nickname = "retrieveAll", notes = "Returns a list of rides...")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success", response = RideRequest.class, responseContainer = "List"),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 403, message = "Forbidden"),
            @ApiResponse(code = 500, message = "Failure")})
    public Iterable<RideRequest> retrieveAll(@RequestParam(value = "status", required = false) RideRequestStatus status) {
        return rideRequestRepository.findAll();
    }

    /*
     * GET /rides/{id}
     */
    @RequestMapping(method = RequestMethod.GET, value = "/{id}")
    @ApiOperation(value = "retrieve", nickname = "retrieve", notes = "Returns a ride with the specified id")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success", response = ResponseEntity.class),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 403, message = "Forbidden"),
            @ApiResponse(code = 500, message = "Failure")})
    public ResponseEntity<?> retrieve(@PathVariable Long id) {
        RideRequest result = rideRequestRepository.findOne(id);

        if (result == null) {
            return ResponseEntity.notFound().build();
        } else {
            return ResponseEntity.ok(result);
        }
    }

    /*
     * POST /rides
     */
    @RequestMapping(method = RequestMethod.POST)
    @PreAuthorize("hasRole('RIDER')")
    @ApiOperation(value = "save", nickname = "save", notes = "Creates a given ride request")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success", response = ResponseEntity.class),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 403, message = "Forbidden"),
            @ApiResponse(code = 500, message = "Failure")})

    public ResponseEntity<?> save(@RequestBody RideRequest rideRequest, Device device) {
        rideRequest.setStatus(RideRequestStatus.UNASSIGNED);    // default to unassigned status

        RideRequest result = rideRequestRepository.save(rideRequest);

        // create URI of where the rideRequest was created
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest().path("/{id}")
                .buildAndExpand(result.getId()).toUri();

        Date currentDate = new Date();

        User requestor = new User("" + rideRequest.getOneCardId(),
                rideRequest.getRequestorFirstName(), rideRequest.getRequestorLastName(), currentDate.toString(), "null@null.null");

        requestor.setLastPasswordResetDate(currentDate);

        List<Authority> authorityList = new ArrayList<Authority>();
        authorityList.add(authorityRepository.findByName(AuthorityName.ROLE_RIDER));

        requestor.setAuthorities(authorityList);

        userRepository.save(requestor);
        
        final String token = jwtTokenUtil.generateToken(JwtUserFactory.create(requestor), device);

        // Return the token
        return ResponseEntity.ok(new JwtAuthenticationResponse(token));
    }

    /*
     * PUT /rides/{id}
     */
    @RequestMapping(method = RequestMethod.PUT, value = "/{id}")
    @ApiOperation(value = "save", nickname = "save", notes = "Updates the given ride request")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success", response = ResponseEntity.class),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 403, message = "Forbidden"),
            @ApiResponse(code = 500, message = "Failure")})
    public ResponseEntity<?> save(@PathVariable Long id, @RequestBody RideRequest rideRequest) {
        RideRequest result = rideRequestRepository.save(rideRequest);

        return ResponseEntity.ok(result);
    }


    /*
     * DELETE /rides/{id}
     */
    @RequestMapping(method = RequestMethod.DELETE, value = "/{id}")
    @ApiOperation(value = "delete", nickname = "delete", notes = "Deletes a ride request")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success", response = ResponseEntity.class),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 403, message = "Forbidden"),
            @ApiResponse(code = 500, message = "Failure")})
    public ResponseEntity<?> delete(@PathVariable Long id) {
        if (rideRequestRepository.findOne(id) == null) {
            return ResponseEntity.notFound().build();
        } else {
            rideRequestRepository.delete(id);
            return ResponseEntity.noContent().build();
        }
    }
}

package edu.csus.asi.saferides.service;

import edu.csus.asi.saferides.mapper.RideRequestMapper;
import edu.csus.asi.saferides.model.ResponseMessage;
import edu.csus.asi.saferides.model.RideRequest;
import edu.csus.asi.saferides.model.RideRequestStatus;
import edu.csus.asi.saferides.model.dto.RideRequestDto;
import edu.csus.asi.saferides.repository.RideRequestRepository;
import edu.csus.asi.saferides.security.JwtTokenUtil;
import edu.csus.asi.saferides.security.repository.AuthorityRepository;
import edu.csus.asi.saferides.security.repository.UserRepository;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import java.net.URI;
import java.util.Date;


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

    @Autowired
    private GeocodingService geocodingService;

    @Autowired
    private RideRequestMapper rideRequestMapper;

    @Value("${jwt.header}")
    private String tokenHeader;


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
            return ResponseEntity.noContent().build();
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
    public ResponseEntity<?> save(HttpServletRequest request, @RequestBody RideRequest rideRequest) {
        String authToken = request.getHeader(this.tokenHeader);
        if (authToken != null) {
            String username = jwtTokenUtil.getUsernameFromToken(authToken);
            rideRequest.setOneCardId(username);
        }
        if (rideRequest.getOneCardId() == null) {
            return ResponseEntity.badRequest().body(new ResponseMessage("OneCardID is null"));
        }
        rideRequest.setRequestDate(new Date());    // default to current datetime
        rideRequest.setStatus(RideRequestStatus.UNASSIGNED);    // default to unassigned status

        geocodingService.setCoordinates(rideRequest);

        RideRequest result = rideRequestRepository.save(rideRequest);

        // create URI of where the rideRequest was created
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest().path("/{id}")
                .buildAndExpand(result.getId()).toUri();

        return ResponseEntity.created(location).body(result);
    }

    /*
     * PUT /rides/{id}
     */
    @RequestMapping(method = RequestMethod.PUT, value = "/{id}")
    @PreAuthorize("hasRole('DRIVER')")
    @ApiOperation(value = "save", nickname = "save", notes = "Updates the given ride request")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success", response = ResponseEntity.class),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 403, message = "Forbidden"),
            @ApiResponse(code = 500, message = "Failure")})
    public ResponseEntity<?> save(@PathVariable Long id, @RequestBody RideRequest rideRequest) {
        RideRequest databaseVersion = rideRequestRepository.findOne(rideRequest.getId());

        geocodingService.setCoordinates(databaseVersion);

        RideRequest result = rideRequestRepository.save(databaseVersion);

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
            return ResponseEntity.noContent().build();
        } else {
            rideRequestRepository.delete(id);
            return ResponseEntity.ok(new ResponseMessage("Ride Deleted"));
        }
    }

    /*
     * GET /rides/mine
     */
    @RequestMapping(method = RequestMethod.GET, value = "/mine")
    @PreAuthorize("hasRole('RIDER')")
    @ApiOperation(value = "retrieveMyRide", nickname = "retrieveMyRide", notes = "Returns currently authenticated user's current ride request...")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success", response = RideRequest.class, responseContainer = "List"),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 403, message = "Forbidden"),
            @ApiResponse(code = 500, message = "Failure")})
    public ResponseEntity<?> retrieveMyRide(HttpServletRequest request) {
        String authToken = request.getHeader(this.tokenHeader);
        String username = jwtTokenUtil.getUsernameFromToken(authToken);

        RideRequest rideRequest = rideRequestRepository.findTop1ByOneCardIdOrderByRequestDateDesc(username);

        if (rideRequest == null) {
            return ResponseEntity.noContent().build();
        } else {
            // TODO this can return a ride request that is old. (but will be the latest)
            // ALSO TODO change the response to a DTO rather than the full ride request
            RideRequestDto dto = rideRequestMapper.map(rideRequest, RideRequestDto.class);
            return ResponseEntity.ok(dto);
        }
    }

}

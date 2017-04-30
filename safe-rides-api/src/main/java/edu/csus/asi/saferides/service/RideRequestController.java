package edu.csus.asi.saferides.service;

import edu.csus.asi.saferides.mapper.RideRequestMapper;
import edu.csus.asi.saferides.model.ResponseMessage;
import edu.csus.asi.saferides.model.RideRequest;
import edu.csus.asi.saferides.model.RideRequestStatus;
import edu.csus.asi.saferides.model.dto.RideRequestDto;
import edu.csus.asi.saferides.repository.RideRequestRepository;
import edu.csus.asi.saferides.security.JwtTokenUtil;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import java.net.URI;
import java.util.Date;

/**
 * Rest API controller for the RideRequest resource
 */
@RestController
@CrossOrigin(origins = {"http://localhost:9000", "https://codeteam6.io"})
@RequestMapping("/rides")
@PreAuthorize("hasRole('COORDINATOR')")
public class RideRequestController {

    /**
     * a singleton for the JwtTokenUtil
     */
    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    /**
     * a singleton for the RideRequestRepository
     */
    @Autowired
    private RideRequestRepository rideRequestRepository;

    /**
     * a singleton for the GeocodingService
     */
    @Autowired
    private GeocodingService geocodingService;

    /**
     * a singleton for the RideRequestMapper
     */
    @Autowired
    private RideRequestMapper rideRequestMapper;

    /**
     * HTTP header that stores the JWT, defined in application.yaml
     */
    @Value("${jwt.header}")
    private String tokenHeader;

    /**
     * GET /rides?status=
     * <p>
     * If status is null, returns all rides. Otherwise returns all rides filtered by the status parameter.
     *
     * @param status (optional query param) the status of the rides to return
     * @return a list of rides
     */
    @RequestMapping(method = RequestMethod.GET)
    @ApiOperation(value = "retrieveAll", nickname = "retrieveAll", notes = "Returns a list of rides...")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success", response = RideRequest.class, responseContainer = "List"),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 403, message = "Forbidden"),
            @ApiResponse(code = 500, message = "Failure")})
    public Iterable<RideRequest> retrieveAll(@RequestParam(value = "status", required = false) RideRequestStatus status) {
        if (status != null) {
            return rideRequestRepository.findByStatus(status);
        } else {
            return rideRequestRepository.findAll();
        }
    }

    /**
     * GET /rides/{id}
     * <p>
     * Returns the ride with the given id
     *
     * @param id the id of the ride to return
     * @return the ride with the given id or 404 if not found
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

    /**
     * POST /rides
     * Creates the given ride request in the database
     * <p>
     * Returns HTTP status code 400 under the following conditions
     * <ul>
     * <li>
     * oneCardId is null
     * </li>
     * </ul>
     *
     * @param request     the HTTP servlet request
     * @param rideRequest request body containing the ride to create
     * @return ResponseEntity containing the created ride and its location
     */
    @RequestMapping(method = RequestMethod.POST)
    @PreAuthorize("hasRole('RIDER')")
    @ApiOperation(value = "save", nickname = "save", notes = "Creates the given ride request")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success", response = ResponseEntity.class),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 403, message = "Forbidden"),
            @ApiResponse(code = 500, message = "Failure")})
    public ResponseEntity<?> save(HttpServletRequest request, @RequestBody RideRequest rideRequest) {
        String authToken = request.getHeader(this.tokenHeader);

        // if oneCardId is null or empty then grab the OneCardId from the authToken(if not null)
        if (StringUtils.isEmpty(rideRequest.getOneCardId()) && authToken != null) {
            String username = jwtTokenUtil.getUsernameFromToken(authToken);
            rideRequest.setOneCardId(username);
        }

        // oneCardId is required
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

    /**
     * PUT /rides/{id}
     * <p>
     * Updates the ride located at rides/{id} with the new data from the request body.
     *
     * @param id          path parameter for id of ride to update
     * @param rideRequest request body containing the ride to update
     * @return the updated ride
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
        geocodingService.setCoordinates(rideRequest);

        RideRequest result = rideRequestRepository.save(rideRequest);

        return ResponseEntity.ok(result);
    }

    /**
     * GET /rides/mine
     * <p>
     * Returns the current ride request for the authenticated rider or no content if it doesn't exist
     *
     * @param request the HTTP servlet request
     * @return the rider's current ride
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

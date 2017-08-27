package edu.csus.asi.saferides.service;

import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import edu.csus.asi.saferides.mapper.RideRequestMapper;
import edu.csus.asi.saferides.model.ResponseMessage;
import edu.csus.asi.saferides.model.RideRequest;
import edu.csus.asi.saferides.model.RideRequestStatus;
import edu.csus.asi.saferides.model.dto.RideRequestDto;
import edu.csus.asi.saferides.model.views.JsonViews;
import edu.csus.asi.saferides.repository.ConfigurationRepository;
import edu.csus.asi.saferides.repository.RideRequestRepository;
import edu.csus.asi.saferides.security.JwtTokenUtil;
import edu.csus.asi.saferides.security.model.AuthorityName;
import edu.csus.asi.saferides.serialization.LocalDateTimeSerializer;
import edu.csus.asi.saferides.utility.Util;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import java.net.URI;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

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
     * a singleton for the ConfigurationRepository
     */
    @Autowired
    private ConfigurationRepository configurationRepository;

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
    public Iterable<RideRequestDto> retrieveAll(@RequestParam(value = "status", required = false) RideRequestStatus status) {
        if (status != null) {
            List<RideRequest> rideRequests = (List<RideRequest>) Util.filterPastRides(configurationRepository.findOne(1), rideRequestRepository.findByStatus(status));
            List<RideRequestDto> dtos = mapRideRequestListToDtoList(rideRequests);

            return dtos;
        } else {
            List<RideRequest> rideRequests = (List<RideRequest>) Util.filterPastRides(configurationRepository.findOne(1), (Collection<RideRequest>) rideRequestRepository.findAll());
            List<RideRequestDto> dtos = mapRideRequestListToDtoList(rideRequests);

            return dtos;
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
            return ResponseEntity.ok(rideRequestMapper.map(result, RideRequestDto.class));
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

        String view = "coordinator";

        if (!jwtTokenUtil.getAuthoritiesFromToken(authToken).contains(AuthorityName.ROLE_DRIVER)) { // if requestor is a rider
            view = "rider";
            // enforce the Safe Rides time window range (only for riders). if not accepting new rides, return bad request
            if (!Util.isAcceptingRideRequests(configurationRepository.findOne(1))) {    // not accepting rides right now
                return ResponseEntity.badRequest().body(new ResponseMessage("SafeRides has stopped accepting new rides."));
            } else {
                // set the OneCard ID in the auth token to be the request's OneCard ID
                String username = jwtTokenUtil.getUsernameFromToken(authToken);
                rideRequest.setOneCardId(username);
            }
        } else if (!jwtTokenUtil.getAuthoritiesFromToken(authToken).contains(AuthorityName.ROLE_COORDINATOR)) { // if not a coordinator or admin or rider
            return ResponseEntity.badRequest().body(new ResponseMessage("Only riders or coordinators can submit a ride request!"));
        }

        // oneCardId is required
        if (!StringUtils.isNumeric(rideRequest.getOneCardId()) || StringUtils.length(rideRequest.getOneCardId()) != 9) {
            return ResponseEntity.badRequest().body(new ResponseMessage("OneCardID is null"));
        }

        if (!StringUtils.isNumeric(rideRequest.getRequestorPhoneNumber()) || StringUtils.length(rideRequest.getRequestorPhoneNumber()) != 10) {
            return ResponseEntity.badRequest().body(new ResponseMessage("Phone number is in an incorrect format"));
        }

        rideRequest.setRequestDate(LocalDateTime.now(ZoneId.of(Util.APPLICATION_TIME_ZONE)));    // default to current datetime
        rideRequest.setStatus(RideRequestStatus.UNASSIGNED);    // default to unassigned status

        geocodingService.setCoordinates(rideRequest);

        RideRequest result = rideRequestRepository.save(rideRequest);

        // create URI of where the rideRequest was created
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest().path("/{id}")
                .buildAndExpand(result.getId()).toUri();

        RideRequestDto dto = rideRequestMapper.map(result, RideRequestDto.class);

        ObjectMapper objectMapper = new ObjectMapper();
        SimpleModule module = new SimpleModule();
        module.addSerializer(LocalDateTime.class, new LocalDateTimeSerializer());
        objectMapper.registerModule(module);
        try {
            String response;
            if (view.equals("coordinator")) {
                response = objectMapper.writerWithView(JsonViews.Coordinator.class).writeValueAsString(dto);
            } else {
                response = objectMapper.writerWithView(JsonViews.Rider.class).writeValueAsString(dto);
            }
            return ResponseEntity.created(location).body(response);
        } catch (JsonProcessingException test2) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
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
        if ((rideRequest.getStatus().equals(RideRequestStatus.CANCELEDBYCOORDINATOR) || rideRequest.getStatus().equals(RideRequestStatus.CANCELEDBYRIDER) || rideRequest.getStatus().equals(RideRequestStatus.CANCELEDOTHER)) && StringUtils.length(rideRequest.getCancelMessage()) < 5) {
            return ResponseEntity.badRequest().body(new ResponseMessage("Canceled message cannot be empty or less than 5 characters."));
        }

        geocodingService.setCoordinates(rideRequest);

        RideRequest result = rideRequestRepository.save(rideRequest);

        return ResponseEntity.ok(rideRequestMapper.map(result, RideRequestDto.class));
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
    @JsonView(JsonViews.Rider.class)
    @ApiOperation(value = "retrieveMyRide", nickname = "retrieveMyRide", notes = "Returns authenticated user's current ride request...")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success", response = RideRequest.class, responseContainer = "List"),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 403, message = "Forbidden"),
            @ApiResponse(code = 500, message = "Failure")})
    public ResponseEntity<?> retrieveMyRide(HttpServletRequest request) {
        String authToken = request.getHeader(this.tokenHeader);
        String username = jwtTokenUtil.getUsernameFromToken(authToken);

        RideRequest rideRequest = rideRequestRepository.findTop1ByOneCardIdOrderByRequestDateDesc(username);

        // filter ride request
        rideRequest = Util.filterPastRide(configurationRepository.findOne(1), rideRequest);

        if (rideRequest == null) {
            return ResponseEntity.noContent().build();
        } else {
            // TODO this can return a ride request that is old. (but will be the latest)
            RideRequestDto dto = rideRequestMapper.map(rideRequest, RideRequestDto.class);

            ObjectMapper objectMapper = new ObjectMapper();
            SimpleModule module = new SimpleModule();
            module.addSerializer(LocalDateTime.class, new LocalDateTimeSerializer());
            objectMapper.registerModule(module);
            try {
                String response = objectMapper.writerWithView(JsonViews.Rider.class).writeValueAsString(dto);
                return ResponseEntity.ok(response);
            } catch (JsonProcessingException test2) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
            }
        }
    }

    /**
     * POST /rides/mine/cancel
     * <p>
     * Cancels the authenticated user's current ride request.
     * <p>
     * Returns HTTP status code 400 under the following conditions:
     * <ul>
     * <li>
     * No ride exists for the user
     * </li>
     * <li>
     * The ride has already been assigned to a driver
     * </li>
     * </ul>
     *
     * @param request HTTP servlet request
     * @return HTTP 200 if ride successfully canceled, error otherwise
     */
    @RequestMapping(method = RequestMethod.POST, value = "/mine/cancel")
    @PreAuthorize("hasRole('RIDER')")
    @ApiOperation(value = "cancelMyRide", nickname = "cancelMyRide", notes = "Cancels authenticated user's current ride request...")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success", response = RideRequest.class, responseContainer = "List"),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 403, message = "Forbidden"),
            @ApiResponse(code = 500, message = "Failure")})
    public ResponseEntity<?> cancelMyRide(HttpServletRequest request) {
        String authToken = request.getHeader(this.tokenHeader);
        String username = jwtTokenUtil.getUsernameFromToken(authToken);

        RideRequest rideRequest = rideRequestRepository.findTop1ByOneCardIdOrderByRequestDateDesc(username);

        // filter ride request
        rideRequest = Util.filterPastRide(configurationRepository.findOne(1), rideRequest);

        if (rideRequest == null) {
            return ResponseEntity.badRequest().body(new ResponseMessage("No ride found for user"));
        } else if (rideRequest.getStatus() != RideRequestStatus.UNASSIGNED) {
            return ResponseEntity.badRequest().body(new ResponseMessage("Ride cannot be canceled while assigned to a driver"));
        } else {
            rideRequest.setStatus(RideRequestStatus.CANCELEDBYRIDER);
            rideRequestRepository.save(rideRequest);
            return ResponseEntity.ok().build();
        }

    }

    private List<RideRequestDto> mapRideRequestListToDtoList(List<RideRequest> rideRequestList) {
        List<RideRequestDto> dtoList = new ArrayList<>();
        for (int i = 0; i < rideRequestList.size(); i++) {
            dtoList.add(rideRequestMapper.map(rideRequestList.get(i), RideRequestDto.class));
        }
        return dtoList;
    }
}

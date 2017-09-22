package edu.csus.asi.saferides.controller;

import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import edu.csus.asi.saferides.mapper.RideRequestMapper;
import edu.csus.asi.saferides.model.*;
import edu.csus.asi.saferides.model.dto.RideRequestDto;
import edu.csus.asi.saferides.model.views.JsonViews;
import edu.csus.asi.saferides.repository.ConfigurationRepository;
import edu.csus.asi.saferides.repository.DriverRepository;
import edu.csus.asi.saferides.repository.RideRequestRepository;
import edu.csus.asi.saferides.repository.UserRepository;
import edu.csus.asi.saferides.security.JwtTokenUtil;
import edu.csus.asi.saferides.serialization.LocalDateTimeSerializer;
import edu.csus.asi.saferides.service.GeocodingService;
import edu.csus.asi.saferides.utility.Util;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
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
     * a singleton for the DriverRepository
     */
    @Autowired
    private DriverRepository driverRepository;

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

    @Autowired
    private UserRepository userRepository;

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
    public Iterable<RideRequestDto> retrieveAll(@RequestParam(value = "status", required = false) RideRequestStatus status) {
        if (status != null) {
            List<RideRequest> rideRequests = (List<RideRequest>) Util.filterPastRides(configurationRepository.findOne(1), rideRequestRepository.findByStatus(status));
            List<RideRequestDto> dtos = rideRequestMapper.mapAsList(rideRequests, RideRequestDto.class);

            return dtos;
        } else {
            List<RideRequest> rideRequests = (List<RideRequest>) Util.filterPastRides(configurationRepository.findOne(1), (Collection<RideRequest>) rideRequestRepository.findAll());
            List<RideRequestDto> dtos = rideRequestMapper.mapAsList(rideRequests, RideRequestDto.class);

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
     * @param request        the HTTP servlet request
     * @param rideRequestDto request body containing the ride to create
     * @return ResponseEntity containing the created ride and its location
     */
    @RequestMapping(method = RequestMethod.POST)
    @PreAuthorize("hasRole('RIDER')")
    @ApiOperation(value = "save", nickname = "save", notes = "Creates the given ride request")
    public ResponseEntity<?> save(HttpServletRequest request, @RequestBody RideRequestDto rideRequestDto) {
        String authToken = request.getHeader(this.tokenHeader);

        RideRequest rideRequest = rideRequestMapper.map(rideRequestDto, RideRequest.class);

        ArrayList<AuthorityName> authorityNames = jwtTokenUtil.getAuthoritiesFromToken(authToken);
        AuthorityName userRole = null;
        if (authorityNames.contains(AuthorityName.ROLE_COORDINATOR)) {
            userRole = AuthorityName.ROLE_COORDINATOR;
        } else if (authorityNames.contains(AuthorityName.ROLE_RIDER)) {
            userRole = AuthorityName.ROLE_RIDER;
        }
        if (userRole == null) {
            return ResponseEntity.badRequest().body(new ResponseMessage("Could not determine user role when submitting a new ride request."));
        }

        User user = null;

        if (userRole == AuthorityName.ROLE_RIDER) { // if requestor is a rider or 'driver'
            // enforce the Safe Rides time window range (only for riders and 'drivers'). if not accepting new rides, return bad request
            if (!Util.isAcceptingRideRequests(configurationRepository.findOne(1))) {    // not accepting rides right now
                return ResponseEntity.badRequest().body(new ResponseMessage("Safe Rides is not currently accepting new ride requests."));
            } else {
                user = userRepository.findByUsernameIgnoreCase(jwtTokenUtil.getUsernameFromToken(authToken));
                if (user == null) {
                    return ResponseEntity.badRequest().body(new ResponseMessage("Could not find user requesting a ride."));
                }
            }
        }

        rideRequest.setRequestDate(LocalDateTime.now(ZoneId.of(Util.APPLICATION_TIME_ZONE)));    // default to current datetime
        rideRequest.setStatus(RideRequestStatus.UNASSIGNED);    // default to unassigned status

        geocodingService.setCoordinates(rideRequest);

        if (user != null) {
            if ((StringUtils.isEmpty(user.getFirstName()) && !StringUtils.isEmpty(rideRequestDto.getRequestorFirstName()))) {
                user.setFirstName(rideRequestDto.getRequestorFirstName());
            }
            if ((StringUtils.isEmpty(user.getLastName()) && !StringUtils.isEmpty(rideRequestDto.getRequestorLastName()))) {
                user.setLastName(rideRequestDto.getRequestorLastName());
            }
            rideRequest.setUser(user);
        }

        // check if user already requested a ride during this period (that was not canceled)
        Configuration configuration = configurationRepository.findOne(1);
        RideRequest previousRideRequest = Util.filterPastRide(configuration, rideRequestRepository.findTop1ByOneCardIdOrderByRequestDateDesc(rideRequest.getOneCardId()));
        if (previousRideRequest != null) {
            if (previousRideRequest.getStatus() != RideRequestStatus.CANCELEDBYCOORDINATOR &&
                    previousRideRequest.getStatus() != RideRequestStatus.CANCELEDBYRIDER &&
                    previousRideRequest.getStatus() != RideRequestStatus.CANCELEDOTHER) {
                return ResponseEntity.badRequest().body(new ResponseMessage("A ride has already been requested today."));
            }
        }

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
            if (userRole == AuthorityName.ROLE_COORDINATOR) {
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
     * @param id             path parameter for id of ride to update
     * @param rideRequestDto request body containing the ride to update
     * @return the updated ride
     */
    @RequestMapping(method = RequestMethod.PUT, value = "/{id}")
    @PreAuthorize("hasRole('RIDER')")
    @ApiOperation(value = "save", nickname = "save", notes = "Updates the given ride request")
    public ResponseEntity<?> save(HttpServletRequest request, @PathVariable Long id, @RequestBody RideRequestDto rideRequestDto) {
        String authToken = request.getHeader(this.tokenHeader);

        User user = userRepository.findByUsernameIgnoreCase(jwtTokenUtil.getUsernameFromToken(authToken));
        if (user == null) {
            return ResponseEntity.badRequest().body(new ResponseMessage("Could not find user updating a ride."));
        }

        ArrayList<AuthorityName> authorityNames = jwtTokenUtil.getAuthoritiesFromToken(authToken);
        AuthorityName userRole = null;
        if (authorityNames.contains(AuthorityName.ROLE_COORDINATOR)) {
            userRole = AuthorityName.ROLE_COORDINATOR;
        } else if (authorityNames.contains(AuthorityName.ROLE_DRIVER)) {
            userRole = AuthorityName.ROLE_DRIVER;
        } else if (authorityNames.contains(AuthorityName.ROLE_RIDER)) {
            userRole = AuthorityName.ROLE_RIDER;
        }
        if (userRole == null) {
            return ResponseEntity.badRequest().body(new ResponseMessage("Could not determine user role when updating a ride request."));
        }

        try {
            return UpdateRideRequest(userRole, rideRequestDto, id, user);
        } catch (ObjectOptimisticLockingFailureException ex) {
            return ResponseEntity.badRequest().body(new ResponseMessage("Could not update the ride request as either the driver or the ride request was already updated. Please refresh and try again."));
        }
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
    public ResponseEntity<?> retrieveMyRide(HttpServletRequest request) {
        String authToken = request.getHeader(this.tokenHeader);
        String username = jwtTokenUtil.getUsernameFromToken(authToken);

        User user = userRepository.findByUsernameIgnoreCase(username);

        RideRequest rideRequest = rideRequestRepository.findTop1ByUserOrderByRequestDateDesc(user);

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
     * Updates the ride request database record
     *
     * @param userRole       the role of the user that is attempting to update the ride request
     * @param rideRequestDto the ride request DTO
     * @param id             the id of the ride request to update
     * @param user           the user that is attempting to update the ride request
     * @return the updated ride request or error
     */
    @Transactional(isolation = Isolation.REPEATABLE_READ)
    private ResponseEntity<?> UpdateRideRequest(AuthorityName userRole, RideRequestDto rideRequestDto, long id, User user) {
        RideRequest rideRequest = rideRequestMapper.map(rideRequestDto, RideRequest.class);
        RideRequest rideRequestFromDb = rideRequestRepository.findOne(id);

        if (rideRequestFromDb == null) {
            return ResponseEntity.badRequest().body(new ResponseMessage("Could not find ride request."));
        }
        RideRequest result = null;

        // Possible rider actions (driver user requesting a ride as well):
        // - cancel ride request (only if not already assigned)
        // Possible driver actions:
        // - update status (only if ride request is assigned to the user)
        // - update start odometer
        // - update end odometer
        if ((userRole == AuthorityName.ROLE_RIDER || userRole == AuthorityName.ROLE_DRIVER)
                && rideRequestFromDb.getStatus() == RideRequestStatus.UNASSIGNED
                && rideRequestFromDb.getUser().getId().equals(user.getId())
                && rideRequest.getStatus() == RideRequestStatus.CANCELEDBYRIDER) {
            rideRequestFromDb.setStatus(RideRequestStatus.CANCELEDBYRIDER);
            result = rideRequestRepository.save(rideRequestFromDb);
        }
        if (userRole == AuthorityName.ROLE_DRIVER
                && rideRequestFromDb.getStatus() != RideRequestStatus.UNASSIGNED
                && rideRequestFromDb.getDriver().getUser().getId().equals(user.getId())) {
            if (rideRequest.getStatus() == RideRequestStatus.ATPICKUPLOCATION
                    || rideRequest.getStatus() == RideRequestStatus.PICKINGUP
                    || rideRequest.getStatus() == RideRequestStatus.DROPPINGOFF
                    || rideRequest.getStatus() == RideRequestStatus.COMPLETE) {
                rideRequestFromDb.setStatus(rideRequest.getStatus());
            }
            if (rideRequestFromDb.getStatus() == RideRequestStatus.ASSIGNED
                    && rideRequestFromDb.getStartOdometer() == 0
                    && rideRequest.getStartOdometer() != 0) {
                rideRequestFromDb.setStartOdometer(rideRequest.getStartOdometer());
            }
            if (rideRequestFromDb.getStatus() == RideRequestStatus.COMPLETE
                    && rideRequestFromDb.getEndOdometer() == 0
                    && rideRequest.getEndOdometer() != 0) {
                rideRequestFromDb.setEndOdometer(rideRequest.getEndOdometer());
            }
            result = rideRequestRepository.save(rideRequestFromDb);
        }
        if (userRole == AuthorityName.ROLE_COORDINATOR) {
            // address changed
            if ((!StringUtils.isEmpty(rideRequest.getDropoffCity()) && !rideRequest.getDropoffCity().equals(rideRequestFromDb.getDropoffCity()))
                    || (!StringUtils.isEmpty(rideRequest.getDropoffLine1()) && !rideRequest.getDropoffLine1().equals(rideRequestFromDb.getDropoffLine1()))
                    || (!StringUtils.isEmpty(rideRequest.getDropoffLine2()) && !rideRequest.getDropoffLine2().equals(rideRequestFromDb.getDropoffLine2()))
                    || (!StringUtils.isEmpty(rideRequest.getPickupCity()) && !rideRequest.getPickupCity().equals(rideRequestFromDb.getPickupCity()))
                    || (!StringUtils.isEmpty(rideRequest.getPickupLine1()) && !rideRequest.getPickupLine1().equals(rideRequestFromDb.getPickupLine1()))
                    || (!StringUtils.isEmpty(rideRequest.getPickupLine2()) && !rideRequest.getPickupLine2().equals(rideRequestFromDb.getPickupLine2()))
                    ) {
                geocodingService.setCoordinates(rideRequest);
                rideRequestFromDb.setPickupCity(rideRequest.getPickupCity());
                rideRequestFromDb.setPickupLatitude(rideRequest.getPickupLatitude());
                rideRequestFromDb.setPickupLine1(rideRequest.getPickupLine1());
                rideRequestFromDb.setPickupLine2(rideRequest.getPickupLine2());
                rideRequestFromDb.setPickupLongitude(rideRequest.getPickupLongitude());
                rideRequestFromDb.setDropoffCity(rideRequest.getDropoffCity());
                rideRequestFromDb.setDropoffLatitude(rideRequest.getDropoffLatitude());
                rideRequestFromDb.setDropoffLine1(rideRequest.getDropoffLine1());
                rideRequestFromDb.setDropoffLine2(rideRequest.getDropoffLine2());
                rideRequestFromDb.setDropoffLongitude(rideRequest.getDropoffLongitude());
            }

            // unassign driver / cancel request
            if ((rideRequest.getStatus() == RideRequestStatus.CANCELEDBYCOORDINATOR
                    || rideRequest.getStatus() == RideRequestStatus.CANCELEDOTHER
                    || rideRequest.getStatus() == RideRequestStatus.CANCELEDBYRIDER)
                    && (rideRequestFromDb.getStatus() != RideRequestStatus.COMPLETE
                    || rideRequestFromDb.getStatus() != RideRequestStatus.CANCELEDBYCOORDINATOR
                    || rideRequestFromDb.getStatus() != RideRequestStatus.CANCELEDOTHER
                    || rideRequestFromDb.getStatus() != RideRequestStatus.CANCELEDBYRIDER)
                    ) {
                if (StringUtils.length(rideRequest.getCancelMessage()) < 5) {
                    return ResponseEntity.badRequest().body(new ResponseMessage("Canceled message cannot be empty or less than 5 characters."));
                }
                rideRequestFromDb.setStatus(rideRequest.getStatus());
                rideRequestFromDb.setCancelMessage(rideRequest.getCancelMessage());
                rideRequest.setDriver(null);
            }

            // assign driver
            if (rideRequest.getStatus() == RideRequestStatus.ASSIGNED && rideRequestFromDb.getStatus() == RideRequestStatus.UNASSIGNED) {
                Driver driver = driverRepository.findOne(rideRequest.getDriver().getId());
                if (driver != null) {
                    if (isDriverAvailable(driver)) {
                        driver.setVersion(driver.getVersion() + 1); // manually bump up version of driver?
                        driverRepository.save(driver);
                        rideRequestFromDb.setStatus(RideRequestStatus.ASSIGNED);
                        rideRequestFromDb.setAssignedDate(LocalDateTime.now(ZoneId.of(Util.APPLICATION_TIME_ZONE)));
                        rideRequestFromDb.setMessageToDriver(rideRequest.getMessageToDriver());
                        rideRequestFromDb.setEstimatedTime(rideRequest.getEstimatedTime());
                        rideRequestFromDb.setDriver(driver);
                    } else {
                        return ResponseEntity.badRequest().body(new ResponseMessage("The driver is busy and cannot be assigned."));
                    }
                }
            }

            rideRequestFromDb.setNumPassengers(rideRequest.getNumPassengers());
            rideRequestFromDb.setRequestorPhoneNumber(rideRequest.getRequestorPhoneNumber());
            result = rideRequestRepository.save(rideRequestFromDb);
        }

        return ResponseEntity.ok(rideRequestMapper.map(result, RideRequestDto.class));
    }

    private boolean isDriverAvailable(Driver driver) {
        RideRequest latestRideRequest = driver.getLatestRideRequest();
        if (latestRideRequest != null) {
            RideRequestStatus status = latestRideRequest.getStatus();
            if (status == RideRequestStatus.ASSIGNED || status == RideRequestStatus.PICKINGUP
                    || status == RideRequestStatus.ATPICKUPLOCATION || status == RideRequestStatus.DROPPINGOFF) {
                return false;
            }
        }
        return true;
    }
}

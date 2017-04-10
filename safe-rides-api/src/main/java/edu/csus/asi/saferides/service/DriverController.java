package edu.csus.asi.saferides.service;

import edu.csus.asi.saferides.model.*;
import edu.csus.asi.saferides.repository.DriverLocationRepository;
import edu.csus.asi.saferides.repository.DriverRepository;
import edu.csus.asi.saferides.repository.RideRequestRepository;
import edu.csus.asi.saferides.security.JwtTokenUtil;
import edu.csus.asi.saferides.security.model.User;
import edu.csus.asi.saferides.security.repository.UserRepository;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import java.net.URI;
import java.util.Set;

/*
 * @author Zeeshan Khaliq
 * 
 * Rest API controller for the Driver resource 
 * */
@RestController
@CrossOrigin(origins = {"http://localhost:9000", "https://codeteam6.io"})
@RequestMapping("/drivers")
@PreAuthorize("hasRole('COORDINATOR')")
public class DriverController {

    // this creates a singleton for DriverRepository
    @Autowired
    private DriverRepository driverRepository;

    // this creates a singleton for RideRequestRepository
    @Autowired
    private RideRequestRepository rideRequestRepository;

    @Autowired
    private DriverLocationRepository driverLocationRepository;

    @Value("${jwt.header}")
    private String tokenHeader;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private UserRepository userRepository;

    /*
     * GET /drivers
     */
    @RequestMapping(method = RequestMethod.GET)
    @ApiOperation(value = "retrieveAll", nickname = "retrieveAll", notes = "Returns a list of drivers...")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success", response = Driver.class, responseContainer = "List"),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 403, message = "Forbidden"),
            @ApiResponse(code = 500, message = "Failure")})
    public Iterable<Driver> retrieveAll(@RequestParam(value = "active", required = false) Boolean active) {
        if (active != null) {
            if (active) {
                return driverRepository.findByActiveTrueOrderByModifiedDateDesc();
            } else {
                return driverRepository.findByActiveFalseOrderByModifiedDateDesc();
            }
        } else {
            return driverRepository.findAllByOrderByModifiedDateDesc();
        }
    }

    /*
     * GET /drivers/{id}
     */
    @RequestMapping(method = RequestMethod.GET, value = "/{id}")
    @ApiOperation(value = "retrieve", nickname = "retrieve", notes = "Returns a driver with the given id")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success", response = Driver.class),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 403, message = "Forbidden"),
            @ApiResponse(code = 404, message = "Not Found"),
            @ApiResponse(code = 500, message = "Failure")})
    public ResponseEntity<?> retrieve(@PathVariable Long id) {
        Driver result = driverRepository.findOne(id);

        if (result == null) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.ok(result);
        }
    }

    /*
     * POST /drivers
     */
    @RequestMapping(method = RequestMethod.POST)
    @ApiOperation(value = "save", nickname = "save", notes = "Creates the given driver")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success", response = ResponseEntity.class),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 403, message = "Forbidden"),
            @ApiResponse(code = 500, message = "Failure")})
    public ResponseEntity<?> save(@RequestBody Driver driver) {
        Driver result = driverRepository.save(driver);

        // create URI of where the driver was created
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(result.getId())
                .toUri();

        return ResponseEntity.created(location).body(result);
    }

    /*
     * PUT /drivers/{id}
     */
    @RequestMapping(method = RequestMethod.PUT, value = "/{id}")
    @ApiOperation(value = "save", nickname = "save", notes = "Updates a driver")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success", response = ResponseEntity.class),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 403, message = "Forbidden"),
            @ApiResponse(code = 500, message = "Failure")})
    public ResponseEntity<?> save(@PathVariable Long id, @RequestBody Driver driver) {
        if (driver.getId() != null && driver.getId() == id) {
            Driver result = driverRepository.findOne(id);

            if (!driver.getActive() && result.getStatus() != DriverStatus.AVAILABLE) {
                return ResponseEntity.badRequest().body("The driver must not have any in progress rides");
            }

            result = driverRepository.save(driver);
            return ResponseEntity.ok(result);
        } else {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
    }

    /*
     * DELETE /drivers/{id}
     */
    @RequestMapping(method = RequestMethod.DELETE, value = "/{id}")
    @ApiOperation(value = "delete", nickname = "delete", notes = "Deletes a driver")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success", response = ResponseEntity.class),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 403, message = "Forbidden"),
            @ApiResponse(code = 500, message = "Failure")})
    public ResponseEntity<?> delete(@PathVariable Long id) {
        if (driverRepository.findOne(id) == null) {
            return ResponseEntity.badRequest().body(new ResponseMessage("Driver not specified"));
        } else {
            driverRepository.delete(id);
            return ResponseEntity.noContent().build();
        }
    }

    /*
     * GET /drivers/{id}/rides
     */
    @RequestMapping(method = RequestMethod.GET, value = "/{id}/rides")
    @PreAuthorize("hasRole('DRIVER')")
    @ApiOperation(value = "retrieveRide", nickname = "retrieveRide", notes = "Retrieves rides assigned to a driver with the given id")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success", response = RideRequest.class, responseContainer = "List"),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 403, message = "Forbidden"),
            @ApiResponse(code = 500, message = "Failure")})
    public ResponseEntity<?> retrieveRide(@PathVariable Long id,
                                          @RequestParam(value = "status", required = false) RideRequestStatus status) {
        Driver result = driverRepository.findOne(id);

        if (result == null) {
            return ResponseEntity.noContent().build();
        } else if (status != null) {
            Set<RideRequest> requests = result.getRides();
            requests.removeIf((RideRequest req) -> req.getStatus() != status);

            return ResponseEntity.ok(requests);

        } else {
            return ResponseEntity.ok(result.getRides());
        }
    }

    /*
     * GET /drivers/{id}/rides
     */
    @RequestMapping(method = RequestMethod.GET, value = "/rides")
    @PreAuthorize("hasRole('DRIVER')")
    @ApiOperation(value = "retrieveRide", nickname = "retrieveRide", notes = "Retrieves rides assigned to the authenticated driver")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success", response = RideRequest.class, responseContainer = "List"),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 403, message = "Forbidden"),
            @ApiResponse(code = 500, message = "Failure")})
    public ResponseEntity<?> retrieveRides(HttpServletRequest request,
                                           @RequestParam(value = "status", required = false) RideRequestStatus status) {
        String authToken = request.getHeader(this.tokenHeader);
        String username = jwtTokenUtil.getUsernameFromToken(authToken);

        User user = userRepository.findByUsername(username);

        Driver driver = driverRepository.findByUser(user);

        if (driver == null) {
            return ResponseEntity.noContent().build();
        } else if (status != null) {
            Set<RideRequest> requests = driver.getRides();
            requests.removeIf((RideRequest req) -> req.getStatus() != status);

            return ResponseEntity.ok(requests);

        } else {
            return ResponseEntity.ok(driver.getRides());
        }
    }

    /*
     * POST /drivers/{id}/rides
     */
    @RequestMapping(method = RequestMethod.POST, value = "/{id}/rides")
    @ApiOperation(value = "assignRideRequest", nickname = "assignRideRequest", notes = "Assigns a ride request to the driver")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success", response = ResponseEntity.class),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 403, message = "Forbidden"),
            @ApiResponse(code = 500, message = "Failure")})
    public ResponseEntity<?> assignRideRequest(@PathVariable Long id, @RequestBody RideRequest rideRequest) {
        Driver driver = driverRepository.findOne(id);
        RideRequest rideReq = rideRequestRepository.findOne(rideRequest.getId());

        rideReq.setStatus(RideRequestStatus.ASSIGNED);
        rideReq.setDriver(driver);
        //set messageToDriver from rideRequest
        rideReq.setMessageToDriver(rideRequest.getMessageToDriver());
        rideReq.setEstimatedTime(rideRequest.getEstimatedTime());
        driver.getRides().add(rideReq);

        driverRepository.save(driver);
        // create URI of where the driver was updated
        URI location = ServletUriComponentsBuilder.fromCurrentContextPath().path("/drivers/{id}")
                .buildAndExpand(driver.getId()).toUri();

        return ResponseEntity.ok(location);
    }

    /*
    * POST /drivers/location
    */
    @RequestMapping(method = RequestMethod.POST, value = "/location")
    @PreAuthorize("hasRole('DRIVER')")
    @ApiOperation(value = "setDriverLocation", nickname = "setDriverLocation", notes = "Authenticated driver updates their latest/current location.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success", response = ResponseEntity.class),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 403, message = "Forbidden"),
            @ApiResponse(code = 500, message = "Failure")})
    public ResponseEntity<?> setDriverLocation(HttpServletRequest request, @RequestBody DriverLocation driverLocation) {
        String authToken = request.getHeader(this.tokenHeader);
        String username = jwtTokenUtil.getUsernameFromToken(authToken);

        User user = userRepository.findByUsername(username);

        Driver driver = driverRepository.findByUser(user);

        if (driver == null) {
            return ResponseEntity.badRequest().body(new ResponseMessage("You cannot update your location as you are not a driver."));
        } else {
            driverLocation.setDriver(driver);
            driverLocationRepository.save(driverLocation);

            return ResponseEntity.ok(driverLocation);
        }
    }

    /*
     * GET /drivers/{id}/location
     */
    @RequestMapping(method = RequestMethod.GET, value = "/{id}/location")
    @ApiOperation(value = "getDriverLocation", nickname = "getDriverLocation", notes = "Retrieves the specified driver's latest/current location.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success", response = ResponseEntity.class),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 403, message = "Forbidden"),
            @ApiResponse(code = 500, message = "Failure")})
    public ResponseEntity<?> getDriverLocation(@PathVariable Long id) {
        Driver driver = driverRepository.findOne(id);
        DriverLocation loc = driverLocationRepository.findTop1ByDriverOrderByCreatedDateDesc(driver);
        if (loc == null) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(loc);
    }
}

package edu.csus.asi.saferides.service;

import edu.csus.asi.saferides.mapper.DriverMapper;
import edu.csus.asi.saferides.model.*;
import edu.csus.asi.saferides.model.dto.DriverDto;
import edu.csus.asi.saferides.repository.ConfigurationRepository;
import edu.csus.asi.saferides.repository.DriverLocationRepository;
import edu.csus.asi.saferides.repository.DriverRepository;
import edu.csus.asi.saferides.repository.RideRequestRepository;
import edu.csus.asi.saferides.security.JwtTokenUtil;
import edu.csus.asi.saferides.security.model.User;
import edu.csus.asi.saferides.security.repository.UserRepository;
import edu.csus.asi.saferides.security.service.UserService;
import edu.csus.asi.saferides.utility.Util;
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
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Rest API controller for the Driver resource
 */
@RestController
@CrossOrigin(origins = {"http://localhost:9000", "https://codeteam6.io"})
@RequestMapping("/drivers")
@PreAuthorize("hasRole('COORDINATOR')")
public class DriverController {

    /**
     * a singleton for the DriverRepository
     */
    @Autowired
    private DriverRepository driverRepository;

    /**
     * a singleton for the RideRequestRepository
     */
    @Autowired
    private RideRequestRepository rideRequestRepository;

    /**
     * a singleton for the DriverLocationRepository
     */
    @Autowired
    private DriverLocationRepository driverLocationRepository;

    /**
     * a singleton for the DriverUserMapper
     */
    @Autowired
    private DriverMapper driverMapper;

    /**
     * a singleton for the ConfigurationRepository
     */
    @Autowired
    private ConfigurationRepository configurationRepository;

    /**
     * HTTP header that stores the JWT, defined in application.yaml
     */
    @Value("${jwt.header}")
    private String tokenHeader;

    /**
     * a singleton for the JwtTokenUtil
     */
    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    /**
     * a singleton for the UserRepository
     */
    @Autowired
    private UserRepository userRepository;

    /**
     * a singleton for the UserService
     */
    @Autowired
    private UserService userService;

    /**
     * GET /drivers?active=
     * <p>
     * If active is null, returns all drivers. Otherwise, returns all drivers filtered by the active parameter.
     *
     * @param active (optional query param) if the driver is active or not
     * @return a list of drivers
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

    /**
     * GET /drivers/{id}
     * <p>
     * Returns the driver with the given id
     *
     * @param id the id of the driver to return
     * @return the driver with the given id or 404 if not found
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
            return ResponseEntity.notFound().build();
        } else {
            return ResponseEntity.ok(result);
        }
    }

    /**
     * POST /drivers
     * <p>
     * Creates the given driver in database
     *
     * @param driverDto request body containing the driver to create and password to create a user
     * @return ResponseEntity containing the created driver and it's location
     */
    @RequestMapping(method = RequestMethod.POST)
    @ApiOperation(value = "save", nickname = "save", notes = "Creates the given driver")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success", response = ResponseEntity.class),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 403, message = "Forbidden"),
            @ApiResponse(code = 500, message = "Failure")})
    public ResponseEntity<?> save(@RequestBody DriverDto driverDto) {
        Driver driver = driverMapper.map(driverDto, Driver.class);

        List<String> errorMessages = validateDriver(driver);

        if (StringUtils.isEmpty(driverDto.getPassword())) {
            errorMessages.add("Password cannot be empty");
        } else if (!Util.isPasswordValid(driverDto.getPassword())) {
            errorMessages.add("Password does not meet security requirements");
        }

        if (errorMessages.size() > 0) {
            return ResponseEntity.badRequest().body(new ResponseMessage(String.join("; ", errorMessages)));
        }

        User user = userService.createDriverUser(driverDto);

        driver.setUser(user);
        Driver result = driverRepository.save(driver);

        // create URI of where the driver was created
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(result.getId()).toUri();

        return ResponseEntity.created(location).body(driverMapper.map(result, DriverDto.class));

    }

    /**
     * PUT /drivers/{id}
     * <p>
     * Updates the driver located at drivers/{id} with the new data from the request body.
     * <p>
     * Returns HTTP status code 400 under the following conditions:
     * <ul>
     * <li>id in path is different from id in request body</li>
     * <li>driver is being deactivated while status is not AVAILABLE</li>
     * </ul>
     *
     * @param id        path parameter for id of driver to update
     * @param driverDto request body containing the driver and password to update
     * @return the updated driver or error message
     */
    @RequestMapping(method = RequestMethod.PUT, value = "/{id}")
    @ApiOperation(value = "save", nickname = "save", notes = "Updates a driver")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success", response = ResponseEntity.class),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 403, message = "Forbidden"),
            @ApiResponse(code = 500, message = "Failure")})
    public ResponseEntity<?> save(@PathVariable Long id, @RequestBody DriverDto driverDto) {
        Driver updatedDriver = driverMapper.map(driverDto, Driver.class);

        if (!updatedDriver.getId().equals(id)) {
            return ResponseEntity.badRequest().body(new ResponseMessage("The id in the path does not match the id in the body"));
        }

        List<String> errorMessages = validateDriver(updatedDriver);

        if (errorMessages.size() > 0) {
            return ResponseEntity.badRequest().body(new ResponseMessage(String.join("; ", errorMessages)));
        }

        Driver existingDriver = driverRepository.findOne(id);

        // return 400 if OneCard ID is modified
        if (!existingDriver.getOneCardId().equals(updatedDriver.getOneCardId())) {
            return ResponseEntity.badRequest().body(new ResponseMessage("OneCard ID is not allowed to be modified"));
        }

        // return 400 if password does not meet security requirements
        if (!StringUtils.isEmpty(driverDto.getPassword()) && !Util.isPasswordValid(driverDto.getPassword())) {
            return ResponseEntity.badRequest().body(new ResponseMessage("New password does not meet security requirements"));
        }

        // return 400 if trying to deactivate a driver that's not AVAILABLE
        if (!updatedDriver.getActive() && existingDriver.getStatus() != DriverStatus.AVAILABLE) {
            return ResponseEntity.badRequest().body(new ResponseMessage("The driver must not have any in progress rides"));
        }

        if (!StringUtils.isEmpty(driverDto.getPassword())) {
            existingDriver.getUser().setLastPasswordResetDate(LocalDateTime.now(ZoneId.of(Util.APPLICATION_TIME_ZONE))); // invalidate existing tokens
            userService.updateDriverUser(driverDto);
        }

        // deactivate user if driver deactivated
        if (!updatedDriver.getActive()) {
            existingDriver.getUser().setActive(false);
        } else {
            existingDriver.getUser().setActive(true);
        }

        updatedDriver.setUser(existingDriver.getUser());
        driverRepository.save(updatedDriver);

        return ResponseEntity.ok(driverMapper.map(updatedDriver, DriverDto.class));
    }


    /*
    * PUT /drivers/endOfNight
    */
    @RequestMapping(method = RequestMethod.PUT, value = "/endofnight")
    @PreAuthorize("hasRole('DRIVER')")
    @ApiOperation(value = "saveEndNightOdo", nickname = "saveEndNightOdo", notes = "Updates a driver's end of night odometer")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success", response = ResponseEntity.class),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 403, message = "Forbidden"),
            @ApiResponse(code = 500, message = "Failure")})
    public ResponseEntity<?> endOfNight(HttpServletRequest request, @RequestBody Long endNightOdo) {
        String authToken = request.getHeader(this.tokenHeader);
        String username = jwtTokenUtil.getUsernameFromToken(authToken);

        User user = userRepository.findByUsernameIgnoreCase(username);

        Driver tempDriver = driverRepository.findByUser(user);

        if (tempDriver != null) {
            tempDriver.setEndOfNightOdo(endNightOdo);
            driverRepository.save(tempDriver);
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.badRequest().body(new ResponseMessage("Driver not found."));
        }
    }

    /**
     * DELETE /drivers/{id}
     * <p>
     * Deletes the driver with the given id
     *
     * @param id path parameter for the driver to delete
     * @return 204 if deleted successfully, 400 if driver with id does not exist
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
            return ResponseEntity.badRequest().body(new ResponseMessage("Driver does not exist"));
        } else {
            driverRepository.delete(id);
            return ResponseEntity.noContent().build();
        }
    }

    /**
     * GET /drivers/{id}/rides
     *
     * @param id     - id of driver to get rides for
     * @param status status to filter rides by
     * @return list of rides for driver with specified id and filtered with specified status
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
        Driver driver = driverRepository.findOne(id);

        return filterDriverRidesByStatus(driver, status);
    }

    /**
     * GET /drivers/rides
     *
     * @param request HTTP servlet request
     * @param status  status to filter rides by
     * @return list of rides for the authenticated driver, filtered by the specified status
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

        User user = userRepository.findByUsernameIgnoreCase(username);

        Driver driver = driverRepository.findByUser(user);

        return filterDriverRidesByStatus(driver, status);
    }

    /**
     * GET /drivers/currentride
     *
     * @param request HTTP servlet request
     * @return the current ride for the authenticated driver
     */
    @RequestMapping(method = RequestMethod.GET, value = "/currentride")
    @PreAuthorize("hasRole('DRIVER')")
    @ApiOperation(value = "currentRide", nickname = "currentRide", notes = "Retrieves currently assigned ride to the authenticated driver")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success", response = RideRequest.class, responseContainer = "List"),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 403, message = "Forbidden"),
            @ApiResponse(code = 500, message = "Failure")})
    public ResponseEntity<?> getDriverCurrentRide(HttpServletRequest request) {
        String authToken = request.getHeader(this.tokenHeader);
        String username = jwtTokenUtil.getUsernameFromToken(authToken);

        User user = userRepository.findByUsernameIgnoreCase(username);

        Driver driver = driverRepository.findByUser(user);
        //if the driver has no status or is Available, then there is no current ride, return empty
        if (driver == null || driver.getStatus() == null || driver.getStatus() == DriverStatus.AVAILABLE) {
            return ResponseEntity.noContent().build();
        } else {
            Collection<RideRequest> requests = driver.getRides();

            // filter requests
            requests = Util.filterPastRides(configurationRepository.findOne(1), requests);

            if (requests != null) {
                for (RideRequest req : requests) {
                    if (req.getStatus() != null && req.getStatus() == RideRequestStatus.ASSIGNED || req.getStatus() == RideRequestStatus.PICKINGUP
                            || req.getStatus() == RideRequestStatus.ATPICKUPLOCATION || req.getStatus() == RideRequestStatus.DROPPINGOFF) {
                        return ResponseEntity.ok(req);
                    }
                }
            }
        }
        return ResponseEntity.noContent().build();
    }

    /**
     * GET /drivers/me
     *
     * @param request HTTP servlet request
     * @return the authenticated driver's Driver object
     */
    @RequestMapping(method = RequestMethod.GET, value = "/me")
    @PreAuthorize("hasRole('DRIVER')")
    @ApiOperation(value = "getDriver", nickname = "getDriver", notes = "Retrieves current driver to the authenticated driver")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success", response = RideRequest.class, responseContainer = "List"),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 403, message = "Forbidden"),
            @ApiResponse(code = 500, message = "Failure")})
    public ResponseEntity<?> getMe(HttpServletRequest request) {
        String authToken = request.getHeader(this.tokenHeader);
        String username = jwtTokenUtil.getUsernameFromToken(authToken);

        User user = userRepository.findByUsernameIgnoreCase(username);

        Driver driver = driverRepository.findByUser(user);

        if (driver == null) {
            return ResponseEntity.notFound().build();
        } else {
            return ResponseEntity.ok(driver);
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
        if (driver == null) {
            return ResponseEntity.badRequest().body(new ResponseMessage("Driver could not be found"));
        }
        if (driver.getStatus() != DriverStatus.AVAILABLE) {
            return ResponseEntity.badRequest().body(new ResponseMessage("The driver is not available to be assigned"));
        }
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

    /**
     * POST /drivers/location
     *
     * @param request        client request (contains the Authorization header)
     * @param driverLocation the new driver location
     * @return the updated driver location or a message
     */
    @RequestMapping(method = RequestMethod.POST, value = "/location")
    @PreAuthorize("hasRole('DRIVER')")
    @ApiOperation(value = "setDriverLocation", nickname = "setDriverLocation", notes = "Authenticated driver updates their latest/current location.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success", response = ResponseEntity.class),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 403, message = "Forbidden"),
            @ApiResponse(code = 500, message = "Failure")})
    public ResponseEntity<?> setDriverLocation(HttpServletRequest request, @RequestBody DriverLocation
            driverLocation) {
        String authToken = request.getHeader(this.tokenHeader);
        String username = jwtTokenUtil.getUsernameFromToken(authToken);

        User user = userRepository.findByUsernameIgnoreCase(username);

        Driver driver = driverRepository.findByUser(user);

        if (driver == null) {
            return ResponseEntity.badRequest().body(new ResponseMessage("You cannot update your location as you are not a driver."));
        } else {
            driverLocation.setDriver(driver);
            driverLocationRepository.save(driverLocation);

            return ResponseEntity.ok(driverLocation);
        }
    }

    /**
     * GET /drivers/{id}/location
     *
     * @param id path param for the id of the driver
     * @return the latest driver location object of the specified driver
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

    /**
     * Helper function to filter a drivers rides by status
     *
     * @param driver the driver that has the rides to be filtered
     * @param status the status of the riders to select
     * @return a ResponseEntity with the list of rides
     */
    private ResponseEntity<?> filterDriverRidesByStatus(Driver driver, RideRequestStatus status) {
        if (driver == null) {
            return ResponseEntity.noContent().build();
        } else if (status != null) {
            Collection<RideRequest> requests = driver.getRides();

            // filter requests
            requests = Util.filterPastRides(configurationRepository.findOne(1), requests);
            if (requests == null) {
                return ResponseEntity.noContent().build();
            } else {
                requests.removeIf((RideRequest req) -> req.getStatus() != status);
            }

            return ResponseEntity.ok(requests);
        } else {
            Collection<RideRequest> requests = driver.getRides();

            // filter requests
            requests = Util.filterPastRides(configurationRepository.findOne(1), requests);
            if (requests == null) {
                return ResponseEntity.noContent().build();
            } else {
                return ResponseEntity.ok(requests);
            }
        }
    }

    private List<String> validateDriver(Driver driver) {
        ArrayList<String> errorMessages = new ArrayList<>();

        if (!StringUtils.isNumeric(driver.getOneCardId()) || StringUtils.length(driver.getOneCardId()) != 9) {
            errorMessages.add("Invalid OneCardID");
        }
        if (StringUtils.isEmpty(driver.getDriverFirstName()) || StringUtils.isEmpty(driver.getDriverLastName())) {
            errorMessages.add("Driver name cannot be empty");
        }
        if (!StringUtils.isNumeric(driver.getPhoneNumber()) || StringUtils.length(driver.getPhoneNumber()) != 10) {
            errorMessages.add("Invalid phone number");
        }
        if (StringUtils.isEmpty(driver.getDlState())) {
            errorMessages.add("Driver license state cannot be empty");
        }
        if (StringUtils.isEmpty(driver.getInsuranceCompany())) {
            errorMessages.add("Insurance cannot be empty");
        }
        if (StringUtils.isEmpty(driver.getVehicle().getMake())) {
            errorMessages.add("Vehicle make cannot be empty");
        }
        if (StringUtils.isEmpty(driver.getVehicle().getModel())) {
            errorMessages.add("Vehicle model cannot be empty");
        }
        if (StringUtils.isEmpty(driver.getVehicle().getYear())) {
            errorMessages.add("Vehicle year cannot be empty");
        }
        if (StringUtils.isEmpty(driver.getVehicle().getLicensePlate())) {
            errorMessages.add("Vehicle license plate cannot be empty");
        }
        if (StringUtils.isEmpty(driver.getVehicle().getColor())) {
            errorMessages.add("Vehicle color cannot be empty");
        }
        if (driver.getVehicle().getSeats() < 2) {
            errorMessages.add("Invalid seat count");
        }

        return errorMessages;
    }

}

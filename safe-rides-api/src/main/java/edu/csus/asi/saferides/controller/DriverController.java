package edu.csus.asi.saferides.controller;

import edu.csus.asi.saferides.mapper.DriverLocationMapper;
import edu.csus.asi.saferides.mapper.DriverMapper;
import edu.csus.asi.saferides.mapper.RideRequestMapper;
import edu.csus.asi.saferides.model.*;
import edu.csus.asi.saferides.model.dto.DriverDto;
import edu.csus.asi.saferides.model.dto.DriverLocationDto;
import edu.csus.asi.saferides.model.dto.RideRequestDto;
import edu.csus.asi.saferides.repository.*;
import edu.csus.asi.saferides.security.JwtTokenUtil;
import edu.csus.asi.saferides.service.UserService;
import edu.csus.asi.saferides.utility.Util;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import java.net.URI;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Rest API controller for the Driver resource
 */
@RestController
@CrossOrigin(origins = {"http://localhost:9000", "http://codeteam6.io"})
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
     * a singleton for the DriverLocationMapper
     */
    @Autowired
    private DriverLocationMapper driverLocationMapper;

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

    @Autowired
    private RideRequestMapper rideRequestMapper;

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
    public Iterable<DriverDto> retrieveAll(@Validated @RequestParam(value = "active", required = false) Boolean active) {
        List<Driver> drivers = driverRepository.findAllByOrderByModifiedDateDesc();
        if (active != null) {
            if (active) {
                return driverMapper.mapAsList(drivers.stream().filter(driver -> driver.getUser().isActive()).collect(Collectors.toList()), DriverDto.class);
            } else {
                return driverMapper.mapAsList(drivers.stream().filter(driver -> !driver.getUser().isActive()).collect(Collectors.toList()), DriverDto.class);
            }
        } else {
            return driverMapper.mapAsList((drivers), DriverDto.class);
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
    public ResponseEntity<?> retrieve(@PathVariable Long id) {
        Driver result = driverRepository.findOne(id);

        if (result == null) {
            return ResponseEntity.notFound().build();
        } else {
            return ResponseEntity.ok(driverMapper.map(result, DriverDto.class));
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
    public ResponseEntity<?> save(@Validated @RequestBody DriverDto driverDto) {
        Driver driver = driverMapper.map(driverDto, Driver.class);
        User user = driverMapper.map(driverDto, User.class);
        driver.setUser(user);

        List<String> errorMessages = validateDriver(driver);

        if (errorMessages.size() > 0) {
            return ResponseEntity.badRequest().body(new ResponseMessage(String.join("; ", errorMessages)));
        }

        User createdUser = userService.createDriverUser(driverDto);

        driver.setUser(createdUser);
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
    public ResponseEntity<?> save(@PathVariable Long id, @Validated @RequestBody DriverDto driverDto) {
        Driver updatedDriver = driverMapper.map(driverDto, Driver.class);
        User updatedUser = driverMapper.map(driverDto, User.class);

        if (!updatedDriver.getId().equals(id)) {
            return ResponseEntity.badRequest().body(new ResponseMessage("The id in the path does not match the id in the body"));
        }

        updatedDriver.setUser(updatedUser);

        List<String> errorMessages = validateDriver(updatedDriver);

        if (errorMessages.size() > 0) {
            return ResponseEntity.badRequest().body(new ResponseMessage(String.join("; ", errorMessages)));
        }

        Driver driverFromDb = driverRepository.findOne(driverDto.getId());

        // return 400 if trying to deactivate a driver that's not AVAILABLE
        if (!updatedDriver.getUser().isActive() && driverFromDb.getStatus() != DriverStatus.AVAILABLE) {
            return ResponseEntity.badRequest().body(new ResponseMessage("The driver must not have any in progress rides"));
        }

        User userFromDb = driverFromDb.getUser();
        Vehicle vehicleFromDb = driverFromDb.getVehicle();

        driverFromDb.setDlChecked(updatedDriver.getDlChecked());
        driverFromDb.setInsuranceChecked(updatedDriver.getInsuranceChecked());
        driverFromDb.setInsuranceCompany(updatedDriver.getInsuranceCompany());
        driverFromDb.setPhoneNumber(updatedDriver.getPhoneNumber());
        driverFromDb.setEndOfNightOdo(updatedDriver.getEndOfNightOdo());

        vehicleFromDb.setMake(driverDto.getVehicle().getMake());
        vehicleFromDb.setModel(driverDto.getVehicle().getModel());
        vehicleFromDb.setYear(driverDto.getVehicle().getYear());
        vehicleFromDb.setLicensePlate(driverDto.getVehicle().getLicensePlate());
        vehicleFromDb.setColor(driverDto.getVehicle().getColor());
        vehicleFromDb.setSeats(driverDto.getVehicle().getSeats());

        userFromDb.setFirstName(updatedUser.getFirstName());
        userFromDb.setLastName(updatedUser.getLastName());
        userFromDb.setActive(updatedUser.isActive());

        driverFromDb.setUser(userFromDb);
        driverFromDb.setVehicle(vehicleFromDb);

        Driver persistedDriver = driverRepository.save(driverFromDb);

        return ResponseEntity.ok(driverMapper.map(persistedDriver, DriverDto.class));
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
    public ResponseEntity<?> delete(@PathVariable Long id) {
        Driver driver = driverRepository.findOne(id);

        if (driver == null) {
            return ResponseEntity.badRequest().body(new ResponseMessage("Driver does not exist"));
        }

        if (driver.getStatus() != DriverStatus.AVAILABLE) {
            return ResponseEntity.badRequest().body(new ResponseMessage("Cannot delete a driver that is not available"));
        }

        driverRepository.delete(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * GET /drivers/{id}/rides
     *
     * @param id      - id of driver to get rides for
     * @param status  status to filter rides by
     * @param current return just the current ride request that is being serviced
     * @return list of rides for driver with specified id and filtered with specified status or current flag
     */
    @RequestMapping(method = RequestMethod.GET, value = "/{id}/rides")
    @PreAuthorize("hasRole('COORDINATOR')")
    @ApiOperation(value = "retrieveRide", nickname = "retrieveRide", notes = "Retrieves rides assigned to a driver with the given id")
    @Deprecated
    public ResponseEntity<?> retrieveRide(@PathVariable Long id,
                                          @RequestParam(value = "status", required = false) RideRequestStatus status,
                                          @RequestParam(value = "current", required = false) Boolean current) {
        return getRidesForDriver(id, status, current);
    }

    /**
     * GET /drivers/rides
     *
     * @return the current ride request for the authenticated driver
     */
    @RequestMapping(method = RequestMethod.GET, value = "/rides")
    @PreAuthorize("hasRole('DRIVER')")
    @ApiOperation(value = "retrieveRide", nickname = "retrieveRide", notes = "Retrieves ride assigned to a driver")
    @Deprecated
    public ResponseEntity<?> retrieveRide(HttpServletRequest request) {
        String authToken = request.getHeader(this.tokenHeader);
        String username = jwtTokenUtil.getUsernameFromToken(authToken);
        User user = userRepository.findByUsernameIgnoreCase(username);
        Driver driver = driverRepository.findByUser(user);

        return getRidesForDriver(driver.getId(), null, true);
    }

    /**
     * POST /drivers/location
     *
     * @param request           client request (contains the Authorization header)
     * @param driverLocationDto the new driver location
     * @return the updated driver location or a message
     */
    @RequestMapping(method = RequestMethod.POST, value = "/location")
    @PreAuthorize("hasRole('DRIVER')")
    @ApiOperation(value = "setDriverLocation", nickname = "setDriverLocation", notes = "Authenticated driver updates their latest/current location.")
    public ResponseEntity<?> setDriverLocation(HttpServletRequest request, @Validated @RequestBody DriverLocationDto
            driverLocationDto) {
        String authToken = request.getHeader(this.tokenHeader);
        String username = jwtTokenUtil.getUsernameFromToken(authToken);

        User user = userRepository.findByUsernameIgnoreCase(username);

        Driver driver = driverRepository.findByUser(user);

        DriverLocation driverLocation = driverLocationMapper.map(driverLocationDto, DriverLocation.class);

        if (driver == null) {
            return ResponseEntity.badRequest().body(new ResponseMessage("You cannot update your location as you are not a driver."));
        } else {
            driverLocation.setDriver(driver);
            driverLocationRepository.save(driverLocation);

            return ResponseEntity.ok(driverLocationDto);
        }
    }

    /**
     * Get the ride requests assigned to a driver
     *
     * @param id      the id of the driver
     * @param status  filter by the status of the request
     * @param current return only the current ride
     * @return list of ride requests
     */
    @Deprecated
    private ResponseEntity<?> getRidesForDriver(long id, RideRequestStatus status, Boolean current) {
        Driver driver = driverRepository.findOne(id);

        if (driver == null) {
            return ResponseEntity.noContent().build();
        }

        if (current != null && current) {
            //if the driver has no status or is Available, then there is no current ride, return empty
            if (driver.getStatus() == null || driver.getStatus() == DriverStatus.AVAILABLE) {
                return ResponseEntity.noContent().build();
            } else {
                Collection<RideRequest> requests = driver.getRides();

                // filter requests
                requests = Util.filterPastRides(configurationRepository.findOne(1), requests);

                if (requests != null) {
                    for (RideRequest req : requests) {
                        if (req.getStatus() != null && req.getStatus() == RideRequestStatus.ASSIGNED || req.getStatus() == RideRequestStatus.PICKINGUP
                                || req.getStatus() == RideRequestStatus.ATPICKUPLOCATION || req.getStatus() == RideRequestStatus.DROPPINGOFF) {
                            return ResponseEntity.ok(rideRequestMapper.map(req, RideRequestDto.class));
                        }
                    }
                }
            }
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

            return ResponseEntity.ok(rideRequestMapper.mapAsList(requests, RideRequestDto.class));
        } else {
            Collection<RideRequest> requests = driver.getRides();

            // filter requests
            requests = Util.filterPastRides(configurationRepository.findOne(1), requests);
            if (requests == null) {
                return ResponseEntity.noContent().build();
            } else {
                return ResponseEntity.ok(rideRequestMapper.mapAsList(requests, RideRequestDto.class));
            }
        }
    }

    private List<String> validateDriver(Driver driver) {
        ArrayList<String> errorMessages = new ArrayList<>();

        if (StringUtils.isEmpty(driver.getUser().getFirstName()) || StringUtils.isEmpty(driver.getUser().getLastName())) {
            errorMessages.add("Driver name cannot be empty");
        }
        if (!StringUtils.isNumeric(driver.getPhoneNumber()) || StringUtils.length(driver.getPhoneNumber()) != 10) {
            errorMessages.add("Invalid phone number");
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

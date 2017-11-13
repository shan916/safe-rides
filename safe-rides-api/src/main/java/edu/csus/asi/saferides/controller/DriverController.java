package edu.csus.asi.saferides.controller;

import edu.csus.asi.saferides.mapper.DriverLocationMapper;
import edu.csus.asi.saferides.mapper.DriverMapper;
import edu.csus.asi.saferides.model.*;
import edu.csus.asi.saferides.model.dto.DriverDto;
import edu.csus.asi.saferides.model.dto.DriverLocationDto;
import edu.csus.asi.saferides.model.dto.EndOfNightSummaryDto;
import edu.csus.asi.saferides.model.dto.EndOfNightSummaryRideDto;
import edu.csus.asi.saferides.repository.*;
import edu.csus.asi.saferides.security.JwtTokenUtil;
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
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Rest API controller for the Driver resource
 */
@RestController
@CrossOrigin(origins = {"http://localhost:9000", "https://codeteam6.io"})
@RequestMapping("/drivers")
@PreAuthorize("hasRole('COORDINATOR')")
class DriverController {

    private final DriverRepository driverRepository;
    private final RideRequestRepository rideRequestRepository;
    private final DriverLocationRepository driverLocationRepository;
    private final DriverMapper driverMapper;
    private final DriverLocationMapper driverLocationMapper;
    private final ConfigurationRepository configurationRepository;
    private final JwtTokenUtil jwtTokenUtil;
    private final UserRepository userRepository;
    private final String tokenHeader;

    /**
     * Driver controller constructor with dependency injection
     *
     * @param driverRepository         Driver Repository
     * @param rideRequestRepository    Ride Request Repository
     * @param driverLocationRepository Driver Location Repository
     * @param driverMapper             Driver Mapper
     * @param driverLocationMapper     Driver Location Mapper
     * @param configurationRepository  Configuration Repository
     * @param jwtTokenUtil             JWT Token Util
     * @param userRepository           User Repository
     * @param tokenHeader              HTTP header that stores the JWT, defined in application.yaml
     */
    @Autowired
    public DriverController(DriverRepository driverRepository, RideRequestRepository rideRequestRepository,
                            DriverLocationRepository driverLocationRepository, DriverMapper driverMapper,
                            DriverLocationMapper driverLocationMapper, ConfigurationRepository configurationRepository,
                            JwtTokenUtil jwtTokenUtil, UserRepository userRepository,
                            @Value("#{@environment['jwt.header'] ?: \"\" }") String tokenHeader) {
        this.driverRepository = driverRepository;
        this.rideRequestRepository = rideRequestRepository;
        this.driverLocationRepository = driverLocationRepository;
        this.driverMapper = driverMapper;
        this.driverLocationMapper = driverLocationMapper;
        this.configurationRepository = configurationRepository;
        this.jwtTokenUtil = jwtTokenUtil;
        this.userRepository = userRepository;
        this.tokenHeader = tokenHeader;
    }

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
    public ResponseEntity<Iterable<DriverDto>> retrieveAll(@Validated @RequestParam(value = "active", required = false) Boolean active) {
        List<Driver> drivers = driverRepository.findAllByOrderByModifiedDateDesc();
        if (active != null) {
            if (active) {
                return ResponseEntity.ok(driverMapper.mapAsList(drivers.stream().filter(driver -> driver.getUser().isActive()).collect(Collectors.toList()), DriverDto.class));
            } else {
                return ResponseEntity.ok(driverMapper.mapAsList(drivers.stream().filter(driver -> !driver.getUser().isActive()).collect(Collectors.toList()), DriverDto.class));
            }
        } else {
            return ResponseEntity.ok(driverMapper.mapAsList((drivers), DriverDto.class));
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
    public ResponseEntity<DriverDto> retrieve(@PathVariable Long id) {
        Driver result = driverRepository.findOne(id);

        if (result == null) {
            return ResponseEntity.notFound().build();
        } else {
            return ResponseEntity.ok(driverMapper.map(result, DriverDto.class));
        }
    }

    /**
     * GET /drivers/me
     * <p>
     * Returns the driver's own record
     *
     * @param request Http Request
     * @return Driver's own record or 404 if driver does not exist
     */
    @RequestMapping(method = RequestMethod.GET, value = "/me")
    @PreAuthorize("hasRole('DRIVER')")
    @ApiOperation(value = "retrieve", nickname = "retrieve", notes = "Returns a driver with the given id")
    public ResponseEntity<DriverDto> retrieve(HttpServletRequest request) {
        String authToken = request.getHeader(this.tokenHeader);
        String username = jwtTokenUtil.getUsernameFromToken(authToken);

        User user = userRepository.findByUsernameIgnoreCase(username);

        Driver result = driverRepository.findByUser(user);

        if (result == null) {
            return ResponseEntity.notFound().build();
        } else {
            return ResponseEntity.ok(driverMapper.map(result, DriverDto.class));
        }
    }

    /**
     * GET /drivers/endofnight
     * <p>
     * Returns the driver's end of night summary for a specified driver
     *
     * @param id the driver's id
     * @return Driver's end of night summary
     */
    @RequestMapping(method = RequestMethod.GET, value = "/{id}/endofnight")
    @PreAuthorize("hasRole('COORDINATOR')")
    @ApiOperation(value = "retrieve", nickname = "retrieve", notes = "Returns the end of the night summary for the driver with the given id")
    public ResponseEntity<EndOfNightSummaryDto> summary(@PathVariable Long id) {
        Driver driver = driverRepository.findOne(id);

        if (driver != null) {
            // get all rides for the current night
            Collection<RideRequest> currentRides = Util.filterPastRides(configurationRepository.findOne(1), driver.getRides());
            Set<EndOfNightSummaryRideDto> endOfNightSummaryRides = new HashSet<>();
            // if driver was assigned to any rides, summarize them
            if (currentRides != null) {
                // night totals
                double totalDistanceSystem = 0;
                long totalDistanceOdometer = 0;
                LocalDateTime latestCompletedRide = LocalDateTime.MIN;

                // get all locations for the driver and sort them in order
                Set<DriverLocation> locationsSet = driver.getLocations();
                List<DriverLocation> locations = locationsSet.stream().sorted(Comparator.comparing(DriverLocation::getCreatedDate)).collect(Collectors.toList());

                // iterate through rides
                for (RideRequest ride : currentRides) {
                    // only summarize completed rides
                    if (ride.getStatus() == RideRequestStatus.COMPLETE) {
                        // get ride info
                        LocalDateTime assignedDate = ride.getAssignedDate();
                        LocalDateTime completedDate = ride.getLastModified();
                        Long startOdometer = ride.getStartOdometer();
                        Long endOdometer = ride.getEndOdometer();

                        // skip ride if the assigned date or completed date is null
                        if (assignedDate == null || completedDate == null) {
                            continue;
                        }

                        // filter locations to time between ride requests
                        List<DriverLocation> filteredLocations = new ArrayList<>();
                        if (locations != null) {
                            filteredLocations = locations.stream().filter(driverLocation -> driverLocation.getCreatedDate().compareTo(assignedDate) >= 0 && driverLocation.getCreatedDate().compareTo(completedDate) <= 0).collect(Collectors.toList());
                        }

                        // create ride summary object
                        EndOfNightSummaryRideDto endOfNightSummaryRide = new EndOfNightSummaryRideDto();
                        endOfNightSummaryRide.setRideAssigned(assignedDate);
                        endOfNightSummaryRide.setRideCompleted(completedDate);
                        endOfNightSummaryRide.setStartOdometer(startOdometer);
                        endOfNightSummaryRide.setEndOdometer(endOdometer);
                        endOfNightSummaryRide.setOdometerDistance(endOdometer - startOdometer);

                        // update odometer total readings
                        totalDistanceOdometer += (endOdometer - startOdometer);

                        double distance = getDistance(filteredLocations);
                        endOfNightSummaryRide.setRecordedDistance(distance);

                        totalDistanceSystem += distance;

                        endOfNightSummaryRides.add(endOfNightSummaryRide);

                        if (latestCompletedRide.compareTo(completedDate) <= 0) {
                            latestCompletedRide = completedDate;
                        }
                    }
                }

                LocalDateTime latestCompletedRideFinal = latestCompletedRide;   // fix lambda complaint?

                // filter locations to time after the last ride request
                List<DriverLocation> filteredLocations = new ArrayList<>();
                if (locations != null) {
                    filteredLocations = locations.stream().filter(driverLocation -> driverLocation.getCreatedDate().compareTo(latestCompletedRideFinal) >= 0).collect(Collectors.toList());
                }

                double distanceAfterLastRide = getDistance(filteredLocations);

                EndOfNightSummaryDto endOfNightSummary = new EndOfNightSummaryDto();
                endOfNightSummary.setRides(endOfNightSummaryRides);
                endOfNightSummary.setDistanceDrivenSystem(totalDistanceSystem + distanceAfterLastRide);
                endOfNightSummary.setDistanceDrivenOdometer(totalDistanceOdometer + driver.getEndOfNightOdo());

                return ResponseEntity.ok(endOfNightSummary);
            }
        } else {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.noContent().build();
    }

    /**
     * POST /drivers
     * <p>
     * Creates the given driver in database
     *
     * @param request   Http request
     * @param driverDto request body containing the driver to create and password to create a user
     * @return ResponseEntity containing the created driver and it's location
     */
    @RequestMapping(method = RequestMethod.POST)
    @ApiOperation(value = "save", nickname = "save", notes = "Creates the given driver")
    public ResponseEntity<?> save(HttpServletRequest request, @Validated @RequestBody DriverDto driverDto) {
        Driver driver = driverMapper.map(driverDto, Driver.class);
        User user = driverMapper.map(driverDto, User.class);
        driver.setUser(user);

        String token = request.getHeader(tokenHeader);
        String username = jwtTokenUtil.getUsernameFromToken(token);
        if (username.equals(user.getUsername())) {
            return ResponseEntity.badRequest().body(new ResponseMessage("You cannot add yourself as a driver"));
        }

        List<String> errorMessages = validateDriver(driver);

        if (errorMessages.size() > 0) {
            return ResponseEntity.badRequest().body(new ResponseMessage(String.join("; ", errorMessages)));
        }

        User currentUser = userRepository.findByUsernameIgnoreCase(driver.getUser().getUsername());

        if (currentUser == null) {
            return ResponseEntity.badRequest().build();
        }

        if (currentUser.isActive() && currentUser.getAuthorityLevel() == AuthorityName.ROLE_ADMIN) {
            return ResponseEntity.badRequest().body(new ResponseMessage("An active administrator cannot be added as a driver!"));
        }

        if (currentUser.isActive() && currentUser.getAuthorityLevel() == AuthorityName.ROLE_COORDINATOR) {
            return ResponseEntity.badRequest().body(new ResponseMessage("An active coordinator cannot be added as a driver!"));
        }

        driver.setUser(currentUser);
        Driver result = driverRepository.save(driver);


        currentUser.setAuthorityLevel(AuthorityName.ROLE_DRIVER);

        currentUser.setFirstName(user.getFirstName());
        currentUser.setLastName(user.getLastName());
        currentUser.setActive(true);
        currentUser.setTokenValidFrom(LocalDateTime.now(ZoneId.of(Util.APPLICATION_TIME_ZONE)));

        userRepository.save(currentUser);

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
     * @param request   Http Request
     * @param id        path parameter for id of driver to update
     * @param driverDto request body containing the driver and password to update
     * @return the updated driver or error message
     */
    @RequestMapping(method = RequestMethod.PUT, value = "/{id}")
    @ApiOperation(value = "save", nickname = "save", notes = "Updates a driver")
    public ResponseEntity<?> save(HttpServletRequest request, @PathVariable Long id, @Validated @RequestBody DriverDto driverDto) {
        Driver updatedDriver = driverMapper.map(driverDto, Driver.class);
        User updatedUser = driverMapper.map(driverDto, User.class);

        String token = request.getHeader(tokenHeader);
        String username = jwtTokenUtil.getUsernameFromToken(token);
        if (username.equals(updatedUser.getUsername())) {
            return ResponseEntity.badRequest().body(new ResponseMessage("You cannot modify yourself"));
        }

        if (!updatedDriver.getId().equals(id)) {
            return ResponseEntity.badRequest().body(new ResponseMessage("The id in the path does not match the id in the body"));
        }

        updatedDriver.setUser(updatedUser);

        List<String> errorMessages = validateDriver(updatedDriver);

        if (errorMessages.size() > 0) {
            return ResponseEntity.badRequest().body(new ResponseMessage(String.join("; ", errorMessages)));
        }

        Driver driverFromDb = driverRepository.findOne(driverDto.getId());
        User userFromDb = driverFromDb.getUser();
        Vehicle vehicleFromDb = driverFromDb.getVehicle();

        // return 400 if trying to deactivate a driver that's not AVAILABLE
        RideRequest latestRequest = driverFromDb.getLatestRideRequest();
        if (!updatedDriver.getUser().isActive() && (latestRequest != null && !Util.rideComplete(latestRequest.getStatus()))) {
            return ResponseEntity.badRequest().body(new ResponseMessage("The driver must not have any in progress rides"));
        } else {
            LocalDateTime now = LocalDateTime.now(ZoneId.of(Util.APPLICATION_TIME_ZONE));
            updatedUser.setTokenValidFrom(now);
            userFromDb.setTokenValidFrom(updatedUser.getTokenValidFrom());
        }

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

        if (!isDriverAvailable(driver)) {
            return ResponseEntity.badRequest().body(new ResponseMessage("Cannot delete a driver that is not available"));
        }

        for (RideRequest ride : driver.getRides()) {
            // todo: save driver statistics for reporting
            ride.setDriver(null);
            rideRequestRepository.save(ride);
        }

        User user = driver.getUser();

        driverRepository.delete(id);
        userRepository.delete(user);

        return ResponseEntity.noContent().build();
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
     * Validates a driver's fields
     *
     * @param driver the driver to validate
     * @return list of validation errors
     */
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

    /**
     * Check if a driver is available
     *
     * @param driver the driver to check if is available
     * @return boolean available status
     */
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

    /**
     * Calculate the distance between a set of gps coordinates
     *
     * @param filteredLocations the list of locations
     * @return distance in miles
     */
    private double getDistance(List<DriverLocation> filteredLocations) {
        // only calculate distance if there is a distance to calculate
        int points = filteredLocations.size();
        DriverLocation[] locationsArray = filteredLocations.toArray(new DriverLocation[0]);
        if (points > 1) {
            double[] longitudes = new double[points];
            double[] latitudes = new double[points];
            for (int i = 0; i < points; i++) {
                longitudes[i] = locationsArray[i].getLongitude();
                latitudes[i] = locationsArray[i].getLatitude();
            }
            return Util.calculateDistance(latitudes, longitudes);

        } else {
            return 0;
        }
    }
}

package edu.csus.asi.saferides.service;

import java.net.URI;
import java.util.List;
import java.util.Set;

import edu.csus.asi.saferides.model.DriverStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import edu.csus.asi.saferides.model.Driver;
import edu.csus.asi.saferides.model.RideRequest;
import edu.csus.asi.saferides.model.RideRequestStatus;
import edu.csus.asi.saferides.repository.DriverRepository;
import edu.csus.asi.saferides.repository.RideRequestRepository;

/*
 * @author Zeeshan Khaliq
 * 
 * Rest API controller for the Driver resource 
 * */
@RestController
@CrossOrigin(origins = {"http://localhost:9000", "https://codeteam6.io"})
@RequestMapping("/drivers")
public class DriverController {

	// this creates a singleton for DriverRepository
	@Autowired
	private DriverRepository driverRepository;
	@Autowired
	private RideRequestRepository rideRequestRepository;

	/*
	 * GET "/drivers"
	 * GET "/drivers?active=true"
	 * GET "/drivers?active=false"
	 * 
	 * @return list drivers based on query param
	 * */
	@RequestMapping(method = RequestMethod.GET)
	public Iterable<Driver> retrieveAll(@RequestParam(value = "active", required = false) Boolean active) {
		if (active != null) {
			return setDriverStatus(driverRepository.findByActive(active));
		} else {
			return setDriverStatus((List<Driver>)driverRepository.findAll());
		}
	}
	
	/*
	 * GET "/drivers/{id} 
	 * 
	 * @param id - the id of the driver to find
	 * 
	 * @return driver with specified id else not found
	 * */
	@RequestMapping(method = RequestMethod.GET, value="/{id}")
	public ResponseEntity<?> retrieve(@PathVariable Long id) {
		Driver result = driverRepository.findOne(id);
		
		if (result == null) {
			return ResponseEntity.notFound().build();
		} else {
			return ResponseEntity.ok(setDriverStatus(result));
		}
	}

	/*
	 * GET "/drivers/{id}/rides?status=active
	 *
	 * @param id - the id of the driver to get the rides for based on query params
	 *
	 * @return driver's assigned ride and ride request status, else not found
	 * */
	@RequestMapping(method = RequestMethod.GET, value="/{id}/rides")
	public ResponseEntity<?> retrieveRide(@PathVariable Long id, @RequestParam(value = "status", required = false) RideRequestStatus status) {
		Driver result = driverRepository.findOne(id);

		if (result == null) {
			return ResponseEntity.notFound().build();
		} else if(status != null) {
			Set<RideRequest> requests = result.getRides();
			requests.removeIf((RideRequest req) -> req.getStatus() != status);

			if (requests.size() > 0) {
				return ResponseEntity.ok(requests);
			} else {
				return ResponseEntity.notFound().build();
			}
		}else{
			Set<RideRequest> requests = result.getRides();
			if (requests.size() > 0) {
				return ResponseEntity.ok(requests);
			} else {
				return ResponseEntity.notFound().build();
			}
		}
	}
	
	/*
	 * POST "/drivers" 
	 * 
	 * Creates the given driver
	 * 
	 * @param driver - the driver to be created in the database
	 * 
	 * @return HTTP response containing saved entity with status of "created" 
	 *  	   and the location header set to location of the entity
	 * */
	@RequestMapping(method = RequestMethod.POST)
	public ResponseEntity<?> save(@RequestBody Driver driver) {
		Driver result = driverRepository.save(driver);
		
		// create URI of where the driver was created
		URI location = ServletUriComponentsBuilder
				.fromCurrentRequest().path("/{id}")
				.buildAndExpand(result.getId()).toUri();
				
		return ResponseEntity.created(location).body(result);
	}

	/*
	 * POST "/drivers/{id}/rides"
	 *
	 * Assigns a ride request to the given driver
	 *
	 * @param rideRequest - the ride request to be assigned to the driver
	 *
	 * @return HTTP response containing saved entity with status of "ok"
	 * */
	@RequestMapping(method = RequestMethod.POST, value="/{id}/rides")
	public ResponseEntity<?> assignRideRequest(@PathVariable Long id, @RequestBody RideRequest rideRequest) {
		Driver driver = driverRepository.findOne(id);
		RideRequest rideReq = rideRequestRepository.findOne(rideRequest.getId());
		
		rideReq.setStatus(RideRequestStatus.ASSIGNED);
		rideReq.setDriver(driver);
		driver.getRides().add(rideReq);
		
		driverRepository.save(driver);
		// create URI of where the driver was updated
		URI location = ServletUriComponentsBuilder
				.fromCurrentContextPath().path("/drivers/{id}")
				.buildAndExpand(driver.getId()).toUri();

		return ResponseEntity.ok(location);
	}

	/*
	 * PUT "/drivers/{id}" 
	 * 
	 * Updates the given driver
	 * 
	 * @param driver - the driver to be updated in the database
	 * 
	 * @return HTTP response containing saved entity with status of "ok"
	 * */
	@RequestMapping(method = RequestMethod.PUT, value = "/{id}")
	public ResponseEntity<?> save(@PathVariable Long id, @RequestBody Driver driver) {
		Driver result = driverRepository.save(driver);
				
		return ResponseEntity.ok(result);
	}
	
	/*
	 * DELETE "/drivers/{id} 
	 * 
	 * @param id - the id of the driver to delete
	 * 
	 * @return HTTP response with status of "no content" if deleted successfully
	 * 		   else response with status of "not found"
	 * */
	@RequestMapping(method = RequestMethod.DELETE, value = "/{id}")
	public ResponseEntity<?> delete(@PathVariable Long id) {
		if (driverRepository.findOne(id) == null) {
			return ResponseEntity.notFound().build();
		} else {
			driverRepository.delete(id);
			return ResponseEntity.noContent().build();
		}
	}

	private List<Driver> setDriverStatus(List<Driver> drivers){
		for (Driver d: drivers) {
            d = setDriverStatus(d);
		}
		return drivers;
	}

    private Driver setDriverStatus(Driver driver) {
        Set<RideRequest> requests = driver.getRides();
        if (requests.stream().filter(req -> req.getStatus() == RideRequestStatus.ASSIGNED).count() > 0) {
            driver.setStatus(DriverStatus.ASSIGNED);
        } else if (requests.stream().filter(req -> req.getStatus() == RideRequestStatus.INPROGRESS).count() > 0) {
            driver.setStatus(DriverStatus.INPROGRESS);
        } else {
            driver.setStatus(DriverStatus.AVAILABLE);
        }

        return driver;
    }
}

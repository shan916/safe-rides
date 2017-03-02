package edu.csus.asi.saferides.service;

import edu.csus.asi.saferides.model.RideRequest;
import edu.csus.asi.saferides.repository.RideRequestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import edu.csus.asi.saferides.model.Status;
import java.net.URI;

/*
 * @author Zeeshan Khaliq
 * 
 * Rest API controller for the RideRequest resource 
 * */
@RestController
@CrossOrigin(origins = {"http://localhost:9000", "https://codeteam6.io"})
@RequestMapping("/rideRequests")
public class RideRequestController {

	// this creates a singleton for RideRequestRepository
	@Autowired
	private RideRequestRepository rideRequestRepository;
	
	/*
	 * GET "/rideRequests"
	 * GET "/rideRequests?status=true"
	 * GET "/rideRequests?status=false"
	 * 
	 * @return list rideRequests based on query param
	 * */
	@RequestMapping(method = RequestMethod.GET)
	public Iterable<RideRequest> retrieveAll(@RequestParam(value = "status", required = false) Status status) {
		return rideRequestRepository.findAll();
	}
	
	/*
	 * GET "/rideRequests/{id} 
	 * 
	 * @param id - the id of the rideRequest to find
	 * 
	 * @return rideRequest with specified id else not found
	 * */
	@RequestMapping(method = RequestMethod.GET, value="/{id}")
	public ResponseEntity<?> retrive(@PathVariable Long id) {
		RideRequest result = rideRequestRepository.findOne(id);
		
		if (result == null) {
			return ResponseEntity.notFound().build();
		} else {
			return ResponseEntity.ok(result);
		}
	}
	
	/*
	 * POST "/rideRequests" 
	 * 
	 * Creates the given rideRequest
	 * 
	 * @param rideRequest - the rideRequest to be created in the database
	 * 
	 * @return HTTP response containing saved entity with status of "created" 
	 *  	   and the location header set to location of the entity
	 * */
	@RequestMapping(method = RequestMethod.POST)
	public ResponseEntity<?> save(@RequestBody RideRequest rideRequest) {
		RideRequest result = rideRequestRepository.save(rideRequest);
		
		// create URI of where the rideRequest was created
		URI location = ServletUriComponentsBuilder
				.fromCurrentRequest().path("/{id}")
				.buildAndExpand(result.getRequestId()).toUri();
				
		return ResponseEntity.created(location).body(result);
	}
	
	/*
	 * PUT "/rideRequests/{id}" 
	 * 
	 * Updates the given rideRequest
	 * 
	 * @param rideRequest - the rideRequest to be updated in the database
	 * 
	 * @return HTTP response containing saved entity with status of "ok"
	 * */
	@RequestMapping(method = RequestMethod.PUT, value = "/{id}")
	public ResponseEntity<?> save(@PathVariable Long id, @RequestBody RideRequest rideRequest) {
		RideRequest result = rideRequestRepository.save(rideRequest);
				
		return ResponseEntity.ok(result);
	}
	
	
	/*
	 * DELETE "/rideRequests/{id} 
	 * 
	 * @param id - the id of the rideRequest to delete
	 * 
	 * @return HTTP response with status of "no content" if deleted successfully
	 * 		   else response with status of "not found"
	 * */
	@RequestMapping(method = RequestMethod.DELETE, value = "/{id}")
	public ResponseEntity<?> delete(@PathVariable Long id) {
		if (rideRequestRepository.findOne(id) == null) {
			return ResponseEntity.notFound().build();
		} else {
			rideRequestRepository.delete(id);
			return ResponseEntity.noContent().build();
		}
	}
	
}

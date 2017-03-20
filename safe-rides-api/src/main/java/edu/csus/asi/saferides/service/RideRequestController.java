package edu.csus.asi.saferides.service;

import edu.csus.asi.saferides.model.RideRequest;
import edu.csus.asi.saferides.repository.RideRequestRepository;
import edu.csus.asi.saferides.security.JwtTokenUtil;
import edu.csus.asi.saferides.security.JwtUser;
import edu.csus.asi.saferides.security.JwtUserFactory;
import edu.csus.asi.saferides.security.model.Authority;
import edu.csus.asi.saferides.security.model.AuthorityName;
import edu.csus.asi.saferides.security.model.User;
import edu.csus.asi.saferides.security.repository.AuthorityRepository;
import edu.csus.asi.saferides.security.repository.UserRepository;
import edu.csus.asi.saferides.security.service.JwtAuthenticationResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.mobile.device.Device;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import edu.csus.asi.saferides.model.RideRequestStatus;
import java.net.URI;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/*
 * @author Ryan Long
 * 
 * Rest API controller for the RideRequest resource 
 * */
@RestController
@CrossOrigin(origins = {"http://localhost:9000", "https://codeteam6.io"})
@RequestMapping("/rides")
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

	/*
	 * GET "/rideRequests"
	 * GET "/rideRequests?status=true"
	 * GET "/rideRequests?status=false"
	 * 
	 * @return list rideRequests based on query param
	 * */
	@RequestMapping(method = RequestMethod.GET)
	public Iterable<RideRequest> retrieveAll(@RequestParam(value = "status", required = false) RideRequestStatus status) {
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
	public ResponseEntity<?> retrieve(@PathVariable Long id) {
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
	public ResponseEntity<?> save(@RequestBody RideRequest rideRequest, Device device) {
		rideRequest.setDate(new Date());	// default to current datetime
		rideRequest.setStatus(RideRequestStatus.UNASSIGNED);	// default to unassigned status

		RideRequest result = rideRequestRepository.save(rideRequest);
		
		// create URI of where the rideRequest was created
		URI location = ServletUriComponentsBuilder
				.fromCurrentRequest().path("/{id}")
				.buildAndExpand(result.getId()).toUri();
				
		//return ResponseEntity.created(location).body(result);

		Date currentDate = new Date();

		User requestor = new User("" + rideRequest.getRequestorId(),
				rideRequest.getRequestorFirstName(), rideRequest.getRequestorLastName(), currentDate.toString(), "null@null.null");

		requestor.setLastPasswordResetDate(currentDate);

		List<Authority> authorityList = new ArrayList<Authority>();
		authorityList.add(authorityRepository.findByName(AuthorityName.ROLE_RIDER));

		requestor.setAuthorities(authorityList);

		userRepository.save(requestor);

		final String token = jwtTokenUtil.generateToken(JwtUserFactory.create(requestor), device);

		// Return the token
		return ResponseEntity.ok(new JwtAuthenticationResponse(token));
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

	private static List<GrantedAuthority> mapToGrantedAuthorities(List<Authority> authorities) {
		return authorities.stream()
				.map(authority -> new SimpleGrantedAuthority(authority.getName().name()))
				.collect(Collectors.toList());
	}
	
}

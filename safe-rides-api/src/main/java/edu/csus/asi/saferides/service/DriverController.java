package edu.csus.asi.saferides.service;

import java.net.URI;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import edu.csus.asi.saferides.model.Driver;
import edu.csus.asi.saferides.repository.DriverRepository;

/*
 * @author Zeeshan Khaliq
 * 
 * Rest API controller for the Driver resource 
 * */
@RestController
@RequestMapping("/drivers")
public class DriverController {

	// this creates a singleton for DriverRepository
	@Autowired
	private DriverRepository driverRepository;
	
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
			return driverRepository.findByActive(active);
		} else {
			return driverRepository.findAll();
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
	public ResponseEntity<?> retrive(@PathVariable Long id) {
		Driver result = driverRepository.findOne(id);
		
		if (result == null) {
			return ResponseEntity.notFound().build();
		} else {
			return ResponseEntity.ok(result);
		}
	}
	
	/*
	 * POST "/drivers" 
	 * 
	 * Creates or saves the given driver
	 * 
	 * @param driver - the driver to be saved in the database
	 * 
	 * @return HTTP response containing saved entity with status of "created" 
	 *  	   and the location header set to location of the entity
	 * */
	@RequestMapping(method = { RequestMethod.POST, RequestMethod.PUT })
	public ResponseEntity<?> save(@Valid @RequestBody Driver driver) {
		Driver result = driverRepository.save(driver);
		
		// create URI of where the driver was created
		URI location = ServletUriComponentsBuilder
				.fromCurrentRequest().path("/{id}")
				.buildAndExpand(result.getId()).toUri();
				
		return ResponseEntity.created(location).body(result);
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
	
}

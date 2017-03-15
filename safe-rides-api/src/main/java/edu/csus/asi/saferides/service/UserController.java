package edu.csus.asi.saferides.service;

import java.net.URI;
import java.util.Iterator;
import java.util.Set;
import javax.validation.Valid;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import edu.csus.asi.saferides.model.User;
import edu.csus.asi.saferides.repository.UserRepository;

/*
 * @author Zeeshan Khaliq
 * 
 * Rest API controller for the User resource 
 * */
@RestController
@CrossOrigin(origins = {"http://localhost:9000", "https://codeteam6.io"})
@RequestMapping("/users")
public class UserController {

	// this creates a singleton for UserRepository
	@Autowired
	private UserRepository userRepository;
	/*
	 * GET "/users"
	 * 
	 * @return list users
	 
	@RequestMapping(method = RequestMethod.GET)
	public Iterable<User> retrieveAll() {
			return userRepository.findAll();
	}
	* */
	
	
	/*
	 * GET "/users/{id} 
	 * 
	 * @param id - the id of the user to find
	 * 
	 * @return user with specified id else not found
	 
	@RequestMapping(method = RequestMethod.GET, value="/{id}")
	public ResponseEntity<?> retrieve(@PathVariable Long id) {
		User result = userRepository.findOne(id);
		
		if (result == null) {
			return ResponseEntity.notFound().build();
		} else {
			return ResponseEntity.ok(result);
		}
	}
	* */
	
	/*
	 * GET "/users/byUsername/{username} 
	 * 
	 * @param username - the username of the user to find
	 * 
	 * @return user with specified username else not found
	 
	@RequestMapping(method = RequestMethod.GET, value="/byUsername/{username}")
	public ResponseEntity<?> retrieve(@PathVariable String username) {
		User result = userRepository.findByUsername(username);
		
		if (result == null) {
			return ResponseEntity.notFound().build();
		} else {
			return ResponseEntity.ok(result);
		}
	}
	* */

	
	/*
	 * POST "/users" 
	 * 
	 * Creates the given user
	 * 
	 * @param user - the user to be created in the database
	 * 
	 * @return HTTP response containing saved entity with status of "created" 
	 *  	   and the location header set to location of the entity
	 * */
	@RequestMapping(method = RequestMethod.POST)
	public ResponseEntity<?> save(@RequestBody User user) {
		User result = userRepository.save(user);
		
		// create URI of where the user was created
		URI location = ServletUriComponentsBuilder
				.fromCurrentRequest().path("/{username}")
				.buildAndExpand(result.getUsername()).toUri();
				
		return ResponseEntity.created(location).body(result);
	}
	
	
	/*
	 * POST "/users/authenticate" 
	 * 
	 * Authenticates the given user
	 * 
	 * @param user - the user to be authenticated in the database
	 * 
	 * @return HTTP response containing saved entity with status of "created" 
	 *  	   and the location header set to location of the entity
	 * */
	@RequestMapping(method = RequestMethod.POST, value="/authenticate")
	public Boolean authenticate(@RequestBody String usrAndPass) {
		String username = null;
		String password = null;
		User result = null;
		
			JsonObject jsonCredentials = new JsonParser().parse(usrAndPass).getAsJsonObject();
			username = jsonCredentials.get("username").getAsString();
			password = jsonCredentials.get("password").getAsString();
		
		if(username != null)
			result = userRepository.findByUsername(username);
		if(result != null)
			return result.checkPassword(password);
		else
			return false;
	}
	
	

	
	/*
	 * PUT "/users/{id}" 
	 * 
	 * Updates the given user
	 * 
	 * @param user - the user to be updated in the database
	 * 
	 * @return HTTP response containing saved entity with status of "ok"
	 * */
	@RequestMapping(method = RequestMethod.PUT, value = "/{username}")
	public ResponseEntity<?> save(@PathVariable String username, @RequestBody User user) {
		User result = userRepository.save(user);
				
		return ResponseEntity.ok(result);
	}
	
	
	/*
	 * DELETE "/users/{id} 
	 * 
	 * @param id - the id of the user to delete
	 * 
	 * @return HTTP response with status of "no content" if deleted successfully
	 * 		   else response with status of "not found"
	 
	@RequestMapping(method = RequestMethod.DELETE, value = "/{id}")
	public ResponseEntity<?> delete(@PathVariable Long id) {
		if (userRepository.findOne(id) == null) {
			return ResponseEntity.notFound().build();
		} else {
			userRepository.delete(id);
			return ResponseEntity.noContent().build();
		}
	}
	* */
}

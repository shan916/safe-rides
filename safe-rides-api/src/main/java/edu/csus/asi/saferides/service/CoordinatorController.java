package edu.csus.asi.saferides.service;

import edu.csus.asi.saferides.model.*;
import edu.csus.asi.saferides.repository.CoordinatorRepository;
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
 * @author Zeeshan Khaliq, Nik Sorvari
 * 
 * Rest API controller for the Coordinator resource 
 * */
@RestController
@CrossOrigin(origins = {"http://localhost:9000", "https://codeteam6.io"})
@RequestMapping("/coordinators")
@PreAuthorize("hasRole('COORDINATOR')")
public class CoordinatorController {

	// this creates a singleton for CoordinatorRepository
    @Autowired
    private CoordinatorRepository coordinatorRepository;
	
    // this creates a singleton for DriverRepository
    @Autowired
    private DriverRepository driverRepository;

    // this creates a singleton for RideRequestRepository
    @Autowired
    private RideRequestRepository rideRequestRepository;

    @Autowired
    private DriverLocationRepository coordinatorLocationRepository;

    @Value("${jwt.header}")
    private String tokenHeader;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private UserRepository userRepository;


    /*
     * GET /coordinators
     */
    @RequestMapping(method = RequestMethod.GET)
    @ApiOperation(value = "retrieveAll", nickname = "retrieveAll", notes = "Returns a list of coordinators...")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success", response = Coordinator.class, responseContainer = "List"),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 403, message = "Forbidden"),
            @ApiResponse(code = 500, message = "Failure")})
    public Iterable<Coordinator> retrieveAll(@RequestParam(value = "active", required = false) Boolean active) {
        if (active != null) {
            if (active) {
                return coordinatorRepository.findByActiveTrueOrderByModifiedDateDesc();
            } else {
                return coordinatorRepository.findByActiveFalseOrderByModifiedDateDesc();
            }
        } else {
            return coordinatorRepository.findAllByOrderByModifiedDateDesc();
        }
    }

    /*
     * GET /coordinators/{id}
     */
    @RequestMapping(method = RequestMethod.GET, value = "/{id}")
    @ApiOperation(value = "retrieve", nickname = "retrieve", notes = "Returns a coordinator with the given id")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success", response = Coordinator.class),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 403, message = "Forbidden"),
            @ApiResponse(code = 404, message = "Not Found"),
            @ApiResponse(code = 500, message = "Failure")})
    public ResponseEntity<?> retrieve(@PathVariable Long id) {
        Coordinator result = coordinatorRepository.findOne(id);

        if (result == null) {
            return ResponseEntity.notFound().build();
        } else {
            return ResponseEntity.ok(result);
        }
    }

    /*
     * POST /coordinators
     */
    @RequestMapping(method = RequestMethod.POST)
    @ApiOperation(value = "save", nickname = "save", notes = "Creates the given coordinator")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success", response = ResponseEntity.class),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 403, message = "Forbidden"),
            @ApiResponse(code = 500, message = "Failure")})
    public ResponseEntity<?> save(@RequestBody Coordinator coordinator) {
        Coordinator result = coordinatorRepository.save(coordinator);

        // create URI of where the coordinator was created
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(result.getId())
                .toUri();

        return ResponseEntity.created(location).body(result);
    }

    /*
     * PUT /coordinators/{id}
     */
    @RequestMapping(method = RequestMethod.PUT, value = "/{id}")
    @ApiOperation(value = "save", nickname = "save", notes = "Updates a coordinator")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success", response = ResponseEntity.class),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 403, message = "Forbidden"),
            @ApiResponse(code = 500, message = "Failure")})
    public ResponseEntity<?> save(@PathVariable Long id, @RequestBody Coordinator coordinator) {
        if (coordinator.getId() != null && coordinator.getId() == id) {
            Coordinator result = coordinatorRepository.findOne(id);
/*
            if (!coordinator.getActive() && result.getStatus() != CoordinatorStatus.AVAILABLE) {
                return ResponseEntity.badRequest().body("The coordinator must not have any in progress rides");
            }
*/
            result = coordinatorRepository.save(coordinator);
            return ResponseEntity.ok(result);
        } else {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
    }

    /*
     * DELETE /coordinators/{id}
     */
    @RequestMapping(method = RequestMethod.DELETE, value = "/{id}")
    @ApiOperation(value = "delete", nickname = "delete", notes = "Deletes a coordinator")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success", response = ResponseEntity.class),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 403, message = "Forbidden"),
            @ApiResponse(code = 500, message = "Failure")})
    public ResponseEntity<?> delete(@PathVariable Long id) {
        if (coordinatorRepository.findOne(id) == null) {
            return ResponseEntity.notFound().build();
        } else {
            coordinatorRepository.delete(id);
            return ResponseEntity.noContent().build();
        }
    }

}

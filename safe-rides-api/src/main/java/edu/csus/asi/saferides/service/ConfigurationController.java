package edu.csus.asi.saferides.service;

import edu.csus.asi.saferides.model.Configuration;
import edu.csus.asi.saferides.model.ResponseMessage;
import edu.csus.asi.saferides.repository.ConfigurationRepository;
import edu.csus.asi.saferides.utility.Util;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

/**
 * Rest API controller for the Configuration resource
 */
@RestController
@CrossOrigin(origins = {"http://localhost:9000", "https://codeteam6.io"})
@RequestMapping("/config")
public class ConfigurationController {

    @Autowired
    ConfigurationRepository configurationRepository;

    /**
     * Check if the application is accepting ride requests at this time
     *
     * @return whether the application is accepting ride requests
     */
    @RequestMapping(value = "/isLive", method = RequestMethod.GET)
    @ApiOperation(value = "isLive", nickname = "Accepting Ride Requests?", notes = "Check if a ride can be requested at this time")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success", response = boolean.class),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 403, message = "Forbidden"),
            @ApiResponse(code = 500, message = "Failure")})
    public boolean isLive() {
        return Util.isAcceptingRideRequests(configurationRepository.findOne(1));
    }

    /**
     * Get current config settings
     *
     * @return the application's current config
     */
    @RequestMapping(value = "/current", method = RequestMethod.GET)
    @PreAuthorize("hasRole('COORDINATOR')")
    @ApiOperation(value = "current", nickname = "Current configuration", notes = "Get the application's current configuration")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success", response = boolean.class),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 403, message = "Forbidden"),
            @ApiResponse(code = 500, message = "Failure")})
    public ResponseEntity<?> current() {
        return ResponseEntity.ok(configurationRepository.findOne(1));
    }

    /**
     * Update configuration properties
     *
     * @param configuration of the application to be updated to
     * @return whether the application is accepting ride requests
     */
    @RequestMapping(value = "/update", method = RequestMethod.PUT)
    @PreAuthorize("hasRole('COORDINATOR')")
    @ApiOperation(value = "updateConfiguration", nickname = "Update configuration", notes = "Update the application configuration")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success", response = boolean.class),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 403, message = "Forbidden"),
            @ApiResponse(code = 500, message = "Failure")})
    public ResponseEntity<?> updateConfiguration(@RequestBody Configuration configuration) {
        // Validate
        boolean valid = true;
        ArrayList<String> validationMessages = new ArrayList<String>();

        if (configuration.getStartTime() == null) {
            valid = false;
            validationMessages.add("Start time cannot be empty.");
        }
        if (configuration.getEndTime() == null) {
            valid = false;
            validationMessages.add("End time cannot be empty.");
        }
        if (configuration.getDaysOfWeek() == null || configuration.getDaysOfWeek().size() == 0) {
            valid = false;
            validationMessages.add("Days of week cannot be empty.");
        }
        if (configuration.getStartTime() != null && configuration.getEndTime() != null &&
                configuration.getStartTime().compareTo(configuration.getEndTime()) == 0) {
            valid = false;
            validationMessages.add("Start date cannot match end date.");
        }
        if (!valid) {
            return ResponseEntity.badRequest().body(new ResponseMessage(String.join("; ", validationMessages)));
        }

        configurationRepository.save(configuration);

        return ResponseEntity.ok(new ResponseMessage("Configuration updated."));
    }
}

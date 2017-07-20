package edu.csus.asi.saferides.service;

import edu.csus.asi.saferides.mapper.ConfigurationMapper;
import edu.csus.asi.saferides.model.Configuration;
import edu.csus.asi.saferides.model.ResponseMessage;
import edu.csus.asi.saferides.model.dto.ConfigurationDto;
import edu.csus.asi.saferides.repository.ConfigurationRepository;
import edu.csus.asi.saferides.utility.Util;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

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
     * a singleton for the RideRequestMapper
     */
    @Autowired
    private ConfigurationMapper configurationMapper;

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
    public ResponseEntity<ConfigurationDto> current() {
        Configuration configuration = configurationRepository.findOne(1);
        ConfigurationDto configurationDto = configurationMapper.map(configuration, ConfigurationDto.class);
        return ResponseEntity.ok(configurationDto);
    }

    /**
     * Update configuration properties
     *
     * @param configurationDto configuration of the application to be updated to
     * @return configuration update status
     */
    @RequestMapping(value = "/update", method = RequestMethod.PUT)
    @PreAuthorize("hasRole('COORDINATOR')")
    @ApiOperation(value = "updateConfiguration", nickname = "Update configuration", notes = "Update the application configuration")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success", response = boolean.class),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 403, message = "Forbidden"),
            @ApiResponse(code = 500, message = "Failure")})
    public ResponseEntity<?> updateConfiguration(@Validated @RequestBody ConfigurationDto configurationDto) {
        Configuration configuration = configurationMapper.map(configurationDto, Configuration.class);

        // the configuration item should always be at id 1. there should not be any other rows
        configuration.setId(1);

        configurationRepository.save(configuration);

        return ResponseEntity.ok(new ResponseMessage("Configuration updated."));
    }
}

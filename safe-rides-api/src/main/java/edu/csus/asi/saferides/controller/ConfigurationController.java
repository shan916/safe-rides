package edu.csus.asi.saferides.controller;

import edu.csus.asi.saferides.mapper.ConfigurationMapper;
import edu.csus.asi.saferides.model.Configuration;
import edu.csus.asi.saferides.model.ResponseMessage;
import edu.csus.asi.saferides.model.dto.ConfigurationDto;
import edu.csus.asi.saferides.repository.ConfigurationRepository;
import edu.csus.asi.saferides.utility.Util;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * Rest API controller for the Configuration resource
 */
@RestController
@CrossOrigin(origins = {"https://safe-rides.com"})
@RequestMapping("/config")
class ConfigurationController {

    private final ConfigurationRepository configurationRepository;
    private final ConfigurationMapper configurationMapper;

    /**
     * Configuration controller constructor with dependency injection
     *
     * @param configurationRepository Configuration Repository
     * @param configurationMapper     Configuration Mapper
     */
    @Autowired
    public ConfigurationController(ConfigurationRepository configurationRepository, ConfigurationMapper configurationMapper) {
        this.configurationRepository = configurationRepository;
        this.configurationMapper = configurationMapper;
    }

    /**
     * Check if the application is accepting ride requests at this time
     *
     * @return whether the application is accepting ride requests
     */
    @RequestMapping(value = "/isLive", method = RequestMethod.GET)
    @ApiOperation(value = "isLive", nickname = "Accepting Ride Requests?", notes = "Check if a ride can be requested at this time")
    public ResponseEntity<ResponseMessage> isLive() {
        return ResponseEntity.ok().body(new ResponseMessage(Boolean.toString(Util.isAcceptingRideRequests(configurationRepository.findOne(1)))));
    }

    /**
     * Get any system notification message if available
     *
     * @return system notification message if available
     */
    @RequestMapping(value = "/message", method = RequestMethod.GET)
    @ApiOperation(value = "message", nickname = "System Notification Message", notes = "Get any system notification message if available")
    public ResponseEntity<ResponseMessage> systemMessage() {
        String message = configurationRepository.findOne(1).getMessage();
        if (StringUtils.isEmpty(message)) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.ok().body(new ResponseMessage(message));
        }
    }

    /**
     * Get current config settings
     *
     * @return the application's current config
     */
    @RequestMapping(value = "/current", method = RequestMethod.GET)
    @PreAuthorize("hasRole('COORDINATOR')")
    @ApiOperation(value = "current", nickname = "Current configuration", notes = "Get the application's current configuration")
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
    public ResponseEntity<ResponseMessage> updateConfiguration(@Validated @RequestBody ConfigurationDto configurationDto) {
        Configuration configuration = configurationMapper.map(configurationDto, Configuration.class);

        // the configuration item should always be at id 1. there should not be any other rows
        configuration.setId(1);

        configurationRepository.save(configuration);

        return ResponseEntity.ok(new ResponseMessage("Configuration updated."));
    }
}

package edu.csus.asi.saferides.service;

import edu.csus.asi.saferides.model.Configuration;
import edu.csus.asi.saferides.repository.ConfigurationRepository;
import edu.csus.asi.saferides.utility.Util;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

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
        Configuration configuration = configurationRepository.findOne(1);
        LocalTime startTime = configuration.getStartTime();
        LocalTime endTime = configuration.getEndTime();
        List<DayOfWeek> dayOfWeeks = configuration.getDaysOfWeek();

        LocalDateTime currentDateTime = LocalDateTime.now();

        return Util.validRideRequestDateTime(currentDateTime, startTime, endTime, dayOfWeeks);
    }
}

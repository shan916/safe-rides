package edu.csus.asi.saferides.service;

import edu.csus.asi.saferides.model.Configuration;
import edu.csus.asi.saferides.repository.ConfigurationRepository;
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
        DayOfWeek currentDOW = currentDateTime.getDayOfWeek();
        LocalTime currentTime = currentDateTime.toLocalTime();
        LocalDateTime startDateTime, endDateTime;

        boolean valid = false;

        // if the startDate and the endDate spans two days
        if (startTime.compareTo(endTime) > 0) { // startTime > endTime (meaning the start time is before midnight and the endTime is after midnight)
            if (currentTime.compareTo(startTime) < 0) {
                // current time is on the second day
                // set the start date to yesterday and the end date to today
                startDateTime = currentDateTime.toLocalDate().minusDays(1).atTime(startTime);
                endDateTime = currentDateTime.toLocalDate().atTime(endTime);

                // check if yesterday was an active day
                valid = dayOfWeeks.contains(currentDOW.minus(1));
            } else {
                // current time is on first day
                // set the start date to today and the end date to tomorrow
                startDateTime = currentDateTime.toLocalDate().atTime(startTime);
                endDateTime = currentDateTime.toLocalDate().plusDays(1).atTime(endTime);

                // check if today is active day
                valid = dayOfWeeks.contains(currentDOW);
            }
        } else {
            // set start date and end date to today.
            startDateTime = currentDateTime.toLocalDate().atTime(startTime);
            endDateTime = currentDateTime.toLocalDate().atTime(endTime);

            // check if today is active date
            valid = dayOfWeeks.contains(currentDOW);
        }

        return valid && currentDateTime.isAfter(startDateTime) && currentDateTime.isBefore(endDateTime);
    }
}

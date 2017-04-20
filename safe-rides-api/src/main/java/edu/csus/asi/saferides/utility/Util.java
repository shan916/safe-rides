package edu.csus.asi.saferides.utility;

import edu.csus.asi.saferides.model.Configuration;
import edu.csus.asi.saferides.model.RideRequest;
import edu.csus.asi.saferides.security.model.Authority;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.time.*;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Provides common utility functions used in the application
 */
public class Util {

    /**
     * Concatenate address parts together
     *
     * @param line1 address line 1
     * @param line2 address line 2 (if null, ignored)
     * @param city  city
     * @return concatenated address
     */
    public static String formatAddress(String line1, String line2, String city) {
        if (line2 == null || line2.length() == 0) {
            return String.format("%s, %s, %s", line1, city, "CA");
        } else {
            return String.format("%s %s, %s, %s", line1, line2, city, "CA");
        }
    }

    /**
     * Helper method to map list of Authority to list of GrantedAuthority
     *
     * @param authorities to map to GrantedAuthority-ies
     * @return list of GrantedAuthority
     */
    public static List<GrantedAuthority> mapToGrantedAuthorities(List<Authority> authorities) {
        return authorities.stream()
                .map(authority -> new SimpleGrantedAuthority(authority.getName().name()))
                .collect(Collectors.toList());
    }

    /**
     * Check if a current date & time is valid for requesting a ride
     *
     * @param currentDateTime the current date & time
     * @param startTime       the application's start time
     * @param endTime         the application's end time
     * @param dayOfWeeks      the application's active days
     * @return whether the date & time during operating hours
     */
    public static boolean validRideRequestDateTime(LocalDateTime currentDateTime, LocalTime startTime, LocalTime endTime, List<DayOfWeek> dayOfWeeks) {
        LocalDateTime[] localDateTimes = getRangeDateTime(currentDateTime, startTime, endTime);
        LocalDateTime startDateTime = localDateTimes[0];
        LocalDateTime endDateTime = localDateTimes[1];

        // returns true if the start day is in the list of dayofweeks the application is active
        // and the currentDateTime is between the startDateTime and endDateTime
        return dayOfWeeks.contains(startDateTime.getDayOfWeek()) && currentDateTime.isAfter(startDateTime) && currentDateTime.isBefore(endDateTime);
    }

    /**
     * Check if application is accepting new ride requests
     *
     * @return whether the current time is during operating hours
     * @throws IllegalStateException
     */
    public static boolean isAcceptingRideRequests(Configuration configuration) throws IllegalStateException {
        if (configuration == null) {
            throw new IllegalStateException("Configuration is missing");
        }

        // if manually set to inactive return false right away
        if (!configuration.isActive()) {
            return false;
        }

        LocalTime startTime = configuration.getStartTime();
        LocalTime endTime = configuration.getEndTime();
        List<DayOfWeek> dayOfWeeks = configuration.getDaysOfWeek();

        LocalDateTime currentDateTime = LocalDateTime.now();

        return validRideRequestDateTime(currentDateTime, startTime, endTime, dayOfWeeks);
    }

    /**
     * Calculate a start date time and an end date time
     *
     * @param currentDateTime the current date & time
     * @param startTime       the application's start time
     * @param endTime         the application's end time
     * @return a start and end datetime
     */
    public static LocalDateTime[] getRangeDateTime(LocalDateTime currentDateTime, LocalTime startTime, LocalTime endTime) {
        LocalTime currentTime = currentDateTime.toLocalTime();
        LocalDateTime startDateTime, endDateTime;

        // if the startTime and the endTime spans two days
        if (startTime.compareTo(endTime) > 0) { // startTime > endTime (meaning the start time is before midnight and the endTime is after midnight)
            if (currentTime.compareTo(startTime) < 0) { // current time is on the end day
                // set the start date to yesterday and the end date to today
                startDateTime = currentDateTime.toLocalDate().minusDays(1).atTime(startTime);
                endDateTime = currentDateTime.toLocalDate().atTime(endTime);
            } else { // current time is on start day
                // set the start date to today and the end date to tomorrow
                startDateTime = currentDateTime.toLocalDate().atTime(startTime);
                endDateTime = currentDateTime.toLocalDate().plusDays(1).atTime(endTime);
            }
        } else {    // start and end time are in the same day
            // set start date and end date to a recent date.
            // if current time is prior to start time set to prior day
            if (currentTime.compareTo(startTime) < 0) {
                startDateTime = currentDateTime.toLocalDate().minusDays(1).atTime(startTime);
                endDateTime = currentDateTime.toLocalDate().minusDays(1).atTime(endTime);
            } else {
                // set to today
                startDateTime = currentDateTime.toLocalDate().atTime(startTime);
                endDateTime = currentDateTime.toLocalDate().atTime(endTime);
            }
        }

        return new LocalDateTime[]{startDateTime, endDateTime};
    }

    /**
     * Filter old ride
     *
     * @param ride the ride to check if old
     * @return null if the ride is old. the ride if current
     * @throws IllegalStateException
     */
    public RideRequest filterPastRide(Configuration configuration, RideRequest ride) throws IllegalStateException {
        if (configuration != null) {
            LocalDateTime startDateTime = Util.getRangeDateTime(LocalDateTime.now(), configuration.getStartTime(), configuration.getEndTime())[0];
            if (ride.getRequestDate().after(Date.from(ZonedDateTime.of(startDateTime, ZoneId.systemDefault()).toInstant()))) {
                return ride;
            } else {
                throw new IllegalStateException("Configuration is missing");
            }
        }
        return null;
    }

    /**
     * Filter old rides
     *
     * @param rides the rides to check if old
     * @return null if all rides are old. the rides that are current
     * @throws IllegalStateException
     */
    public Iterable<RideRequest> filterPastRides(Configuration configuration, List<RideRequest> rides) throws IllegalStateException {
        if (configuration != null) {
            LocalDateTime startDateTime = Util.getRangeDateTime(LocalDateTime.now(), configuration.getStartTime(), configuration.getEndTime())[0];
            // this is part of the reason why to change to java.time.LocalDateTime rather than java.util.Date
            rides.removeIf(r -> r.getRequestDate().before(Date.from(ZonedDateTime.of(startDateTime, ZoneId.systemDefault()).toInstant())));
            return rides;
        } else {
            throw new IllegalStateException("Configuration is missing");
        }
    }
}

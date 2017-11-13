package edu.csus.asi.saferides.utility;

import edu.csus.asi.saferides.model.AuthorityName;
import edu.csus.asi.saferides.model.Configuration;
import edu.csus.asi.saferides.model.RideRequest;
import edu.csus.asi.saferides.model.RideRequestStatus;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Provides common utility functions used in the application
 */
public class Util {

    public static final String APPLICATION_TIME_ZONE = "America/Los_Angeles";

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
     * Helper method to map authority level to list of GrantedAuthority
     *
     * @param authorityLevel to map to GrantedAuthority-ies
     * @return list of GrantedAuthority
     */
    public static List<GrantedAuthority> mapToGrantedAuthorities(AuthorityName authorityLevel) {
        List<GrantedAuthority> authorities = new ArrayList<>();

        switch (authorityLevel) {
            case ROLE_RIDER:
                authorities.add(new SimpleGrantedAuthority("ROLE_RIDER"));
                break;
            case ROLE_DRIVER:
                authorities.add(new SimpleGrantedAuthority("ROLE_RIDER"));
                authorities.add(new SimpleGrantedAuthority("ROLE_DRIVER"));
                break;
            case ROLE_COORDINATOR:
                authorities.add(new SimpleGrantedAuthority("ROLE_RIDER"));
                authorities.add(new SimpleGrantedAuthority("ROLE_DRIVER"));
                authorities.add(new SimpleGrantedAuthority("ROLE_COORDINATOR"));
                break;
            case ROLE_ADMIN:
                authorities.add(new SimpleGrantedAuthority("ROLE_RIDER"));
                authorities.add(new SimpleGrantedAuthority("ROLE_DRIVER"));
                authorities.add(new SimpleGrantedAuthority("ROLE_COORDINATOR"));
                authorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
                break;
        }

        return authorities;
    }

    /**
     * Checks whether password meets security requirements
     *
     * @param password the password to check
     * @return true if password greater than or equal to 8 characters, false otherwise
     */
    public static boolean isPasswordValid(String password) {
        return (!StringUtils.isBlank(password) && StringUtils.length(password) >= 8);
    }

    /**
     * Check if a current datetime is valid for requesting a ride
     *
     * @param currentDateTime the current datetime
     * @param startTime       the application's start time
     * @param endTime         the application's end time
     * @param dayOfWeeks      the application's active days
     * @return whether the datetime during operating hours
     */
    private static boolean validRideRequestDateTime(LocalDateTime currentDateTime, LocalTime startTime, LocalTime endTime, List<DayOfWeek> dayOfWeeks) {
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
     * @param configuration the application configuration settings from the database
     * @return whether the current time is during operating hours
     * @throws IllegalStateException thrown when configuration is null
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

        LocalDateTime currentDateTime = LocalDateTime.now(ZoneId.of(Util.APPLICATION_TIME_ZONE));

        return validRideRequestDateTime(currentDateTime, startTime, endTime, dayOfWeeks);
    }

    /**
     * Calculate a start datetime and an end datetime
     *
     * @param currentDateTime the current datetime
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
     * Filter old ride. 'Current' rides are any rides that were requested after the start time of the current day
     * even if the current day is not selected to be an active Safe Rides day
     *
     * @param ride          the ride to check if old
     * @param configuration the application configuration settings from the database
     * @return null if the ride is old. the ride if current
     * @throws IllegalStateException thrown if configuration is null
     */
    public static RideRequest filterPastRide(Configuration configuration, RideRequest ride) throws IllegalStateException {
        if (ride == null) {
            return null;
        }
        if (configuration != null) {
            LocalDateTime startDateTime = Util.getRangeDateTime(LocalDateTime.now(ZoneId.of(Util.APPLICATION_TIME_ZONE)), configuration.getStartTime(), configuration.getEndTime())[0];
            if (ride.getRequestDate().compareTo(startDateTime) >= 0) {
                return ride;
            } else {
                return null;
            }
        } else {
            throw new IllegalStateException("Configuration is missing");
        }
    }

    /**
     * Filter old rides. 'Current' rides are any rides that were requested after the start time of the current day
     * even if the current day is not selected to be an active Safe Rides day
     *
     * @param rides         the rides to check if old
     * @param configuration the application configuration settings from the database
     * @return null if all rides are old. the rides that are current
     * @throws IllegalStateException thrown if configuration is null
     */
    public static Collection<RideRequest> filterPastRides(Configuration configuration, Collection<RideRequest> rides) throws IllegalStateException {
        if (rides.size() == 0) {
            return null;
        }
        if (configuration != null) {
            LocalDateTime startDateTime = Util.getRangeDateTime(LocalDateTime.now(ZoneId.of(Util.APPLICATION_TIME_ZONE)), configuration.getStartTime(), configuration.getEndTime())[0];
            rides.removeIf(r -> r.getRequestDate().compareTo(startDateTime) < 0);
            return rides;
        } else {
            throw new IllegalStateException("Configuration is missing");
        }
    }

    /**
     * Calculate the distance between two coordinates
     *
     * @param lat1 latitude 1
     * @param lon1 longitude 1
     * @param lat2 latitude 2
     * @param lon2 longitude 2
     * @return distance in meters
     */
    private static double calculateDistance(double lat1, double lon1, double lat2, double lon2) {
        // http://www.movable-type.co.uk/scripts/latlong.html
        // https://stackoverflow.com/a/27237104
        double x = Math.toRadians(lon2 - lon1) * Math.cos(Math.toRadians(lat1 + lat2) / 2);
        double y = Math.toRadians(lat2 - lat1);
        return Math.sqrt(x * x + y * y) * 6371e3;
    }

    /**
     * Calculate the distance between a set of coordinates
     *
     * @param latitudes  set of latitudes
     * @param longitudes set of longitudes
     * @return distance in miles
     */
    public static double calculateDistance(double[] latitudes, double[] longitudes) {
        double total = 0;
        for (int i = 0; i < latitudes.length - 1; i++) {
            total += calculateDistance(latitudes[i], longitudes[i], latitudes[i + 1], longitudes[i + 1]);
        }
        return total / 1609.344;
    }

    /**
     * Check if a ride is completed by the status
     *
     * @param status the ride status
     * @return complete
     */
    public static boolean rideComplete(RideRequestStatus status) {
        return (status == RideRequestStatus.CANCELEDBYCOORDINATOR
                || status == RideRequestStatus.CANCELEDBYRIDER
                || status == RideRequestStatus.CANCELEDOTHER
                || status == RideRequestStatus.COMPLETE);
    }
}

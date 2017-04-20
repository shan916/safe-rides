package edu.csus.asi.saferides.service;

import edu.csus.asi.saferides.model.Configuration;
import edu.csus.asi.saferides.model.RideRequest;
import edu.csus.asi.saferides.repository.ConfigurationRepository;
import edu.csus.asi.saferides.utility.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.List;

/**
 * RideRequest Service
 */
@Service
public class RideRequestService {
    @Autowired
    ConfigurationRepository configurationRepository;

    /**
     * Filter old ride
     *
     * @param ride the ride to check if old
     * @return null if the ride is old. the ride if current
     * @throws IllegalStateException
     */
    public RideRequest filterPastRide(RideRequest ride) throws IllegalStateException {
        Configuration configuration = configurationRepository.findOne(1);
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
    public Iterable<RideRequest> filterPastRides(List<RideRequest> rides) throws IllegalStateException {
        Configuration configuration = configurationRepository.findOne(1);
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

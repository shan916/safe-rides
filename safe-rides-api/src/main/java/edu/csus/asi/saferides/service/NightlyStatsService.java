package edu.csus.asi.saferides.service;

import edu.csus.asi.saferides.model.Configuration;
import edu.csus.asi.saferides.model.NightlyStats;
import edu.csus.asi.saferides.model.RideRequest;
import edu.csus.asi.saferides.model.RideRequestStatus;
import edu.csus.asi.saferides.repository.ConfigurationRepository;
import edu.csus.asi.saferides.repository.NightlyStatsRepository;
import edu.csus.asi.saferides.repository.RideRequestRepository;
import edu.csus.asi.saferides.utility.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.OptionalDouble;
import java.util.stream.DoubleStream;

/**
 * Provides methods containing logic for CRUD operations for Users
 */
@Service
public class NightlyStatsService {

    private final NightlyStatsRepository nightlyStatsRepository;
    private final RideRequestRepository rideRequestRepository;
    private final ConfigurationRepository configurationRepository;

    /**
     * Dependency injection
     *
     * @param nightlyStatsRepository  Nightly Stats Repository
     * @param rideRequestRepository   Ride Request Repository
     * @param configurationRepository Configuration Repository
     */
    @Autowired
    public NightlyStatsService(NightlyStatsRepository nightlyStatsRepository, RideRequestRepository rideRequestRepository, ConfigurationRepository configurationRepository) {
        this.nightlyStatsRepository = nightlyStatsRepository;
        this.rideRequestRepository = rideRequestRepository;
        this.configurationRepository = configurationRepository;
    }

    public NightlyStats aggregateStats() {
        // get config
        Configuration configuration = configurationRepository.findOne(1);

        // don't aggregate stats if the night is still in progress
        if (Util.isAcceptingRideRequests(configuration)) {
            return null;
        }

        // get current time
        LocalDateTime now = LocalDateTime.now(ZoneId.of(Util.APPLICATION_TIME_ZONE));

        // get the start date of the night
        LocalDateTime start = Util.getRangeDateTime(now, configuration.getStartTime(), configuration.getEndTime())[0];
        LocalDate aggregationDate = start.toLocalDate();

        // check if stats are not already present. If already present return them
        NightlyStats stats = nightlyStatsRepository.findByDateAggregated(aggregationDate);
        if (stats == null) {
            stats = new NightlyStats();
        } else {
            return stats;
        }

        // get all rides
        Collection<RideRequest> rides = (Collection<RideRequest>) rideRequestRepository.findAll();

        // get only the rides for the past/current night
        rides = Util.filterPastRides(configuration, rides);

        // make sure that there are rides to aggregate
        if (rides != null) {
            // initialize stats
            long requestsFulfilled = 0;
            long requestsCanceled = 0;
            long riders = 0;
            List<Double> distances = new ArrayList<>();
            List<Double> timesToAssignment = new ArrayList<>();
            List<Double> timesToFulfillment = new ArrayList<>();

            // loop rides (ensure that all rides are completed)
            for (RideRequest ride : rides) {
                if (Util.rideComplete(ride.getStatus())) {
                    // fulfilled
                    if (ride.getStatus() == RideRequestStatus.COMPLETE) {
                        requestsFulfilled++;
                        if (ride.getLastModified() != null) {
                            timesToFulfillment.add((double) (ChronoUnit.MINUTES.between(ride.getRequestDate(), ride.getLastModified())));
                        }
                    } else if (ride.getStatus() == RideRequestStatus.CANCELEDBYCOORDINATOR || ride.getStatus() == RideRequestStatus.CANCELEDBYRIDER
                            || ride.getStatus() == RideRequestStatus.CANCELEDOTHER) { // canceled
                        requestsCanceled++;
                    }
                    riders += ride.getNumPassengers();
                    distances.add((double) (ride.getEndOdometer() - ride.getStartOdometer()));
                    if (ride.getAssignedDate() != null) {
                        timesToAssignment.add((double) (ChronoUnit.MINUTES.between(ride.getRequestDate(), ride.getAssignedDate())));
                    }
                } else {
                    // don't aggregate if there is a ride that is not completed
                    return null;
                }
            }

            // sort arrays
            DoubleStream sortedDistances0 = distances.stream().mapToDouble(Double::byteValue).sorted();
            DoubleStream sortedFulfillmentTime0 = timesToFulfillment.stream().mapToDouble(Double::byteValue).sorted();
            DoubleStream sortedAssignmentTime0 = timesToAssignment.stream().mapToDouble(Double::byteValue).sorted();
            DoubleStream sortedDistances1 = distances.stream().mapToDouble(Double::byteValue).sorted();
            DoubleStream sortedFulfillmentTime1 = timesToFulfillment.stream().mapToDouble(Double::byteValue).sorted();
            DoubleStream sortedAssignmentTime1 = timesToAssignment.stream().mapToDouble(Double::byteValue).sorted();
            DoubleStream sortedDistances2 = distances.stream().mapToDouble(Double::byteValue).sorted();
            DoubleStream sortedFulfillmentTime2 = timesToFulfillment.stream().mapToDouble(Double::byteValue).sorted();
            DoubleStream sortedAssignmentTime2 = timesToAssignment.stream().mapToDouble(Double::byteValue).sorted();
            DoubleStream sortedDistances3 = distances.stream().mapToDouble(Double::byteValue).sorted();
            DoubleStream sortedFulfillmentTime3 = timesToFulfillment.stream().mapToDouble(Double::byteValue).sorted();
            DoubleStream sortedAssignmentTime3 = timesToAssignment.stream().mapToDouble(Double::byteValue).sorted();


            // process data
            OptionalDouble fastestAssignmentTime = sortedAssignmentTime0.min();
            OptionalDouble shortestDistance = sortedDistances0.min();
            OptionalDouble fastestFulfillmentTime = sortedFulfillmentTime0.min();

            OptionalDouble slowestAssignmentTime = sortedAssignmentTime1.max();
            OptionalDouble longestDistance = sortedDistances1.max();
            OptionalDouble slowestFulfillmentTime = sortedFulfillmentTime1.max();

            OptionalDouble averageAssignmentTime = sortedAssignmentTime2.average();
            OptionalDouble averageDistance = sortedDistances2.average();
            OptionalDouble averageFulfillmentTime = sortedFulfillmentTime2.average();

            OptionalDouble medianAssignmentTime = timesToAssignment.size() % 2 == 0
                    ? sortedAssignmentTime3.skip(distances.size() / 2 - 1).limit(2).average()
                    : sortedAssignmentTime3.skip(distances.size() / 2).findFirst();
            OptionalDouble medianDistance = distances.size() % 2 == 0
                    ? sortedDistances3.skip(distances.size() / 2 - 1).limit(2).average()
                    : sortedDistances3.skip(distances.size() / 2).findFirst();
            OptionalDouble medianFulfillmentTime = timesToFulfillment.size() % 2 == 0
                    ? sortedFulfillmentTime3.skip(distances.size() / 2 - 1).limit(2).average()
                    : sortedFulfillmentTime3.skip(distances.size() / 2).findFirst();

            if (fastestAssignmentTime.isPresent()) {
                stats.setFastestTimeToAssignment(fastestAssignmentTime.getAsDouble());
            }
            if (slowestAssignmentTime.isPresent()) {
                stats.setSlowestTimeToAssignment(slowestAssignmentTime.getAsDouble());
            }
            if (averageAssignmentTime.isPresent()) {
                stats.setAverageTimeToAssignment(averageAssignmentTime.getAsDouble());
            }
            if (medianAssignmentTime.isPresent()) {
                stats.setMedianTimeToAssignment(medianAssignmentTime.getAsDouble());
            }

            if (shortestDistance.isPresent()) {
                stats.setShortestDistance(shortestDistance.getAsDouble());
            }
            if (longestDistance.isPresent()) {
                stats.setLongestDistance(longestDistance.getAsDouble());
            }
            if (averageDistance.isPresent()) {
                stats.setAverageDistance(averageDistance.getAsDouble());
            }
            if (medianDistance.isPresent()) {
                stats.setMedianDistance(medianDistance.getAsDouble());
            }

            if (fastestFulfillmentTime.isPresent()) {
                stats.setFastestFulfillmentTime(fastestFulfillmentTime.getAsDouble());

            }
            if (slowestFulfillmentTime.isPresent()) {
                stats.setSlowestFulfillmentTime(slowestFulfillmentTime.getAsDouble());
            }
            if (averageFulfillmentTime.isPresent()) {
                stats.setAverageFulfillmentTime(averageFulfillmentTime.getAsDouble());
            }
            if (medianFulfillmentTime.isPresent()) {
                stats.setMedianFulfillmentTime(medianFulfillmentTime.getAsDouble());
            }

            stats.setDateAggregated(aggregationDate);
            stats.setRequestsCanceled(requestsCanceled);
            stats.setRequestsFulfilled(requestsFulfilled);
            stats.setRiders(riders);

            // save and return
            return nightlyStatsRepository.save(stats);
        }

        return null;
    }
}

package edu.csus.asi.saferides.controller;

import edu.csus.asi.saferides.model.Configuration;
import edu.csus.asi.saferides.model.NightlyStats;
import edu.csus.asi.saferides.model.ResponseMessage;
import edu.csus.asi.saferides.model.RideRequest;
import edu.csus.asi.saferides.repository.ConfigurationRepository;
import edu.csus.asi.saferides.repository.DriverRepository;
import edu.csus.asi.saferides.repository.NightlyStatsRepository;
import edu.csus.asi.saferides.repository.RideRequestRepository;
import edu.csus.asi.saferides.service.NightlyStatsService;
import edu.csus.asi.saferides.utility.Util;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Collection;

/**
 * API controller for authenticating with CAS
 */
@RestController
@PreAuthorize("hasRole('COORDINATOR')")
@CrossOrigin(origins = {"http://localhost:9000", "https://codeteam6.io"})
@RequestMapping("/reports")
class ReportsController {

    private final NightlyStatsRepository nightlyStatsRepository;
    private final DriverRepository driverRepository;
    private final RideRequestRepository rideRequestRepository;
    private final ConfigurationRepository configurationRepository;
    private final NightlyStatsService nightlyStatsService;

    /**
     * Reports controller constructor with dependency injection
     *
     * @param nightlyStatsRepository  Nightly Stats Repository
     * @param driverRepository        Driver Repository
     * @param rideRequestRepository   Ride Request Repository
     * @param configurationRepository Configuration Repository
     * @param nightlyStatsService     Nightly Stats Service
     */
    @Autowired
    public ReportsController(NightlyStatsRepository nightlyStatsRepository, DriverRepository driverRepository,
                             RideRequestRepository rideRequestRepository, ConfigurationRepository configurationRepository,
                             NightlyStatsService nightlyStatsService) {
        this.nightlyStatsRepository = nightlyStatsRepository;
        this.driverRepository = driverRepository;
        this.rideRequestRepository = rideRequestRepository;
        this.configurationRepository = configurationRepository;
        this.nightlyStatsService = nightlyStatsService;
    }

    /**
     * Get a report of the nightly stats before a date, after a date, between a date range, or all stats
     *
     * @param beginDate Stats after this day. optional, exclusive
     * @param endDate   Stats before this day. optional, exclusive
     * @return stats breakdown on a per "night" basis.
     */
    @RequestMapping(value = "", method = RequestMethod.GET)
    @ApiOperation(value = "getReports", nickname = "getReports", notes = "Get a report of the nightly stats of Safe Rides. Date variables are exclusive.")
    public ResponseEntity<Iterable<NightlyStats>> getReports(@RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate beginDate, @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        if (beginDate == null && endDate == null) {
            return ResponseEntity.ok(nightlyStatsRepository.findAll());
        } else if (beginDate == null) {
            return ResponseEntity.ok(nightlyStatsRepository.findByDateAggregatedBefore(endDate));
        } else if (endDate == null) {
            return ResponseEntity.ok(nightlyStatsRepository.findByDateAggregatedAfter(beginDate));
        } else {
            return ResponseEntity.ok(nightlyStatsRepository.findByDateAggregatedBetween(beginDate, endDate));
        }
    }

    /**
     * Trigger aggregation to start. Will delete any ride requests older than 10 days and any drivers that weren't updated in the past 4 years.
     *
     * @return stats breakdown for the night
     */
    @RequestMapping(value = "", method = RequestMethod.POST)
    @ApiOperation(value = "aggregateStats", nickname = "aggregateStats", notes = "Get all data for the night and aggregate to a nightly statistic")
    public ResponseEntity<?> aggregateStats() {
        // check if report can be run
        // all drivers are deactivated
        if (driverRepository.countByUser_Active(true) > 0) {
            return ResponseEntity.badRequest().body(new ResponseMessage("Cannot aggregate data yet because there is at least one active driver."));
        }

        // all rides are in their final states (completed or canceled)
        Configuration configuration = configurationRepository.findOne(1);
        Collection<RideRequest> rides = (Collection<RideRequest>) rideRequestRepository.findAll();

        // get only the rides for the past/current night
        rides = Util.filterPastRides(configuration, rides);

        // make sure that there are rides to aggregate
        if (rides != null) {
            for (RideRequest ride : rides) {
                if (!Util.rideComplete(ride.getStatus())) {
                    return ResponseEntity.badRequest().body(new ResponseMessage("Cannot aggregate data yet because there is at least one ride request that is still active."));
                }
            }
        }
        // not currently accepting rides
        if (Util.isAcceptingRideRequests(configuration)) {
            return ResponseEntity.badRequest().body(new ResponseMessage("Cannot aggregate data yet because the Safe Rides is currently accepting ride requests."));
        }

        // aggregate!
        NightlyStats stats = nightlyStatsService.aggregateStats();

        // prune older records
        // 10 day old ride requests records
        LocalDate now = LocalDate.now(ZoneId.of(Util.APPLICATION_TIME_ZONE));
        LocalDate rideRequestRetention = now.minusDays(10);
        rideRequestRepository.deleteByRequestDateBefore(rideRequestRetention.atStartOfDay());

        // 4 year old driver records
        LocalDate inactiveDriverRetention = now.minusYears(4);
        driverRepository.deleteByModifiedDateBeforeAndUser_Active(inactiveDriverRetention.atStartOfDay(), false);

        if (stats == null) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.ok(stats);
        }
    }
}

package edu.csus.asi.saferides.controller;

import edu.csus.asi.saferides.repository.NightlyStatsRepository;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

/**
 * API controller for authenticating with CAS
 */
@RestController
@PreAuthorize("hasRole('COORDINATOR')")
@CrossOrigin(origins = {"http://localhost:9000", "https://codeteam6.io"})
@RequestMapping("/reports")
public class ReportsController {

    @Autowired
    NightlyStatsRepository nightlyStatsRepository;

    /**
     * Get a report of the nightly stats before a date, after a date, between a date range, or all stats
     *
     * @param beginDate Stats after this day. optional, exclusive
     * @param endDate   Stats before this day. optional, exclusive
     * @return stats breakdown on a per "night" basis.
     */
    @RequestMapping(value = "", method = RequestMethod.GET)
    @ApiOperation(value = "getReports", nickname = "getReports", notes = "Get a report of the nightly stats of Safe Rides. Date variables are exclusive.")
    public ResponseEntity<?> getReports(@RequestParam(required = false) LocalDate beginDate, @RequestParam(required = false) LocalDate endDate) {
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
}

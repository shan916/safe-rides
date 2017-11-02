package edu.csus.asi.saferides.repository;

import edu.csus.asi.saferides.model.NightlyStats;
import org.springframework.data.repository.CrudRepository;

import java.time.LocalDate;
import java.util.List;

/**
 * Datastore interface for nightly reports. Stores aggregated stats for each night that is run at the end of the night
 */
public interface NightlyStatsRepository extends CrudRepository<NightlyStats, Integer> {

    List<NightlyStats> findByDateAggregatedBetween(LocalDate begin, LocalDate end);

    List<NightlyStats> findByDateAggregatedAfter(LocalDate begin);

    List<NightlyStats> findByDateAggregatedBefore(LocalDate end);

    NightlyStats findByDateAggregated(LocalDate end);
}

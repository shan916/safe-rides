package edu.csus.asi.saferides.model.dto;

import java.util.Set;

/**
 * The driver's end-of-night summary
 */
public class EndOfNightSummaryDto {

    /**
     * The distance the driver drove by odometer reportings in miles
     */
    private long distanceDrivenOdometer;

    /**
     * The distance the driver drove by system's gps recordings in miles
     */
    private double distanceDrivenSystem;

    /**
     * The list of the rides that was assigned to the driver
     */
    private Set<EndOfNightSummaryRideDto> rides;

    public long getDistanceDrivenOdometer() {
        return distanceDrivenOdometer;
    }

    public void setDistanceDrivenOdometer(long distanceDrivenOdometer) {
        this.distanceDrivenOdometer = distanceDrivenOdometer;
    }

    public double getDistanceDrivenSystem() {
        return distanceDrivenSystem;
    }

    public void setDistanceDrivenSystem(double distanceDrivenSystem) {
        this.distanceDrivenSystem = distanceDrivenSystem;
    }

    public Set<EndOfNightSummaryRideDto> getRides() {
        return rides;
    }

    public void setRides(Set<EndOfNightSummaryRideDto> rides) {
        this.rides = rides;
    }
}

package edu.csus.asi.saferides.model.dto;

import java.time.LocalDateTime;

/**
 * A breakdown of the driver's end-of-night summary on a per-ride basis
 */
public class EndOfNightSummaryRideDto {
    /**
     * The time the ride request was assigned to the driver
     */
    private LocalDateTime rideAssigned;

    /**
     * The time the ride request was completed by the driver
     */
    private LocalDateTime rideCompleted;

    /**
     * The recorded odometer reading at the beginning of the ride request in miles
     */
    private long startOdometer;

    /**
     * The recorded odometer reading at the end of the ride request in miles
     */
    private long endOdometer;

    /**
     * The distance traveled from driver recordings in miles
     */
    private long odometerDistance;

    /**
     * The distance the system calculated in miles
     */
    private double recordedDistance;

    public LocalDateTime getRideAssigned() {
        return rideAssigned;
    }

    public void setRideAssigned(LocalDateTime rideAssigned) {
        this.rideAssigned = rideAssigned;
    }

    public LocalDateTime getRideCompleted() {
        return rideCompleted;
    }

    public void setRideCompleted(LocalDateTime rideCompleted) {
        this.rideCompleted = rideCompleted;
    }

    public long getStartOdometer() {
        return startOdometer;
    }

    public void setStartOdometer(long startOdometer) {
        this.startOdometer = startOdometer;
    }

    public long getEndOdometer() {
        return endOdometer;
    }

    public void setEndOdometer(long endOdometer) {
        this.endOdometer = endOdometer;
    }

    public long getOdometerDistance() {
        return odometerDistance;
    }

    public void setOdometerDistance(long odometerDistance) {
        this.odometerDistance = odometerDistance;
    }

    public double getRecordedDistance() {
        return recordedDistance;
    }

    public void setRecordedDistance(double recordedDistance) {
        this.recordedDistance = recordedDistance;
    }
}

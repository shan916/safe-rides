package edu.csus.asi.saferides.model.dto;

import io.swagger.annotations.ApiModelProperty;

import java.time.LocalDateTime;

/**
 * A breakdown of the driver's end-of-night summary on a per-ride basis
 */
public class EndOfNightSummaryRideDto {
    @ApiModelProperty(value = "The time the ride request was assigned to the driver")
    private LocalDateTime rideAssigned;

    @ApiModelProperty(value = "The time the ride request was completed by the driver")
    private LocalDateTime rideCompleted;

    @ApiModelProperty(value = "The recorded odometer reading at the beginning of the ride request in miles")
    private long startOdometer;

    @ApiModelProperty(value = "The recorded odometer reading at the end of the ride request in miles")
    private long endOdometer;

    @ApiModelProperty(value = "The distance traveled from driver recordings in miles")
    private long odometerDistance;

    @ApiModelProperty(value = "The distance the system calculated in miles")
    private double recordedDistance;

    /**
     * Get the time the ride was assigned
     *
     * @return time the ride was assigned
     */
    public LocalDateTime getRideAssigned() {
        return rideAssigned;
    }

    /**
     * Set the time the ride was assigned
     *
     * @param rideAssigned time the ride was assigned
     */
    public void setRideAssigned(LocalDateTime rideAssigned) {
        this.rideAssigned = rideAssigned;
    }

    /**
     * Get the time the ride was completed
     *
     * @return time the ride was completed
     */
    public LocalDateTime getRideCompleted() {
        return rideCompleted;
    }

    /**
     * Set the time the ride was completed
     *
     * @param rideCompleted time the ride was completed
     */
    public void setRideCompleted(LocalDateTime rideCompleted) {
        this.rideCompleted = rideCompleted;
    }

    /**
     * Get the start odometer reading
     *
     * @return start odometer reading
     */
    public long getStartOdometer() {
        return startOdometer;
    }

    /**
     * Set the start odometer reading
     *
     * @param startOdometer start odometer reading
     */
    public void setStartOdometer(long startOdometer) {
        this.startOdometer = startOdometer;
    }

    /**
     * Get the end odometer reading
     *
     * @return end odometer reading
     */
    public long getEndOdometer() {
        return endOdometer;
    }

    /**
     * Set the end odometer reading
     *
     * @param endOdometer end odometer reading
     */
    public void setEndOdometer(long endOdometer) {
        this.endOdometer = endOdometer;
    }

    /**
     * Get the trip distance via odometer readings
     *
     * @return trip distance via odometer readings
     */
    public long getOdometerDistance() {
        return odometerDistance;
    }

    /**
     * Set the trip distance via odometer readings
     *
     * @param odometerDistance trip distance via odometer readings
     */
    public void setOdometerDistance(long odometerDistance) {
        this.odometerDistance = odometerDistance;
    }

    /**
     * Get the trip distance via gps readings
     *
     * @return trip distance via gps readings
     */
    public double getRecordedDistance() {
        return recordedDistance;
    }

    /**
     * Set the trip distance via gps readings
     *
     * @param recordedDistance trip distance via gps readings
     */
    public void setRecordedDistance(double recordedDistance) {
        this.recordedDistance = recordedDistance;
    }
}

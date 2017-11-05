package edu.csus.asi.saferides.model.dto;

import io.swagger.annotations.ApiModelProperty;

import java.util.Set;

/**
 * The driver's end-of-night summary
 */
public class EndOfNightSummaryDto {

    @ApiModelProperty(value = "The distance the driver drove by odometer reportings in miles")
    private long distanceDrivenOdometer;

    @ApiModelProperty(value = "The distance the driver drove by system's gps recordings in miles")
    private double distanceDrivenSystem;

    @ApiModelProperty(value = "The list of the rides that was assigned to the driver")
    private Set<EndOfNightSummaryRideDto> rides;

    /**
     * Get the distance driven via odometer readings
     *
     * @return distance driven via odometer readings
     */
    public long getDistanceDrivenOdometer() {
        return distanceDrivenOdometer;
    }

    /**
     * Set the distance driven via odometer readings
     *
     * @param distanceDrivenOdometer the distance driven via odometer readings
     */
    public void setDistanceDrivenOdometer(long distanceDrivenOdometer) {
        this.distanceDrivenOdometer = distanceDrivenOdometer;
    }

    /**
     * Get the distance driven via system gps calculations
     *
     * @return distance driven via system gps calculations
     */
    public double getDistanceDrivenSystem() {
        return distanceDrivenSystem;
    }

    /**
     * Set the distance driven via system gps calculations
     *
     * @param distanceDrivenSystem distance driven via system gps calculations
     */
    public void setDistanceDrivenSystem(double distanceDrivenSystem) {
        this.distanceDrivenSystem = distanceDrivenSystem;
    }

    /**
     * Get the mileage breakdown per ride request
     *
     * @return the mileage breakdown per ride request
     */
    public Set<EndOfNightSummaryRideDto> getRides() {
        return rides;
    }

    /**
     * Set the mileage breakdown per ride request
     *
     * @param rides the mileage breakdown per ride request
     */
    public void setRides(Set<EndOfNightSummaryRideDto> rides) {
        this.rides = rides;
    }
}

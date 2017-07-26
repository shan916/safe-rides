package edu.csus.asi.saferides.model.dto;

import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;

/**
 * Data transfer object for the Driver Location object
 */
public class DriverLocationDto {
    /**
     * The latitude of the driver location
     */
    @NotNull
    @DecimalMin("-90.0")
    @DecimalMax("90.0")
    private Double latitude;

    /**
     * The longitude of the driver location
     */
    @NotNull
    @DecimalMin("-180.0")
    @DecimalMax("180.0")
    private Double longitude;

    /**
     * Get the latitude
     *
     * @return the driver's latitude
     */
    public Double getLatitude() {
        return latitude;
    }

    /**
     * Set the latitude
     *
     * @param latitude of the driver
     */
    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    /**
     * Get the Longitude
     *
     * @return the driver's longitude
     */
    public Double getLongitude() {
        return longitude;
    }

    /**
     * Set the longitude
     *
     * @param longitude of the driver
     */
    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }
}

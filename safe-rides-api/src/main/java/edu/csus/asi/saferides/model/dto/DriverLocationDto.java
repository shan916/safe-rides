package edu.csus.asi.saferides.model.dto;

import io.swagger.annotations.ApiModelProperty;

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
    @NotNull(message = "latitude cannot be null.")
    @DecimalMin(value = "-90.0", message = "A valid latitude value cannot be less than -90 degrees")
    @DecimalMax(value = "90.0", message = "A valid latitude value cannot be more than 90 degrees")
    @ApiModelProperty(value = "The latitude of the driver's current location", required = true)
    private Double latitude;

    /**
     * The longitude of the driver location
     */
    @NotNull(message = "longitude cannot be null.")
    @DecimalMin(value = "-180.0", message = "A valid longitude value cannot be less than -180 degrees")
    @DecimalMax(value = "180.0", message = "A valid longitude value cannot be more than 180 degrees")
    @ApiModelProperty(value = "The longitude of the driver's current location", required = true)
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

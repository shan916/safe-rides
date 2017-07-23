package edu.csus.asi.saferides.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import edu.csus.asi.saferides.model.Driver;
import edu.csus.asi.saferides.model.RideRequestStatus;
import edu.csus.asi.saferides.serialization.LocalDateTimeSerializer;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

/**
 * Data Transfer Object for RideRequest.
 */
public class RideRequestDto {

    @ApiModelProperty(value = "The id of the ride request", readOnly = true)
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long id;

    @ApiModelProperty(value = "The driver assigned to the ride")
    private Driver driver;

    @ApiModelProperty(value = "The ride requestor's 9 digit One Card ID")
    @NotNull(message = "oneCardId must not be null")
    @Size(min = 9, max = 9, message = "oneCardId must be 9 digits")
    private String oneCardId;

    @ApiModelProperty(value = "The date the ride was requested", readOnly = true)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    private LocalDateTime requestDate;

    @ApiModelProperty(value = "The date the ride request was last modified", readOnly = true)
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private LocalDateTime lastModified;

    @ApiModelProperty(value = "The date the ride was assigned to a driver", readOnly = true)
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private LocalDateTime assignedDate;

    @ApiModelProperty(value = "The ride requestor's first name")
    @NotNull(message = "requestorFirstName must not be null")
    @Size(min = 1, max = 30, message = "requestorFirstName must be between 1 and 30 characters")
    private String requestorFirstName;

    @ApiModelProperty(value = "The ride requestor's last name")
    @NotNull(message = "requestorLastName must not be null")
    @Size(min = 1, max = 30, message = "requestorLastName must be between 1 and 30 characters")
    private String requestorLastName;

    @ApiModelProperty(value = "The ride requestor's 10 digit phone number", example = "9161234567")
    @NotNull(message = "requestorPhoneNumber must not be null")
    @Size(min = 10, max = 10, message = "requestorPhoneNumber must be 10 digits")
    private String requestorPhoneNumber;

    @ApiModelProperty(value = "The number of passengers including the ride requestor", allowableValues = "range[1, 3]")
    @NotNull(message = "numPassengers must not be null")
    @Min(value = 1, message = "numPassengers must be greater than or equal to 1")
    @Max(value = 3, message = "numPassenger must be less than or equal to 3")
    private Integer numPassengers;

    // TODO: min, max values?
    @ApiModelProperty(value = "The odometer reading of the driver's vehicle at the beginning of the ride")
    private Integer startOdometer;

    // TODO: min, max values?
    @ApiModelProperty(value = "The odometer reading of the driver's vehicle at the time of ride completion")
    private Integer endOdometer;

    @ApiModelProperty(value = "Line 1 of rider's pickup address")
    @NotNull(message = "pickupLine1 must not be null")
    // TODO: min, max size?
    private String pickupLine1;

    @ApiModelProperty(value = "Line 2 of rider's pickup address")
    private String pickupLine2;

    @ApiModelProperty(value = "City of rider's pickup location")
    @NotNull(message = "pickupCity must not be null")
    // TODO: min, max size?
    private String pickupCity;

    @ApiModelProperty(value = "Line 1 of rider's dropoff address")
    @NotNull(message = "dropoffLine1 must not be null")
    // TODO: min, max size?
    private String dropoffLine1;

    @ApiModelProperty(value = "Line 2 of rider's dropoff address")
    private String dropoffLine2;

    @ApiModelProperty(value = "City of rider's dropoff location")
    @NotNull(message = "dropOffCity must not be null")
    // TODO: min, max size?
    private String dropOffCity;

    private RideRequestStatus status;

    private String cancelMessage;

    private String messageToDriver;

    private String estimatedTime;

    private Double pickupLatitude;

    private Double pickupLongitude;

    private Double dropoffLatitude;

    private Double dropoffLongitude;

    /**
     * ================================================
     */

    /**
     * The first name of the driver assigned to the ride
     */
    private String driverName;

    /**
     * The color of the vehicle for the driver assigned to the rider
     */
    private String vehicleColor;

    /**
     * The year of the vehicle for the driver assigned to the rider
     */
    private String vehicleYear;

    /**
     * The make of the vehicle for the driver assigned to the rider
     */
    private String vehicleMake;

    /**
     * The model of the vehicle for the driver assigned to the rider
     */
    private String vehicleModel;

    /**
     * The license plate of the vehicle for the driver assigned to the rider
     */
    private String vehicleLicensePlate;

    /**
     * Gets the status of the ride request
     *
     * @return the status of the ride request
     */
    public RideRequestStatus getStatus() {
        return status;
    }

    /**
     * Sets the status of the ride request
     *
     * @param status that status of the ride request
     */
    public void setStatus(RideRequestStatus status) {
        this.status = status;
    }

    /**
     * Gets the estimated time for the driver to arrive at the pickup location
     *
     * @return the estimated time for the driver to arrive at the pickup location
     */
    public String getEstimatedTime() {
        return estimatedTime;
    }

    /**
     * Sets the estimated time for the driver to arrive at the pickup location
     *
     * @param estimatedTime the estimated time for the driver to arrive at the pickup location
     */
    public void setEstimatedTime(String estimatedTime) {
        this.estimatedTime = estimatedTime;
    }

    /**
     * Gets the first name of the driver assigned to the ride
     *
     * @return the first name of the driver assigned to the ride
     */
    public String getDriverName() {
        return driverName;
    }

    /**
     * Sets the first name of the driver assigned to the ride
     *
     * @param driverName the first name of the driver assigned to the ride
     */
    public void setDriverName(String driverName) {
        this.driverName = driverName;
    }

    /**
     * Gets the color of the vehicle for the driver assigned to the rider
     *
     * @return the color of the vehicle for the driver assigned to the rider
     */
    public String getVehicleColor() {
        return vehicleColor;
    }

    /**
     * Sets the color of the vehicle for the driver assigned to the rider
     *
     * @param vehicleColor the color of the vehicle for the driver assigned to the rider
     */
    public void setVehicleColor(String vehicleColor) {
        this.vehicleColor = vehicleColor;
    }

    /**
     * Gets the year of the vehicle for the driver assigned to the rider
     *
     * @return the year of the vehicle for the driver assigned to the rider
     */
    public String getVehicleYear() {
        return vehicleYear;
    }

    /**
     * Sets the year of the vehicle for the driver assigned to the rider
     *
     * @param vehicleYear the year of the vehicle for the driver assigned to the rider
     */
    public void setVehicleYear(String vehicleYear) {
        this.vehicleYear = vehicleYear;
    }

    /**
     * Gets the make of the vehicle for the driver assigned to the rider
     *
     * @return the make of the vehicle for the driver assigned to the rider
     */
    public String getVehicleMake() {
        return vehicleMake;
    }

    /**
     * Sets the make of the vehicle for the driver assigned to the rider
     *
     * @param vehicleMake the make of the vehicle for the driver assigned to the rider
     */
    public void setVehicleMake(String vehicleMake) {
        this.vehicleMake = vehicleMake;
    }

    /**
     * Gets the model of the vehicle for the driver assigned to the rider
     *
     * @return the model of the vehicle for the driver assigned to the rider
     */
    public String getVehicleModel() {
        return vehicleModel;
    }

    /**
     * Sets the model of the vehicle for the driver assigned to the rider
     *
     * @param vehicleModel the model of the vehicle for the driver assigned to the rider
     */
    public void setVehicleModel(String vehicleModel) {
        this.vehicleModel = vehicleModel;
    }

    /**
     * Gets the license plate of the vehicle for the driver assigned to the rider
     *
     * @return the license plate of the vehicle for the driver assigned to the rider
     */
    public String getVehicleLicensePlate() {
        return vehicleLicensePlate;
    }

    /**
     * Sets the license plate of the vehicle for the driver assigned to the rider
     *
     * @param vehicleLicensePlate the license plate of the vehicle for the driver assigned to the rider
     */
    public void setVehicleLicensePlate(String vehicleLicensePlate) {
        this.vehicleLicensePlate = vehicleLicensePlate;
    }

    /**
     * Gets the last modified timestamp for the ride
     *
     * @return the last modified timestamp for the ride
     */
    public LocalDateTime getLastModified() {
        return lastModified;
    }

    /**
     * Sets the last modified timestamp for the ride
     *
     * @param lastModified the last modified timestamp for the ride
     */
    public void setLastModified(LocalDateTime lastModified) {
        this.lastModified = lastModified;
    }

    /**
     * Gets the time the ride was assigned to a driver
     *
     * @return the time the ride was assigned to a driver
     */
    public LocalDateTime getAssignedDate() {
        return assignedDate;
    }

    /**
     * Sets the time the ride was assigned to a driver
     *
     * @param assignedDate the time the ride was assigned to a driver
     */
    public void setAssignedDate(LocalDateTime assignedDate) {
        this.assignedDate = assignedDate;
    }

    /**
     * Gets the string representation of the RideRequestDto object
     *
     * @return the string representation of the RideRequestDto object
     */
    @Override
    public String toString() {
        return "RideRequestDto{" + "status=" + status + ", estimatedTime='" + estimatedTime + '\''
                + ", lastModified=" + lastModified + ", assignedDate=" + assignedDate + ", driverName='" + driverName
                + '\'' + ", vehicleColor='" + vehicleColor + '\'' + ", vehicleYear='" + vehicleYear + '\''
                + ", vehicleMake='" + vehicleMake + '\'' + ", vehicleModel='" + vehicleModel + '\''
                + ", vehicleLicensePlate='" + vehicleLicensePlate + '\'' + '}';
    }
}

package edu.csus.asi.saferides.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import edu.csus.asi.saferides.model.RideRequestStatus;
import edu.csus.asi.saferides.model.views.JsonViews;
import edu.csus.asi.saferides.security.dto.UserDto;
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

//    private String driverName;
//    private String vehicleColor;
//    private String vehicleYear;
//    private String vehicleMake;
//    private String vehicleModel;
//    private String vehicleLicensePlate;

    @ApiModelProperty(value = "The id of the ride request", readOnly = true)
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @JsonView(JsonViews.Rider.class)
    private Long id;

    @ApiModelProperty(value = "The id of the driver assigned to the ride")
    @JsonView(JsonViews.Rider.class)
    private Long driverId;

    @ApiModelProperty(value = "The ride requestor's 9 digit One Card ID")
    @NotNull(message = "oneCardId must not be null")
    @Size(min = 9, max = 9, message = "oneCardId must be 9 digits")
    @JsonView(JsonViews.Rider.class)
    private String oneCardId;

    @ApiModelProperty(value = "The date the ride was requested", readOnly = true)
    @JsonView(JsonViews.Rider.class)
    @JsonDeserialize
    private LocalDateTime requestDate;

    @ApiModelProperty(value = "The date the ride request was last modified", readOnly = true)
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @JsonView(JsonViews.Rider.class)
    private LocalDateTime lastModified;

    @ApiModelProperty(value = "The date the ride was assigned to a driver", readOnly = true)
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @JsonView(JsonViews.Rider.class)
    private LocalDateTime assignedDate;

    @ApiModelProperty(value = "The ride requestor's first name")
    @NotNull(message = "requestorFirstName must not be null")
    @Size(min = 1, max = 30, message = "requestorFirstName must be between 1 and 30 characters")
    @JsonView(JsonViews.Driver.class)
    private String requestorFirstName;

    @ApiModelProperty(value = "The ride requestor's last name")
    @NotNull(message = "requestorLastName must not be null")
    @Size(min = 1, max = 30, message = "requestorLastName must be between 1 and 30 characters")
    @JsonView(JsonViews.Driver.class)
    private String requestorLastName;

    @ApiModelProperty(value = "The ride requestor's 10 digit phone number", example = "9161234567")
    @NotNull(message = "requestorPhoneNumber must not be null")
    @Size(min = 10, max = 10, message = "requestorPhoneNumber must be 10 digits")
    @JsonView(JsonViews.Driver.class)
    private String requestorPhoneNumber;

    @ApiModelProperty(value = "The number of passengers including the ride requestor", allowableValues = "range[1, 3]")
    @NotNull(message = "numPassengers must not be null")
    @Min(value = 1, message = "numPassengers must be greater than or equal to 1")
    @Max(value = 3, message = "numPassenger must be less than or equal to 3")
    @JsonView(JsonViews.Driver.class)
    private Integer numPassengers;

    // TODO: min, max values?
    @ApiModelProperty(value = "The odometer reading of the driver's vehicle at the beginning of the ride")
    @JsonView(JsonViews.Driver.class)
    private Integer startOdometer;

    // TODO: min, max values?
    @ApiModelProperty(value = "The odometer reading of the driver's vehicle at the time of ride completion")
    @JsonView(JsonViews.Driver.class)
    private Integer endOdometer;

    @ApiModelProperty(value = "Line 1 of rider's pickup address")
    @NotNull(message = "pickupLine1 must not be null")
    @JsonView(JsonViews.Driver.class)
    // TODO: min, max size?
    private String pickupLine1;

    @ApiModelProperty(value = "Line 2 of rider's pickup address")
    @JsonView(JsonViews.Driver.class)
    private String pickupLine2;

    @ApiModelProperty(value = "City of rider's pickup location")
    @NotNull(message = "pickupCity must not be null")
    @JsonView(JsonViews.Driver.class)
    // TODO: min, max size?
    private String pickupCity;

    @ApiModelProperty(value = "Line 1 of rider's dropoff address")
    @NotNull(message = "dropoffLine1 must not be null")
    @JsonView(JsonViews.Driver.class)
    // TODO: min, max size?
    private String dropoffLine1;

    @ApiModelProperty(value = "Line 2 of rider's dropoff address")
    @JsonView(JsonViews.Driver.class)
    private String dropoffLine2;

    @ApiModelProperty(value = "City of rider's dropoff location")
    @NotNull(message = "dropoffCity must not be null")
    @JsonView(JsonViews.Driver.class)
    // TODO: min, max size?
    private String dropoffCity;

    @ApiModelProperty(value = "The status of the ride request",
            allowableValues = "UNASSIGNED, ASSIGNED, PICKINGUP, ATPICKUPLOCATION, DROPPINGOFF, COMPLETE, CANCELEDBYCOORDINATOR, CANCELEDBYRIDER, CANCELEDOTHER")
    @NotNull(message = "status must not be null")
    @JsonView(JsonViews.Rider.class)
    private RideRequestStatus status;

    @ApiModelProperty(value = "The reason for cancellation if ride request is cancelled")
    @Size(max = 255, message = "cancelMessage must be less than 255 characters")
    @JsonView(JsonViews.Driver.class)
    private String cancelMessage;

    @ApiModelProperty(value = "An optional message to the driver")
    @Size(max = 255, message = "messageToDriver must be less than 255 characters")
    @JsonView(JsonViews.Driver.class)
    private String messageToDriver;

    @ApiModelProperty(value = "The estimated time of arrival of the driver after the ride is ASSIGNED")
    @JsonView(JsonViews.Rider.class)
    private String estimatedTime;

    @ApiModelProperty(value = "The latitude of the pickup location of the ride", readOnly = true)
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @JsonView(JsonViews.Coordinator.class)
    private Double pickupLatitude;

    @ApiModelProperty(value = "The longitude of the pickup location of the ride", readOnly = true)
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @JsonView(JsonViews.Coordinator.class)
    private Double pickupLongitude;

    @ApiModelProperty(value = "The latitude of the dropoff location of the ride", readOnly = true)
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @JsonView(JsonViews.Coordinator.class)
    private Double dropoffLatitude;

    @ApiModelProperty(value = "The longitude of the dropoff location of the ride", readOnly = true)
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @JsonView(JsonViews.Coordinator.class)
    private Double dropoffLongitude;

    @ApiModelProperty(value = "The assigned driver's first name", readOnly = true)
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @JsonView(JsonViews.Rider.class)
    private String driverName;

    @ApiModelProperty(value = "The assigned driver's vehicle year", readOnly = true)
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @JsonView(JsonViews.Rider.class)
    private String vehicleYear;

    @ApiModelProperty(value = "The assigned driver's vehicle color", readOnly = true)
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @JsonView(JsonViews.Rider.class)
    private String vehicleColor;

    @ApiModelProperty(value = "The assigned driver's vehicle make", readOnly = true)
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @JsonView(JsonViews.Rider.class)
    private String vehicleMake;

    @ApiModelProperty(value = "The assigned driver's vehicle model", readOnly = true)
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @JsonView(JsonViews.Rider.class)
    private String vehicleModel;

    @ApiModelProperty(value = "The assigned driver's vehicle license plate", readOnly = true)
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @JsonView(JsonViews.Rider.class)
    private String vehicleLicensePlate;

    private UserDto user;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getDriverId() {
        return driverId;
    }

    public void setDriverId(Long driverId) {
        this.driverId = driverId;
    }

    public String getOneCardId() {
        return oneCardId;
    }

    public void setOneCardId(String oneCardId) {
        this.oneCardId = oneCardId;
    }

    public LocalDateTime getRequestDate() {
        return requestDate;
    }

    public void setRequestDate(LocalDateTime requestDate) {
        this.requestDate = requestDate;
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

    public String getRequestorFirstName() {
        return requestorFirstName;
    }

    public void setRequestorFirstName(String requestorFirstName) {
        this.requestorFirstName = requestorFirstName;
    }

    public String getRequestorLastName() {
        return requestorLastName;
    }

    public void setRequestorLastName(String requestorLastName) {
        this.requestorLastName = requestorLastName;
    }

    public String getRequestorPhoneNumber() {
        return requestorPhoneNumber;
    }

    public void setRequestorPhoneNumber(String requestorPhoneNumber) {
        this.requestorPhoneNumber = requestorPhoneNumber;
    }

    public Integer getNumPassengers() {
        return numPassengers;
    }

    public void setNumPassengers(Integer numPassengers) {
        this.numPassengers = numPassengers;
    }

    public Integer getStartOdometer() {
        return startOdometer;
    }

    public void setStartOdometer(Integer startOdometer) {
        this.startOdometer = startOdometer;
    }

    public Integer getEndOdometer() {
        return endOdometer;
    }

    public void setEndOdometer(Integer endOdometer) {
        this.endOdometer = endOdometer;
    }

    public String getPickupLine1() {
        return pickupLine1;
    }

    public void setPickupLine1(String pickupLine1) {
        this.pickupLine1 = pickupLine1;
    }

    public String getPickupLine2() {
        return pickupLine2;
    }

    public void setPickupLine2(String pickupLine2) {
        this.pickupLine2 = pickupLine2;
    }

    public String getPickupCity() {
        return pickupCity;
    }

    public void setPickupCity(String pickupCity) {
        this.pickupCity = pickupCity;
    }

    public String getDropoffLine1() {
        return dropoffLine1;
    }

    public void setDropoffLine1(String dropoffLine1) {
        this.dropoffLine1 = dropoffLine1;
    }

    public String getDropoffLine2() {
        return dropoffLine2;
    }

    public void setDropoffLine2(String dropoffLine2) {
        this.dropoffLine2 = dropoffLine2;
    }

    public String getDropoffCity() {
        return dropoffCity;
    }

    public void setDropoffCity(String dropoffCity) {
        this.dropoffCity = dropoffCity;
    }

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

    public String getCancelMessage() {
        return cancelMessage;
    }

    public void setCancelMessage(String cancelMessage) {
        this.cancelMessage = cancelMessage;
    }

    public String getMessageToDriver() {
        return messageToDriver;
    }

    public void setMessageToDriver(String messageToDriver) {
        this.messageToDriver = messageToDriver;
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

    public Double getPickupLatitude() {
        return pickupLatitude;
    }

    public void setPickupLatitude(Double pickupLatitude) {
        this.pickupLatitude = pickupLatitude;
    }

    public Double getPickupLongitude() {
        return pickupLongitude;
    }

    public void setPickupLongitude(Double pickupLongitude) {
        this.pickupLongitude = pickupLongitude;
    }

    public Double getDropoffLatitude() {
        return dropoffLatitude;
    }

    public void setDropoffLatitude(Double dropoffLatitude) {
        this.dropoffLatitude = dropoffLatitude;
    }

    public Double getDropoffLongitude() {
        return dropoffLongitude;
    }

    public void setDropoffLongitude(Double dropoffLongitude) {
        this.dropoffLongitude = dropoffLongitude;
    }

    public String getDriverName() {
        return driverName;
    }

    public void setDriverName(String driverName) {
        this.driverName = driverName;
    }

    public String getVehicleYear() {
        return vehicleYear;
    }

    public void setVehicleYear(String vehicleYear) {
        this.vehicleYear = vehicleYear;
    }

    public String getVehicleColor() {
        return vehicleColor;
    }

    public void setVehicleColor(String vehicleColor) {
        this.vehicleColor = vehicleColor;
    }

    public String getVehicleMake() {
        return vehicleMake;
    }

    public void setVehicleMake(String vehicleMake) {
        this.vehicleMake = vehicleMake;
    }

    public String getVehicleModel() {
        return vehicleModel;
    }

    public void setVehicleModel(String vehicleModel) {
        this.vehicleModel = vehicleModel;
    }

    public String getVehicleLicensePlate() {
        return vehicleLicensePlate;
    }

    public void setVehicleLicensePlate(String vehicleLicensePlate) {
        this.vehicleLicensePlate = vehicleLicensePlate;
    }

    public UserDto getUser() {
        return user;
    }

    public void setUser(UserDto user) {
        this.user = user;
    }
}

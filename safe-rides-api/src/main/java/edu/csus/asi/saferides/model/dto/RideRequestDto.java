package edu.csus.asi.saferides.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import edu.csus.asi.saferides.model.RideRequestStatus;
import edu.csus.asi.saferides.model.views.JsonViews;
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
    private Long startOdometer;

    // TODO: min, max values?
    @ApiModelProperty(value = "The odometer reading of the driver's vehicle at the time of ride completion")
    @JsonView(JsonViews.Driver.class)
    private Long endOdometer;

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
    private String driverFirstName;

    @ApiModelProperty(value = "The assigned driver's last name", readOnly = true)
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @JsonView(JsonViews.Rider.class)
    private String driverLastName;

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

    /**
     * Get the id
     *
     * @return id
     */
    public Long getId() {
        return id;
    }

    /**
     * Set the id
     *
     * @param id id
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Get the assigned driver's id
     *
     * @return assigned driver's id
     */
    public Long getDriverId() {
        return driverId;
    }

    /**
     * Set the assigned driver's id
     *
     * @param driverId the assigned driver's id
     */
    public void setDriverId(Long driverId) {
        this.driverId = driverId;
    }

    /**
     * Get the rider's one card id
     *
     * @return rider's one card id
     */
    public String getOneCardId() {
        return oneCardId;
    }

    /**
     * Set the rider's one card id
     *
     * @param oneCardId rider's one card id
     */
    public void setOneCardId(String oneCardId) {
        this.oneCardId = oneCardId;
    }

    /**
     * Get the ride's request date
     *
     * @return ride's request date
     */
    public LocalDateTime getRequestDate() {
        return requestDate;
    }

    /**
     * Set the ride's request date
     *
     * @param requestDate ride's request date
     */
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

    /**
     * Get the requestor's first name
     *
     * @return requestor's first name
     */
    public String getRequestorFirstName() {
        return requestorFirstName;
    }

    /**
     * Set the requestor's first name
     *
     * @param requestorFirstName requestor's first name
     */
    public void setRequestorFirstName(String requestorFirstName) {
        this.requestorFirstName = requestorFirstName;
    }

    /**
     * Get the requestor's last name
     *
     * @return requestor's last name
     */
    public String getRequestorLastName() {
        return requestorLastName;
    }

    /**
     * Set the requestor's last name
     *
     * @param requestorLastName requestor's last name
     */
    public void setRequestorLastName(String requestorLastName) {
        this.requestorLastName = requestorLastName;
    }

    /**
     * Get the requestor's phone number
     *
     * @return requestor's phone number
     */
    public String getRequestorPhoneNumber() {
        return requestorPhoneNumber;
    }

    /**
     * Set the requestor's phone number
     *
     * @param requestorPhoneNumber requestor's phone number
     */
    public void setRequestorPhoneNumber(String requestorPhoneNumber) {
        this.requestorPhoneNumber = requestorPhoneNumber;
    }

    /**
     * Get the number of passengers
     *
     * @return number of passengers
     */
    public Integer getNumPassengers() {
        return numPassengers;
    }

    /**
     * Set the number of passengers
     *
     * @param numPassengers number of passengers
     */
    public void setNumPassengers(Integer numPassengers) {
        this.numPassengers = numPassengers;
    }

    /**
     * Get the driver's start odometer for the ride request
     *
     * @return driver's start odometer for the ride request
     */
    public Long getStartOdometer() {
        return startOdometer;
    }

    /**
     * Set driver's start odometer for the ride request
     *
     * @param startOdometer driver's start odometer for the ride request
     */
    public void setStartOdometer(Long startOdometer) {
        this.startOdometer = startOdometer;
    }

    /**
     * Get the driver's end odometer for the ride request
     *
     * @return driver's end odometer for the ride request
     */
    public Long getEndOdometer() {
        return endOdometer;
    }

    /**
     * Set the driver's end odometer for the ride request
     *
     * @param endOdometer driver's end odometer for the ride request
     */
    public void setEndOdometer(Long endOdometer) {
        this.endOdometer = endOdometer;
    }

    /**
     * Get the request's pickup address: line 1
     *
     * @return request's pickup address: line 1
     */
    public String getPickupLine1() {
        return pickupLine1;
    }

    /**
     * Set the request's pickup address: line 1
     *
     * @param pickupLine1 request's pickup address: line 1
     */
    public void setPickupLine1(String pickupLine1) {
        this.pickupLine1 = pickupLine1;
    }

    /**
     * Get the request's pickup address: line 2
     *
     * @return request's pickup address: line 2
     */
    public String getPickupLine2() {
        return pickupLine2;
    }

    /**
     * Set the request's pickup address: line 2
     *
     * @param pickupLine2 request's pickup address: line 2
     */
    public void setPickupLine2(String pickupLine2) {
        this.pickupLine2 = pickupLine2;
    }

    /**
     * Get the request's pickup address: city
     *
     * @return request's pickup address: city
     */
    public String getPickupCity() {
        return pickupCity;
    }

    /**
     * Set the request's pickup address: city
     *
     * @param pickupCity request's pickup address: city
     */
    public void setPickupCity(String pickupCity) {
        this.pickupCity = pickupCity;
    }

    /**
     * Get the request's dropoff address: line 1
     *
     * @return request's dropoff address: line 1
     */
    public String getDropoffLine1() {
        return dropoffLine1;
    }

    /**
     * Set the request's dropoff address: line 1
     *
     * @param dropoffLine1 request's dropoff address: line 1
     */
    public void setDropoffLine1(String dropoffLine1) {
        this.dropoffLine1 = dropoffLine1;
    }

    /**
     * Get the request's dropoff address: line 2
     *
     * @return request's dropoff address: line 2
     */
    public String getDropoffLine2() {
        return dropoffLine2;
    }

    /**
     * Set the request's dropoff address: line 2
     *
     * @param dropoffLine2 request's dropoff address: line 2
     */
    public void setDropoffLine2(String dropoffLine2) {
        this.dropoffLine2 = dropoffLine2;
    }

    /**
     * Get the request's dropoff address: city
     *
     * @return request's dropoff address: city
     */
    public String getDropoffCity() {
        return dropoffCity;
    }

    /**
     * Set the request's dropoff address: city
     *
     * @param dropoffCity request's dropoff address: cit
     */
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

    /**
     * Get the cancel message
     *
     * @return cancel message
     */
    public String getCancelMessage() {
        return cancelMessage;
    }

    /**
     * Set the cancel message
     *
     * @param cancelMessage cancel message
     */
    public void setCancelMessage(String cancelMessage) {
        this.cancelMessage = cancelMessage;
    }

    /**
     * Get the message to the driver
     *
     * @return message to the driver
     */
    public String getMessageToDriver() {
        return messageToDriver;
    }

    /**
     * Set the message to the driver
     *
     * @param messageToDriver message to the driver
     */
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

    /**
     * Get the pickup latitude
     *
     * @return pickup latitude
     */
    public Double getPickupLatitude() {
        return pickupLatitude;
    }

    /**
     * Set the pickup latitude
     *
     * @param pickupLatitude pickup latitude
     */
    public void setPickupLatitude(Double pickupLatitude) {
        this.pickupLatitude = pickupLatitude;
    }

    /**
     * Get the pickup longitude
     *
     * @return pickup longitude
     */
    public Double getPickupLongitude() {
        return pickupLongitude;
    }

    /**
     * Set the pickup longitude
     *
     * @param pickupLongitude pickup longitude
     */
    public void setPickupLongitude(Double pickupLongitude) {
        this.pickupLongitude = pickupLongitude;
    }

    /**
     * Get the dropoff latitude
     *
     * @return dropoff latitude
     */
    public Double getDropoffLatitude() {
        return dropoffLatitude;
    }

    /**
     * Set the dropoff latitude
     *
     * @param dropoffLatitude dropoff latitude
     */
    public void setDropoffLatitude(Double dropoffLatitude) {
        this.dropoffLatitude = dropoffLatitude;
    }

    /**
     * Get the dropoff longitude
     *
     * @return dropoff longitude
     */
    public Double getDropoffLongitude() {
        return dropoffLongitude;
    }

    /**
     * Set the dropoff longitude
     *
     * @param dropoffLongitude dropoff longitude
     */
    public void setDropoffLongitude(Double dropoffLongitude) {
        this.dropoffLongitude = dropoffLongitude;
    }

    /**
     * Get the driver's first name
     *
     * @return driver's first name
     */
    public String getDriverFirstName() {
        return driverFirstName;
    }

    /**
     * Set the driver's first name
     *
     * @param driverName driver's first name
     */
    public void setDriverFirstName(String driverName) {
        this.driverFirstName = driverName;
    }

    /**
     * Get the driver's last name
     *
     * @return driver's last name
     */
    public String getDriverLastName() {
        return driverLastName;
    }

    /**
     * Set the driver's last name
     *
     * @param driverName driver's last name
     */
    public void setDriverLastName(String driverName) {
        this.driverLastName = driverName;
    }

    /**
     * Get the driver's vehicle year
     *
     * @return driver's vehicle year
     */
    public String getVehicleYear() {
        return vehicleYear;
    }

    /**
     * Set the driver's vehicle year
     *
     * @param vehicleYear driver's vehicle year
     */
    public void setVehicleYear(String vehicleYear) {
        this.vehicleYear = vehicleYear;
    }

    /**
     * Get the driver's vehicle color
     *
     * @return driver's vehicle color
     */
    public String getVehicleColor() {
        return vehicleColor;
    }

    /**
     * Set the driver's vehicle color
     *
     * @param vehicleColor driver's vehicle color
     */
    public void setVehicleColor(String vehicleColor) {
        this.vehicleColor = vehicleColor;
    }

    /**
     * Get the driver's vehicle make
     *
     * @return driver's vehicle make
     */
    public String getVehicleMake() {
        return vehicleMake;
    }

    /**
     * Set the driver's vehicle make
     *
     * @param vehicleMake driver's vehicle make
     */
    public void setVehicleMake(String vehicleMake) {
        this.vehicleMake = vehicleMake;
    }

    /**
     * Get the driver's vehicle model
     *
     * @return driver's vehicle model
     */
    public String getVehicleModel() {
        return vehicleModel;
    }

    /**
     * Set the driver's vehicle model
     *
     * @param vehicleModel driver's vehicle model
     */
    public void setVehicleModel(String vehicleModel) {
        this.vehicleModel = vehicleModel;
    }

    /**
     * Get the driver's vehicle license plate
     *
     * @return driver's vehicle license plate
     */
    public String getVehicleLicensePlate() {
        return vehicleLicensePlate;
    }

    /**
     * Set the driver's vehicle license plate
     *
     * @param vehicleLicensePlate driver's vehicle license plate
     */
    public void setVehicleLicensePlate(String vehicleLicensePlate) {
        this.vehicleLicensePlate = vehicleLicensePlate;
    }
}

package edu.csus.asi.saferides.model.dto;

import edu.csus.asi.saferides.model.Vehicle;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotNull;

/**
 * Data transfer object for the Driver object
 */
public class DriverDto {
    @ApiModelProperty(value = "The id of the driver")
    private Long id;

    @ApiModelProperty(value = "The driver's username", required = true)
    @NotNull(message = "username cannot be null")
    private String username;

    @ApiModelProperty(value = "The driver's first name", required = true)
    @NotNull(message = "driverFirstName cannot be null")
    private String driverFirstName;

    @ApiModelProperty(value = "The driver's last name", required = true)
    @NotNull(message = "driverLastName cannot be null")
    private String driverLastName;

    @ApiModelProperty(value = "The driver's phone number", required = true)
    @NotNull(message = "phoneNumber cannot be null")
    private String phoneNumber;

    @ApiModelProperty(value = "Confirmation that the driver's licence has been checked", required = true)
    @NotNull(message = "dlChecked cannot be null")
    private Boolean dlChecked;

    @ApiModelProperty(value = "Confirmation that the driver's insurance has been checked", required = true)
    @NotNull(message = "insuranceChecked cannot be null")
    private Boolean insuranceChecked;

    @ApiModelProperty(value = "The insurance company that the driver has their policy under", required = true)
    @NotNull(message = "insuranceCompany cannot be null")
    private String insuranceCompany;

    @ApiModelProperty(value = "Driver status whether they can be assigned to a ride request", required = true)
    @NotNull(message = "active cannot be null")
    private Boolean active;

    @ApiModelProperty(value = "The driver's vehicle")
    private Vehicle vehicle;

    @ApiModelProperty(value = "The driver's recorded odometer value at the end of the Safe Rides' night")
    private long endOfNightOdo;

    @ApiModelProperty(value = "The driver's latest recorded location")
    private DriverLocationDto location;

    @ApiModelProperty(value = "The driver's current ride request")
    private RideRequestDto currentRideRequest;

    /**
     * Get the driver's username
     *
     * @return driver's username
     */
    public String getUsername() {
        return username;
    }

    /**
     * Set the driver's username
     *
     * @param username driver's username
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Get the driver's first name
     *
     * @return first name
     */
    public String getDriverFirstName() {
        return driverFirstName;
    }

    /**
     * Set the driver's first name
     *
     * @param driverFirstName driver's first name
     */
    public void setDriverFirstName(String driverFirstName) {
        this.driverFirstName = driverFirstName;
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
     * @param driverLastName driver's last name
     */
    public void setDriverLastName(String driverLastName) {
        this.driverLastName = driverLastName;
    }

    /**
     * Get the driver's phone number
     *
     * @return phone number
     */
    public String getPhoneNumber() {
        return phoneNumber;
    }

    /**
     * Set the driver's phone number
     *
     * @param phoneNumber driver's phone number
     */
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    /**
     * Get the driver's insurance coverage checked
     *
     * @return driver's insurance coverage checked
     */
    public Boolean getDlChecked() {
        return dlChecked;
    }

    /**
     * Set driver's insurance coverage checked
     *
     * @param dlChecked insurance coverage checked
     */
    public void setDlChecked(Boolean dlChecked) {
        this.dlChecked = dlChecked;
    }

    /**
     * Get the driver's insurance coverage checked
     *
     * @return driver's insurance coverage checked
     */
    public Boolean getInsuranceChecked() {
        return insuranceChecked;
    }

    /**
     * Set the driver's insurance coverage checked
     *
     * @param insuranceChecked driver's insurance coverage checked
     */
    public void setInsuranceChecked(Boolean insuranceChecked) {
        this.insuranceChecked = insuranceChecked;
    }

    /**
     * Get the driver's insurance company name
     *
     * @return driver's insurance company name
     */
    public String getInsuranceCompany() {
        return insuranceCompany;
    }

    /**
     * Set the driver's insurance company name
     *
     * @param insuranceCompany driver's insurance company name
     */
    public void setInsuranceCompany(String insuranceCompany) {
        this.insuranceCompany = insuranceCompany;
    }

    /**
     * Get the driver's active status
     *
     * @return driver's active status
     */
    public Boolean getActive() {
        return active;
    }

    /**
     * Set the driver's active status
     *
     * @param active driver's active status
     */
    public void setActive(Boolean active) {
        this.active = active;
    }

    /**
     * Get the driver's id
     *
     * @return driver's id
     */
    public Long getId() {
        return id;
    }

    /**
     * Set the driver's id
     *
     * @param id driver's id
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Get the driver's vehicle
     *
     * @return driver's vehicle
     */
    public Vehicle getVehicle() {
        return vehicle;
    }

    /**
     * Set the driver's vehicle
     *
     * @param vehicle driver's vehicle
     */
    public void setVehicle(Vehicle vehicle) {
        this.vehicle = vehicle;
    }

    /**
     * Get the driver's end of night odometer reading
     *
     * @return driver's end of night odometer reading
     */
    public long getEndOfNightOdo() {
        return endOfNightOdo;
    }

    /**
     * Set the driver's end of night odometer reading
     *
     * @param endOfNightOdo driver's end of night odometer reading
     */
    public void setEndOfNightOdo(long endOfNightOdo) {
        this.endOfNightOdo = endOfNightOdo;
    }

    /**
     * Get the driver's location
     *
     * @return driver's location
     */
    public DriverLocationDto getLocation() {
        return location;
    }

    /**
     * Set the driver's location
     *
     * @param location driver's location
     */
    public void setLocation(DriverLocationDto location) {
        this.location = location;
    }

    /**
     * Get the driver's current ride request
     *
     * @return driver's current ride request
     */
    public RideRequestDto getCurrentRideRequest() {
        return currentRideRequest;
    }

    /**
     * Set the driver's current ride request
     *
     * @param currentRideRequest driver's current ride request
     */
    public void setCurrentRideRequest(RideRequestDto currentRideRequest) {
        this.currentRideRequest = currentRideRequest;
    }
}

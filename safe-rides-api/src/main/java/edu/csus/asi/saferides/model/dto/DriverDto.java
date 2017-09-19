package edu.csus.asi.saferides.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import edu.csus.asi.saferides.model.DriverStatus;
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

    @ApiModelProperty(value = "The driver's status", readOnly = true)
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private DriverStatus status;

    @ApiModelProperty(value = "The driver's vehicle")
    private Vehicle vehicle;

    @ApiModelProperty(value = "The driver's recorded odometer value at the end of the Safe Rides' night")
    private long endOfNightOdo;

    @ApiModelProperty(value = "The driver's latest recorded location")
    private DriverLocationDto location;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getDriverFirstName() {
        return driverFirstName;
    }

    public void setDriverFirstName(String driverFirstName) {
        this.driverFirstName = driverFirstName;
    }

    public String getDriverLastName() {
        return driverLastName;
    }

    public void setDriverLastName(String driverLastName) {
        this.driverLastName = driverLastName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public Boolean getDlChecked() {
        return dlChecked;
    }

    public void setDlChecked(Boolean dlChecked) {
        this.dlChecked = dlChecked;
    }

    public Boolean getInsuranceChecked() {
        return insuranceChecked;
    }

    public void setInsuranceChecked(Boolean insuranceChecked) {
        this.insuranceChecked = insuranceChecked;
    }

    public String getInsuranceCompany() {
        return insuranceCompany;
    }

    public void setInsuranceCompany(String insuranceCompany) {
        this.insuranceCompany = insuranceCompany;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Vehicle getVehicle() {
        return vehicle;
    }

    public void setVehicle(Vehicle vehicle) {
        this.vehicle = vehicle;
    }

    public long getEndOfNightOdo() {
        return endOfNightOdo;
    }

    public void setEndOfNightOdo(long endOfNightOdo) {
        this.endOfNightOdo = endOfNightOdo;
    }

    public DriverStatus getStatus() {
        return status;
    }

    public void setStatus(DriverStatus status) {
        this.status = status;
    }

    public DriverLocationDto getLocation() {
        return location;
    }

    public void setLocation(DriverLocationDto location) {
        this.location = location;
    }
}

package edu.csus.asi.saferides.model.dto;

import edu.csus.asi.saferides.model.RideRequestStatus;

import java.time.LocalDateTime;

/**
 * Data Transfer Object for RideRequest.
 * Contains a limited subset of the RideRequestObject.
 */
public class RideRequestDto {

    /**
     * Ride requestor's first name
     */
    private String requestorFirstName;

    /**
     * Ride requestor's last name
     */
    private String requestorLastName;

    /**
     * The status of the ride. eg. UNASSIGNED, ASSIGNED, etc.
     */
    private RideRequestStatus status;

    /**
     * The estimated time for the driver to arrive at the pickup location after the ride has been ASSIGNED
     */
    private String estimatedTime;

    /**
     * The last modified timestamp for the ride
     */
    private LocalDateTime lastModified;

    /**
     * The time the ride was assigned to a driver
     */
    private LocalDateTime assignedDate;

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
     * Gets the ride requestor's first name
     *
     * @return the ride requestor's first name
     */
    public String getRequestorFirstName() {
        return requestorFirstName;
    }

    /**
     * Sets the ride requestor's first name
     *
     * @param requestorFirstName the ride requestor's first name
     */
    public void setRequestorFirstName(String requestorFirstName) {
        this.requestorFirstName = requestorFirstName;
    }

    /**
     * Gets the ride requestor's last name
     *
     * @return the ride requestor's last name
     */
    public String getRequestorLastName() {
        return requestorLastName;
    }

    /**
     * Sets the ride requestor's last name
     *
     * @param requestorLastName the ride requestor's last name
     */
    public void setRequestorLastName(String requestorLastName) {
        this.requestorLastName = requestorLastName;
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
        return "RideRequestDto{" + "requestorFirstName='" + requestorFirstName + '\'' + ", requestorLastName='"
                + requestorLastName + '\'' + ", status=" + status + ", estimatedTime='" + estimatedTime + '\''
                + ", lastModified=" + lastModified + ", assignedDate=" + assignedDate + ", driverName='" + driverName
                + '\'' + ", vehicleColor='" + vehicleColor + '\'' + ", vehicleYear='" + vehicleYear + '\''
                + ", vehicleMake='" + vehicleMake + '\'' + ", vehicleModel='" + vehicleModel + '\''
                + ", vehicleLicensePlate='" + vehicleLicensePlate + '\'' + '}';
    }
}

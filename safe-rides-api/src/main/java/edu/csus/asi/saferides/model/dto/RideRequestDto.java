package edu.csus.asi.saferides.model.dto;

/*
 * @author Zeeshan Khaliq
 *
 * Data Transfer Object for RideRequest
 * */

import edu.csus.asi.saferides.model.RideRequestStatus;

public class RideRequestDto {

    private String requestorFirstName;

    private String requestorLastName;

    private RideRequestStatus status;

    private String estimatedTime;

    private String driverName;

    private String vehicleColor;

    private String vehicleYear;

    private String vehicleMake;

    private String vehicleModel;

    private String vehicleLicensePlate;

    public RideRequestDto(String requestorFirstName, String requestorLastName, RideRequestStatus status, String estimatedTime, String driverName, String vehicleColor, String vehicleYear, String vehicleMake, String vehicleModel, String vehicleLicensePlate) {
        this.requestorFirstName = requestorFirstName;
        this.requestorLastName = requestorLastName;
        this.status = status;
        this.estimatedTime = estimatedTime;
        this.driverName = driverName;
        this.vehicleColor = vehicleColor;
        this.vehicleYear = vehicleYear;
        this.vehicleMake = vehicleMake;
        this.vehicleModel = vehicleModel;
        this.vehicleLicensePlate = vehicleLicensePlate;
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

    public RideRequestStatus getStatus() {
        return status;
    }

    public void setStatus(RideRequestStatus status) {
        this.status = status;
    }

    public String getEstimatedTime() {
        return estimatedTime;
    }

    public void setEstimatedTime(String estimatedTime) {
        this.estimatedTime = estimatedTime;
    }

    public String getDriverName() {
        return driverName;
    }

    public void setDriverName(String driverName) {
        this.driverName = driverName;
    }

    public String getVehicleColor() {
        return vehicleColor;
    }

    public void setVehicleColor(String vehicleColor) {
        this.vehicleColor = vehicleColor;
    }

    public String getVehicleYear() {
        return vehicleYear;
    }

    public void setVehicleYear(String vehicleYear) {
        this.vehicleYear = vehicleYear;
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

    @Override
    public String toString() {
        return "RideRequestDto{" +
                "requestorFirstName='" + requestorFirstName + '\'' +
                ", requestorLastName='" + requestorLastName + '\'' +
                ", status=" + status +
                ", estimatedTime='" + estimatedTime + '\'' +
                ", driverName='" + driverName + '\'' +
                ", vehicleColor='" + vehicleColor + '\'' +
                ", vehicleMake='" + vehicleMake + '\'' +
                ", vehicleModel='" + vehicleModel + '\'' +
                ", vehicleLicensePlate='" + vehicleLicensePlate + '\'' +
                '}';
    }
}
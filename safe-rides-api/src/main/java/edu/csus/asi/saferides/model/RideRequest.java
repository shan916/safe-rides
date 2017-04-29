package edu.csus.asi.saferides.model;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.util.Date;

/**
 * A RideRequest holds rider info, the time the request was made, last modified, assigned,
 * the number of passengers,
 * the place the rider needs to be picked up at,
 * the place the rider needs to be dropped off at,
 * an optional message for the coordinator to create and for the assigned driver to see,
 * and the odometer readings for assigned driver vehicle mileage
 */
@Entity
public class RideRequest {

    /**
     * Many to one relationship between Driver and RideRequest so one Driver can serve many RideRequests
     */
    @ManyToOne
    private Driver driver;

    /**
     * Primary key
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    /**
     * The ride requestor's One Card ID
     */
    @Column(nullable = false)
    private String oneCardId;

    /**
     * The date the ride was requested
     */
    @Column(updatable = false)
    private Date requestDate;

    /**
     * The last modified timestamp for the ride
     */
    @Column
    private Date lastModified;

    /**
     * The time the ride was assigned to a driver
     */
    @Column
    private Date assignedDate;

    /**
     * Ride requestor's first name
     */
    @Column(nullable = false)
    private String requestorFirstName;

    /**
     * Ride requestor's last name
     */
    @Column(nullable = false)
    private String requestorLastName;

    /**
     * Ride requestor's phone number
     */
    @Column(nullable = false)
    private String requestorPhoneNumber;

    /**
     * Number of passengers including the ride requestor
     */
    @Column(nullable = false)
    @Min(1)
    @Max(3)
    private int numPassengers;

    /**
     * The driver's start odometer at the time of assignment
     */
    @Column
    private int startOdometer;

    /**
     * The driver's end odometer at the time of ride completion
     */
    @Column
    private int endOdometer;

    /**
     * Line 1 of rider's pickup address
     */
    @Column(nullable = false)
    private String pickupLine1;

    /**
     * Line 2 of rider's pickup address
     */
    @Column
    private String pickupLine2;

    /**
     * City of rider's pickup location
     */
    @Column(nullable = false)
    private String pickupCity;

    /**
     * Line 1 of rider's drop-off address
     */
    @Column(nullable = false)
    private String dropoffLine1;

    /**
     * Line 2 of rider's drop-off address
     */
    @Column
    private String dropoffLine2;

    /**
     * City of rider's dropoff location
     */
    @Column(nullable = false)
    private String dropoffCity;

    /**
     * The status of the ride request
     */
    @Enumerated(EnumType.STRING)
    private RideRequestStatus status;

    /**
     * Reason for cancellation if ride request is cancelled
     */
    @Column(nullable = true)
    private String cancelMessage;

    /**
     * Optional message to driver
     */
    @Column(nullable = true)
    private String messageToDriver;

    /**
     * The estimated time for the driver to arrive at the pickup location after the ride has been ASSIGNED
     */
    @Column(nullable = true)
    private String estimatedTime;

    /**
     * Latitude of pickup location of ride
     */
    @Column(precision = 10, scale = 2)
    private Double pickupLatitude;

    /**
     * Longitude of pickup location of ride
     */
    @Column(precision = 10, scale = 2)
    private Double pickupLongitude;

    /**
     * Latitude of drop-off location of ride
     */
    @Column(precision = 10, scale = 2)
    private Double dropoffLatitude;

    /**
     * Longitude of drop-off location of ride
     */
    @Column(precision = 10, scale = 2)
    private Double dropoffLongitude;

    /**
     * Constructor used by JPA to construct RideRequest Entity
     */
    protected RideRequest() {
    }

    public RideRequest(String oneCardId, String requestorFirstName, String requestorLastName,
                       String requestorPhoneNumber, int numPassengers, String pickupLine1, String pickupCity,
                       String dropoffLine1, String dropoffCity) {
        super();
        this.oneCardId = oneCardId;
        this.requestDate = new Date();
        this.requestorFirstName = requestorFirstName;
        this.requestorLastName = requestorLastName;
        this.requestorPhoneNumber = requestorPhoneNumber;
        this.numPassengers = numPassengers;
        this.pickupLine1 = pickupLine1;
        this.pickupCity = pickupCity;
        this.dropoffLine1 = dropoffLine1;
        this.dropoffCity = dropoffCity;
        this.status = RideRequestStatus.UNASSIGNED;
    }

    /**
     * <ul>
     * <li>
     * Sets the requestDate on creation
     * </li>
     * <li>
     * Sets the assignedDate on update of status to ASSIGNED
     * </li>
     * <li>
     * Sets the lastModified date on update
     * </li>
     * </ul>
     */
    @PreUpdate
    @PrePersist
    public void updateTimeStamps() {
        lastModified = new Date();

        if (status == RideRequestStatus.ASSIGNED) {
            assignedDate = new Date();
        }

        if (requestDate == null) {
            requestDate = new Date();
        }
    }

    /**
     * Gets the driver assigned to the ride
     *
     * @return the driver assigned to the ride
     */
    public Driver getDriver() {
        return driver;
    }

    /**
     * Sets the driver for the ride
     *
     * @param driver the driver to be assigned to the ride
     */
    public void setDriver(Driver driver) {
        this.driver = driver;
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
     * Gets the primary key
     *
     * @return the primary key
     */
    public Long getId() {
        return id;
    }

    /**
     * Sets the primary key
     *
     * @param id the primary key
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Gets the One Card ID of the ride requestor
     *
     * @return the One Card ID of the ride requestor
     */
    public String getOneCardId() {
        return oneCardId;
    }

    /**
     * Sets the One Card ID of the ride requestor
     *
     * @param oneCardId the One Card ID of the ride requestor
     */
    public void setOneCardId(String oneCardId) {
        this.oneCardId = oneCardId;
    }

    /**
     * Gets the date the ride was requested
     *
     * @return the date the ride was requested
     */
    public Date getRequestDate() {
        return requestDate;
    }

    /**
     * Sets the date the ride was requested
     *
     * @param requestDate the date the ride was requested
     */
    public void setRequestDate(Date requestDate) {
        this.requestDate = requestDate;
    }

    /**
     * Gets the last modified timestamp for the ride
     *
     * @return the last modified timestamp for the ride
     */
    public Date getLastModified() {
        return lastModified;
    }

    /**
     * Sets the last modified timestamp for the ride
     *
     * @param lastModified the last modified timestamp for the ride
     */
    public void setLastModified(Date lastModified) {
        this.lastModified = lastModified;
    }

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
     * Gets the ride requestor's phone number
     *
     * @return the ride requestor's phone number
     */
    public String getRequestorPhoneNumber() {
        return requestorPhoneNumber;
    }

    /**
     * Sets the ride requestor's phone number
     *
     * @param requestorPhoneNumber the ride requestor's phone number
     */
    public void setRequestorPhoneNumber(String requestorPhoneNumber) {
        this.requestorPhoneNumber = requestorPhoneNumber;
    }

    /**
     * Gets the number of passengers including the ride requestor
     *
     * @return
     */
    public int getNumPassengers() {
        return numPassengers;
    }

    /**
     * Sets the number of passengers including the ride requestor
     *
     * @param numPassengers the number of passengers including the ride requestor
     */
    public void setNumPassengers(int numPassengers) {
        this.numPassengers = numPassengers;
    }

    /**
     * Gets the driver's start odometer at the time of assignment
     *
     * @return driver's start odometer at the time of assignment
     */
    public int getStartOdometer() {
        return startOdometer;
    }

    /**
     * Sets the driver's start odometer at the time of assignment
     *
     * @param startOdometer the driver's start odometer at the time of assignment
     */
    public void setStartOdometer(int startOdometer) {
        this.startOdometer = startOdometer;
    }

    /**
     * Gets the driver's end odometer at the time of ride completion
     *
     * @return the driver's end odometer at the time of ride completion
     */
    public int getEndOdometer() {
        return endOdometer;
    }

    /**
     * Sets the driver's end odometer at the time of ride completion
     *
     * @param endOdometer the driver's end odometer at the time of ride completion
     */
    public void setEndOdometer(int endOdometer) {
        this.endOdometer = endOdometer;
    }

    /**
     * Gets line 1 of rider's pickup address
     *
     * @return line 1 of rider's pickup address
     */
    public String getPickupLine1() {
        return pickupLine1;
    }

    /**
     * Sets line 1 of rider's pickup address
     *
     * @param pickupLine1 line 1 of rider's pickup address
     */
    public void setPickupLine1(String pickupLine1) {
        this.pickupLine1 = pickupLine1;
    }

    /**
     * Gets line 2 of rider's pickup address
     *
     * @return line 2 of rider's pickup address
     */
    public String getPickupLine2() {
        return pickupLine2;
    }

    /**
     * Sets line 2 of rider's pickup address
     *
     * @param pickupLine2 line 2 of rider's pickup address
     */
    public void setPickupLine2(String pickupLine2) {
        this.pickupLine2 = pickupLine2;
    }

    /**
     * Gets the city of rider's pickup location
     *
     * @return the city of rider's pickup location
     */
    public String getPickupCity() {
        return pickupCity;
    }

    /**
     * Sets city of rider's pickup location
     *
     * @param pickupCity city of rider's pickup location
     */
    public void setPickupCity(String pickupCity) {
        this.pickupCity = pickupCity;
    }

    /**
     * Gets line 1 of rider's drop-off address
     *
     * @return line 1 of rider's drop-off address
     */
    public String getDropoffLine1() {
        return dropoffLine1;
    }

    /**
     * Sets line 1 of rider's drop-off address
     *
     * @param dropoffLine1 line 1 of rider's drop-off address
     */
    public void setDropoffLine1(String dropoffLine1) {
        this.dropoffLine1 = dropoffLine1;
    }

    /**
     * Gets line 2 of rider's drop-off address
     *
     * @return line 2 of rider's drop-off address
     */
    public String getDropoffLine2() {
        return dropoffLine2;
    }

    /**
     * Sets line 2 of rider's drop-off address
     *
     * @param dropoffLine2 line 2 of rider's drop-off address
     */
    public void setDropoffLine2(String dropoffLine2) {
        this.dropoffLine2 = dropoffLine2;
    }

    /**
     * Gets city of rider's drop-off location
     *
     * @return city of rider's drop-off location
     */
    public String getDropoffCity() {
        return dropoffCity;
    }

    /**
     * Sets city of rider's drop-off location
     *
     * @param dropoffCity city of rider's drop-off location
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
     * @param status the status of the ride request
     */
    public void setStatus(RideRequestStatus status) {
        this.status = status;
    }

    /**
     * Gets the reason for cancellation if the ride request is cancelled
     *
     * @return the reason for cancellation if the ride request is cancelled
     */
    public String getCancelMessage() {
        return cancelMessage;
    }

    /**
     * Sets the reason for cancellation if the ride request is cancelled
     *
     * @param cancelMessage the reason for cancellation if the ride request is cancelled
     */
    public void setCancelMessage(String cancelMessage) {
        this.cancelMessage = cancelMessage;
    }

    /**
     * Gets the optional message to the driver assigned to the ride
     *
     * @return the optional message to the driver assigned to the ride
     */
    public String getMessageToDriver() {
        return messageToDriver;
    }

    /**
     * Sets an optional message to the driver assigned to the ride
     *
     * @param messageToDriver message to the driver assigned to the ride
     */
    public void setMessageToDriver(String messageToDriver) {
        this.messageToDriver = messageToDriver;
    }

    /**
     * Gets the latitude of pickup location of ride
     *
     * @return the latitude of pickup location of ride
     */
    public Double getPickupLatitude() {
        return pickupLatitude;
    }

    /**
     * Sets the latitude of pickup location of ride
     *
     * @param pickupLatitude the latitude of pickup location of ride
     */
    public void setPickupLatitude(Double pickupLatitude) {
        this.pickupLatitude = pickupLatitude;
    }

    /**
     * Gets the longitude of pickup location of ride
     *
     * @return the longitude of pickup location of ride
     */
    public Double getPickupLongitude() {
        return pickupLongitude;
    }

    /**
     * Sets the longitude of pickup location of ride
     *
     * @param pickupLongitude the longitude of pickup location of ride
     */
    public void setPickupLongitude(Double pickupLongitude) {
        this.pickupLongitude = pickupLongitude;
    }

    /**
     * Gets the latitude of drop-off location of ride
     *
     * @return the latitude of drop-off location of ride
     */
    public Double getDropoffLatitude() {
        return dropoffLatitude;
    }

    /**
     * Sets the latitude of drop-off location of ride
     *
     * @param dropoffLatitude the latitude of drop-off location of ride
     */
    public void setDropoffLatitude(Double dropoffLatitude) {
        this.dropoffLatitude = dropoffLatitude;
    }

    /**
     * Gets the longitude of drop-off location of ride
     *
     * @return the longitude of drop-off location of ride
     */
    public Double getDropoffLongitude() {
        return dropoffLongitude;
    }

    /**
     * Sets the longitude of drop-off location of ride
     *
     * @param getPickupLongitude the longitude of drop-off location of ride
     */
    public void setDropoffLongitude(Double getPickupLongitude) {
        this.dropoffLongitude = getPickupLongitude;
    }

    /**
     * Gets the time the ride was assigned to a driver
     *
     * @return the time the ride was assigned to a driver
     */
    public Date getAssignedDate() {
        return assignedDate;
    }

    /**
     * Sets the time the ride was assigned to a driver
     *
     * @param assignedDate the time the ride was assigned to a driver
     */
    public void setAssignedDate(Date assignedDate) {
        this.assignedDate = assignedDate;
    }

    /**
     * Gets the string representation of the RideRequest object
     *
     * @return the string representation of the RideRequest object
     */
    @Override
    public String toString() {
        return "RideRequest{" +
                "driver=" + driver +
                ", id=" + id +
                ", oneCardId='" + oneCardId + '\'' +
                ", requestDate=" + requestDate +
                ", lastModified=" + lastModified +
                ", assignedDate=" + assignedDate +
                ", requestorFirstName='" + requestorFirstName + '\'' +
                ", requestorLastName='" + requestorLastName + '\'' +
                ", requestorPhoneNumber='" + requestorPhoneNumber + '\'' +
                ", numPassengers=" + numPassengers +
                ", startOdometer=" + startOdometer +
                ", endOdometer=" + endOdometer +
                ", pickupLine1='" + pickupLine1 + '\'' +
                ", pickupLine2='" + pickupLine2 + '\'' +
                ", pickupCity='" + pickupCity + '\'' +
                ", dropoffLine1='" + dropoffLine1 + '\'' +
                ", dropoffLine2='" + dropoffLine2 + '\'' +
                ", dropoffCity='" + dropoffCity + '\'' +
                ", status=" + status +
                ", cancelMessage='" + cancelMessage + '\'' +
                ", messageToDriver='" + messageToDriver + '\'' +
                ", estimatedTime='" + estimatedTime + '\'' +
                ", pickupLatitude=" + pickupLatitude +
                ", pickupLongitude=" + pickupLongitude +
                ", dropoffLatitude=" + dropoffLatitude +
                ", dropoffLongitude=" + dropoffLongitude +
                '}';
    }
}

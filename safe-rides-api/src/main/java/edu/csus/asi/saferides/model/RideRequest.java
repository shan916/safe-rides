package edu.csus.asi.saferides.model;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.util.Date;

@Entity
public class RideRequest {
    @ManyToOne
    private Driver driver;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

	@Column(nullable = false)
	private String oneCardId;

	@Column(updatable = false)
	private Date requestDate;

    @Column(nullable = false)
    private String requestorFirstName;

    @Column(nullable = false)
    private String requestorLastName;

	@Column(nullable = false)
	private String requestorPhoneNumber;

    @Column(nullable = false)
    @Min(1)
    @Max(3)
    private int numPassengers;

    @Column
    private int startOdometer;

    @Column
    private int endOdometer;

    @Column(nullable = false)
    private String pickupLine1;

    @Column
    private String pickupLine2;

    @Column(nullable = false)
    private String pickupCity;

    @Column(nullable = false)
    private String dropoffLine1;

    @Column
    private String dropoffLine2;

    @Column(nullable = false)
    private String dropoffCity;

    @Enumerated(EnumType.STRING)
    private RideRequestStatus status;

    @Column(nullable = true)
    private String cancelMessage;

    @Column(nullable = true)
    private String messageToDriver;

    @Column(nullable = true)
    private String estimatedTime;

    @Column(precision = 10, scale = 2)
    private Double pickupLatitude;

    @Column(precision = 10, scale = 2)
    private Double pickupLongitude;

    @Column(precision = 10, scale = 2)
    private Double dropoffLatitude;

    @Column(precision = 10, scale = 2)
    private Double dropoffLongitude;

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

    @PreUpdate
    @PrePersist
    public void updateTimeStamps() {
        if (requestDate == null) {
            requestDate = new Date();
        }
    }

    public Driver getDriver() {
        return driver;
    }

    public void setDriver(Driver driver) {
        this.driver = driver;
    }

    public String getEstimatedTime() {
        return estimatedTime;
    }

    public void setEstimatedTime(String estimatedTime) {
        this.estimatedTime = estimatedTime;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

	public String getOneCardId() {
		return oneCardId;
	}

	public void setOneCardId(String oneCardId) {
		this.oneCardId = oneCardId;
	}

	public Date getRequestDate() {
		return requestDate;
	}

	public void setRequestDate(Date requestDate) {
		this.requestDate = requestDate;
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

    public int getNumPassengers() {
        return numPassengers;
    }

    public void setNumPassengers(int numPassengers) {
        this.numPassengers = numPassengers;
    }

    public int getStartOdometer() {
        return startOdometer;
    }

    public void setStartOdometer(int startOdometer) {
        this.startOdometer = startOdometer;
    }

    public int getEndOdometer() {
        return endOdometer;
    }

    public void setEndOdometer(int endOdometer) {
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

    public RideRequestStatus getStatus() {
        return status;
    }

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

    public void setDropoffLongitude(Double getPickupLongitude) {
        this.dropoffLongitude = getPickupLongitude;
    }

    @Override
    public String toString() {
        return "RideRequest{" +
                "driver=" + driver +
                ", id=" + id +
                ", oneCardId=" + oneCardId +
                ", requestDate=" + requestDate +
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

package edu.csus.asi.saferides.model;

import javax.persistence.*;
import java.util.Date;

@Entity
public class RideRequest {
	@ManyToOne
	private Driver driver;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@Column(nullable = false)
	private int requestorId;

	@Column(nullable = false)
	private Date date;

	@Column(nullable = false)
	private String requestorFirstName;

	@Column(nullable = false)
	private String requestorLastName;

	@Column(nullable = false)
	private String requestorContactNumber;

	@Column(nullable = false)
	private int numPassengers;

	@Column
	private int startOdometer;

	@Column
	private int endOdometer;

	@Column(nullable = false)
	private String pickupLine1;

	@Column
	private String pickupLine2;

	@Column (nullable = false)
	private String pickupCity;

	@Column(nullable = false)
	private String pickupZip;

	@Column(nullable = false)
	private String dropoffLine1;

	@Column
	private String dropoffLine2;

	@Column(nullable = false)
	private String dropoffCity;

	@Column(nullable = false)
	private String dropoffZip;

	@Enumerated(EnumType.STRING)
	private RideRequestStatus status;

	@Column(nullable = true)
	private String cancelMessage;

	protected RideRequest() { }

	public RideRequest(int requestorId, String requestorFirstName, String requestorLastName,
					   String requestorContactNumber, int numPassengers, String pickupLine1, String pickupCity,
					   String pickupZip, String dropoffLine1, String dropoffCity, String dropoffZip ) {
		super();
		this.requestorId = requestorId;
		this.date = new Date();
		this.requestorFirstName = requestorFirstName;
		this.requestorLastName = requestorLastName;
		this.requestorContactNumber = requestorContactNumber;
		this.numPassengers = numPassengers;
		this.pickupLine1 = pickupLine1;
		this.pickupCity = pickupCity;
		this.pickupZip = pickupZip;
		this.dropoffLine1 = dropoffLine1;
		this.dropoffCity = dropoffCity;
		this.dropoffZip = dropoffZip;
		this.status = RideRequestStatus.UNASSIGNED;
	}

	public Driver getDriver() {
		return driver;
	}

	public void setDriver(Driver driver) {
		this.driver = driver;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public int getRequestorId() {
		return requestorId;
	}

	public void setRequestorId(int requestorId) {
		this.requestorId = requestorId;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
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

	public String getRequestorContactNumber() {
		return requestorContactNumber;
	}

	public void setRequestorContactNumber(String requestorContactNumber) {
		this.requestorContactNumber = requestorContactNumber;
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

	public String getPickupZip() {
		return pickupZip;
	}

	public void setPickupZip(String pickupZip) {
		this.pickupZip = pickupZip;
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

	public String getDropoffZip() {
		return dropoffZip;
	}

	public void setDropoffZip(String dropoffZip) {
		this.dropoffZip = dropoffZip;
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


	@Override
	public String toString() {
		return "RideRequest{" +
				"driver=" + driver +
				", id=" + id +
				", requestorId=" + requestorId +
				", date=" + date +
				", requestorFirstName='" + requestorFirstName + '\'' +
				", requestorLastName='" + requestorLastName + '\'' +
				", requestorContactNumber='" + requestorContactNumber + '\'' +
				", numPassengers=" + numPassengers +
				", startOdometer=" + startOdometer +
				", endOdometer=" + endOdometer +
				", pickupLine1='" + pickupLine1 + '\'' +
				", pickupLine2='" + pickupLine2 + '\'' +
				", pickupCity='" + pickupCity + '\'' +
				", pickupZip='" + pickupZip + '\'' +
				", dropoffLine1='" + dropoffLine1 + '\'' +
				", dropoffLine2='" + dropoffLine2 + '\'' +
				", dropoffCity='" + dropoffCity + '\'' +
				", dropoffZip='" + dropoffZip + '\'' +
				", status=" + status +
				", cancelMessage=" + cancelMessage +
				'}';
	}
}

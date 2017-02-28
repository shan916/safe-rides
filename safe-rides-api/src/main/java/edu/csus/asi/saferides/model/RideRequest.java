package edu.csus.asi.saferides.model;

import javax.persistence.*;
import java.util.Date;

@Entity
public class RideRequest {

	@ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	private Driver driver;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long requestId;

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

	@Column(nullable = true)
	private int startOdometer;

	@Column(nullable = true)
	private int endOdometer;

	@Column(nullable = false)
	private String startAddress;

	@Column(nullable = false)
	private String endAddress;

	@Column(nullable = false)
	private double startLatitude;

	@Column(nullable = false)
	private double endLatitude;

	@Column(nullable = false)
	private double startLongitude;

	@Column(nullable = false)
	private double endLongitude;

	@Enumerated(EnumType.STRING)
	private Status status;

	protected RideRequest() { }

	public RideRequest(int requestorId, String requestorFirstName, String requestorLastName,
			String requestorContactNumber, int numPassengers, String startAddress,
			String endAddress, double startLatitude, double endLatitude, double startLongitude, double endLongitude) {
		super();
		this.requestorId = requestorId;
		this.date = new Date();
		this.requestorFirstName = requestorFirstName;
		this.requestorLastName = requestorLastName;
		this.requestorContactNumber = requestorContactNumber;
		this.numPassengers = numPassengers;
		this.startAddress = startAddress;
		this.endAddress = endAddress;
		this.startLatitude = startLatitude;
		this.endLatitude = endLatitude;
		this.startLongitude = startLongitude;
		this.endLongitude = endLongitude;
		this.status = Status.Unassigned;
	}

	public Driver getDriver() {
		return driver;
	}

	public void setDriver(Driver driver) {
		this.driver = driver;
	}

	public Long getRequestId() {
		return requestId;
	}

	public void setRequestId(Long requestId) {
		this.requestId = requestId;
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

	public String getStartAddress() {
		return startAddress;
	}

	public void setStartAddress(String startAddress) {
		this.startAddress = startAddress;
	}

	public String getEndAddress() {
		return endAddress;
	}

	public void setEndAddress(String endAddress) {
		this.endAddress = endAddress;
	}

	public double getStartLatitude() {
		return startLatitude;
	}

	public void setStartLatitude(double startLatitude) {
		this.startLatitude = startLatitude;
	}

	public double getEndLatitude() {
		return endLatitude;
	}

	public void setEndLatitude(double endLatitude) {
		this.endLatitude = endLatitude;
	}

	public double getStartLongitude() {
		return startLongitude;
	}

	public void setStartLongitude(double startLongitude) {
		this.startLongitude = startLongitude;
	}

	public double getEndLongitude() {
		return endLongitude;
	}

	public void setEndLongitude(double endLongitude) {
		this.endLongitude = endLongitude;
	}

	@Override
	public String toString() {
		return "RideRequest [driver=" + driver + ", requestId=" + requestId + ", requestorId=" + requestorId + ", date="
				+ date + ", requestorFirstName=" + requestorFirstName + ", requestorLastName=" + requestorLastName
				+ ", requestorContactNumber=" + requestorContactNumber + ", numPassengers=" + numPassengers
				+ ", startOdometer=" + startOdometer + ", endOdometer=" + endOdometer + ", startAddress=" + startAddress
				+ ", endAddress=" + endAddress + ", startLatitude=" + startLatitude + ", endLatitude=" + endLatitude
				+ ", startLongitude=" + startLongitude + ", endLongitude=" + endLongitude + "]";
	}
}

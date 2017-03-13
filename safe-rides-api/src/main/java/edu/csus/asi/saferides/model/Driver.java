package edu.csus.asi.saferides.model;

import java.util.Set;

import javax.persistence.*;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonIgnore;

/*
 * @author Zeeshan Khaliq
 * 
 * Model object for Driver Entity
 * */

@Entity
public class Driver {
	@OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	private Vehicle vehicle;

	@JsonIgnore
	@OneToMany(mappedBy = "driver")
	private Set<RideRequest> rides;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	@Column(nullable = false, unique = true, length = 9)
	@Size(min = 9, max = 9)
	private String csusId;
	
	@Column(nullable = false)
	@Size(min = 2, max = 30)
	private String driverFirstName;
	
	@Column(nullable = false)
	@Size(min = 2, max = 30)
	private String driverLastName;
	
	@Column(nullable = false, length = 10)
	@Size(min = 10, max = 10)
	private String phoneNumber;
	
	@Column(nullable = false, length = 2)
	private String dlState;
	
	@Column(nullable = false, unique = true)
	private String dlNumber;
	
	@Column(nullable = false)
	private String gender;
	
	@Column(nullable = false)
	private Boolean insuranceChecked;
	
	@Column(nullable = false)
	private Boolean active;

	@Transient
	private DriverStatus status;
	
	protected Driver() { }
	
	public Driver(String csusId, String driverFirstName, String driverLastName, String phoneNumber, String dlState, String dlNumber, String gender, Boolean insuranceChecked, Boolean active) {
		super();
		this.csusId = csusId;
		this.driverFirstName = driverFirstName;
		this.driverLastName = driverLastName;
		this.phoneNumber = phoneNumber;
		this.dlState = dlState;
		this.dlNumber = dlNumber;
		this.gender = gender;
		this.insuranceChecked = insuranceChecked;
		this.active = active;
	}
	
	public Vehicle getVehicle() {
		return vehicle;
	}
	
	public void setVehicle(Vehicle vehicle) {
		this.vehicle = vehicle;
	}
	
	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}
	
	public String getCsusId() {
		return csusId;
	}

	public void setCsusId(String csusId) {
		this.csusId = csusId;
	}

	public String getdriverFirstName() {
		return driverFirstName;
	}
	
	public void setdriverFirstName(String name) {
		this.driverFirstName = name;
	}
	
	public String getdriverLastName() {
		return driverLastName;
	}
	
	public void setdriverLastName(String name) {
		this.driverLastName = name;
	}
	
	public String getPhoneNumber() {
		return phoneNumber;
	}
	
	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
	
	public String getDlState() {
		return dlState;
	}

	public void setDlState(String dlState) {
		this.dlState = dlState;
	}

	public String getDlNumber() {
		return dlNumber;
	}
	
	public void setDlNumber(String dlNumber) {
		this.dlNumber = dlNumber;
	}
	
	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public Boolean isInsuranceChecked() {
		return insuranceChecked;
	}
	
	public void setInsuranceChecked(Boolean insuranceChecked) {
		this.insuranceChecked = insuranceChecked;
	}
	
	public Boolean isActive() {
		return active;
	}
	
	public void setActive(Boolean active) {
		this.active = active;
	}

	public Set<RideRequest> getRides() {
		return rides;
	}

	public void setRides(Set<RideRequest> rides) {
		this.rides = rides;
	}

	public DriverStatus getStatus() {
		return status;
	}

	public void setStatus(DriverStatus status) {
		this.status = status;
	}

	@Override
	public String toString() {
		return "Driver [vehicle=" + vehicle + ", rides=" + rides + ", id=" + id + ", csusId=" + csusId
				+ ", driverFirstName=" + driverFirstName + ", driverLastName=" + driverLastName + ", phoneNumber="
				+ phoneNumber + ", dlState=" + dlState + ", dlNumber=" + dlNumber + ", gender=" + gender
				+ ", insuranceChecked=" + insuranceChecked + ", active=" + active + "]";
	}

}

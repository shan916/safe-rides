package edu.csus.asi.saferides.model;

import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

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
	@GeneratedValue(strategy = GenerationType.AUTO) // Will generate a unique id automatically
	private Long id;
	
	@Column(nullable = false, unique = true)
	private String csusId;
	
	@Column(nullable = false)
	private String name;
	
	@Column(nullable = false)
	private String dlState;
	
	@Column(nullable = false, unique = true)
	private String dlNumber;
	
	@Column(nullable = false)
	private String sex;
	
	@Column(nullable = false)
	private Boolean insuranceChecked;
	
	@Column(nullable = false)
	private Boolean active;
	
	// protected Constructor required for JPA
	protected Driver() { }
	
	public Driver(String csusId, String name, String dlNumber, String dlState, String sex, Boolean insuranceChecked, Boolean active) {
		super();
		this.csusId = csusId;
		this.name = name;
		this.dlNumber = dlNumber;
		this.dlState = dlState;
		this.sex = sex;
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

	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
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
	
	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
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

	public void assignRideRequest(RideRequest rideRequest){
		rideRequest.setStatus(Status.ASSIGNED);
		Set<RideRequest> reqs = getRides();
		reqs.add(rideRequest);
		setRides(reqs);
	}

	@Override
	public String toString() {
		return "Driver [id=" + id + ", csusId=" + csusId + ", name=" + name + ", dlNumber=" + dlNumber
				+ ", insuranceChecked=" + insuranceChecked + ", active=" + active + "]";
	}

}

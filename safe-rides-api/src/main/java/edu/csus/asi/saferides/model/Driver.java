package edu.csus.asi.saferides.model;

import java.util.Date;
import java.util.HashSet;
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
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Transient;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonIgnore;
import edu.csus.asi.saferides.security.model.User;

/*
 * @author Zeeshan Khaliq
 *
 * Model object for Driver Entity
 * */

@Entity
public class Driver {

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
	@Size(min = 3)
	private String insuranceCompany;

	@Column(nullable = false)
	private Boolean active;

	@Transient
	DriverStatus status;

	@JsonIgnore
	@Column(updatable = false)
	private Date createdDate;

	@JsonIgnore
	private Date modifiedDate;

	@OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	private Vehicle vehicle;

	@JsonIgnore
	@OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	private User user;

	@JsonIgnore
	@OneToMany(mappedBy = "driver")
	private Set<RideRequest> rides;

	protected Driver() {
	}

	public Driver(String csusId, String driverFirstName, String driverLastName, String phoneNumber, String dlState,
				  String dlNumber, String gender, Boolean insuranceChecked, String insuranceCompany, Boolean active) {
		super();
		this.csusId = csusId;
		this.driverFirstName = driverFirstName;
		this.driverLastName = driverLastName;
		this.phoneNumber = phoneNumber;
		this.dlState = dlState;
		this.dlNumber = dlNumber;
		this.gender = gender;
		this.insuranceChecked = insuranceChecked;
		this.insuranceCompany = insuranceCompany;
		this.active = active;
		this.user = new User(csusId, driverFirstName, driverLastName, "pass", "email@email.email");
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
		if (this.rides == null) {
			return new HashSet<RideRequest>();
		}
		return rides;
	}

	public void setRides(Set<RideRequest> rides) {
		this.rides = rides;
	}

	public DriverStatus getStatus() {
		for (RideRequest ride : getRides()) {
			if (ride.getStatus() == RideRequestStatus.ASSIGNED) {
				return DriverStatus.ASSIGNED;
			} else if (ride.getStatus() == RideRequestStatus.INPROGRESS) {
				return DriverStatus.INPROGRESS;
			} else {
				return DriverStatus.AVAILABLE;
			}
		}

		return DriverStatus.AVAILABLE;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public Date getModifiedDate() {
		return modifiedDate;
	}

	public String getInsuranceCompany() {
		return insuranceCompany;
	}

	public void setInsuranceCompany(String insuranceCompany) {
		this.insuranceCompany = insuranceCompany;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	@PreUpdate
	@PrePersist
	public void updateTimeStamps() {
		modifiedDate = new Date();
		if (createdDate == null) {
			createdDate = new Date();
		}
	}

	@Override
	public String toString() {
		return "Driver{" +
				"id=" + id +
				", csusId='" + csusId + '\'' +
				", driverFirstName='" + driverFirstName + '\'' +
				", driverLastName='" + driverLastName + '\'' +
				", phoneNumber='" + phoneNumber + '\'' +
				", dlState='" + dlState + '\'' +
				", dlNumber='" + dlNumber + '\'' +
				", gender='" + gender + '\'' +
				", insuranceChecked=" + insuranceChecked +
				", insuranceCompany='" + insuranceCompany + '\'' +
				", active=" + active +
				", status=" + status +
				", createdDate=" + createdDate +
				", modifiedDate=" + modifiedDate +
				", vehicle=" + vehicle +
				", user=" + user +
				", rides=" + rides +
				'}';
	}
}
package edu.csus.asi.saferides.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import edu.csus.asi.saferides.security.model.User;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

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

	@Column(nullable = false, unique = true, length = 20)
	private String dlNumber;

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
	@OneToMany(mappedBy = "driver")
	private Set<RideRequest> rides;

	@JsonIgnore
	@OneToMany(mappedBy = "driver")
	private Set<DriverLocation> locations;

	@JsonIgnore
	@ManyToOne(fetch=FetchType.LAZY)
	private User user;

	@PreUpdate
	@PrePersist
	public void updateTimeStamps() {
		modifiedDate = new Date();
		if (createdDate == null) {
			createdDate = new Date();
		}
	}

	protected Driver() {
	}

	public Driver(String csusId, String driverFirstName, String driverLastName, String phoneNumber, String dlState,
				  String dlNumber, Boolean insuranceChecked, String insuranceCompany, Boolean active) {
		super();
		this.csusId = csusId;
		this.driverFirstName = driverFirstName;
		this.driverLastName = driverLastName;
		this.phoneNumber = phoneNumber;
		this.dlState = dlState;
		this.dlNumber = dlNumber;
		this.insuranceChecked = insuranceChecked;
		this.insuranceCompany = insuranceCompany;
		this.active = active;
		this.user = new User(csusId, driverFirstName, driverLastName, "pass", "driver@null.null");
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

	public void setStatus(DriverStatus status) {
		this.status = status;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public Date getModifiedDate() {
		return modifiedDate;
	}

	public void setModifiedDate(Date modifiedDate) {
		this.modifiedDate = modifiedDate;
	}

	public Vehicle getVehicle() {
		return vehicle;
	}

	public void setVehicle(Vehicle vehicle) {
		this.vehicle = vehicle;
	}

	public Set<RideRequest> getRides() {
		if (this.rides == null) {
			return new HashSet<RideRequest>();
		}
		return rides;
	}

	public DriverStatus getStatus() {
        boolean assigned = false;
        boolean pickingUp = false;

        for (RideRequest ride : getRides()) {
            RideRequestStatus rideStatus = ride.getStatus();

            switch (rideStatus) {
                case DROPPINGOFF:
                    return DriverStatus.DROPPINGOFF;
                case PICKINGUP:
                    pickingUp = true;
                    break;
                case ASSIGNED:
                    assigned = true;
                    break;
                default:
                    break;
            }
        }

        if (pickingUp) {
            return DriverStatus.PICKINGUP;
        } else if (assigned) {
            return DriverStatus.ASSIGNED;
        } else {
            return DriverStatus.AVAILABLE;
        }
  }

	public Set<DriverLocation> getLocations() {
		return locations;
	}

	public void setLocations(Set<DriverLocation> locations) {
		this.locations = locations;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
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
				", insuranceChecked=" + insuranceChecked +
				", insuranceCompany='" + insuranceCompany + '\'' +
				", active=" + active +
				", status=" + status +
				", createdDate=" + createdDate +
				", modifiedDate=" + modifiedDate +
				", vehicle=" + vehicle +
				", rides=" + rides +
				", locations=" + locations +
				", user=" + user +
				'}';
	}
}

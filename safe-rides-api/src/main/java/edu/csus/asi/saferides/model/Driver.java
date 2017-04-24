package edu.csus.asi.saferides.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import edu.csus.asi.saferides.security.model.User;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * Model object for Driver Entity
 */

@Entity
public class Driver {

    /**
     * Primary key
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    /**
     * CSUS ID of driver
     */
    @Column(nullable = false, unique = true, length = 9)
    @Size(min = 9, max = 9)
    private String csusId;

    /**
     * First name of the driver
     */
    @Column(nullable = false)
    @Size(min = 2, max = 30)
    private String driverFirstName;

    /**
     * Last name of the driver
     */
    @Column(nullable = false)
    @Size(min = 2, max = 30)
    private String driverLastName;

    /**
     * Phone number of the driver
     */
    @Column(nullable = false, length = 10)
    @Size(min = 10, max = 10)
    private String phoneNumber;

    /**
     * Abbreviation name of the U.S. state on the driver's license
     */
    @Column(nullable = false, length = 2)
    private String dlState;

    /**
     * Driver's license number
     */
    @Column(nullable = false, unique = true, length = 20)
    private String dlNumber;

    /**
     * Is insurance checked
     */
    @Column(nullable = false)
    private Boolean insuranceChecked;

    /**
     * Name of the insurance company
     */
    @Column(nullable = false)
    @Size(min = 3)
    private String insuranceCompany;

    /**
     * Is driver active
     */
    @Column(nullable = false)
    private Boolean active;

    /**
     * JPA indicates not to serialized the status field, that is, it is not to
     * be persisted in the database because they have different meanings.
     */
    @Transient
    DriverStatus status;

    /**
     * Date of creation
     */
    @JsonIgnore
    @Column(updatable = false)
    private Date createdDate;

    /**
     * Date of modification
     */
    @JsonIgnore
    private Date modifiedDate;

    /**
     * One-to-one relationship for vehicle
     */
    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Vehicle vehicle;

    /**
     * One-to-many relationship for setting ride requests to a driver
     */
    @JsonIgnore
    @OneToMany(mappedBy = "driver")
    private Set<RideRequest> rides;

    /**
     * One-to-many relationship for setting driver locations to a driver
     */
    @JsonIgnore
    @OneToMany(mappedBy = "driver")
    private Set<DriverLocation> locations;

    /**
     * user field is hidden from other classes other than User class object
     */
    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

	@Column(nullable = false)
	private long endOfNightOdo;

    /**
     * Updates time stamp date if modified or creates a new date
     * if one doesn't already exist.
     */
    @PreUpdate
    @PrePersist
    public void updateTimeStamps() {
        modifiedDate = new Date();
        if (createdDate == null) {
            createdDate = new Date();
        }
    }

    /**
     * Constructor used by JPA
     */
    protected Driver() {
    }

    /**
     * Constructor for creating a driver object
     *
     * @param csusId           the CSUS ID of the driver
     * @param driverFirstName  the driver's first name
     * @param driverLastName   the driver's last name
     * @param phoneNumber      the phone number for the driver
     * @param dlState          the driver license state abbreviation
     * @param dlNumber         the driver license number
     * @param insuranceChecked indicates if insurance has been checked for the drive
     * @param insuranceCompany the driver's insurance company
     * @param active           indicates if the driver is active
     */
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

    /**
     * Get ID
     *
     * @return ID
     */
    public Long getId() {
        return id;
    }

    /**
     * Set ID
     *
     * @param id
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Get CSUS ID
     *
     * @return csusId
     */
    public String getCsusId() {
        return csusId;
    }

    /**
     * Set CSUS ID
     *
     * @param csusId
     */
    public void setCsusId(String csusId) {
        this.csusId = csusId;
    }

    /**
     * Get driver's first name
     *
     * @return driverFirstName
     */
    public String getDriverFirstName() {
        return driverFirstName;
    }

    /**
     * Set driver's first name
     *
     * @param driverFirstName
     */
    public void setDriverFirstName(String driverFirstName) {
        this.driverFirstName = driverFirstName;
    }

    /**
     * Get driver's last name
     *
     * @return the driver's last name
     */
    public String getDriverLastName() {
        return driverLastName;
    }

    /**
     * Set driver's last name
     *
     * @param driverLastName
     */
    public void setDriverLastName(String driverLastName) {
        this.driverLastName = driverLastName;
    }

    /**
     * Get driver's phone number
     *
     * @return phoneNumber
     */
    public String getPhoneNumber() {
        return phoneNumber;
    }

    /**
     * Set driver's phone number
     *
     * @param phoneNumber
     */
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    /**
     * Get abbreviation name of the U.S. state on the driver's license
     *
     * @return dlState
     */
    public String getDlState() {
        return dlState;
    }

    /**
     * Set abbreviation name of the U.S. state on the driver's license
     *
     * @param dlState
     */
    public void setDlState(String dlState) {
        this.dlState = dlState;
    }

    /**
     * Get driver's license number
     *
     * @return dlNumber
     */
    public String getDlNumber() {
        return dlNumber;
    }

    /**
     * Set driver's license number
     *
     * @param dlNumber
     */
    public void setDlNumber(String dlNumber) {
        this.dlNumber = dlNumber;
    }

    /**
     * Get boolean if whether insurance is checked or not
     *
     * @return insuranceChecked
     */
    public Boolean getInsuranceChecked() {
        return insuranceChecked;
    }

    /**
     * Set boolean if whether insurance is checked or not
     *
     * @param insuranceChecked
     */
    public void setInsuranceChecked(Boolean insuranceChecked) {
        this.insuranceChecked = insuranceChecked;
    }

    /**
     * Get insurance company name
     *
     * @return insuranceCompany
     */
    public String getInsuranceCompany() {
        return insuranceCompany;
    }

    /**
     * Set insurance company name
     *
     * @param insuranceCompany
     */
    public void setInsuranceCompany(String insuranceCompany) {
        this.insuranceCompany = insuranceCompany;
    }

    /**
     * Get boolean if whether driver is active or not
     *
     * @return active
     */
    public Boolean getActive() {
        return active;
    }

    /**
     * Set boolean if whether driver is active or not
     *
     * @param active
     */
    public void setActive(Boolean active) {
        this.active = active;
    }

    /**
     * Set driver's status
     *
     * @param status
     */
    public void setStatus(DriverStatus status) {
        this.status = status;
    }

    /**
     * Get created date
     *
     * @return createdDate
     */
    public Date getCreatedDate() {
        return createdDate;
    }

    /**
     * Set created date
     *
     * @param createdDate
     */
    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    /**
     * Get modified date
     *
     * @return modifiedDate
     */
    public Date getModifiedDate() {
        return modifiedDate;
    }

    /**
     * Set modified date
     *
     * @param modifiedDate
     */
    public void setModifiedDate(Date modifiedDate) {
        this.modifiedDate = modifiedDate;
    }

    /**
     * Get vehicle
     *
     * @return vehicle
     */
    public Vehicle getVehicle() {
        return vehicle;
    }

    /**
     * Set vehicle
     *
     * @param vehicle
     */
    public void setVehicle(Vehicle vehicle) {
        this.vehicle = vehicle;
    }

    /**
     * Get the driver's end of night odometer
     *
     * @return the driver's end of night odometer
     */
	public long getEndOfNightOdo() {
		return endOfNightOdo;
	}

    /**
     * Set the driver's end of night odometer
     *
     * @param endOfNightOdo the driver's end of night odometer
     */
	public void setEndOfNightOdo(long endOfNightOdo) {
		this.endOfNightOdo = endOfNightOdo;
	}

    /**
     * Get rides. If there are no ride request it returns a new
     * hash set of ride requests.
     *
     * @return rides
     */
    public Set<RideRequest> getRides() {
        if (this.rides == null) {
            return new HashSet<RideRequest>();
        }
        return rides;
    }

    /**
     * Gets the status of the driver whether they are assigned a ride request or
     * currently picking up a rider or dropping off a rider or are available.
     *
     * @return driver status
     */
    public DriverStatus getStatus() {
        boolean assigned = false;
        boolean pickingUp = false;
		boolean atPickupLocation = false;

        for (RideRequest ride : getRides()) {
            RideRequestStatus rideStatus = ride.getStatus();

            switch (rideStatus) {
                case DROPPINGOFF:
                    return DriverStatus.DROPPINGOFF;
                case PICKINGUP:
                    pickingUp = true;
                    break;
				case ATPICKUPLOCATION:
					atPickupLocation = true;
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
        } else if(atPickupLocation){
        	return DriverStatus.ATPICKUPLOCATION;
		} else if (assigned) {
            return DriverStatus.ASSIGNED;
        } else {
            return DriverStatus.AVAILABLE;
        }
    }

    /**
     * Get driver's locations
     *
     * @return locations
     */
    public Set<DriverLocation> getLocations() {
        return locations;
    }

    /**
     * Set driver's locations
     *
     * @param locations
     */
    public void setLocations(Set<DriverLocation> locations) {
        this.locations = locations;
    }

    /**
     * Get user
     *
     * @return user
     */
    public User getUser() {
        return user;
    }

    /**
     * Set user
     *
     * @param user
     */
    public void setUser(User user) {
        this.user = user;
    }

    /**
     * Gets a string object representing the values of the Driver Object
     *
     * @return Driver string object
     */
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
                ", endOfNightOdometer=" + endOfNightOdometer +
                '}';
    }
}

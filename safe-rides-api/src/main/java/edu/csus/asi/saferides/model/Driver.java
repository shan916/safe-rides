package edu.csus.asi.saferides.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import edu.csus.asi.saferides.security.ArgonPasswordEncoder;
import edu.csus.asi.saferides.security.model.User;
import edu.csus.asi.saferides.utility.Util;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.time.ZoneId;
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
     * OneCard ID of driver
     */
    @Column(nullable = false, unique = true, length = 9)
    @Size(min = 9, max = 9)
    private String oneCardId;

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
    private DriverStatus status;

    /**
     * Date of creation
     */
    @JsonIgnore
    @Column(updatable = false)
    private LocalDateTime createdDate;

    /**
     * Date of modification
     */
    @JsonIgnore
    private LocalDateTime modifiedDate;

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

    /**
     * the driver's odometer reading at the end of the night
     */
    @Column(nullable = false)
    private long endOfNightOdo;

    /**
     * Updates time stamp date if modified or creates a new date
     * if one doesn't already exist.
     */
    @PreUpdate
    @PrePersist
    public void updateTimeStamps() {
        LocalDateTime now = LocalDateTime.now(ZoneId.of(Util.APPLICATION_TIME_ZONE));
        modifiedDate = now;
        if (createdDate == null) {
            createdDate = now;
        }
    }

    /**
     * Constructor used by JPA to construct a Driver entity
     */
    protected Driver() {
    }

    /**
     * Constructor for creating a driver object
     *
     * @param oneCardId        the OneCard ID of the driver
     * @param driverFirstName  the driver's first name
     * @param driverLastName   the driver's last name
     * @param phoneNumber      the phone number for the driver
     * @param dlState          the driver license state abbreviation
     * @param dlNumber         the driver license number
     * @param insuranceChecked indicates if insurance has been checked for the drive
     * @param insuranceCompany the driver's insurance company
     * @param active           indicates if the driver is active
     */
    public Driver(String oneCardId, String driverFirstName, String driverLastName, String phoneNumber, String dlState,
                  String dlNumber, Boolean insuranceChecked, String insuranceCompany, Boolean active) {
        super();
        this.oneCardId = oneCardId;
        this.driverFirstName = driverFirstName;
        this.driverLastName = driverLastName;
        this.phoneNumber = phoneNumber;
        this.dlState = dlState;
        this.dlNumber = dlNumber;
        this.insuranceChecked = insuranceChecked;
        this.insuranceCompany = insuranceCompany;
        this.active = active;
        this.user = new User(oneCardId, driverFirstName, driverLastName);
        this.user.setPassword((new ArgonPasswordEncoder().encode("pass")));
    }

    /**
     * Get primary key
     *
     * @return primary key
     */
    public Long getId() {
        return id;
    }

    /**
     * Set primary key
     *
     * @param id the primary key
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Get the OneCard ID of the driver
     *
     * @return the OneCard ID of the driver
     */
    public String getOneCardId() {
        return oneCardId;
    }

    /**
     * Set the OneCard ID of the driver
     *
     * @param oneCardId the OneCard ID of the driver
     */
    public void setOneCardId(String oneCardId) {
        this.oneCardId = oneCardId;
    }

    /**
     * Get driver's first name
     *
     * @return the driver's first name
     */
    public String getDriverFirstName() {
        return driverFirstName;
    }

    /**
     * Set the driver's first name
     *
     * @param driverFirstName the driver's first name
     */
    public void setDriverFirstName(String driverFirstName) {
        this.driverFirstName = driverFirstName;
    }

    /**
     * Get the driver's last name
     *
     * @return the driver's last name
     */
    public String getDriverLastName() {
        return driverLastName;
    }

    /**
     * Set the driver's last name
     *
     * @param driverLastName the driver's last name
     */
    public void setDriverLastName(String driverLastName) {
        this.driverLastName = driverLastName;
    }

    /**
     * Get driver's phone number
     *
     * @return driver's phone number
     */
    public String getPhoneNumber() {
        return phoneNumber;
    }

    /**
     * Set driver's phone number
     *
     * @param phoneNumber driver's phone number
     */
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    /**
     * Get abbreviation name of the U.S. state on the driver's license
     *
     * @return abbreviation name of the U.S. state on the driver's license
     */
    public String getDlState() {
        return dlState;
    }

    /**
     * Set abbreviation name of the U.S. state on the driver's license
     *
     * @param dlState abbreviation name of the U.S. state on the driver's license
     */
    public void setDlState(String dlState) {
        this.dlState = dlState;
    }

    /**
     * Get driver's license number
     *
     * @return driver's license number
     */
    public String getDlNumber() {
        return dlNumber;
    }

    /**
     * Set driver's license number
     *
     * @param dlNumber driver's license number
     */
    public void setDlNumber(String dlNumber) {
        this.dlNumber = dlNumber;
    }

    /**
     * Get boolean if whether insurance is checked or not
     *
     * @return whether insurance is checked or not
     */
    public Boolean getInsuranceChecked() {
        return insuranceChecked;
    }

    /**
     * Set boolean indicating whether insurance is checked or not
     *
     * @param insuranceChecked whether insurance is checked or not
     */
    public void setInsuranceChecked(Boolean insuranceChecked) {
        this.insuranceChecked = insuranceChecked;
    }

    /**
     * Get insurance company name
     *
     * @return insurance company name
     */
    public String getInsuranceCompany() {
        return insuranceCompany;
    }

    /**
     * Set insurance company name
     *
     * @param insuranceCompany insurance company name
     */
    public void setInsuranceCompany(String insuranceCompany) {
        this.insuranceCompany = insuranceCompany;
    }

    /**
     * Get boolean indicating whether driver is active or not
     *
     * @return whether driver is active or not
     */
    public Boolean getActive() {
        return active;
    }

    /**
     * Set boolean indicating whether driver is active or not
     *
     * @param active whether driver is active or not
     */
    public void setActive(Boolean active) {
        this.active = active;
    }

    /**
     * Set driver's status
     *
     * @param status the driver's status
     */
    public void setStatus(DriverStatus status) {
        this.status = status;
    }

    /**
     * Get created date
     *
     * @return creation date of the driver
     */
    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    /**
     * Set created date
     *
     * @param createdDate the creation date of the driver
     */
    public void setCreatedDate(LocalDateTime createdDate) {
        this.createdDate = createdDate;
    }

    /**
     * Get modified date
     *
     * @return last modified date
     */
    public LocalDateTime getModifiedDate() {
        return modifiedDate;
    }

    /**
     * Set modified date
     *
     * @param modifiedDate modified date of driver
     */
    public void setModifiedDate(LocalDateTime modifiedDate) {
        this.modifiedDate = modifiedDate;
    }

    /**
     * Get vehicle owned by the driver
     *
     * @return vehicle owned by the driver
     */
    public Vehicle getVehicle() {
        return vehicle;
    }

    /**
     * Set vehicle owned by the driver
     *
     * @param vehicle vehicle owned by the driver
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
        } else if (atPickupLocation) {
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
     * @param locations driver's locations
     */
    public void setLocations(Set<DriverLocation> locations) {
        this.locations = locations;
    }

    /**
     * Get user associated with driver
     *
     * @return user associated with driver
     */
    public User getUser() {
        return user;
    }

    /**
     * Set user associated with driver
     *
     * @param user user associated with driver
     */
    public void setUser(User user) {
        this.user = user;
    }

    /**
     * Gets a string object representing the values of the Driver Object
     *
     * @return Driver string object
     */
/*    @Override
    public String toString() {
        return "Driver{" +
                "id=" + id +
                ", oneCardId='" + oneCardId + '\'' +
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
                ", endOfNightOdo=" + endOfNightOdo +
                '}';
    }
    */
}

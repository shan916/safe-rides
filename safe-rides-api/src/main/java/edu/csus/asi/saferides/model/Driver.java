package edu.csus.asi.saferides.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import edu.csus.asi.saferides.utility.Util;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Optional;
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

    @Version
    private Timestamp version;

    /**
     * Phone number of the driver
     */
    @Column(nullable = false, length = 10)
    @Size(min = 10, max = 10)
    private String phoneNumber;

    /**
     * Indicates if driver's license has been checked
     */
    @Column(nullable = false)
    private Boolean dlChecked;

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
     * JPA indicates not to serialized the status field, that is, it is not to
     * be persisted in the database because they have different meanings.
     */
    @Transient
    private DriverLocation latestDriverLocation;

    /**
     * JPA indicates not to serialized the status field, that is, it is not to
     * be persisted in the database because they have different meanings.
     */
    @Transient
    private DriverLocation latestRideRequest;

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
     * The driver's user object
     */
    @JsonIgnore
    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
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
    public Driver() {
        // set to public because orika attempts to initialize it for soem reason and the following is thrown:
        // java.lang.IllegalAccessError: tried to access method edu.csus.asi.saferides.model.Driver.<init>()V from class edu.csus.asi.saferides.model.Driver_Driver_ObjectFactory252669379489596252677445534647$8
    }

    /**
     * Constructor for creating a driver object
     *
     * @param oneCardId        the OneCard ID of the driver
     * @param driverFirstName  the driver's first name
     * @param driverLastName   the driver's last name
     * @param phoneNumber      the phone number for the driver
     * @param dlChecked        indicates if driver's license has been checked
     * @param insuranceChecked indicates if insurance has been checked for the drive
     * @param insuranceCompany the driver's insurance company
     * @param active           indicates if the driver is active
     */
    public Driver(String oneCardId, String driverFirstName, String driverLastName, String phoneNumber,
                  Boolean dlChecked, Boolean insuranceChecked, String insuranceCompany, Boolean active) {
        super();
        this.phoneNumber = phoneNumber;
        this.dlChecked = dlChecked;
        this.insuranceChecked = insuranceChecked;
        this.insuranceCompany = insuranceCompany;
        this.user = new User(oneCardId, driverFirstName, driverLastName);
        this.user.setActive(active);
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
     * Get whether driver's license has been checked
     *
     * @return whether driver's license has been checked
     */
    public Boolean getDlChecked() {
        return dlChecked;
    }

    /**
     * Set whether driver's license has been checked
     *
     * @param dlChecked whether driver's license has been checked
     */
    public void setDlChecked(Boolean dlChecked) {
        this.dlChecked = dlChecked;
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
     * Gets the latest recorded location of the driver if exists
     *
     * @return drivers's location or null
     */
    public DriverLocation getLatestDriverLocation() {
        Set<DriverLocation> locations = getLocations();
        if (locations != null) {
            Optional<DriverLocation> latestLocation = locations.stream().max(Comparator.comparing(DriverLocation::getCreatedDate));
            if (latestLocation.isPresent()) {
                return latestLocation.get();
            }
        }
        return null;
    }

    /**
     * Gets the latest ride request assigned to the driver if exists
     *
     * @return ride requerst or null
     */
    public RideRequest getLatestRideRequest() {
        Set<RideRequest> rides = getRides();
        if (rides != null) {
            Optional<RideRequest> latestRideRequest = rides.stream().max(Comparator.comparing(RideRequest::getLastModified));
            if (latestRideRequest.isPresent()) {
                return latestRideRequest.get();
            }
        }
        return null;
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

    public Timestamp getVersion() {
        return version;
    }

    public void setVersion(Timestamp version) {
        this.version = version;
    }
}

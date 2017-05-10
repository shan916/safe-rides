package edu.csus.asi.saferides.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.time.ZoneId;

/**
 * A DriverLocation contains the longitude and latitude of a driver.
 * Since the driver position is not updated constantly, DriverLocation
 * also contains the time at which the location was last updated.
 */
@Entity
public class DriverLocation {
    /**
     * Primary key
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    /**
     * The latitude of the driver location
     */
    @Column(precision = 10, scale = 2)
    private Double latitude;

    /**
     * The longitude of the driver location
     */
    @Column(precision = 10, scale = 2)
    private Double longitude;

    /**
     * The date the driver location object was saved to the database
     */
    @JsonIgnore
    @Column(updatable = false)
    private LocalDateTime createdDate;

    /**
     * The driver that this location is mapped to
     */
    @ManyToOne
    private Driver driver;

    /**
     * Update the timestamp right before saving to the database
     */
    @PrePersist
    public void updateTimeStamps() {
        createdDate = LocalDateTime.now(ZoneId.of("America/Los_Angeles"));
    }

    /**
     * Constructor used by JPA
     */
    protected DriverLocation() {
    }

    /**
     * Constructor for creating a new DriverLocation object
     *
     * @param latitude  of the driver
     * @param longitude of the driver
     */
    public DriverLocation(Double latitude, Double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    /**
     * Get the primary key
     *
     * @return driver location's primary key
     */
    public Long getId() {
        return id;
    }

    /**
     * Set the primary key
     *
     * @param id of the driver location
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Get the latitude
     *
     * @return the driver's latitude
     */
    public Double getLatitude() {
        return latitude;
    }

    /**
     * Set the latitude
     *
     * @param latitude of the driver
     */
    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    /**
     * Get the Longitude
     *
     * @return the driver's longitude
     */
    public Double getLongitude() {
        return longitude;
    }

    /**
     * Set the longitude
     *
     * @param longitude of the driver
     */
    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    /**
     * Get the created date
     *
     * @return the date the drive location was saved to the database
     */
    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    /**
     * Set the created date
     *
     * @param createdDate of the driver location
     */
    public void setCreatedDate(LocalDateTime createdDate) {
        this.createdDate = createdDate;
    }

    /**
     * Get the driver
     *
     * @return the driver that this driver location is mapped to
     */
    public Driver getDriver() {
        return driver;
    }

    /**
     * Set the driver
     *
     * @param driver that owns this location
     */
    public void setDriver(Driver driver) {
        this.driver = driver;
    }

    /**
     * Gets the string representation of the Driver Object
     *
     * @return DriverLocation string object
     */
    @Override
    public String toString() {
        return "DriverLocation{" +
                "id=" + id +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                ", createdDate=" + createdDate +
                ", driver=" + driver +
                '}';
    }
}

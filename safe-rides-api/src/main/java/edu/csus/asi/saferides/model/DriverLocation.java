package edu.csus.asi.saferides.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.Date;

@Entity
public class DriverLocation {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(precision = 10, scale = 2)
    private Double latitude;

    @Column(precision = 10, scale = 2)
    private Double longitude;

    @JsonIgnore
    @Column(updatable = false)
    private Date createdDate;

    @JsonIgnore
    @ManyToOne
    private Driver driver;

    @PrePersist
    public void updateTimeStamps() {
        createdDate = new Date();
    }

    protected DriverLocation() {
    }

    public DriverLocation(Double latitude, Double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public Driver getDriver() {
        return driver;
    }

    public void setDriver(Driver driver) {
        this.driver = driver;
    }

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

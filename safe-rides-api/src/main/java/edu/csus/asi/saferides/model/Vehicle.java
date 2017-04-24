package edu.csus.asi.saferides.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import javax.validation.constraints.Min;

/*
 * @author Zeeshan Khaliq
 * 
 * Model object for Vehicle Entity
 * */

@Entity
public class Vehicle {

    /**
     * driver field is hidden from other classes other than Driver class object
     */
    @JsonIgnore
    @OneToOne
    private Driver driver;

    /**
     * Primary Key
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    /**
     * Vehicle's make
     */
    @Column(nullable = false)
    private String make;

    /**
     * Vehicle's model
     */
    @Column(nullable = false)
    private String model;

    /**
     * Vehicle's year
     */
    @Column(nullable = false, length = 4)
    private String year;

    /**
     * Vehicle's license plate
     */
    @Column(nullable = false)
    private String licensePlate;

    /**
     * Vehicle's color
     */
    @Column(nullable = false)
    private String color;

    /**
     * Vehicle's seats
     */
    @Column(nullable = false)
    @Min(2)
    private Integer seats;

    /**
     * Constructor used by JPA
     */
    protected Vehicle() {
    }

    /**
     * Constructor for creating a vehicle object
     *
     * @param driver
     * @param make
     * @param model
     * @param year
     * @param licensePlate
     * @param color
     * @param seats
     */
    public Vehicle(Driver driver, String make, String model, String year, String licensePlate, String color,
                   Integer seats) {
        super();
        this.driver = driver;
        this.make = make;
        this.model = model;
        this.year = year;
        this.licensePlate = licensePlate;
        this.color = color;
        this.seats = seats;
    }

    /**
     * Get driver
     *
     * @return driver
     */
    public Driver getDriver() {
        return driver;
    }

    /**
     * Set driver
     *
     * @param driver
     */
    public void setDriver(Driver driver) {
        this.driver = driver;
    }

    /**
     * Get vehicle's ID
     *
     * @return id
     */
    public Long getVehicleId() {
        return id;
    }

    /**
     * Set vehicle's ID
     *
     * @param vehicleId
     */
    public void setVehicleId(Long vehicleId) {
        this.id = vehicleId;
    }

    /**
     * Get vehicle's make
     *
     * @return make
     */
    public String getMake() {
        return make;
    }

    /**
     * Set vehicle's make
     *
     * @param make
     */
    public void setMake(String make) {
        this.make = make;
    }

    /**
     * Get vehicle's model
     *
     * @return model
     */
    public String getModel() {
        return model;
    }

    /**
     * Set vehicle's model
     *
     * @param model
     */
    public void setModel(String model) {
        this.model = model;
    }

    /**
     * Get vehicle's year
     *
     * @return
     */
    public String getYear() {
        return year;
    }

    /**
     * Set vehicle's year
     *
     * @param year
     */
    public void setYear(String year) {
        this.year = year;
    }

    /**
     * Get vehicle's license plate
     *
     * @return licensePlate
     */
    public String getLicensePlate() {
        return licensePlate;
    }

    /**
     * Set vehicle's license plate
     *
     * @param licensePlate
     */
    public void setLicensePlate(String licensePlate) {
        this.licensePlate = licensePlate;
    }

    /**
     * Get vehicle's color
     *
     * @return
     */
    public String getColor() {
        return color;
    }

    /**
     * Set vehicle's color
     *
     * @param color
     */
    public void setColor(String color) {
        this.color = color;
    }

    /**
     * Get vehicle's number of seats
     *
     * @return
     */
    public Integer getSeats() {
        return seats;
    }

    /**
     * Set vehicle's number of seats
     *
     * @param seats
     */
    public void setSeats(Integer seats) {
        this.seats = seats;
    }

    /**
     * Gets a string object representing the values of the Vehicle Object
     *
     * @return Vehicle string object
     */
    @Override
    public String toString() {
        return "Vehicle [driver=" + driver + ", id=" + id + ", make=" + make + ", model=" + model + ", year=" + year
                + ", licensePlate=" + licensePlate + ", color=" + color + ", seats=" + seats + "]";
    }

}

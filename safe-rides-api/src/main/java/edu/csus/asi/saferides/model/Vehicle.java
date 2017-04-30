package edu.csus.asi.saferides.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import javax.validation.constraints.Min;

/**
 * Model object for Vehicle Entity
 */

@Entity
public class Vehicle {

    /**
     * One to one relationship between Driver and Vehicle.
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
     * The number of seats in the vehicle including the driver seat
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
     * @param driver - the driver that owns the vehicle
     * @param make the make of the vehicle
     * @param model the model of the vehicle
     * @param year the year of the vehicle
     * @param licensePlate the license plate of the vehicle
     * @param color the color of the vehicle
     * @param seats the number of seats in the vehicle including the driver seat
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
     * @return the driver that owns the vehicle
     */
    public Driver getDriver() {
        return driver;
    }

    /**
     * Set driver
     *
     * @param driver the driver that owns the vehicle
     */
    public void setDriver(Driver driver) {
        this.driver = driver;
    }

    /**
     * Get primary key
     *
     * @return primary key of vehicle
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
     * Get vehicle's make
     *
     * @return the vehicle's make
     */
    public String getMake() {
        return make;
    }

    /**
     * Set vehicle's make
     *
     * @param make the vehicle's make
     */
    public void setMake(String make) {
        this.make = make;
    }

    /**
     * Get vehicle's model
     *
     * @return the vehicle's model
     */
    public String getModel() {
        return model;
    }

    /**
     * Set vehicle's model
     *
     * @param model the vehicle's model
     */
    public void setModel(String model) {
        this.model = model;
    }

    /**
     * Get vehicle's year
     *
     * @return the vehicle's year
     */
    public String getYear() {
        return year;
    }

    /**
     * Set vehicle's year
     *
     * @param year the vehicle's year
     */
    public void setYear(String year) {
        this.year = year;
    }

    /**
     * Get vehicle's license plate
     *
     * @return the vehicle's license plate
     */
    public String getLicensePlate() {
        return licensePlate;
    }

    /**
     * Set vehicle's license plate
     *
     * @param licensePlate the vehicle's license plate
     */
    public void setLicensePlate(String licensePlate) {
        this.licensePlate = licensePlate;
    }

    /**
     * Get vehicle's color
     *
     * @return the vehicle's color
     */
    public String getColor() {
        return color;
    }

    /**
     * Set vehicle's color
     *
     * @param color the vehicle's color
     */
    public void setColor(String color) {
        this.color = color;
    }

    /**
     * Get the number of seats in the vehicle including the driver seat
     *
     * @return the number of seats in the vehicle including the driver seat
     */
    public Integer getSeats() {
        return seats;
    }

    /**
     * Set vehicle's number of seats
     *
     * @param seats the number of seats in the vehicle including the driver seat
     */
    public void setSeats(Integer seats) {
        this.seats = seats;
    }

    /**
     * Gets a string object representing the values of the Vehicle Object
     *
     * @return string representation of the Vehicle object
     */
    @Override
    public String toString() {
        return "Vehicle [driver=" + driver + ", id=" + id + ", make=" + make + ", model=" + model + ", year=" + year
                + ", licensePlate=" + licensePlate + ", color=" + color + ", seats=" + seats + "]";
    }

}

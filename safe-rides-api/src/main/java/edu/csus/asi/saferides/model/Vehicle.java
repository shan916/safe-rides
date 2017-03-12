package edu.csus.asi.saferides.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.validation.constraints.Min;

import com.fasterxml.jackson.annotation.JsonIgnore;

/*
 * @author Zeeshan Khaliq
 * 
 * Model object for Vehicle Entity
 * */

@Entity
public class Vehicle {

	@JsonIgnore
	@OneToOne
	private Driver driver;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	@Column(nullable = false)
	private String make;
	
	@Column(nullable = false)
	private String model;
	
	@Column(nullable = false, length = 4)
	private String year;
	
	@Column(nullable = false)
	private String licensePlate;
	
	@Column(nullable = false)
	private String color;
	
	@Column(nullable = false)
	@Min(2)
	private Integer seats;
	
	protected Vehicle() { }
	
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
	
	public Driver getDriver() {
		return driver;
	}

	public void setDriver(Driver driver) {
		this.driver = driver;
	}

	public Long getVehicleId() {
		return id;
	}

	public void setVehicleId(Long vehicleId) {
		this.id = vehicleId;
	}

	public String getMake() {
		return make;
	}

	public void setMake(String make) {
		this.make = make;
	}

	public String getModel() {
		return model;
	}

	public void setModel(String model) {
		this.model = model;
	}

	public String getYear() {
		return year;
	}

	public void setYear(String year) {
		this.year = year;
	}

	public String getLicensePlate() {
		return licensePlate;
	}

	public void setLicensePlate(String licensePlate) {
		this.licensePlate = licensePlate;
	}

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}

	public Integer getSeats() {
		return seats;
	}

	public void setSeats(Integer seats) {
		this.seats = seats;
	}

	@Override
	public String toString() {
		return "Vehicle [driver=" + driver + ", id=" + id + ", make=" + make + ", model=" + model + ", year=" + year
				+ ", licensePlate=" + licensePlate + ", color=" + color + ", seats=" + seats + "]";
	}

}

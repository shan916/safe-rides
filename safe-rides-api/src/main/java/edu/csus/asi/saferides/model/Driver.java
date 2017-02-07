package edu.csus.asi.saferides.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/*
 * @author Zeeshan Khaliq
 * 
 * Model object for Driver Entity
 * */

@Entity
public class Driver {

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO) // Will generate a unique id automatically
	private Long id;
	
	@Column(nullable = false)
	private String firstName;
	
	@Column(nullable = false)
	private String lastName;
	
	@Column(nullable = false, unique = true)
	private String dlNumber;
	
	@Column(nullable = false)
	private Boolean insuranceChecked;
	
	@Column(nullable = false)
	private Boolean active;
	
	// protected Constructor required for JPA
	protected Driver() { }
	
	public Driver(String firstName, String lastName, String dlNumber, Boolean insuranceChecked, Boolean active) {
		super();
		this.firstName = firstName;
		this.lastName = lastName;
		this.dlNumber = dlNumber;
		this.insuranceChecked = insuranceChecked;
		this.active = active;
	}
	
	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}
	
	public String getFirstName() {
		return firstName;
	}
	
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	
	public String getLastName() {
		return lastName;
	}
	
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	
	public String getDlNumber() {
		return dlNumber;
	}
	
	public void setDlNumber(String dlNumber) {
		this.dlNumber = dlNumber;
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

	@Override
	public String toString() {
		return "Driver [id=" + id + ", firstName=" + firstName + ", lastName=" + lastName + ", dlNumber=" + dlNumber
				+ ", insuranceChecked=" + insuranceChecked + ", active=" + active + "]";
	}

}

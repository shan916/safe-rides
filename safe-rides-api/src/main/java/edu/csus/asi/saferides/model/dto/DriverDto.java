package edu.csus.asi.saferides.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import edu.csus.asi.saferides.model.DriverStatus;
import edu.csus.asi.saferides.model.Vehicle;

/**
 *  Data transfer object for the Driver object
 */
public class DriverDto {

	private Long id;
	private String oneCardId;
	private String driverFirstName;
	private String driverLastName;
	private String phoneNumber;
	private String dlState;
	private String dlNumber;
	private Boolean insuranceChecked;
	private String insuranceCompany;
	private Boolean active;

	@JsonProperty(access = JsonProperty.Access.READ_ONLY)
	private DriverStatus status;

	@JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
	private String password;

	private Vehicle vehicle;
	private long endOfNightOdo;

	public String getOneCardId() {
		return oneCardId;
	}

	public void setOneCardId(String oneCardId) {
		this.oneCardId = oneCardId;
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

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

    public Long getId() { return id; }

	public void setId(Long id) { this.id = id; }

	public Vehicle getVehicle() {
		return vehicle;
	}

	public void setVehicle(Vehicle vehicle) {
		this.vehicle = vehicle;
	}

	public long getEndOfNightOdo() {
		return endOfNightOdo;
	}

	public void setEndOfNightOdo(long endOfNightOdo) {
		this.endOfNightOdo = endOfNightOdo;
	}

	public DriverStatus getStatus() {
		return status;
	}

	public void setStatus(DriverStatus status) {
		this.status = status;
	}
}

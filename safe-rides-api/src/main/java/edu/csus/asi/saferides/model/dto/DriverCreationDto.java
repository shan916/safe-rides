package edu.csus.asi.saferides.model.dto;

/*
 * @author Zeeshan Khaliq
 *
 * Data Transfer Object for RideRequest
 * */



public class DriverCreationDto {

	private String csusId;
	private String driverFirstName;
	private String driverLastName;
	private String phoneNumber;
	private String dlState;
    private String dlNumber;
    private Boolean insuranceChecked;
    private String insuranceCompany;
    private Boolean active;
    private String password;

    public DriverCreationDto(){}
    
    public DriverCreationDto(String csusId, String driverFirstName, String driverLastName, String phoneNumber,
			String dlState, String dlNumber, Boolean insuranceChecked, String insuranceCompany, Boolean active,
			String password) {
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
		this.password = password;
	}

	public String getCsusId() {
		return csusId;
	}

	public void setCsusId(String csusId) {
		this.csusId = csusId;
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

	@Override
	public String toString() {
		return "DriverCreationDto [csusId=" + csusId + ", driverFirstName=" + driverFirstName + ", driverLastName="
				+ driverLastName + ", phoneNumber=" + phoneNumber + ", dlState=" + dlState + ", dlNumber=" + dlNumber
				+ ", insuranceChecked=" + insuranceChecked + ", insuranceCompany=" + insuranceCompany + ", active="
				+ active + ", password=" + password + "]";
	}
    
    
}

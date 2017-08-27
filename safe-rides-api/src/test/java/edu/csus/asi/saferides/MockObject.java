package edu.csus.asi.saferides;

import com.fasterxml.jackson.annotation.JsonView;
import edu.csus.asi.saferides.model.DriverStatus;
import edu.csus.asi.saferides.model.RideRequestStatus;
import edu.csus.asi.saferides.model.views.JsonViews;
import org.springframework.stereotype.Component;

@Component
public class MockObject {

    private RideRequestStatus rideRequestStatus;
    private DriverStatus driverStatus;

    @JsonView(JsonViews.Driver.class)
    private String messageToDriver;

    @JsonView(JsonViews.Rider.class)
    private String requesterPhoneNumber;

    public RideRequestStatus getRideRequestStatus() {
        return rideRequestStatus;
    }

    public void setRideRequestStatus(RideRequestStatus rideRequestStatus) {
        this.rideRequestStatus = rideRequestStatus;
    }

    public DriverStatus getDriverStatus() {
        return driverStatus;
    }

    public void setDriverStatus(DriverStatus driverStatus) {
        this.driverStatus = driverStatus;
    }

    public String getMessageToDriver() {
        return messageToDriver;
    }

    public void setMessageToDriver(String messageToDriver) {
        this.messageToDriver = messageToDriver;
    }

    public String getRequesterPhoneNumber() {
        return requesterPhoneNumber;
    }

    public void setRequesterPhoneNumber(String requesterPhoneNumber) {
        this.requesterPhoneNumber = requesterPhoneNumber;
    }
}
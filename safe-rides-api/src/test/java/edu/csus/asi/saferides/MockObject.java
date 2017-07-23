package edu.csus.asi.saferides;

import edu.csus.asi.saferides.model.RideRequestStatus;
import org.springframework.stereotype.Component;

@Component
public class MockObject {

    private RideRequestStatus rideRequestStatus;

    public RideRequestStatus getRideRequestStatus() {
        return rideRequestStatus;
    }

    public void setRideRequestStatus(RideRequestStatus rideRequestStatus) {
        this.rideRequestStatus = rideRequestStatus;
    }

}

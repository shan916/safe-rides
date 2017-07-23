package edu.csus.asi.saferides.model;

import com.fasterxml.jackson.annotation.JsonCreator;

/**
 * An enumerator for the statuses a RideRequest can hold.
 * UNASSIGNED: A ride request typically starts unassigned.
 * ASSIGNED: A ride request is assigned to a drive.
 * PICKINGUP: The driver is heading to the pick up location.
 * DROPPINGOFF: The driver has the rider(s) and is heading to the drop off location.
 * COMPLETE: The driver dropped off the rider(s) and is ready for another ride request.
 * CANCELEDBYCOORDINATOR: ride request is cancelled by a coordinator.
 * CANCELEDBYRIDER: ride request is cancelled by the rider.
 * CANCELEDOTHER: ride request is cancelled by another authority.
 */

public enum RideRequestStatus {
    UNASSIGNED,
    ASSIGNED,
    PICKINGUP,
    ATPICKUPLOCATION,
    DROPPINGOFF,
    COMPLETE,
    CANCELEDBYCOORDINATOR,
    CANCELEDBYRIDER,
    CANCELEDOTHER;

    /**
     * Deserializes json value
     *
     * @param value the json value
     * @return the RideRequestStatus for the given value
     */
    @JsonCreator
    public static RideRequestStatus create(String value) {
        if (value == null) {
            return null;
        }

        for (RideRequestStatus rideRequestStatus : values()) {
            if (value.equals(rideRequestStatus.name())) {
                return rideRequestStatus;
            }
        }

        return null;
    }
}
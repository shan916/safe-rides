package edu.csus.asi.saferides.model;

import com.fasterxml.jackson.annotation.JsonCreator;

/**
 * Enum for possible driver statuses
 * AVAILABLE - Driver is available (no rides are assigned)
 * ASSIGNED - Driver is assigned to a ride
 * PICKINGUP - Driver is on their way to pick up a rider
 * DROPPINGOFF - Driver is on their way to drop off a rider
 */
public enum DriverStatus {
    AVAILABLE,
    ASSIGNED,
    PICKINGUP,
    ATPICKUPLOCATION,
    DROPPINGOFF;

    /**
     * Deserializes Json value
     *
     * @param value the Json value
     * @return the DriverStatus for the given value
     */
    @JsonCreator
    public static DriverStatus create(String value) {
        if (value == null) {
            return null;
        }

        for (DriverStatus driverStatus : values()) {
            if (value.equals(driverStatus.name())) {
                return driverStatus;
            }
        }

        return null;
    }

}
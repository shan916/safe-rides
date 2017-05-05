package edu.csus.asi.saferides.model;

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
    DROPPINGOFF
}
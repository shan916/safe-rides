package edu.csus.asi.saferides.model;

public enum RideRequestStatus {
	UNASSIGNED,
	ASSIGNED,
	PICKINGUP,
	ATPICKUPLOCATION,
	DROPPINGOFF,
	COMPLETE,
	CANCELEDBYCOORDINATOR,
	CANCELEDBYRIDER,
	CANCELEDOTHER
}
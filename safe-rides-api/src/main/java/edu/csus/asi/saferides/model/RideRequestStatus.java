package edu.csus.asi.saferides.model;

public enum RideRequestStatus {
	UNASSIGNED,
	ASSIGNED,
	INPROGRESS,
	COMPLETE,
	CANCELEDBYCOORDINATOR,
	CANCELEDBYREQUESTOR;
}
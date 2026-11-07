package com.entity;

public class Registration {
	private int registrationId;
	private int userId;
	private int conferenceId;
	private String status;
	private Integer waitPos;
	
public Registration(int registrationId, int userId, int conferenceId, String status, Integer waitPos) {
	this.registrationId = registrationId;
	this.userId = userId;
	this.conferenceId = conferenceId;
	this.status = status;
	this.waitPos = waitPos;
}
public int getRegId() {
	return registrationId;
}
public int getUserId() {
	return userId;
}
public int getConfId() {
	return conferenceId;
}
public String getStatus() {
	return status;
}
public Integer getWaitPos() {
	return waitPos;
}
public void setRegistrationState(String status, Integer waitPos) {
	if("CONFIRMED".equals(status)) {
		if(waitPos !=null) {
			throw new IllegalArgumentException("Confirmed registrations cannot have a waiting position.");
		}
	} else if("WAITLISTED".equals(status)) {
		if(waitPos == null || waitPos <=0) {
			throw new IllegalArgumentException("Waiting position must be positive.");
		}
	} else {
		throw new IllegalArgumentException("Invalid registration status.");
	}
	this.status = status;
	this.waitPos = waitPos;
}
@Override
public String toString() {
	return "Registration ID: + registrationId"
			+ "\nUser ID: "+ userId
			+ "\nConference ID: "+ conferenceId
			+ "\nStatus: "+ status
			+"\nWaiting position: "  + waitPos;
}
}

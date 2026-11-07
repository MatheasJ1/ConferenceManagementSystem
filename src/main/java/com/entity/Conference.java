package com.entity;

import java.time.LocalDateTime;

public class Conference {
	private int conferenceId;
	private String title, description;
	private int capacity;
	private LocalDateTime startTime, endTime;
	private String location;
	
public Conference(int conferenceId, String title, String description, int capacity,LocalDateTime startTime, LocalDateTime endTime, String location) {
	this.conferenceId = conferenceId;
	setTitle(title);
	setDescription(description);
	setCapacity(capacity);
	setTimes(startTime, endTime);
	setLocation(location);
}
public int getConferenceId() {
	return conferenceId;
}
public String getTitle() {
	return title;
}
public void setTitle(String title) {
	this.title = title;
}
public String getDescription() {
	return description;
}
public void setDescription(String description) {
	this.description = description;
}
public int getCapacity() {
	return capacity;
}
public void setCapacity(int capacity) {
	this.capacity = capacity;
}
public LocalDateTime getStartTime() {
	return startTime;
}
public LocalDateTime getEndTime() {
	return endTime;
}
public void setTimes(LocalDateTime startTime, LocalDateTime endTime) {
	if(startTime == null || endTime == null || !endTime.isAfter(startTime)) {
		throw new IllegalArgumentException("Invalid Schedule.");
	}
	this.startTime = startTime;
	this.endTime = endTime;
}
public String getLocation() {
	return location;
}
public void setLocation(String location) {
	this.location = location;
}
public String toString() {
	return "Conference: " + title
			+"\nConference ID: " + conferenceId
			+"\nDescription: " + description
			+"\nCapacity: " + capacity
			+"\nStart Time: " + startTime
			+"\nEnd Time: " + endTime
			+"\nLocation: " + location;
	
}
}

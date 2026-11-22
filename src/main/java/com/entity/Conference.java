package com.entity;

import java.time.LocalDateTime;
import java.util.Objects;

public class Conference {
	private int conferenceId;
	private String title;
	private String description;
	private int capacity;
	private LocalDateTime startTime;
	private LocalDateTime endTime;
	private String location;

	public Conference(int conferenceId, String title, String description, int capacity, LocalDateTime startTime, LocalDateTime endTime, String location) {
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

	public void setConferenceId(int conferenceId) {
		this.conferenceId = conferenceId;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		if (title == null || title.isBlank()) {
			throw new IllegalArgumentException("Conference title is required.");
		}

		this.title = title.trim();
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description == null ? "" : description.trim();
	}

	public int getCapacity() {
		return capacity;
	}

	public void setCapacity(int capacity) {
		if (capacity <= 0) {
			throw new IllegalArgumentException("Capacity must be greater than zero.");
		}

		this.capacity = capacity;
	}

	public LocalDateTime getStartTime() {
		return startTime;
	}

	public LocalDateTime getEndTime() {
		return endTime;
	}

	public void setTimes(LocalDateTime startTime, LocalDateTime endTime) {
		if (startTime == null || endTime == null || !endTime.isAfter(startTime)) {
			throw new IllegalArgumentException("The end date and time must be after the start date and time.");
		}

		this.startTime = startTime;
		this.endTime = endTime;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location == null ? "" : location.trim();
	}

	@Override
	public String toString() {
		return title + " (" + startTime + " - " + endTime + ")";
	}
}

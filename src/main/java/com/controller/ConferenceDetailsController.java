package com.controller;

import java.io.IOException;
import java.sql.SQLException;
import java.time.format.DateTimeFormatter;

import com.entity.Conference;
import com.entity.User;
import com.view.ConferenceManagementApp;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

public class ConferenceDetailsController {
	private User currentUser;
	private Conference currentConf;
	@FXML
	private Label welcomeLabel;
	@FXML
	private Label conferenceTitleLabel;
	@FXML
	private Label statusLabel;
	@FXML
	private Label startDateLabel;
	@FXML
	private Label endDateLabel;
	@FXML
	private Label locationLabel;
	@FXML
	private Label capacityLabel;
	@FXML
	private Button logoutButton;
	@FXML
	private Button backButton;
	@FXML
	private Button browseButton;
	@FXML
	private Button myRegistrationsButton;
	@FXML
	private Button registerButton;
	@FXML
	private Label descriptionLabel;
	@FXML
	public void goBack() throws IOException {
	    ConferenceManagementApp.showParticipantDashboard(currentUser);
	}
	@FXML
	public void logout() throws IOException{
		ConferenceManagementApp.showLogin();
	}
	@FXML
	public void goToDash() throws IOException{
		ConferenceManagementApp.showParticipantDashboard(currentUser);
	}
	public void setDetails(User user, Conference conference) {
		currentUser = user;
		currentConf = conference;
		DateTimeFormatter formatter =
	            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

	        welcomeLabel.setText("Welcome, " + user.getFullName());

	        conferenceTitleLabel.setText(conference.getTitle());

	        startDateLabel.setText(
	            conference.getStartTime().format(formatter));

	        endDateLabel.setText(
	            conference.getEndTime().format(formatter));

	        locationLabel.setText(conference.getLocation());

	        capacityLabel.setText(
	            conference.getCapacity() + " participants");

	        descriptionLabel.setText(conference.getDescription());

	        statusLabel.setText("Not checked");
	}
	public void register() {
		
	}
	@FXML
	public void myRegistrations()
	        throws IOException, SQLException {

	    ConferenceManagementApp.showMyRegistrations(currentUser);
	}
}

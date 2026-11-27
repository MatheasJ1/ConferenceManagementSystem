package com.controller;

import java.util.logging.Logger;

import com.entity.Conference;
import com.entity.User;
import com.view.ConferenceManagementApp;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.SimpleIntegerProperty;

import java.io.IOException;
import java.sql.SQLException;
import java.time.format.DateTimeFormatter;

public class ParticipantDashboardController {
	private User currentUser;
	private static final Logger LOGGER = Logger.getLogger(ParticipantDashboardController.class.getName());
	@FXML
	private Label welcomeLabel;
	@FXML
	private TextField searchField;
	@FXML
	private TableView<Conference> conferenceTable;
	@FXML
	private TableColumn<Conference,String> titleColumn;
	@FXML
	private TableColumn<Conference,String> startColumn;
	@FXML
	private TableColumn<Conference,String> endColumn;
	@FXML
	private TableColumn<Conference,String> locationColumn;
	@FXML
	private TableColumn<Conference,Number> capacityColumn;
	@FXML
	private TableColumn<Conference,String> statusColumn;
	@FXML
	private Button registerButton;
	@FXML
	private Button searchButton;
	@FXML
	private Button myRegistrationsButton;
	@FXML
	private Button viewButton;
	@FXML
	private Button browseButton;
	@FXML
	private Button logoutButton;
	public void setUser(User user) {
		currentUser = user;
		welcomeLabel.setText("Welcome, " + user.getFullName());
	}
	@FXML
	public void initialize() {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyy-MM-dd HH:mm");
		titleColumn.setCellValueFactory(v-> new SimpleStringProperty(v.getValue().getTitle()));
		startColumn.setCellValueFactory(v->new SimpleStringProperty(v.getValue().getStartTime().format(formatter)));
		endColumn.setCellValueFactory(v ->
        new SimpleStringProperty(
            v.getValue().getEndTime().format(formatter)));

    locationColumn.setCellValueFactory(v ->
        new SimpleStringProperty(v.getValue().getLocation()));

    capacityColumn.setCellValueFactory(v ->
        new SimpleIntegerProperty(v.getValue().getCapacity()));

    conferenceTable.setColumnResizePolicy(
        TableView.CONSTRAINED_RESIZE_POLICY_ALL_COLUMNS);
		
	}
	@FXML
	public void logout() throws IOException{
		ConferenceManagementApp.showLogin();
	}
	@FXML
	public void viewDetails() throws IOException {

	    Conference selected =
	        conferenceTable.getSelectionModel().getSelectedItem();

	    if (selected == null) {
	        Alert alert = new Alert(Alert.AlertType.WARNING);
	        alert.setContentText("Please select a conference.");
	        alert.showAndWait();
	        return;
	    }

	    ConferenceManagementApp.showConferenceDetails(
	        currentUser, selected);
	}
	@FXML
	public void myRegistrations()
	        throws IOException, SQLException {

	    ConferenceManagementApp.showMyRegistrations(currentUser);
	}
	
	
}

package com.controller;

import java.io.IOException;
import java.sql.SQLException;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import com.entity.Conference;
import com.entity.Registration;
import com.entity.User;
import com.view.ConferenceManagementApp;

import DAO.ConferenceDAO;
import DAO.RegistrationDAO;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

public class RegistrationsController {
	private static final Logger LOGGER = Logger.getLogger(RegistrationsController.class.getName());
	private User currentUser;
	@FXML
	private Button browseButton;
	@FXML
	private Button viewButton;
	@FXML
	private Button logoutButton;
	@FXML
	private Button myRegistrationsButton;
	@FXML
	private Button cancelButton;
	@FXML
	private Label totalLabel;
	@FXML
	private Label confirmedLabel;
	@FXML
	private Label waitLabel;
	@FXML
	private Label welcomeLabel;
	@FXML
	private TableView<Conference> registrationTable;
	@FXML
	private TableColumn<Conference,String> titleColumn;
	@FXML
	private TableColumn<Conference,String> startColumn;
	@FXML
	private TableColumn<Conference,String> locationColumn;
	@FXML
	private TableColumn<Conference,String> registeredColumn;
	@FXML
	private TableColumn<Conference,String> statusColumn;
	@FXML
	private ComboBox<String> statusFilter;
	@FXML
	public void goBack() throws IOException{
		ConferenceManagementApp.showParticipantDashboard(currentUser);
	}
	@FXML
	public void viewDetails() throws IOException {

	    Conference selected =
	        registrationTable.getSelectionModel().getSelectedItem();

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
	public void logout() throws IOException{
		ConferenceManagementApp.showLogin();
	}
	@FXML
	public void cancelRegistration() throws IOException{
		Conference selected = registrationTable.getSelectionModel().getSelectedItem();
		if(selected == null) {
			Alert alert = new Alert(Alert.AlertType.WARNING);
			alert.setContentText("Please select a registration.");
			alert.showAndWait();
			return;
		}
		Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
		confirm.setTitle("Cancel Registration");
		confirm.setHeaderText("Cancel " + selected.getTitle() + "?");
		confirm.setContentText("Are you sure you want to cancel your registration?");
		if(confirm.showAndWait().orElse(ButtonType.CANCEL)== ButtonType.OK ) {
			 try {
		            RegistrationDAO dao = new RegistrationDAO();

		            dao.cancelRegistration(
		                currentUser.getUserId(),
		                selected.getConferenceId()
		            );

		            loadRegistrations();
		            updateStatistics();

		            Alert success = new Alert(Alert.AlertType.INFORMATION);
		            success.setContentText("Registration cancelled successfully.");
		            success.showAndWait();

		        } catch (SQLException e) {
		            Alert error = new Alert(Alert.AlertType.ERROR);
		            error.setContentText("Unable to cancel registration.");
		            error.showAndWait();
		            e.printStackTrace();
		        }
		}
		
	}
	private void updateStatistics() throws SQLException {

	    RegistrationDAO dao = new RegistrationDAO();

	    List<Registration> registrations =
	        dao.getRegistrationsByUser(currentUser.getUserId());

	    long confirmed = registrations.stream()
	        .filter(r -> r.getStatus().equals("CONFIRMED"))
	        .count();

	    long waitlisted = registrations.stream()
	        .filter(r -> r.getStatus().equals("WAITLISTED"))
	        .count();

	    totalLabel.setText(String.valueOf(confirmed + waitlisted));
	    confirmedLabel.setText(String.valueOf(confirmed));
	    waitLabel.setText(String.valueOf(waitlisted));
	}
	private void loadRegistrations() throws SQLException {

	    RegistrationDAO registrationDAO = new RegistrationDAO();
	    ConferenceDAO conferenceDAO = new ConferenceDAO();

	    List<Registration> registrations =
	        registrationDAO.getRegistrationsByUser(currentUser.getUserId());

	    List<Conference> conferences = new ArrayList<>();

	    for (Registration registration : registrations) {

	        Conference conference =
	            conferenceDAO.getConferenceById(registration.getConfId());

	        if (conference != null) {
	            conferences.add(conference);
	        }
	    }

	    registrationTable.setItems(
	        FXCollections.observableArrayList(conferences)
	    );
	}
	public void setUser(User user) throws SQLException {

	    currentUser = user;

	    welcomeLabel.setText("Welcome, " + user.getFullName());

	    loadRegistrations();
	    updateStatistics();
	}
	@FXML
	public void refresh() throws SQLException {
	    loadRegistrations();
	    updateStatistics();
	}
	@FXML
	public void initialize() {
		statusFilter.getItems().addAll(
			    "All Registrations",
			    "Confirmed",
			    "Waitlisted",
			    "Cancelled"
			);

			statusFilter.setValue("All Registrations");

	    DateTimeFormatter formatter =
	        DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

	    titleColumn.setCellValueFactory(v ->
	        new SimpleStringProperty(v.getValue().getTitle()));

	    startColumn.setCellValueFactory(v ->
	        new SimpleStringProperty(
	            v.getValue().getStartTime().format(formatter)));

	    locationColumn.setCellValueFactory(v ->
	        new SimpleStringProperty(v.getValue().getLocation()));

	    registrationTable.setColumnResizePolicy(
	        TableView.CONSTRAINED_RESIZE_POLICY_ALL_COLUMNS
	    );
	}
}

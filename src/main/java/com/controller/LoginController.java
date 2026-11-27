package com.controller;

import java.io.IOException;
import java.sql.SQLException;

import org.mindrot.jbcrypt.BCrypt;

import com.entity.Role;
import com.entity.User;
import com.view.ConferenceManagementApp;

import DAO.UserDAO;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class LoginController {
	@FXML
	private TextField emailField;
	@FXML
	private PasswordField passwordField;
	@FXML
	private Label messageLabel;
	@FXML
	public void login() throws SQLException, IOException {
		String email = emailField.getText().trim();
		String password = passwordField.getText().trim();
		if(email.isEmpty() || password.isEmpty() ) {
			messageLabel.setText("Please fill in all fields.");
			return;
		}
		UserDAO userDAO = new UserDAO();
		User user = userDAO.getUserByEmail(email);
		if(user == null) {
			messageLabel.setText("Incorrect E-mail or password.");
			emailField.clear();
			passwordField.clear();
			return;
		}
		String hashedPassword = userDAO.getPasswordHash(user.getUserId());
		if(hashedPassword == null || !BCrypt.checkpw(password, hashedPassword)) {
			messageLabel.setText("Incorrect E-mail or password.");
			return;
		}
		if(user.getRole() != Role.PARTICIPANT) {
			messageLabel.setText("Please use the admin login page.");
			return;
		}
		messageLabel.setText("Login successful!");
		ConferenceManagementApp.showParticipantDashboard(user);
	}
	@FXML
	public void goToCreateAccount() throws IOException{
		ConferenceManagementApp.showCreateAccount();
	}
	@FXML
	public void goToAdminLogin() throws IOException{
		ConferenceManagementApp.showAdminLogin();
	}
	@FXML
	public void exitApp() {
		Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
		alert.setTitle("Exit Application");
		alert.setHeaderText("Are you sure you want to exit?");
		alert.setContentText("The Application will close.");
		if(alert.showAndWait().orElse(ButtonType.CANCEL) == ButtonType.OK) {
			Platform.exit();
		}
		
		
	}
}

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
import java.util.logging.*;

public class AdminLoginController {
	private static final Logger LOGGER = Logger.getLogger(AdminLoginController.class.getName());
	@FXML
	private TextField emailField;
	@FXML
	private PasswordField passwordField;
	@FXML
	private Label messageLabel;
	@FXML
	public void adminLogin() {
		System.out.println("Button clicked.");
		String email = emailField.getText().trim();
		String password = passwordField.getText().trim();
		
		messageLabel.setText("");
		
		if(email.isEmpty() || password.isEmpty()) {
			messageLabel.setText("Please fill in all fields.");
			return;
		}
		UserDAO userDAO = new UserDAO();
		try {
			User user = userDAO.getUserByEmail(email);
			if(user== null) {
				messageLabel.setText("Invalid administrator credentials.");
				String hashedPassword = userDAO.getPasswordHash(user.getUserId());
				if(hashedPassword == null || !BCrypt.checkpw(password, hashedPassword)) {
					messageLabel.setText("Invalid administrator credentials.");
					return;
				}
				if(user.getRole() != Role.ADMIN) {
					messageLabel.setText("Access denied. Administrators only.");
				}
				messageLabel.setText("Login successful!");
				ConferenceManagementApp.showConferenceManagement(user);
			}
		} catch (SQLException e) {
			
			LOGGER.log(Level.SEVERE, "Failed to login.", e);
			
		}
		
	}
	@FXML
	public void goToCreateAccount() throws IOException {
		ConferenceManagementApp.showCreateAccount();
	}
	@FXML
	public void goToLogin() throws IOException {
		ConferenceManagementApp.showLogin();
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

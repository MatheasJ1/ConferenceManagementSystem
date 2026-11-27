package com.controller;

import java.io.IOException;
import java.sql.SQLException;
import java.util.logging.Level;

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

public class CreateAccountController {
	
	@FXML
	private TextField fullNameField;
	@FXML
	private TextField emailField;
	@FXML
	private TextField ageField;
	@FXML
	private PasswordField passwordField;
	@FXML
	private Label messageLabel;
	@FXML
	public void createAccount() {
		String name = fullNameField.getText().trim();
		String email = emailField.getText().trim();
		String ageText = ageField.getText().trim();
		String password = passwordField.getText();
		
		messageLabel.setText("");
		
		if(name.isEmpty() || email.isEmpty() || ageText.isEmpty() || password.isEmpty()) {
			messageLabel.setText("Please fill in all fields.");
			return;
		}
		int age;
		try {
			age=Integer.parseInt(ageText);
			
			if(age<=0) {
				messageLabel.setText("Please enter a valid age.");
				return;
			}
		} catch (NumberFormatException e) {
			messageLabel.setText("Age must be a number.");
			return;
		}
		if(password.length() < 8) {
			messageLabel.setText("Password must contain atleast 8 characters.");
		}
		confirmAccountCreation(name,email,age,password);
	}
	private void confirmAccountCreation(String name, String email, int age, String password) {
		Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
		alert.setTitle("Create Account");
		alert.setHeaderText("Confirm account creation");
		alert.setContentText("Are you sure you want to create this account?");
		
		if(alert.showAndWait().orElse(ButtonType.CANCEL) == ButtonType.OK) {
			UserDAO userDAO = new UserDAO();
			try {
				if(userDAO.getUserByEmail(email) != null) {
					messageLabel.setText("E-mail is already registered.");
					return;
				}
				User user = new User(0,name,email,age,Role.PARTICIPANT);
				String passwordHash = BCrypt.hashpw(password, BCrypt.gensalt(12));
				userDAO.addUser(user, passwordHash);
				Alert success = new Alert(Alert.AlertType.INFORMATION);
				success.setTitle("Success");
				success.setHeaderText("Account created successfully!");
				success.setContentText("You can now log in.");
				success.showAndWait();
				ConferenceManagementApp.showLogin();
			} catch (SQLException e) {
				e.printStackTrace();
			} catch(IOException e) {
				messageLabel.setText("Account created. Please open the login page!");
				
			}
			messageLabel.setText("Validation and confirmation successful");
		}
	}
	@FXML
	public void goToLogin() throws IOException{
		ConferenceManagementApp.showLogin();
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

package com.view;

import com.controller.ConferenceController;
import com.controller.ConferenceDetailsController;
import com.controller.ParticipantDashboardController;
import com.controller.RegistrationsController;
import com.entity.Conference;
import com.entity.Role;
import com.entity.User;
import com.service.ConferenceService;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Locale;
import java.util.ResourceBundle;

public class ConferenceManagementApp extends Application {
	private static Stage mainStage;
    @Override
    public void start(Stage stage) {
    	FXMLLoader loader = new FXMLLoader(getClass().getResource("/createAccount.fxml"));
    	try {
    		mainStage = stage;
			Scene scene = new Scene(loader.load());
			stage.setTitle("Create Account");
			stage.setScene(scene);
			stage.show();
		} catch (IOException e) {
			e.printStackTrace();
		}
    }
    public static void showCreateAccount() throws IOException{
    	switchPage("createAccount.fxml");
    }
    public static void showLogin() throws IOException{
    	switchPage("login.fxml");
    }
    public static void showAdminLogin() throws IOException{
    	switchPage("adminLogin.fxml");
    }
    public static void switchPage(String file) throws IOException {
    	FXMLLoader loader = new FXMLLoader(ConferenceManagementApp.class.getResource("/" + file));
    	mainStage.setScene(new Scene(loader.load()));
    	mainStage.show();
    }
    public static void showConferenceManagement(User user) {
    	
        User developmentAdmin = new User(1, "Development Administrator", "admin@example.com", 30, Role.ADMIN);
        ResourceBundle bundle = ResourceBundle.getBundle("translations.messages", Locale.CANADA);
        ConferenceController controller = new ConferenceController(new ConferenceService(), developmentAdmin);
        ConferenceManagementView view = new ConferenceManagementView(controller, bundle);
        mainStage.setTitle(bundle.getString("conference.management"));
        mainStage.setScene(new Scene(view.build(), 1050, 650));
        mainStage.show();
    }
    public static void showParticipantDashboard(User user) throws IOException{
    	FXMLLoader loader = new FXMLLoader(ConferenceManagementApp.class.getResource("/participantDashboard.fxml"));
    	Scene scene = new Scene(loader.load());
    	ParticipantDashboardController controller = loader.getController();
    	controller.setUser(user);
    	mainStage.setTitle("Participant Dashboard");
    	mainStage.setScene(scene);
    	mainStage.show();
    }
    public static void showConferenceDetails(User user,
            Conference conference) throws IOException {

        FXMLLoader loader = new FXMLLoader(
            ConferenceManagementApp.class.getResource(
                "/ConferenceDetails.fxml"));

        Scene scene = new Scene(loader.load(), 1050, 650);

        ConferenceDetailsController controller =
            loader.getController();

        controller.setDetails(user, conference);

        mainStage.setTitle("Conference Details");
        mainStage.setScene(scene);
        mainStage.show();
    }
    public static void showMyRegistrations(User user)
            throws IOException, SQLException {

        FXMLLoader loader = new FXMLLoader(
            ConferenceManagementApp.class.getResource(
                "/myRegistrations.fxml"));

        Scene scene = new Scene(loader.load(), 1050, 650);

        RegistrationsController controller = loader.getController();

        controller.setUser(user);

        mainStage.setTitle("My Registrations");
        mainStage.setScene(scene);
        mainStage.show();
    }
    

    public static void main(String[] args) {
        launch(args);
    }
}

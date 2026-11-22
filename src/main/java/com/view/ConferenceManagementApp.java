package com.view;

import com.controller.ConferenceController;
import com.entity.Role;
import com.entity.User;
import com.service.ConferenceService;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;
import java.util.Locale;
import java.util.ResourceBundle;

public class ConferenceManagementApp extends Application {
    @Override
    public void start(Stage stage) {
        User developmentAdmin = new User(1, "Development Administrator", "admin@example.com", 30, Role.ADMIN);
        ResourceBundle bundle = ResourceBundle.getBundle("translations.messages", Locale.CANADA);
        ConferenceController controller = new ConferenceController(new ConferenceService(), developmentAdmin);
        ConferenceManagementView view = new ConferenceManagementView(controller, bundle);
        stage.setTitle(bundle.getString("conference.management"));
        stage.setScene(new Scene(view.build(), 1050, 650));
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}

package com.view;

import com.controller.ConferenceController;
import com.entity.Conference;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.ResourceBundle;

public class ConferenceManagementView {
    private final ConferenceController controller;
    private final ResourceBundle messages;
    private final TableView<Conference> table = new TableView<>();
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    public ConferenceManagementView(ConferenceController controller, ResourceBundle messages) {
        this.controller = controller;
        this.messages = messages;
    }

    public Parent build() {
        TableColumn<Conference, String> title = new TableColumn<>(messages.getString("conference.title"));
        title.setCellValueFactory(v -> new javafx.beans.property.SimpleStringProperty(v.getValue().getTitle()));
        TableColumn<Conference, String> date = new TableColumn<>(messages.getString("conference.start"));
        date.setCellValueFactory(v -> new javafx.beans.property.SimpleStringProperty(v.getValue().getStartTime().format(formatter)));
        TableColumn<Conference, String> end = new TableColumn<>(messages.getString("conference.end"));
        end.setCellValueFactory(v -> new javafx.beans.property.SimpleStringProperty(v.getValue().getEndTime().format(formatter)));
        TableColumn<Conference, String> location = new TableColumn<>(messages.getString("conference.location"));
        location.setCellValueFactory(v -> new javafx.beans.property.SimpleStringProperty(v.getValue().getLocation()));
        TableColumn<Conference, Number> capacity = new TableColumn<>(messages.getString("conference.capacity"));
        capacity.setCellValueFactory(v -> new javafx.beans.property.SimpleIntegerProperty(v.getValue().getCapacity()));
        table.getColumns().addAll(title, date, end, location, capacity);
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_ALL_COLUMNS);

        Button add = new Button(messages.getString("button.create"));
        Button edit = new Button(messages.getString("button.edit"));
        Button delete = new Button(messages.getString("button.delete"));
        Button refresh = new Button(messages.getString("button.refresh"));
        add.setOnAction(e -> editDialog(null));
        edit.setOnAction(e -> {
            Conference selected = table.getSelectionModel().getSelectedItem();
            if (selected != null) editDialog(selected);
            else alert(messages.getString("error.selectConference"));
        });
        delete.setOnAction(e -> deleteSelected());
        refresh.setOnAction(e -> refresh());

        HBox actions = new HBox(10, add, edit, delete, refresh);
        actions.setPadding(new Insets(10));
        BorderPane pane = new BorderPane(table);
        pane.setTop(new VBox(new Label(messages.getString("conference.management")), actions));
        pane.setPadding(new Insets(12));
        refresh();
        return pane;
    }

    private void refresh() {
        try { table.setItems(FXCollections.observableArrayList(controller.loadConferences())); }
        catch (Exception ex) { alert(messages.getString("error.loadConferences") + "\n" + ex.getMessage()); }
    }

    private void editDialog(Conference existing) {
        Dialog<Conference> dialog = new Dialog<>();
        dialog.setTitle(existing == null ? messages.getString("conference.create") : messages.getString("conference.edit"));
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        TextField title = new TextField(existing == null ? "" : existing.getTitle());
        TextArea description = new TextArea(existing == null ? "" : existing.getDescription());
        description.setPrefRowCount(3);
        TextField capacity = new TextField(existing == null ? "1" : Integer.toString(existing.getCapacity()));
        TextField start = new TextField(existing == null ? "" : existing.getStartTime().format(formatter));
        TextField end = new TextField(existing == null ? "" : existing.getEndTime().format(formatter));
        TextField location = new TextField(existing == null ? "" : existing.getLocation());

        GridPaneBuilder form = new GridPaneBuilder();
        form.add(messages.getString("conference.title"), title);
        form.add(messages.getString("conference.description"), description);
        form.add(messages.getString("conference.capacity"), capacity);
        form.add(messages.getString("conference.startFormat"), start);
        form.add(messages.getString("conference.endFormat"), end);
        form.add(messages.getString("conference.location"), location);
        dialog.getDialogPane().setContent(form.node());

        dialog.setResultConverter(button -> {
            if (button != ButtonType.OK) return null;
            try {
                int id = existing == null ? 0 : existing.getConferenceId();
                return new Conference(id, title.getText(), description.getText(),
                        Integer.parseInt(capacity.getText().trim()),
                        LocalDateTime.parse(start.getText().trim(), formatter),
                        LocalDateTime.parse(end.getText().trim(), formatter), location.getText());
            } catch (RuntimeException ex) {
                alert(messages.getString("error.invalidConference") + "\n" + ex.getMessage());
                return null;
            }
        });

        dialog.showAndWait().ifPresent(conference -> {
            try {
                if (existing == null) controller.create(conference);
                else controller.update(conference);
                refresh();
            } catch (Exception ex) { alert(messages.getString("error.saveConference") + "\n" + ex.getMessage()); }
        });
    }

    private void deleteSelected() {
        Conference selected = table.getSelectionModel().getSelectedItem();
        if (selected == null) { alert(messages.getString("error.selectConference")); return; }
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION,
                messages.getString("conference.confirmDelete") + " " + selected.getTitle(),
                ButtonType.YES, ButtonType.NO);
        confirm.setHeaderText(null);
        if (confirm.showAndWait().orElse(ButtonType.NO) == ButtonType.YES) {
            try { controller.delete(selected.getConferenceId()); refresh(); }
            catch (Exception ex) { alert(messages.getString("error.deleteConference") + "\n" + ex.getMessage()); }
        }
    }

    private void alert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION, message, ButtonType.OK);
        alert.setHeaderText(null);
        alert.showAndWait();
    }

    /** Small layout helper to keep the view independent of FXML files. */
    private static class GridPaneBuilder {
        private final javafx.scene.layout.GridPane grid = new javafx.scene.layout.GridPane();
        private int row;
        GridPaneBuilder() { grid.setHgap(10); grid.setVgap(8); grid.setPadding(new Insets(12)); }
        void add(String label, javafx.scene.Node field) {
            grid.add(new Label(label), 0, row);
            grid.add(field, 1, row++);
            javafx.scene.layout.GridPane.setHgrow(field, javafx.scene.layout.Priority.ALWAYS);
        }
        javafx.scene.layout.GridPane node() { return grid; }
    }
}

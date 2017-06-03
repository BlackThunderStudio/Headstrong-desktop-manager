package com.headstrongpro.desktop.view;

import com.headstrongpro.desktop.core.SyncHandler;
import com.headstrongpro.desktop.core.fxControls.Footer;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;

/**
 * ContextView
 */
public abstract class ContextView<T> {

    // Top controls
    @FXML
    public Button toggleEditButton;
    @FXML
    public Button editButton;
    @FXML
    public Button cancelButton;
    @FXML
    public Button deleteButton;

    @FXML
    public HBox topControls; // Horizontal row with top controls

    protected T contextItem = null; // Currently set context item

    protected MainWindowView mainWindowView; // Main view controller

    protected ArrayList<TextField> textFields = new ArrayList<>(); // List of form text fields
    protected ArrayList<RadioButton> radioButtons = new ArrayList<>(); // List of form text fields

    private boolean editMode = false; // Whether making changes is allowed

    public void setMainWindowView(MainWindowView mainWindowView) {
        this.mainWindowView = mainWindowView;
    }

    /**
     * Changes the context items and invokes population of the form
     */
    public void changeContextItem(T t) {
        this.contextItem = t;
        populateForm();
        if (editMode) toggleEditMode();
    }

    public T getContextItem() {
        return contextItem;
    }

    /**
     * Sets the values on context initialization
     */
    public abstract void populateForm();

    /**
     * Clears all the text input
     */
    protected void clearFields() {
        textFields.forEach(TextInputControl::clear);
        radioButtons.forEach(ToggleButton::disarm);
    }

    /**
     * Validates the contexts window input fields
     *
     * @param textFields a collection of TextField objects
     * @return True if ALL the objects are valid, otherwise false
     */
    protected boolean validateInput(TextField... textFields) {
        return Arrays.stream(textFields)
                .map(TextInputControl::getText)
                .filter(e -> e.isEmpty() || e.contains(";") || e.contains(":") || e.contains("^"))
                .count() == 0;
    }

    /***
     * asks the user if he wants to reload the data in case of a database inconsistency
     * If the user declines, then it resets the fields in a context window
     *
     * @param handler function implemented by SyncHandler
     */
    protected void handleOutOfSync(SyncHandler<T> handler) {
        Alert a = new Alert(Alert.AlertType.CONFIRMATION);
        mainWindowView.getContentView().footer.show("Warning! Data inconsistency!", Footer.NotificationType.WARNING);
        a.setHeaderText("Warning! Database contains newer data.");
        a.setContentText("Do you want to reload the data? Clicking 'Cancel' will clear all the input");
        Optional<ButtonType> response = a.showAndWait();
        response.ifPresent(e -> {
            if (ButtonType.OK.equals(e)) {
                changeContextItem(handler.handle());
            } else {
                clearFields();
            }
        });
    }

    protected void displayNotImplementedError() {
        mainWindowView.getContentView().footer.show("Feature not yet implemented, patience is advised.", Footer.NotificationType.INFORMATION);
    }

    /**
     * By default, hide buttons of editing mode and disable changing fields
     */
    protected void setDefaults() {
        topControls.getChildren().removeAll(editButton, cancelButton);
        textFields.forEach(tf -> tf.setEditable(false));
        radioButtons.forEach(rb -> rb.setDisable(true));
    }

    @FXML
    public void toggleEditMode() {
        if (editMode) {
            topControls.getChildren().removeAll(editButton, cancelButton);
            topControls.getChildren().addAll(toggleEditButton, deleteButton);
            textFields.forEach(tf -> tf.setEditable(false));
            radioButtons.forEach(rb -> rb.setDisable(true));
        } else {
            topControls.getChildren().removeAll(toggleEditButton, deleteButton);
            topControls.getChildren().addAll(editButton, cancelButton);
            textFields.forEach(tf -> tf.setEditable(true));
            radioButtons.forEach(rb -> rb.setDisable(false));
        }
        editMode = !editMode;
    }

    @FXML
    public void handleCancel() {
        toggleEditMode();
        if (contextItem != null) populateForm();
    }
}

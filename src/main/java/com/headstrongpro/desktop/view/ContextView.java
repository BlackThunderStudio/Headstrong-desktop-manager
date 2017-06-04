package com.headstrongpro.desktop.view;

import com.headstrongpro.desktop.controller.IContentController;
import com.headstrongpro.desktop.core.SyncHandler;
import com.headstrongpro.desktop.core.exception.DatabaseOutOfSyncException;
import com.headstrongpro.desktop.core.exception.ModelSyncException;
import com.headstrongpro.desktop.core.fxControls.Footer;
import com.headstrongpro.desktop.model.IModel;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;

/**
 * Abstract base class for context views
 */
public abstract class ContextView<T> {

    // Top controls
    @FXML
    public Button toggleEditButton, saveButton, cancelButton, deleteButton;

    @FXML
    public HBox topControls; // Horizontal row with top controls

    protected T contextItem = null; // Currently set context item

    protected MainWindowView mainWindowView; // MainWindow parent controller

    protected IContentController<T> controller; // Data controller

    protected ArrayList<TextField> textFields = new ArrayList<>(); // List of form text fields
    protected ArrayList<RadioButton> radioButtons = new ArrayList<>(); // List of form text fields

    protected SyncHandler<T> handler = () -> {
        IModel modelItem = (IModel) contextItem;
        try {
            return controller.getById(modelItem.getId());
        } catch (ModelSyncException e) {
            e.printStackTrace();
            mainWindowView.getContentView().footer.show(e.getMessage(), Footer.NotificationType.ERROR);
        }
        return null;
    };
    private boolean editMode = false; // Whether making changes is allowed

    /**
     * Changes the context items and invokes population of the form
     */
    public void changeContextItem(T t) {
        this.contextItem = t;
        populateForm();
        if (editMode) toggleEditMode();
    }

    /**
     * Sets the values on context initialization
     */
    protected abstract void populateForm();

    /**
     * Clears all the text input fields
     */
    protected void clearFields() {
        textFields.forEach(TextInputControl::clear);
        radioButtons.forEach(ToggleButton::disarm);
    }

    /**
     * By default, hide buttons of editing mode and disable changing fields
     */
    protected void setDefaults() {
        topControls.getChildren().removeAll(saveButton, cancelButton);
        textFields.forEach(tf -> tf.setEditable(false));
        radioButtons.forEach(rb -> rb.setDisable(true));
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

    /**
     * Asks the user if he wants to reload the data in case of a database inconsistency
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

    /**
     * Toggles editing mode by enabling making changes in the form
     * and changes top controls
     */
    @FXML
    public void toggleEditMode() {
        if (editMode) {
            topControls.getChildren().removeAll(saveButton, cancelButton);
            topControls.getChildren().addAll(toggleEditButton, deleteButton);
            textFields.forEach(tf -> tf.setEditable(false));
            radioButtons.forEach(rb -> rb.setDisable(true));
        } else {
            topControls.getChildren().removeAll(toggleEditButton, deleteButton);
            topControls.getChildren().addAll(saveButton, cancelButton);
            textFields.forEach(tf -> tf.setEditable(true));
            radioButtons.forEach(rb -> rb.setDisable(false));
        }
        editMode = !editMode;
    }

    /**
     * Pops up a dialog window with delete confirmation
     */
    @FXML
    public void handleDelete() {
        Alert a = new Alert(Alert.AlertType.CONFIRMATION);
        a.setHeaderText(String.format("Are you sure you want to delete %s from the list?", contextItem));
        a.setContentText("You cannot take that action back");
        Optional<ButtonType> response = a.showAndWait();
        response.ifPresent(btn -> {
            if (ButtonType.OK.equals(btn)) {
                try {
                    mainWindowView.getContentView().footer.show(String.format("Deleting %s...", contextItem), Footer.NotificationType.LOADING);
                    controller.delete(contextItem);
                    mainWindowView.getContentView().footer.show("Entry deleted.", Footer.NotificationType.COMPLETED);
                    mainWindowView.getContentView().handleRefresh();
                } catch (ModelSyncException e) {
                    e.printStackTrace();
                    mainWindowView.getContentView().footer.show(e.getMessage(), Footer.NotificationType.ERROR, Footer.FADE_LONG);
                } catch (DatabaseOutOfSyncException e) {
                    handleOutOfSync(handler);
                }
            }
        });
        clearFields();
    }

    /**
     * Ends editing mode and cancels any changes
     */
    @FXML
    public void handleCancel() {
        toggleEditMode();
        if (contextItem != null) populateForm();
    }

    /**
     * Displays an error for not yet implemented features
     */
    protected void displayNotImplementedError() {
        mainWindowView.getContentView().footer.show("Feature not yet implemented, patience is advised.", Footer.NotificationType.INFORMATION);
    }

    public T getContextItem() {
        return contextItem;
    }

    public void setMainWindowView(MainWindowView mainWindowView) {
        this.mainWindowView = mainWindowView;
    }
}

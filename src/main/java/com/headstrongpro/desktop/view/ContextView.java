package com.headstrongpro.desktop.view;

import com.headstrongpro.desktop.core.SyncHandler;
import com.headstrongpro.desktop.core.fxControls.Footer;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputControl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;

/**
 * ContextView
 */
public abstract class ContextView<T> {
    // Currently set context item
    protected T contextItem = null;

    // Main view controller
    protected MainWindowView mainWindowView;

    // List of form text fields
    protected ArrayList<TextField> textFields;

    public void setMainWindowView(MainWindowView mainWindowView) {
        this.mainWindowView = mainWindowView;
    }

    /**
     * Changes the context items and invokes population of the form
     */
    public void changeContextItem(T t) {
        this.contextItem = t;
        populateForm();
    }

    public T getContextItem(){
        return contextItem;
    }

    /**
     * Sets the values on context initialization
     */
    public abstract void populateForm();

    /**
     * Clears all the text input
     */
    protected abstract void clearFields();

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
    protected void handleOutOfSync(SyncHandler<T> handler){
        Alert a = new Alert(Alert.AlertType.CONFIRMATION);
        mainWindowView.getContentView().footer.show("Warning! Data inconsistency!", Footer.NotificationType.WARNING);
        a.setHeaderText("Warning! Database contains newer data.");
        a.setContentText("Do you want to reload the data? Clicking 'Cancel' will clear all the input");
        Optional<ButtonType> response = a.showAndWait();
        response.ifPresent(e -> {
            if(ButtonType.OK.equals(e)){
                changeContextItem(handler.handle());
            } else {
                clearFields();
            }
        });
    }
}

package com.headstrongpro.desktop.view;

import javafx.scene.control.TextField;
import javafx.scene.control.TextInputControl;

import java.util.ArrayList;
import java.util.Arrays;

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
}

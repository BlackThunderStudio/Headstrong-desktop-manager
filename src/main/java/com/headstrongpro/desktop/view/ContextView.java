package com.headstrongpro.desktop.view;

import javafx.scene.control.TextField;
import javafx.scene.control.TextInputControl;

import java.util.Arrays;

/**
 * ContextView
 */
public abstract class ContextView<T> {

    protected T contextItem = null;
    protected MainWindowView mainWindowView;

    public void changeContextItem(T t) {
        this.contextItem = t;
        setFields();
    }

    public void setMainWindowView(MainWindowView mainWindowView) {
        this.mainWindowView = mainWindowView;
    }

    /***
     * sets the values on context initialization
     */
    public abstract void setFields();

    /***
     * Clears all the text input
     */
    protected abstract void clearFields();

    /***
     * Validates the contexts window input fields
     * @param textFields a collection of TextField objects
     * @return True if ALL the objects are valid, otherwise False
     */
    protected boolean validateInput(TextField... textFields){
        return Arrays.stream(textFields)
                .map(TextInputControl::getText)
                .filter(e -> e.isEmpty() || e.contains(";") || e.contains(":") || e.contains("^"))
                .count() == 0;
    }
}

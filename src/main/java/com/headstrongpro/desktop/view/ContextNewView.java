package com.headstrongpro.desktop.view;

import javafx.fxml.FXML;
import javafx.scene.control.Button;

/**
 * Created by jakub on 26/05/2017.
 */
public abstract class ContextNewView<T> {

    //Common controls
    @FXML
    public Button saveButton;
    @FXML
    public Button cancelButton;

    protected MainWindowView mainWindowView;


    public void setMainWindowView(MainWindowView mainWindowView) {
        this.mainWindowView = mainWindowView;
    }


}


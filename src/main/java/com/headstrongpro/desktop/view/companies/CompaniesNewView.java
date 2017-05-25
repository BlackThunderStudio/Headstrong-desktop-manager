package com.headstrongpro.desktop.view.companies;

/**
 * Created by 1062085 on 25-May-17.
 */

// "New" context view controls

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;


public class CompaniesNewView {
    @FXML
    public TextField newCompanyNameTextfield;
    @FXML
    public TextField newCompanyCvrTextfield;
    @FXML
    public TextField newCompanyStreetTextfield;
    @FXML
    public TextField newCompanyPostalTextfield;
    @FXML
    public TextField newCompanyCityTextfield;
    @FXML
    public TextField newCompanyCountryTextfield;
    @FXML
    public Button companySaveButton;
    @FXML
    public Button companyCancelButton;
}

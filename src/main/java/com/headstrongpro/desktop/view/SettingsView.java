package com.headstrongpro.desktop.view;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.*;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by Rajmund Staniek on 19-May-17.
 */
public class SettingsView implements Initializable {
    @FXML
    public TextField tf_path;
    @FXML
    public Button btn_open;

    @FXML
    public void btn_open_onClick(ActionEvent actionEvent) {
        Stage stage = new Stage();
        FileChooser fc = new FileChooser();
        fc.setTitle("Open configuration file");
        fc.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Config files", "*.json", "*.cfg"),
                new FileChooser.ExtensionFilter("All files", "*.*")
        );
        File file = fc.showOpenDialog(stage);
        tf_path.setText(file.getAbsolutePath());

        String jarPath = SettingsView.class.getProtectionDomain().getCodeSource().getLocation().getPath();
        String[] split = jarPath.split("/");
        String newPath = "";
        for (int i = 0; i < split.length - 1; i++) {
            newPath += split[i] + "/";
        }
        File newFIle = new File(newPath + "/cfg/");

        if (!newFIle.exists()) {
            newFIle.mkdirs();
        }

        byte[] buffer = new byte[4096];

        int length;

        try {
            InputStream in = new FileInputStream(file);
            OutputStream out = new FileOutputStream(newFIle);

            while ((length = in.read(buffer)) > 0) {
                out.write(buffer, 0, length);
            }

            in.close();
            out.close();
            System.out.println("File saved to: " + newFIle.getAbsolutePath());
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        tf_path.setEditable(false);
    }

}

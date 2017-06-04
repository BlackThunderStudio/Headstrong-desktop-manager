package com.headstrongpro.desktop.view.resources;

import com.headstrongpro.desktop.controller.ResourcesController;
import com.headstrongpro.desktop.core.SyncHandler;
import com.headstrongpro.desktop.core.exception.DatabaseOutOfSyncException;
import com.headstrongpro.desktop.core.exception.ModelSyncException;
import com.headstrongpro.desktop.core.fxControls.Footer;
import com.headstrongpro.desktop.model.resource.AudioResource;
import com.headstrongpro.desktop.model.resource.Resource;
import com.headstrongpro.desktop.view.ContextView;
import com.jfoenix.controls.JFXSlider;
import com.jfoenix.controls.JFXSpinner;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;

import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

/**
 * desktop-manager
 * <p>
 * <p>
 * Created by rajmu on 17.05.26.
 */
public class ResourcesAudioContext extends ContextView<AudioResource> implements Initializable {

    private static final String BTN_PLAY = "Play";
    private static final String BTN_PAUSE = "Pause";


    @FXML
    public TextField nameField;

    @FXML
    public TextField descriptionField;

    @FXML
    public Button editButton;

    @FXML
    public Button deleteButton;

    @FXML
    public JFXSlider audioSeekbarSlider;

    @FXML
    public Button playPauseButton;

    @FXML
    public Label labelCurrentPlaybackTime;

    @FXML
    public Label labelDuration;

    @FXML
    public JFXSpinner fileLoader;

    private ResourcesController controller;
    private Media media;
    private MediaPlayer mediaPlayer;
    private boolean isPlaying;
    private SyncHandler<AudioResource> syncHandler = () -> {
        try {
            Resource.ofType(controller.getResourceById(contextItem.getId()));
        } catch (ModelSyncException e) {
            e.printStackTrace();
            mainWindowView.getContentView().footer.show(e.getMessage(), Footer.NotificationType.ERROR, Footer.FADE_LONG);
        }
        return null;
    };

    @Override
    public void populateForm() {
        nameField.setText(contextItem.getName());
        descriptionField.setText(contextItem.getDescription());
        initPlayer();
    }

    private void initPlayer() {
        fileLoader.setVisible(true);
        playPauseButton.setDisable(true);
        audioSeekbarSlider.setDisable(true);
        playPauseButton.setText(BTN_PLAY);
        labelCurrentPlaybackTime.setText("");
        labelDuration.setText("");
        try {
            URL audioURL = new URL(contextItem.getUrl());
            media = new Media(audioURL.toURI().toString());
            mediaPlayer = new MediaPlayer(media);
        } catch (MalformedURLException | URISyntaxException e) {
            e.printStackTrace();
        }
        isPlaying = false;

        mediaPlayer.setOnReady(() -> {
            audioSeekbarSlider.setMin(0);
            audioSeekbarSlider.setValue(0.0);
            System.out.println("Media duration: " + media.getDuration().toSeconds());
            setDurationLabel(media.getDuration(), labelDuration);
            audioSeekbarSlider.setMax(media.getDuration().toSeconds());
            playPauseButton.setDisable(false);
            fileLoader.setVisible(false);
        });

        mediaPlayer.currentTimeProperty().addListener(((observable, oldValue, newValue) -> updateValues()));

        audioSeekbarSlider.setOnMouseClicked(e -> mediaPlayer.seek(Duration.seconds(audioSeekbarSlider.getValue())));

        mediaPlayer.setOnError(() -> {
            this.disablePlayer();
            mainWindowView.getContentView().footer.show("Error! Could not load the file from a server.", Footer.NotificationType.ERROR, Footer.FADE_LONG);
        });
        mediaPlayer.setOnEndOfMedia(this::disablePlayer);
    }

    @Override
    protected void clearFields() {
        nameField.clear();
        descriptionField.clear();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        controller = new ResourcesController();
        audioSeekbarSlider.setValue(0.0);
        labelCurrentPlaybackTime.setText("");
        labelDuration.setText("");
    }

    private void updateValues() {
        if (audioSeekbarSlider != null) {
            Platform.runLater(() -> {
                audioSeekbarSlider.setDisable(media.getDuration().isUnknown());
                setDurationLabel(mediaPlayer.getCurrentTime(), labelCurrentPlaybackTime);
            });
        }
    }

    private void disablePlayer() {
        fileLoader.setVisible(false);
        mediaPlayer.stop();
        isPlaying = false;
        audioSeekbarSlider.setValue(0.0);
        playPauseButton.setText(BTN_PLAY);
    }

    private void setDurationLabel(Duration duration, Label label) {
        if (!audioSeekbarSlider.isDisabled() &&
                !audioSeekbarSlider.isValueChanging() &&
                media.getDuration().greaterThan(Duration.ZERO)) {
            audioSeekbarSlider.setValue(duration.toSeconds());
        }
        int mins = (int) duration.toMinutes();
        String secs = String.valueOf((int) duration.toSeconds() % 60);
        if (((int) duration.toSeconds() % 60) < 10) {
            secs = "0" + secs;
        }
        label.setText(String.format("%d:%s", mins, secs));
    }

    @FXML
    public void editOnClick(ActionEvent actionEvent) {
        if (validateInput(nameField, descriptionField)) {
            contextItem.setName(nameField.getText());
            contextItem.setDescription(descriptionField.getText());
            try {
                mainWindowView.getContentView().footer.show("Updating " + contextItem.getName() + "...", Footer.NotificationType.LOADING);
                controller.editResource(contextItem);
                mainWindowView.getContentView().footer.show("Resource updated.", Footer.NotificationType.COMPLETED, Footer.FADE_QUICK);
                mainWindowView.getContentView().refreshButton.fire();
            } catch (DatabaseOutOfSyncException e) {
                e.printStackTrace();
                handleOutOfSync(syncHandler);
            } catch (ModelSyncException e) {
                e.printStackTrace();
                mainWindowView.getContentView().footer.show(e.getMessage(), Footer.NotificationType.ERROR, Footer.FADE_LONG);
            }
        } else {
            mainWindowView.getContentView().footer.show("Values are invalid!", Footer.NotificationType.WARNING);
        }
    }

    @FXML
    public void deleteOnClick(ActionEvent actionEvent) {
        Alert a = new Alert(Alert.AlertType.CONFIRMATION);
        a.setHeaderText("Are you sure you want to delete " + contextItem.getName() + "?");
        a.setContentText("You cannot take that action back");
        Optional<ButtonType> response = a.showAndWait();
        response.ifPresent(btn -> {
            if (ButtonType.OK.equals(btn)) {
                mainWindowView.getContentView().footer.show("Deleting " + contextItem.getName() + "...", Footer.NotificationType.LOADING);
                try {
                    controller.delete(contextItem);
                    mainWindowView.getContentView().footer.show("Resource deleted.", Footer.NotificationType.COMPLETED);
                    mainWindowView.getContentView().refreshButton.fire();
                } catch (DatabaseOutOfSyncException e) {
                    e.printStackTrace();
                    handleOutOfSync(syncHandler);
                } catch (ModelSyncException e) {
                    e.printStackTrace();
                    mainWindowView.getContentView().footer.show(e.getMessage(), Footer.NotificationType.ERROR, Footer.FADE_LONG);
                }
            }
        });
    }

    @FXML
    public void playPauseOnClick(ActionEvent event) {
        if (isPlaying) {
            playPauseButton.setText(BTN_PLAY);
            mediaPlayer.pause();
            isPlaying = false;
        } else {
            playPauseButton.setText(BTN_PAUSE);
            mediaPlayer.play();
            isPlaying = true;
        }
    }

    public void stopAudio() {
        if (isPlaying) {
            mediaPlayer.pause();
            isPlaying = false;
        }
    }
}

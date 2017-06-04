package com.headstrongpro.desktop.view.resources;

import com.headstrongpro.desktop.controller.ResourcesController;
import com.headstrongpro.desktop.core.exception.DatabaseOutOfSyncException;
import com.headstrongpro.desktop.core.exception.ModelSyncException;
import com.headstrongpro.desktop.core.fxControls.Footer;
import com.headstrongpro.desktop.model.resource.AudioResource;
import com.headstrongpro.desktop.model.resource.Resource;
import com.headstrongpro.desktop.view.ContextView;
import com.jfoenix.controls.JFXSlider;
import com.jfoenix.controls.JFXSpinner;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;

import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Arrays;
import java.util.ResourceBundle;

/**
 * Audio Resources Context View
 */
public class AudioResourcesContextView extends ContextView<Resource> implements Initializable {

    private static final String BTN_PLAY = "Play";
    private static final String BTN_PAUSE = "Pause";

    @FXML
    public TextField nameField, descriptionField;

    @FXML
    public JFXSlider audioSeekBarSlider;

    @FXML
    public Button playPauseButton;

    @FXML
    public Label labelCurrentPlaybackTime, labelDuration;

    @FXML
    public JFXSpinner fileLoader;

    private Media media;
    private MediaPlayer mediaPlayer;
    private boolean isPlaying;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        textInputControls.addAll(Arrays.asList(
                nameField,
                descriptionField
        ));
        controller = new ResourcesController();
        audioSeekBarSlider.setValue(0.0);
        labelCurrentPlaybackTime.setText("");
        labelDuration.setText("");
    }

    @Override
    public void populateForm() {
        nameField.setText(contextItem.getName());
        descriptionField.setText(contextItem.getDescription());
        initPlayer();
    }

    @FXML
    public void handleEdit() {
        if (validateInput(nameField, descriptionField)) {
            contextItem.setName(nameField.getText());
            contextItem.setDescription(descriptionField.getText());
            try {
                mainWindowView.getContentView().footer.show("Updating " + contextItem.getName() + "...", Footer.NotificationType.LOADING);
                controller.edit(contextItem);
                mainWindowView.getContentView().footer.show("Resource updated.", Footer.NotificationType.COMPLETED, Footer.FADE_QUICK);
                mainWindowView.getContentView().refreshButton.fire();
            } catch (DatabaseOutOfSyncException e) {
                e.printStackTrace();
                handleOutOfSync(handler);
            } catch (ModelSyncException e) {
                e.printStackTrace();
                mainWindowView.getContentView().footer.show(e.getMessage(), Footer.NotificationType.ERROR, Footer.FADE_LONG);
            }
        } else {
            mainWindowView.getContentView().footer.show("Values are invalid!", Footer.NotificationType.WARNING);
        }
    }

    @FXML
    public void playPauseOnClick() {
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

    void stopAudio() {
        if (isPlaying) {
            mediaPlayer.pause();
            isPlaying = false;
        }
    }

    private void initPlayer() {
        AudioResource audioResource = (AudioResource) contextItem;

        fileLoader.setVisible(true);
        playPauseButton.setDisable(true);
        audioSeekBarSlider.setDisable(true);
        playPauseButton.setText(BTN_PLAY);
        labelCurrentPlaybackTime.setText("");
        labelDuration.setText("");
        try {
            URL audioURL = new URL(audioResource.getUrl());
            media = new Media(audioURL.toURI().toString());
            mediaPlayer = new MediaPlayer(media);
        } catch (MalformedURLException | URISyntaxException e) {
            e.printStackTrace();
        }
        isPlaying = false;

        mediaPlayer.setOnReady(() -> {
            audioSeekBarSlider.setMin(0);
            audioSeekBarSlider.setValue(0.0);
            System.out.println("Media duration: " + media.getDuration().toSeconds());
            setDurationLabel(media.getDuration(), labelDuration);
            audioSeekBarSlider.setMax(media.getDuration().toSeconds());
            playPauseButton.setDisable(false);
            fileLoader.setVisible(false);
        });

        mediaPlayer.currentTimeProperty().addListener(((observable, oldValue, newValue) -> updateValues()));

        audioSeekBarSlider.setOnMouseClicked(e -> mediaPlayer.seek(Duration.seconds(audioSeekBarSlider.getValue())));

        mediaPlayer.setOnError(() -> {
            this.disablePlayer();
            mainWindowView.getContentView().footer.show("Error! Could not load the file from a server.", Footer.NotificationType.ERROR, Footer.FADE_LONG);
        });
        mediaPlayer.setOnEndOfMedia(this::disablePlayer);
    }

    private void updateValues() {
        if (audioSeekBarSlider != null) {
            Platform.runLater(() -> {
                audioSeekBarSlider.setDisable(media.getDuration().isUnknown());
                setDurationLabel(mediaPlayer.getCurrentTime(), labelCurrentPlaybackTime);
            });
        }
    }

    private void disablePlayer() {
        fileLoader.setVisible(false);
        mediaPlayer.stop();
        isPlaying = false;
        audioSeekBarSlider.setValue(0.0);
        playPauseButton.setText(BTN_PLAY);
    }

    private void setDurationLabel(Duration duration, Label label) {
        if (!audioSeekBarSlider.isDisabled() &&
                !audioSeekBarSlider.isValueChanging() &&
                media.getDuration().greaterThan(Duration.ZERO)) {
            audioSeekBarSlider.setValue(duration.toSeconds());
        }
        int mins = (int) duration.toMinutes();
        String secs = String.valueOf((int) duration.toSeconds() % 60);
        if (((int) duration.toSeconds() % 60) < 10) {
            secs = "0" + secs;
        }
        label.setText(String.format("%d:%s", mins, secs));
    }
}

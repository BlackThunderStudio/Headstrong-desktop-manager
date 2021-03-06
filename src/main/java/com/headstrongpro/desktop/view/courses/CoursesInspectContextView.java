package com.headstrongpro.desktop.view.courses;

import com.headstrongpro.desktop.controller.CoursesController;
import com.headstrongpro.desktop.core.fxControls.Footer;
import com.headstrongpro.desktop.model.Course;
import com.headstrongpro.desktop.model.resource.AudioResource;
import com.headstrongpro.desktop.model.resource.Resource;
import com.headstrongpro.desktop.model.resource.ResourceType;
import com.headstrongpro.desktop.view.ContentSource;
import com.headstrongpro.desktop.view.ContextView;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Callback;
import javafx.util.Duration;

import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Courses Inspect Context View
 */
public class CoursesInspectContextView extends ContextView<Course> implements Initializable {

    private static final double PREVIEW_TIME_FRACTION = 0.025;
    private static final double PREVIEW_INIT_TIME = 0.25;
    private static final double PREVIEW_RANDOM_DELTA = 0.9;
    @FXML
    public Label labelName;
    @FXML
    public Button coursesNewCancelButton;
    @FXML
    public ListView<Resource> resourcesListView;
    private CoursesController controller;
    private ObservableList<Resource> resources;
    private Resource selected;
    private ContextMenu listContextMenu;
    private MediaPlayer mediaPlayer;

    @Override
    protected void populateForm() {
        labelName.setText(contextItem.getName());
        Task<ObservableList<Resource>> loadResources = new Task<ObservableList<Resource>>() {
            @Override
            protected ObservableList<Resource> call() throws Exception {
                return FXCollections.observableArrayList(controller.getAssignedResources(contextItem));
            }
        };

        loadResources.valueProperty().addListener(((observable, oldValue, newValue) -> {
            if (newValue != null) {
                this.resources = newValue;
                resourcesListView.setItems(this.resources);

                resourcesListView.setCellFactory(new Callback<ListView<Resource>, ListCell<Resource>>() {
                    @Override
                    public ListCell<Resource> call(ListView<Resource> param) {
                        return new ListCell<Resource>() {
                            @Override
                            protected void updateItem(Resource r, boolean b) {
                                super.updateItem(r, b);
                                if (r != null) {
                                    setText(String.format("%s\n(%s)", r.getName(), determineType(r)));
                                }
                            }
                        };
                    }
                });
            }
        }));

        new Thread(loadResources).start();
    }

    @FXML
    public void cancelCourseOnClick() {
        mainWindowView.changeContext(ContentSource.COURSES);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        controller = new CoursesController();

        resourcesListView.getSelectionModel().selectedItemProperty().addListener(((observable, oldValue, newValue) -> {
            if (newValue != null) {
                selected = newValue;
            }
        }));

        //context menu
        listContextMenu = new ContextMenu();
        MenuItem previewItem = new MenuItem("Preview");
        previewItem.setOnAction(e -> {
            if (selected.getType().equals(ResourceType.AUDIO)) previewAudio();
        });
        listContextMenu.getItems().add(previewItem);

        resourcesListView.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
            if (event.getButton().equals(MouseButton.SECONDARY)) {
                listContextMenu.show(resourcesListView, event.getScreenX(), event.getScreenY());
            }
        });
    }

    private String determineType(Resource resource) {
        if (resource.getType().equals(ResourceType.TEXT)) return "Text";
        else if (resource.getType().equals(ResourceType.AUDIO)) return "Audio";
        else if (resource.getType().equals(ResourceType.IMAGE)) return "Image";
        else return "Video";
    }

    private void previewAudio() {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
        }
        System.out.println("Previewing " + selected.getName());
        AudioResource audio = Resource.ofType(selected);
        try {
            mainWindowView.getContentView().footer.show("Buffering resource...", Footer.NotificationType.LOADING);
            assert audio != null;
            Media media = new Media(new URL(audio.getUrl()).toURI().toString());
            mediaPlayer = new MediaPlayer(media);

            mediaPlayer.setOnReady(() -> {
                mainWindowView.getContentView().footer.hide();
                int duration = (int) media.getDuration().toMillis();
                double randomModifier = 1.0 + (ThreadLocalRandom.current().nextDouble(-0.5, 0.51) * PREVIEW_RANDOM_DELTA);
                mediaPlayer.seek(Duration.millis(duration * PREVIEW_INIT_TIME * randomModifier));
                mediaPlayer.setVolume(0.01);
                mediaPlayer.play();
                Timeline timeline = new Timeline(
                        new KeyFrame(Duration.millis(1500),
                                new KeyValue(mediaPlayer.volumeProperty(), 1))
                );
                timeline.play();
                Task<Void> waitTillTimeTrigger = new Task<Void>() {
                    @Override
                    protected Void call() throws Exception {
                        Thread.sleep((long) ((long) duration * PREVIEW_TIME_FRACTION));
                        Platform.runLater(() -> new Timeline(new KeyFrame(Duration.seconds(1), new KeyValue(mediaPlayer.volumeProperty(), 0))).play());
                        return null;
                    }
                };

                new Thread(waitTillTimeTrigger).start();
            });
            mediaPlayer.volumeProperty().addListener(((observable, oldValue, newValue) -> {
                if (oldValue.doubleValue() > newValue.doubleValue() && newValue.doubleValue() == 0.0) {
                    mediaPlayer.stop();
                    System.out.println("Preview stopped.");
                }
            }));
            mediaPlayer.setOnError(() -> mainWindowView.getContentView().footer.show("Error!, Could not playback media!", Footer.NotificationType.ERROR));
            mediaPlayer.setOnEndOfMedia(mediaPlayer::stop);
        } catch (URISyntaxException | MalformedURLException e) {
            e.printStackTrace();
            mainWindowView.getContentView().footer.show(e.getMessage(), Footer.NotificationType.ERROR);
        }
    }
}

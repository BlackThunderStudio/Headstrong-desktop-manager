package com.headstrongpro.desktop.view.courses;

import com.headstrongpro.desktop.controller.CoursesController;
import com.headstrongpro.desktop.controller.ResourcesController;
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

/**
 * desktop-manager
 * <p>
 * <p>
 * Created by rajmu on 17.06.04.
 */
public class CoursesInspectContextView extends ContextView<Course> implements Initializable {

    @FXML
    public Label labelName;
    @FXML
    public Button coursesNewCancelButton;
    @FXML
    public ListView<Resource> resourcesListView;

    private ResourcesController resourcesController;
    private CoursesController controller;
    private ObservableList<Resource> resources;
    private Resource selected;

    private ContextMenu listContextMenu;

    private static final double PREVIEW_TIME_FRACTION = 0.05;

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
            if(newValue != null){
                this.resources = newValue;
                resourcesListView.setItems(this.resources);

                resourcesListView.setCellFactory(new Callback<ListView<Resource>, ListCell<Resource>>() {
                    @Override
                    public ListCell<Resource> call(ListView<Resource> param) {
                        return new ListCell<Resource>(){
                            @Override
                            protected void updateItem(Resource r, boolean b){
                                super.updateItem(r, b);
                                if(r != null){
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
        resourcesController = new ResourcesController();
        controller = new CoursesController();

        resourcesListView.getSelectionModel().selectedItemProperty().addListener(((observable, oldValue, newValue) -> {
            if(newValue != null){
                selected = newValue;
            }
        }));

        //context menu
        listContextMenu = new ContextMenu();
        MenuItem previewItem = new MenuItem("Preview");
        previewItem.setOnAction(e -> {
            if(selected.getType().equals(ResourceType.AUDIO)) previewAudio();
        });
        listContextMenu.getItems().add(previewItem);

        resourcesListView.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
            if(event.getButton().equals(MouseButton.SECONDARY)){
                listContextMenu.show(resourcesListView, event.getScreenX(), event.getScreenY());
            }
        });
    }

    private String determineType(Resource resource){
        if(resource.getType().equals(ResourceType.TEXT)) return "Text";
        else if(resource.getType().equals(ResourceType.AUDIO)) return "Audio";
        else if(resource.getType().equals(ResourceType.IMAGE)) return "Image";
        else return "Video";
    }

    private MediaPlayer mediaPlayer;

    private void previewAudio(){
        System.out.println("Previewing " + selected.getName());
        AudioResource audio = Resource.ofType(selected);
        try {
            mainWindowView.getContentView().footer.show("Buffering resource...", Footer.NotificationType.LOADING);
            Media media = new Media(new URL(audio.getUrl()).toURI().toString());
            mediaPlayer = new MediaPlayer(media);

            mediaPlayer.setOnReady(() -> {
                mainWindowView.getContentView().footer.hide();
                int duration = (int) media.getDuration().toMillis();
                mediaPlayer.seek(Duration.millis(duration * 0.25));
                mediaPlayer.setVolume(0);
                mediaPlayer.play();
                Timeline timeline = new Timeline(
                  new KeyFrame(Duration.seconds(1),
                          new KeyValue(mediaPlayer.volumeProperty(), 1))
                );
                timeline.play();
                Task<Void> waitTillTimeTrigger = new Task<Void>() {
                    @Override
                    protected Void call() throws Exception {
                        Thread.sleep((long) ((long) duration * PREVIEW_TIME_FRACTION));
                        Platform.runLater(mediaPlayer::stop);
                        return null;
                    }
                };

                new Thread(waitTillTimeTrigger).start();
            });
            mediaPlayer.setOnError(() -> mainWindowView.getContentView().footer.show("Error!, Could not playback media!", Footer.NotificationType.ERROR));
            mediaPlayer.setOnEndOfMedia(mediaPlayer::stop);
        } catch (URISyntaxException | MalformedURLException e) {
            e.printStackTrace();
            mainWindowView.getContentView().footer.show(e.getMessage(), Footer.NotificationType.ERROR);
        }
    }
}

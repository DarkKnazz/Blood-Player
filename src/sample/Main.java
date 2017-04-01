package sample;

import com.beaglebuddy.id3.pojo.AttachedPicture;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Slider;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.File;
import java.io.IOException;
import java.util.*;

import com.beaglebuddy.mp3.MP3;
import com.beaglebuddy.id3.enums.PictureType;

public class Main extends Application {
    public List<File> list;
    List<Label> labelList = new ArrayList<>();
    MediaPlayer mediaPlayer;
    int Checked = 0;
    public FileChooser fileChooser = new FileChooser();
    private double xOffset = 0;
    private double yOffset = 0;
    int kol = 0;
    int counter;
    Label elemList;
    String str;
    @Override
    public void start(Stage primaryStage) throws Exception{
        VBox playlist = new VBox();
        playlist.setId("playlist");
        primaryStage.initStyle(StageStyle.UNDECORATED);
        MediaPlayer[] mediaPl = new MediaPlayer[1];
        VBox root = new VBox();

        root.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                xOffset = event.getSceneX();
                yOffset = event.getSceneY();
            }
        });
        root.setOnMouseDragged(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                primaryStage.setX(event.getScreenX() - xOffset);
                primaryStage.setY(event.getScreenY() - yOffset);
            }
        });

        ScrollPane sp = new ScrollPane();
        sp.setContent(playlist);
        sp.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        sp.setPadding(new Insets(10, 10, 10, 20));
        root.setPadding(new Insets(20, 0,0, 0));
        root.getStylesheets().add
                (Main.class.getResource("app.css").toExternalForm());
        Label musicBox = new Label("No tracks is playing");
        musicBox.setId("musicBox");
        musicBox.setPadding(new Insets(0, 0,0, 20));
        Slider slider = new Slider();
        slider.setMin(0);
        slider.setMax(100);
        slider.setValue(0);
        Label musicTime = new Label("00:00");
        musicTime.setId("musicTime");
        ImageView songImage = new ImageView(new Image("file:resources/Cover.jpg"));
        ImageView playPause = new ImageView(new Image("file:src/images/play.png"));
        ImageView next = new ImageView(new Image("file:src/images/pause.png"));
        ImageView prev = new ImageView(new Image("file:src/images/pause.png"));
        ImageView exitButton = new ImageView(new Image("file:src/images/exit.png"));

        next.setFitWidth(70.0);
        next.setFitHeight(70.0);
        playPause.setFitWidth(70.0);
        playPause.setFitHeight(70.0);
        prev.setFitWidth(70.0);
        prev.setFitHeight(70.0);
        songImage.setFitWidth(110.0);
        songImage.setFitHeight(110.0);
        exitButton.setFitWidth(20.0);
        exitButton.setFitHeight(20.0);
        songImage.setId("songImage");

        HBox cnrButtons = new HBox();
        cnrButtons.getChildren().addAll(prev, playPause, next);

        HBox song = new HBox();
        song.getChildren().addAll(slider, musicTime);
        song.setPadding(new Insets(0, 0,0, 10));

        VBox control = new VBox();
        control.getChildren().addAll(musicBox, song, cnrButtons);
        control.setPadding(new Insets(0, 0,0, 0));


        VBox mainBlock = new VBox();
        HBox mainUnit = new HBox();
        HBox exitButt = new HBox();

        exitButt.getChildren().add(exitButton);
        exitButt.setPadding(new Insets(-10, 0, 0, 280));
        mainUnit.setPadding(new Insets(0, 0,0, 5));
        mainUnit.getChildren().addAll(songImage, control);
        mainBlock.getChildren().addAll(exitButt, mainUnit);
        Button browse = new Button("Browse");
        Button save = new Button("Save Playlist");
        Button load = new Button("Load Playlist");
        HBox buttons = new HBox();
        buttons.getChildren().addAll(browse, save, load);
        root.getChildren().add(mainBlock);
        root.getChildren().add(buttons);
        root.getChildren().add(sp);
        primaryStage.setTitle("Blood Player");
        primaryStage.setResizable(false);
        primaryStage.getIcons().add(new Image("file:resources/logo.png"));
        primaryStage.setScene(new Scene(root, 320, 400));
        primaryStage.show();


        browse.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                list = fileChooser.showOpenMultipleDialog(primaryStage);
                String path;
                if(list != null) {
                    System.out.println(list.get(0));
                    labelList.clear();
                    playlist.getChildren().clear();
                    List<AttachedPicture> pict = new ArrayList<AttachedPicture>();
                    for(counter = 0;counter < list.size();counter++){
                        path = list.get(counter).getAbsolutePath();
                        path = path.replace("\\", "/");
                        System.out.println(path);
                        try{
                            MP3 mp3 = new MP3(path);
                            elemList = new Label(mp3.getBand() + " - " + mp3.getTitle());
                            String st = mp3.getLyrics();
                            st = st.replace("\\", "/");
                            System.out.println(st);
                            File f = new File(st);
                            songImage.setImage(new Image(f.toURI().toURL().toString()));
                        }catch(IOException ex) {
                            System.out.println("Error!");
                        }
                        labelList.add(elemList);
                        playlist.getChildren().add(labelList.get(counter));
                    }
                    Media media = new Media(list.get(0).toURI().toString());
                    mediaPlayer = new MediaPlayer(media);
                    labelList.get(0).setStyle("-fx-background-color: white");
                }
            }
        });

        playPause.setOnMouseClicked(event -> {
            if(list != null){
                if(Checked == 0){
                    mediaPlayer.play();
                    Checked = 1;
                }
                else{
                    mediaPlayer.pause();
                    Checked = 0;
                }
            }
        });

        exitButton.setOnMouseClicked(event -> {
            Stage stage = (Stage) exitButton.getScene().getWindow();
            stage.close();
        });
    }


    public static void main(String[] args) {
        launch(args);
    }
}

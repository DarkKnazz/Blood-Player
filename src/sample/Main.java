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
import java.text.DecimalFormat;
import java.util.*;

import com.beaglebuddy.mp3.MP3;
import com.beaglebuddy.id3.enums.PictureType;
import javafx.util.Duration;

public class Main extends Application {
    public List<File> list;
    List<Label> labelList = new ArrayList<>();
    MediaPlayer mediaPlayer;
    List<MediaPlayer>mediaList = new ArrayList<>();
    int Checked = 0;
    public FileChooser fileChooser = new FileChooser();
    private double xOffset = 0;
    private double yOffset = 0;
    int kol = 0;
    int counter = 0;
    int playCnt = 0;
    String path;
    Label elemList;
    String str;
    double totalTimeOfMusic;
    String duration;
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
        ImageView next = new ImageView(new Image("file:src/images/next.png"));
        ImageView prev = new ImageView(new Image("file:src/images/prev.png"));
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
                if(list != null) {
                    System.out.println(list.get(0));
                    labelList.clear();
                    mediaList.clear();
                    playlist.getChildren().clear();
                    for(counter = 0;counter < list.size();counter++){
                        path = list.get(counter).getAbsolutePath();
                        path = path.replace("\\", "/");
                        System.out.println(path);
                        try{
                            MP3 mp3 = new MP3(path);
                            elemList = new Label(mp3.getBand() + " - " + mp3.getTitle());
                            /*String st = mp3.getLyrics();
                            st = st.replace("\\", "/");
                            System.out.println(st);
                            File f = new File(st);
                            songImage.setImage(new Image(f.toURI().toURL().toString()));*/
                        }catch(IOException ex) {
                            System.out.println("Error!");
                        }
                        musicBox.setText("No tracks is playing");
                        musicTime.setText("00,00");
                        slider.setValue(0);
                        labelList.add(elemList);
                        playlist.getChildren().add(labelList.get(counter));
                        mediaList.add(new MediaPlayer(new Media(list.get(counter).toURI().toString())));
                    }
                }
            }
        });

        playPause.setOnMouseClicked(event -> {
            if(list != null){
                if(Checked == 0){
                    mediaList.get(playCnt).play();
                    path = list.get(playCnt).getAbsolutePath();
                    path = path.replace("\\", "/");
                    try{
                        MP3 mp3 = new MP3(path);
                        musicBox.setText(mp3.getBand() + " - " + mp3.getTitle());
                    }catch(IOException ex){}
                    labelList.get(playCnt).setStyle("-fx-text-fill: white");

                    totalTimeOfMusic=mediaList.get(playCnt).getTotalDuration().toSeconds();
                    System.out.println(totalTimeOfMusic);

                    mediaList.get(playCnt).currentTimeProperty().addListener((Observable)->{
                        if(slider.isValueChanging()){
                            mediaList.get(playCnt).seek(Duration.seconds((slider.getValue()*(totalTimeOfMusic)/100)));
                        }
                        if(slider.isPressed()){
                            mediaList.get(playCnt).seek(Duration.seconds((slider.getValue()*(totalTimeOfMusic)/100)));
                        }
                        //updateValues();
                        slider.setValue((mediaList.get(playCnt).getCurrentTime().toSeconds()*100)/totalTimeOfMusic);
                        System.out.println("ok"+mediaList.get(playCnt).getCurrentTime().toSeconds());
                        musicTime.setText('0'+ String.valueOf(new DecimalFormat("#0.00").format(mediaList.get(playCnt).getCurrentTime().toMinutes())));
                    });

                    mediaList.get(playCnt).setCycleCount(MediaPlayer.INDEFINITE);
                    Checked = 1;
                }
                else{
                    mediaList.get(playCnt).pause();
                    Checked = 0;
                }
            }
        });

        next.setOnMouseClicked(event -> {
            if(list != null){
                if(Checked == 1){
                    musicTime.setText("00,00");
                    slider.setValue(0);
                    mediaList.get(playCnt).stop();
                    playCnt++;
                    if(playCnt == list.size()){
                        labelList.get(list.size()-1).setStyle("-fx-text-fill: rgb(236, 4, 42)");
                        playCnt = 0;
                    }
                    else
                        labelList.get(playCnt-1).setStyle("-fx-text-fill: rgb(236, 4, 42)");
                    mediaList.get(playCnt).play();
                    totalTimeOfMusic=mediaList.get(playCnt).getTotalDuration().toSeconds();
                    System.out.println(totalTimeOfMusic);
                    labelList.get(playCnt).setStyle("-fx-text-fill: white");
                    path = list.get(playCnt).getAbsolutePath();
                    path = path.replace("\\", "/");
                    try{
                        MP3 mp3 = new MP3(path);
                        musicBox.setText(mp3.getTitle());
                    }catch(IOException ex){}
                    Checked = 1;
                }
                if(Checked == 0){
                    musicTime.setText("00,00");
                    slider.setValue(0);
                    mediaList.get(playCnt).stop();
                    labelList.get(playCnt).setStyle("-fx-text-fill: rgb(236, 4, 42)");
                    playCnt++;
                    if(playCnt == list.size())
                        playCnt = 0;
                    path = list.get(playCnt).getAbsolutePath();
                    path = path.replace("\\", "/");
                    try{
                        MP3 mp3 = new MP3(path);
                        musicBox.setText(mp3.getTitle());
                    }catch(IOException ex){}
                    labelList.get(playCnt).setStyle("-fx-text-fill: white");
                    mediaList.get(playCnt).play();
                    Checked = 1;
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

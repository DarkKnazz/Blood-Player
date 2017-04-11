package sample;

import com.beaglebuddy.id3.pojo.AttachedPicture;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
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

import java.beans.IntrospectionException;
import java.io.*;
import java.text.DecimalFormat;
import java.util.*;

import com.beaglebuddy.mp3.MP3;
import com.beaglebuddy.id3.enums.PictureType;
import javafx.util.Duration;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

public class Main extends Application {
    public List<File> list;
    List<Label> labelList = new ArrayList<>();
    List<String> listTracks = new ArrayList<>();
    MediaPlayer mediaPlayer;
    List<MediaPlayer>mediaList = new ArrayList<>();
    int Checked = -1;
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

    int timeMin = 0;
    int diff = 0;
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
        ProgressBar progressBar = new ProgressBar();
        progressBar.setProgress(0);
        Label musicTime = new Label("00:00");
        musicTime.setId("musicTime");
        ImageView songImage = new ImageView(new Image("file:resources/Cover.jpg"));
        ImageView playPause = new ImageView(new Image("file:src/images/play.png"));
        ImageView next = new ImageView(new Image("file:src/images/next.png"));
        ImageView prev = new ImageView(new Image("file:src/images/prev.png"));
        ImageView exitButton = new ImageView(new Image("file:src/images/exit.png"));
        ImageView addSong = new ImageView(new Image("file:src/images/playlist.png"));

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
        addSong.setFitWidth(20.0);
        addSong.setFitHeight(20.0);
        songImage.setId("songImage");

        HBox cnrButtons = new HBox();
        cnrButtons.getChildren().addAll(prev, playPause, next);

        HBox song = new HBox();
        song.getChildren().addAll(progressBar, musicTime);
        song.setPadding(new Insets(0, 0,0, 10));

        VBox control = new VBox();
        control.getChildren().addAll(musicBox, song, cnrButtons);
        control.setPadding(new Insets(0, 0,0, 0));


        VBox mainBlock = new VBox();
        HBox mainUnit = new HBox();
        HBox exitButt = new HBox();
        exitButt.getChildren().addAll(addSong, exitButton);
        exitButt.setPadding(new Insets(-10, 0, 0, 260));
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
                playCnt = 0;
                browseFunc(playlist, songImage, musicBox, musicTime, progressBar);
            }
        });

        playPause.setOnMouseClicked(event -> {
            if(list != null){
                if(Checked == 0){
                    mediaList.get(playCnt).play();
                    playPause.setImage(new Image("file:src/images/pause.png"));
                    path = list.get(playCnt).getAbsolutePath();
                    path = path.replace("\\", "/");
                    mp3Worker(songImage, musicBox, path);
                    labelList.get(playCnt).setStyle("-fx-text-fill: white");

                    moveSlider(progressBar, musicTime, playCnt);

                    Checked = 1;
                }
                else{
                    mediaList.get(playCnt).pause();
                    playPause.setImage(new Image("file:src/images/play.png"));
                    Checked = 0;
                }
            }
        });

        next.setOnMouseClicked(event -> {
            if(list != null){
                if(Checked == 1){
                    mediaList.get(playCnt).stop();
                    musicTime.setText("00,00");
                    progressBar.setProgress(0);
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
                    moveSlider(progressBar, musicTime, playCnt);
                    mp3Worker(songImage, musicBox, path);


                    Checked = 1;
                }
                if(Checked == 0){
                    mediaList.get(playCnt).stop();
                    musicTime.setText("00,00");
                    progressBar.setProgress(0);
                    labelList.get(playCnt).setStyle("-fx-text-fill: rgb(236, 4, 42)");
                    playCnt++;
                    if(playCnt == list.size())
                        playCnt = 0;
                    path = list.get(playCnt).getAbsolutePath();
                    path = path.replace("\\", "/");
                    mp3Worker(songImage, musicBox, path);
                    labelList.get(playCnt).setStyle("-fx-text-fill: white");
                    moveSlider(progressBar, musicTime, playCnt);
                    mediaList.get(playCnt).play();
                    Checked = 1;
                }
            }
        });

        prev.setOnMouseClicked(event -> {
            if(list != null){
                if(Checked == 1){
                    mediaList.get(playCnt).stop();
                    musicTime.setText("00,00");
                    progressBar.setProgress(0);
                    playCnt--;
                    if(playCnt == -1){
                        labelList.get(0).setStyle("-fx-text-fill: rgb(236, 4, 42)");
                        playCnt = list.size()-1;
                    }
                    else
                        labelList.get(playCnt+1).setStyle("-fx-text-fill: rgb(236, 4, 42)");
                    mediaList.get(playCnt).play();
                    totalTimeOfMusic=mediaList.get(playCnt).getTotalDuration().toSeconds();
                    System.out.println(totalTimeOfMusic);
                    labelList.get(playCnt).setStyle("-fx-text-fill: white");
                    path = list.get(playCnt).getAbsolutePath();
                    path = path.replace("\\", "/");
                    moveSlider(progressBar, musicTime, playCnt);
                    mp3Worker(songImage, musicBox, path);


                    Checked = 1;
                }
                if(Checked == 0){
                    mediaList.get(playCnt).stop();
                    musicTime.setText("00,00");
                    progressBar.setProgress(0);
                    labelList.get(playCnt).setStyle("-fx-text-fill: rgb(236, 4, 42)");
                    playCnt--;
                    if(playCnt == -1)
                        playCnt = list.size()-1;
                    path = list.get(playCnt).getAbsolutePath();
                    path = path.replace("\\", "/");
                    mp3Worker(songImage, musicBox, path);
                    labelList.get(playCnt).setStyle("-fx-text-fill: white");
                    moveSlider(progressBar, musicTime, playCnt);
                    mediaList.get(playCnt).play();
                    Checked = 1;
                }
            }
        });

        exitButton.setOnMouseClicked(event -> {
            Stage stage = (Stage) exitButton.getScene().getWindow();
            stage.close();
        });

        addSong.setOnMouseClicked(event -> {
            listTracks.add(list.get(playCnt).getAbsolutePath());
        });

        save.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                for(int i = 0; i < listTracks.size(); i++){
                    System.out.println(listTracks.get(i));
                }
            }
        });

        load.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                try(BufferedReader reader = new BufferedReader(new FileReader(fileChooser.showOpenDialog(primaryStage)))){
                    String txt;
                    List<String> names = new ArrayList<String>();
                    list = new ArrayList<File>();
                    while((txt = reader.readLine()) != null) {
                        names.add(txt);
                    }
                    System.out.println(names.get(0));
                    if(names != null){
                        playCnt = 0;
                        for(int j =0; j < names.size(); j++){
                            list.add(new File(names.get(j)));
                        }
                    }
                    browseFunc(playlist, songImage, musicBox, musicTime, progressBar);
                }
                catch(IOException ex){}
            }
        });
    }

    public void moveSlider(ProgressBar progressBar, Label musicTime, int cnt){
        totalTimeOfMusic=mediaList.get(cnt).getTotalDuration().toSeconds();
        System.out.println(totalTimeOfMusic);

        diff = 0; timeMin = 0;

        mediaList.get(cnt).currentTimeProperty().addListener((Observable)->{
            //updateValues();
            int timeSec = 0;
            String t1 = "0", t2 = "0", tItog = "";
            double check = 0;
            progressBar.setProgress((mediaList.get(cnt).getCurrentTime().toSeconds())/totalTimeOfMusic);
            timeSec = (int)mediaList.get(cnt).getCurrentTime().toSeconds();
            check = mediaList.get(cnt).getCurrentTime().toSeconds();
            System.out.println(check);
            if((timeSec % 60 == 0)&&(timeSec != 0)){
                timeSec = 0;
                try{TimeUnit.SECONDS.sleep(1);}
                catch(InterruptedException ex){}
                timeMin++;
                diff += 60;
            }
            if(timeSec - diff <= 9){
                t1 = t1 + String.valueOf(timeSec - diff);
            }
            else
                t1 = String.valueOf(timeSec - diff);
            if(timeMin <= 9){
                t2 = t2 + String.valueOf(timeMin);
            }
            else
                t2 = String.valueOf(timeMin);
            tItog = tItog + t2 + ":" + t1;
            musicTime.setText(tItog);

            double curTime = mediaList.get(cnt).getCurrentTime().toSeconds();
            if(curTime == totalTimeOfMusic){
                mediaList.get(cnt).stop();
                labelList.get(cnt).setStyle("-fx-text-fill: rgb(236, 4, 42)");
            }
        });
    }

    public void browseFunc(VBox playlist, ImageView songImage, Label musicBox, Label musicTime, ProgressBar progressBar){
        if(Checked != -1){
            mediaList.get(playCnt).stop();
            Checked = 0;
        }
        if(list != null) {
            Checked = 0;
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
                    String st1 = mp3.getBand();
                    String st2 = mp3.getTitle();
                    if(st1 == null || st2 == null)
                        elemList = new Label(path);
                    else
                        elemList = new Label(st1 + " - " + st2);
                }catch(IOException ex) {
                    System.out.println("Error!");
                }
                musicBox.setText("No tracks is playing");
                musicTime.setText("00,00");
                progressBar.setProgress(0);
                labelList.add(elemList);
                playlist.getChildren().add(labelList.get(counter));
                mediaList.add(new MediaPlayer(new Media(list.get(counter).toURI().toString())));
            }
        }
    }

    public void mp3Worker(ImageView songImage, Label musicBox, String path){
        try{
            MP3 mp3 = new MP3(path);
            musicBox.setText(mp3.getTitle());
            String imagePath = mp3.getLyrics();
            if(imagePath != null) {
                imagePath = imagePath.replace("\\", "/");
                File f = new File(imagePath);
                songImage.setImage(new Image(f.toURI().toURL().toString()));
            }
        }catch(IOException ex){}
    }
    public static void main(String[] args) {
        launch(args);
    }
}

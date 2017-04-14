package BloodPlayer;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 * Главный класс программы, через него производится запуск и отрисовка всей графики приложения.
 */
public class Main extends Application {
    private double xOffset = 0;
    private double yOffset = 0;
    @Override
    public void start(Stage primaryStage) throws Exception{
        VBox playlist = new VBox();
        playlist.setId("playlist");
        primaryStage.initStyle(StageStyle.UNDECORATED);
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
        ImageView songImage = new ImageView(new Image("file:resources/logo.png"));
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

        AudioControl audioControl = new AudioControl(primaryStage, playlist, songImage, playPause, next, prev, musicBox, musicTime, progressBar);

        browse.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                audioControl.browseHandle();
            }
        });

        playPause.setOnMouseClicked(event -> {
            audioControl.playPause();
        });

        next.setOnMouseClicked(event -> {
            audioControl.nextSong();
        });

        prev.setOnMouseClicked(event -> {
            audioControl.prevSong();
        });

        exitButton.setOnMouseClicked(event -> {
            Stage stage = (Stage) exitButton.getScene().getWindow();
            stage.close();
        });

        addSong.setOnMouseClicked(event -> {
            audioControl.addSong();
        });

        save.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                audioControl.savePlaylist();
            }
        });

        load.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                audioControl.loadPlaylist();
            }
        });
    }

    /**
     * Метод, в котором и происходит запуск и отрисовка.
     * @param args Параметры командной строки
     */
    public static void main(String[] args) {
        launch(args);
    }
}

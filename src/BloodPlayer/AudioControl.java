package BloodPlayer;

import com.beaglebuddy.mp3.MP3;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Класс контроллера аудио композиций.
 * Центр логики приложения. Здесь описываются все обработчики событий кнопок и переключатели
 */
public class AudioControl {
    private FileChooser fileChooser = new FileChooser();
    private int Checked = -1;                                 //Флаг для проверки воспроизведения

    private int counter = 0;
    private int playCnt = 0;
    private String path;
    private Label elemList;
    private double totalTimeOfMusic;

    private int timeMin = 0;
    private int diff = 0;

    private FileWorker fileWorker = new FileWorker();

    private int curDuration;

    //переменные - элементы гуи приложения
    Stage primaryStage;
    VBox playlist;
    ImageView songImage;
    ImageView playPause;
    ImageView next, prev;
    Label musicBox;
    Label musicTime;
    ProgressBar progressBar;

    /**
     * Конструктор класса контроллерв, который инициализируется в главном классе
     * @param pSt Сцена программы
     * @param p Список плейлиста программы
     * @param sI Картинка песни
     * @param pP Кнопка старт/пауза
     * @param n Кнопка следующей песни
     * @param pr Кнопка предыдущей песни
     * @param mB Метка для названия песни
     * @param mT Метка для вывода времени песни
     * @param pB Элемент прогресса песни
     */
    public AudioControl(Stage pSt, VBox p, ImageView sI, ImageView pP, ImageView n, ImageView pr,Label mB, Label mT, ProgressBar pB){
        primaryStage = pSt;
        playlist = p;
        songImage = sI;
        playPause = pP;
        next = n; prev = pr;
        musicBox = mB;
        musicTime = mT;
        progressBar = pB;
    }

    /**
     * Метод поиска песен, множественный выбор с фильтрацией по mp3
     */
    public void browseHandle(){
        fileWorker.createList(primaryStage);
        diff = 0; timeMin = 0;
        browseFunc(playlist, songImage, playPause, musicBox, musicTime, progressBar);
    }

    /**
     * Метод воспроизведения или приостановки текущей композиции
     */
    public void playPause(){
        if(fileWorker.getList() != null){
            if(Checked == 0){
                fileWorker.getMediaList().get(playCnt).play();
                playPause.setImage(new Image("file:src/images/pause.png"));
                path = fileWorker.getList().get(playCnt).getAbsolutePath();
                path = path.replace("\\", "/");
                mp3Worker(songImage, musicBox, path);
                fileWorker.getLabelList().get(playCnt).setStyle("-fx-text-fill: white");
                curDuration = (int)fileWorker.getMediaList().get(playCnt).getTotalDuration().toSeconds();
                moveSlider(progressBar, musicTime);

                Checked = 1;
            }
            else{
                fileWorker.getMediaList().get(playCnt).pause();
                playPause.setImage(new Image("file:src/images/play.png"));
                Checked = 0;
            }
        }
    }

    /**
     * Метод переключения на следующую песню.
     * При достижении конца списка переходим на начало
     */
    public void nextSong(){
        if(fileWorker.getList() != null){
            if(Checked == 1){
                fileWorker.getMediaList().get(playCnt).stop();
                diff = 0; timeMin = 0;
                musicTime.setText("00,00");
                progressBar.setProgress(0);
                playCnt++;
                if(playCnt == fileWorker.getList().size()){
                    System.out.println(playCnt);
                    fileWorker.getLabelList().get(fileWorker.getList().size()-1).setStyle("-fx-text-fill: rgb(236, 4, 42)");
                    playCnt = 0;
                }
                else
                    fileWorker.getLabelList().get(playCnt-1).setStyle("-fx-text-fill: rgb(236, 4, 42)");
                fileWorker.getMediaList().get(playCnt).play();
                totalTimeOfMusic=fileWorker.getMediaList().get(playCnt).getTotalDuration().toSeconds();
                fileWorker.getLabelList().get(playCnt).setStyle("-fx-text-fill: white");
                path = fileWorker.getList().get(playCnt).getAbsolutePath();
                path = path.replace("\\", "/");
                moveSlider(progressBar, musicTime);
                mp3Worker(songImage, musicBox, path);

                playPause.setImage(new Image("file:src/images/pause.png"));
                Checked = 1;
            }
            if(Checked == 0){
                fileWorker.getMediaList().get(playCnt).stop();
                diff = 0; timeMin = 0;
                musicTime.setText("00,00");
                progressBar.setProgress(0);
                fileWorker.getLabelList().get(playCnt).setStyle("-fx-text-fill: rgb(236, 4, 42)");
                playCnt++;
                if(playCnt == fileWorker.getList().size())
                    playCnt = 0;
                path = fileWorker.getList().get(playCnt).getAbsolutePath();
                path = path.replace("\\", "/");
                mp3Worker(songImage, musicBox, path);
                fileWorker.getLabelList().get(playCnt).setStyle("-fx-text-fill: white");
                moveSlider(progressBar, musicTime);
                fileWorker.getMediaList().get(playCnt).play();
                playPause.setImage(new Image("file:src/images/pause.png"));
                Checked = 1;
            }
        }
    }

    /**
     * Метод переключения на предыдущую песню.
     * При достижении начала списка переходим на конец.
     */
    public void prevSong(){
        if(fileWorker.getList() != null){
            if(Checked == 1){
                fileWorker.getMediaList().get(playCnt).stop();
                diff = 0; timeMin = 0;
                musicTime.setText("00,00");
                progressBar.setProgress(0);
                playCnt--;
                if(playCnt == -1){
                    fileWorker.getLabelList().get(0).setStyle("-fx-text-fill: rgb(236, 4, 42)");
                    playCnt = fileWorker.getList().size()-1;
                }
                else
                    fileWorker.getLabelList().get(playCnt+1).setStyle("-fx-text-fill: rgb(236, 4, 42)");
                fileWorker.getMediaList().get(playCnt).play();
                totalTimeOfMusic=fileWorker.getMediaList().get(playCnt).getTotalDuration().toSeconds();
                fileWorker.getLabelList().get(playCnt).setStyle("-fx-text-fill: white");
                path = fileWorker.getList().get(playCnt).getAbsolutePath();
                path = path.replace("\\", "/");
                moveSlider(progressBar, musicTime);
                mp3Worker(songImage, musicBox, path);

                playPause.setImage(new Image("file:src/images/pause.png"));
                Checked = 1;
            }
            if(Checked == 0){
                fileWorker.getMediaList().get(playCnt).stop();
                diff = 0; timeMin = 0;
                musicTime.setText("00,00");
                progressBar.setProgress(0);
                fileWorker.getLabelList().get(playCnt).setStyle("-fx-text-fill: rgb(236, 4, 42)");
                playCnt--;
                if(playCnt == -1)
                    playCnt = fileWorker.getList().size()-1;
                path = fileWorker.getList().get(playCnt).getAbsolutePath();
                path = path.replace("\\", "/");
                mp3Worker(songImage, musicBox, path);
                fileWorker.getLabelList().get(playCnt).setStyle("-fx-text-fill: white");
                moveSlider(progressBar, musicTime);
                fileWorker.getMediaList().get(playCnt).play();
                playPause.setImage(new Image("file:src/images/pause.png"));
                Checked = 1;
            }
        }
    }

    /**
     * Метод добавления текущей проигрываемой песни в плейлист
     */
    public void addSong(){
        fileWorker.createTracksList(fileWorker.getList().get(playCnt).getAbsolutePath());
    }

    /**
     * Метод сохранения плейлиста в файл в компьюетере.
     * Для этого отводится специальная папка, имя файла генерируется программно
     */
    public void savePlaylist(){
        try{
            int numberFile = 1;
            String pathFile;
            File filePlaylist;
            while(true){
                pathFile = "E:/Games/playlists/Playlist_" + String.valueOf(numberFile) + ".blood";
                filePlaylist = new File(pathFile);
                if(filePlaylist.exists()){
                    numberFile++;
                }
                else{
                    filePlaylist.createNewFile();
                    break;
                }
            }

            if(fileWorker.getListTracks() != null){
                String tempStr;
                PrintWriter outFile = new PrintWriter(filePlaylist);
                for(int i = 0; i < fileWorker.getListTracks().size(); i++){
                    outFile.println(fileWorker.getListTracks().get(i));
                    outFile.append("\n"); //переходим на новую строку
                }
                fileWorker.clearTracksList();
                outFile.close();
            }
        }catch(IOException ex){}
    }

    /**
     * Метод загрузки плейлиста с компьютера.
     * Также присутсвует фильтрация по плейлистам
     */
    public void loadPlaylist(){
        diff = 0; timeMin = 0;
        FileChooser.ExtensionFilter extFilter =
                new FileChooser.ExtensionFilter("File playlists (*.blood)", "*.blood");
        fileChooser.getExtensionFilters().add(extFilter);
        File file = fileChooser.showOpenDialog(primaryStage);
        fileChooser.getExtensionFilters().remove(extFilter);
        playCnt = 0;

        if(file != null) {
            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                String txt;
                List<String> names = new ArrayList<String>();
                fileWorker.setMemList();
                if (reader != null) {
                    while ((txt = reader.readLine()) != null) {
                        names.add(txt);
                        System.out.println(txt);
                    }
                    if (names != null) {
                        playCnt = 0;
                        for (int j = 0; j < names.size(); j+=2) {
                            fileWorker.addElemToList(new File(names.get(j)));
                        }
                    }
                    Checked = -1;
                    fileWorker.clearMediaList();
                    playCnt = 0;
                    System.out.println(fileWorker.getList().size());

                    browseFunc(playlist, songImage, playPause, musicBox, musicTime, progressBar);
                }
            } catch (IOException ex) {}
        }
    }

    /**
     * Вспомогательная функция для формирования коллекций после поиска
     * @param playlist Элемент списка плейлиста
     * @param songImage Элемент картинки песни
     * @param playPause Элемент кнопки пауза
     * @param musicBox Элемент названия песни
     * @param musicTime Элемент времени
     * @param progressBar Элемент индикатора прогресса
     */
    public void browseFunc(VBox playlist, ImageView songImage,ImageView playPause, Label musicBox, Label musicTime, ProgressBar progressBar){
        if(Checked != -1){
            System.out.println(playCnt);
            fileWorker.getMediaList().get(playCnt).stop();
            songImage.setImage(new Image("file:resources/logo.png"));
            playPause.setImage(new Image("file:src/images/play.png"));
            Checked = 0;
        }
        playCnt = 0;
        if(fileWorker.getList() != null) {
            Checked = 0;
            fileWorker.clearLabelList();
            fileWorker.clearMediaList();
            playlist.getChildren().clear();
            for(counter = 0;counter < fileWorker.getList().size();counter++){
                path = fileWorker.getList().get(counter).getAbsolutePath();
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
                musicTime.setText("00:00");
                progressBar.setProgress(0);
                fileWorker.addLabelList(elemList);
                playlist.getChildren().add(fileWorker.getLabelList().get(counter));
                fileWorker.addMediaList(new MediaPlayer(new Media(fileWorker.getList().get(counter).toURI().toString())));
                //mediaList.add(new MediaPlayer(new Media(list.get(counter).toURI().toString())));
            }
        }
    }

    /**
     * Метод для работы с файлами музыки и сторонней библиотекой
     * @param songImage Элемен для вставки картинки
     * @param musicBox Элемент названия песни
     * @param path Путь к песне
     */
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
            else
                songImage.setImage(new Image("file:resources/logo.png"));
        }catch(IOException ex){}
    }

    /**
     * Эта функция всегда работает когда воспроизводится песня.
     * Она обновляет прогресс бар и время песни.
     * Когда доходим до конца песни, то перемещаемся на следующую в списке
     * @param progressBar Элемент прогресса песни
     * @param musicTime Метка для постоянного обновления времени
     */
    public void moveSlider(ProgressBar progressBar, Label musicTime){
        totalTimeOfMusic=fileWorker.getMediaList().get(playCnt).getTotalDuration().toSeconds();

        //diff = 0; timeMin = 0;

        fileWorker.getMediaList().get(playCnt).currentTimeProperty().addListener((Observable)->{
            //updateValues();
            int timeSec = 0;
            String t1 = "0", t2 = "0", tItog = "";
            double check = 0;
            progressBar.setProgress((fileWorker.getMediaList().get(playCnt).getCurrentTime().toSeconds())/totalTimeOfMusic);
            timeSec = (int)fileWorker.getMediaList().get(playCnt).getCurrentTime().toSeconds();
            check = fileWorker.getMediaList().get(playCnt).getCurrentTime().toSeconds();
            if((timeSec % 60 == 0)&&(timeSec != 0)){
                timeSec = 0;
                try{
                    TimeUnit.SECONDS.sleep(1);}
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

            int curTime = (int)fileWorker.getMediaList().get(playCnt).getCurrentTime().toSeconds();
            if(curTime == curDuration){
                fileWorker.getMediaList().get(playCnt).stop();

                musicTime.setText("00,00");
                progressBar.setProgress(0);
                playCnt++;
                diff = 0; timeMin = 0;
                if(playCnt == fileWorker.getList().size()){
                    System.out.println(playCnt);
                    fileWorker.getLabelList().get(fileWorker.getList().size()-1).setStyle("-fx-text-fill: rgb(236, 4, 42)");
                    playCnt = 0;
                }
                else
                    fileWorker.getLabelList().get(playCnt-1).setStyle("-fx-text-fill: rgb(236, 4, 42)");
                fileWorker.getMediaList().get(playCnt).play();
                totalTimeOfMusic=fileWorker.getMediaList().get(playCnt).getTotalDuration().toSeconds();
                fileWorker.getLabelList().get(playCnt).setStyle("-fx-text-fill: white");
                path = fileWorker.getList().get(playCnt).getAbsolutePath();
                path = path.replace("\\", "/");
                moveSlider(progressBar, musicTime);
                mp3Worker(songImage, musicBox, path);

                playPause.setImage(new Image("file:src/images/pause.png"));
                Checked = 1;
            }
        });
    }
}

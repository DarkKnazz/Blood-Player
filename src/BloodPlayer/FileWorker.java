package BloodPlayer;

import javafx.scene.control.Label;
import javafx.scene.media.MediaPlayer;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Класс для работы со всеми коллекциями программы
 * Своеобразный класс-хранилище
 */
public class FileWorker {
    private List<File> list;                                  //List for files of music
    private List<Label> labelList = new ArrayList<>();        //List for labels in playlists
    private List<String> listTracks = new ArrayList<>();      //List for storing data for playlists
    private List<MediaPlayer> mediaList = new ArrayList<>();  //List for storing media tracks

    /**
     * Метод создания листа с помощью Проводника Windows
     * @param primaryStage параметром передаем главную сцену
     */
    public void createList(Stage primaryStage){
        FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter extFilter =
                new FileChooser.ExtensionFilter("MP3 files (*.mp3)", "*.mp3");
        fileChooser.getExtensionFilters().add(extFilter);
        list = fileChooser.showOpenMultipleDialog(primaryStage);
        fileChooser.getExtensionFilters().remove(extFilter);
    }

    /**
     * Метод выделения памяти для списка файлов
     */
    public void setMemList(){
        list = new ArrayList<File>();
    }

    /**
     * Метод добавления элемента в список файлов
     * @param f Файл для добавления в конец списка
     */
    public void addElemToList(File f){ list.add(f); }

    /**
     * Метод для добавления элемента в список треков плейлиста
     * @param t Строка для добавления в список треков плейлиста
     */
    public void createTracksList(String t){
        listTracks.add(t);
    }

    /**
     * Метод возврата списка файлов
     * @return Возвращает список файлов песен
     */
    public List<File> getList(){
        return list;
    }

    /**
     * Метод возврата списка меток для плейлиста
     * @return Возвращает список меток с названием песен
     */
    public List<Label> getLabelList(){
        return labelList;
    }

    /**
     * Метод возврата списка композиций
     * @return Возвращает список песен для проигрывания
     */
    public List<MediaPlayer> getMediaList(){
        return mediaList;
    }

    /**
     * Метод возврата списка файлов плейлиста
     * @return Возвращает список с плейлистом
     */
    public List<String> getListTracks(){
        return listTracks;
    }

    /**
     * Метод очистки списка меток
     */
    public void clearLabelList(){
        labelList.clear();
    }

    /**
     * Метод очистки списка файлов песен
     */
    public void clearList(){
        list.clear();
    }

    /**
     * Метод очистки списка песен плейлиста
     */
    public void clearTracksList(){
        listTracks.clear();
    }

    /**
     * Метод очистки списка песен для проигрывания
     */
    public void clearMediaList(){
        mediaList.clear();
    }

    /**
     * Метод добавления метки с названием композиции
     * @param temp Строка с названием трека
     */
    public void addLabelList(Label temp){
        labelList.add(temp);
    }

    /**
     * Метод добавления песен в список треков воспроизведения
     * @param mediaPlayer Плеер для воспроизведения
     */
    public void addMediaList(MediaPlayer mediaPlayer){
        mediaList.add(mediaPlayer);
    }
}

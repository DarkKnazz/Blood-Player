package sample;

import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;

import java.text.DecimalFormat;
import java.util.List;

public class threadSlider extends Thread {
    private List<MediaPlayer> mediaList;
    Slider slider;
    int playCnt;
    Label musicTime;
    int Checked;
    List<Label> labelList;
    public threadSlider(List<MediaPlayer> list, Slider slid, Label music,int cnt, List<Label> lblList, int Check){
        mediaList = list;
        slider = slid;
        musicTime = music;
        playCnt = cnt;
        labelList = lblList;
        Checked = Check;
    }

    public void run(){
        double totalTimeOfMusic=mediaList.get(playCnt).getTotalDuration().toSeconds();
        System.out.println(totalTimeOfMusic);

        mediaList.get(playCnt).currentTimeProperty().addListener((Observable)-> {
            if (slider.isValueChanging()) {
                mediaList.get(playCnt).seek(Duration.seconds((slider.getValue() * (totalTimeOfMusic) / 100)));
            }
            if (slider.isPressed()) {
                mediaList.get(playCnt).seek(Duration.seconds((slider.getValue() * (totalTimeOfMusic) / 100)));
            }
            //updateValues();
            slider.setValue((mediaList.get(playCnt).getCurrentTime().toSeconds() * 100) / totalTimeOfMusic);
            System.out.println("ok" + mediaList.get(playCnt).getCurrentTime().toSeconds());
            musicTime.setText('0' + String.valueOf(new DecimalFormat("#0.00").format(mediaList.get(playCnt).getCurrentTime().toMinutes())));

            double curTime = mediaList.get(playCnt).getCurrentTime().toSeconds();
            if (curTime == totalTimeOfMusic) {
                mediaList.get(playCnt).stop();
                labelList.get(playCnt).setStyle("-fx-text-fill: rgb(236, 4, 42)");
                playCnt++;
                if (playCnt == labelList.size()) {
                    playCnt = 0;
                    mediaList.get(playCnt).stop();
                    musicTime.setText("00,00");
                    slider.setValue(0);
                    Checked = 0;
                } else {
                    mediaList.get(playCnt).play();
                    labelList.get(playCnt).setStyle("-fx-text-fill: white");
                }
            }
            mediaList.get(playCnt).setCycleCount(MediaPlayer.INDEFINITE);
        });
    }

}

package top.youm.rocchi.core.music;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author YouM
 * Created on 2023/7/17
 * dinner(stupid) QQ Music Player and Tencent fuck you  - 6
 * QQMusic 提供操作接口 UI渲染需要
 * 1.获取用户头像
 * 2.获取歌单列表
 * 3.获取歌单内的所有歌曲songmID
 * 4.获取歌单内的所有歌曲的pictureID
 * 5.对QQ音乐进行攻击(?
 */
public class QQMusic extends Application {
    public static void main(String[] args) throws IOException {
        Application.launch(args);
    }
    @Override
    public void start(Stage primaryStage) throws Exception {

        String url = "http://www.gov.cn/guoqing/guoge/hc.mp3";
        Media media = new Media(url);
        MediaPlayer mplayer = new MediaPlayer(media);
        MediaView mView = new MediaView(mplayer);
        System.out.println(media.getSource());

        Pane pane = new Pane();
        pane.getChildren().add(mView);
        mView.fitHeightProperty().bind(pane.heightProperty());
        mView.fitWidthProperty().bind(pane.widthProperty());

        Scene scene = new Scene(pane,640,360);
        primaryStage.setTitle("MediaDemo");
        primaryStage.setScene(scene);
        primaryStage.show();

        mplayer.play();
    }



    /*URL url = new URL(songList.get(0).substring(1,songList.get(0).length()-1));

        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.setRequestProperty("Content-Type","audio/mpeg");
        connection.setRequestProperty("Host", "c.y.qq.com");
        connection.setRequestProperty("Cookie", qqMusicUser.getUserCookie());
        connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:109.0) Gecko/20100101 Firefox/115.0");
        connection.setDoOutput(true);
        connection.setDoInput(true);
        connection.setUseCaches(false);

        InputStream is = connection.getInputStream();
        // 创建文件，并设置默认文件名
        File file = new File("E:\\Users\\Administrator\\Photos\\music.mp3");
        FileOutputStream out = new FileOutputStream(file);
        int i = 0;
        while((i = is.read()) != -1){
            out.write(i);
        }
        is.close();
        out.close();*/
}

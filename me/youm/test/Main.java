//package me.youm.test;
//
//import javafx.application.Application;
//import javafx.scene.Scene;
//import javafx.scene.control.Button;
//import javafx.scene.layout.Pane;
//import javafx.scene.media.Media;
//import javafx.scene.media.MediaPlayer;
//import javafx.scene.media.MediaView;
//import javafx.stage.Stage;
//import top.youm.rocchi.core.music.QQMusicApi;
//import top.youm.rocchi.core.music.QQMusicUser;
//import top.youm.rocchi.core.music.Result;
//
//import java.io.IOException;
//import java.util.ArrayList;
//import java.util.List;
//
//public class Main extends Application {
//    public static void main(String[] args) {
//        Application.launch(args);
//    }
//
//    @Override
//    public void start(Stage primaryStage) throws Exception {
//        QQMusicUser qqMusicUser = new QQMusicUser(
//                "1055965862",
//                "RK=xL3R6gxtZo; ptcz=3d4db4a093ca2a53f80089044c77ae560812685317ea750bbbee4fae428285f2; pgv_pvid=3559226323; fqm_pvqid=3f988bd7-a9ee-40d0-8a31-b30fa147e2ae; ts_refer=i.y.qq.com/; ts_uid=8569512824; ptui_loginuin=1551223499; euin=oKnk7KEs7Kcsoc**; tmeLoginType=2; psrf_access_token_expiresAt=1697555041; music_ignore_pskey=202306271436Hn@vBj; pac_uid=1_1055965862; iip=0; qqmusic_key=Q_H_L_5dd91xkwuqu3NOGm3-C4tRmFWQuJTpWJxG3J2xVj9PBB3KTHQO7bkGQ; psrf_musickey_createtime=1689779041; uin=1055965862; psrf_qqaccess_token=8665422AA632F14F5214A73408016E49; qm_keyst=Q_H_L_5dd91xkwuqu3NOGm3-C4tRmFWQuJTpWJxG3J2xVj9PBB3KTHQO7bkGQ; psrf_qqrefresh_token=6B3754C0825D9D0C7BCDA00101CEBC43; wxrefresh_token=; wxopenid=; wxunionid=; psrf_qqunionid=B38DC7194B21AC884EAC52F562DD6FB1; psrf_qqopenid=69E7C7C9400E974A85995A96D2C99108; qm_keyst=Q_H_L_5dd91xkwuqu3NOGm3-C4tRmFWQuJTpWJxG3J2xVj9PBB3KTHQO7bkGQ; fqm_sessionid=946b431b-489d-42c7-a1bb-beb11889e56c; pgv_info=ssid=s9374559734; ts_last=y.qq.com/n/ryqq/profile"
//        );
//        QQMusicApi api = new QQMusicApi(qqMusicUser.getUserUin(), qqMusicUser.getUserCookie());
//        List<String> songList = this.getSongList(api);
//        String url  = songList.get(0).substring(1, songList.get(0).length() - 1);
//        Media media = new Media(url);
//        MediaPlayer mplayer = new MediaPlayer(media);
//        MediaView mView = new MediaView(mplayer);
//        System.out.println(media.getSource());
//
//        Pane pane = new Pane();
//        pane.getChildren().add(mView);
//        Button button = new Button("播放歌曲");
//        button.setOnMouseClicked(event-> mplayer.play());
//
//        pane.getChildren().add(button);
//        mView.fitHeightProperty().bind(pane.heightProperty());
//        mView.fitWidthProperty().bind(pane.widthProperty());
//
//        Scene scene = new Scene(pane, 640, 360);
//        primaryStage.setTitle("MediaDemo");
//        primaryStage.setScene(scene);
//        primaryStage.show();
//    }
//
//}
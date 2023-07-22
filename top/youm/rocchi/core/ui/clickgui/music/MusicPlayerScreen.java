package top.youm.rocchi.core.ui.clickgui.music;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import top.youm.rocchi.Rocchi;
import top.youm.rocchi.core.music.Music;
import top.youm.rocchi.core.music.QQMusicApi;
import top.youm.rocchi.core.music.QQMusicUser;
import top.youm.rocchi.core.music.Result;
import top.youm.rocchi.core.ui.Component;
import top.youm.rocchi.core.ui.MouseType;
import top.youm.rocchi.core.ui.clickgui.music.layout.Layout;
import top.youm.rocchi.core.ui.clickgui.music.layout.container.Container;
import top.youm.rocchi.core.ui.clickgui.music.state.UIState;
import top.youm.rocchi.core.ui.clickgui.music.sub.MusicButton;
import top.youm.rocchi.core.ui.clickgui.music.sub.PlayButton;
import top.youm.rocchi.core.ui.font.FontLoaders;
import top.youm.rocchi.core.ui.theme.Icon;
import top.youm.rocchi.core.ui.theme.Theme;
import top.youm.rocchi.utils.render.RoundedUtil;
import top.youm.rocchi.utils.tools.Catcher;

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MusicPlayerScreen extends GuiScreen {
    public static int x, y;
    public static int screenWidth = 500, screenHeight = 320;
    ArrayList<MusicButton> components = new ArrayList<>();
    public int dragX, dragY;
    public boolean isDragging = false;
    public boolean sizeDragging = false;
    public static boolean changed;
    private QQMusicUser qqMusicUser;
    private QQMusicApi api;
    private Container container = new Container(Layout.Row,components,15);
    private MusicButton musicButton;
    public static boolean isPlay = false;
    public MusicPlayerScreen() {
        qqMusicUser = new QQMusicUser(
                "1055965862",
                "RK=xL3R6gxtZo; ptcz=3d4db4a093ca2a53f80089044c77ae560812685317ea750bbbee4fae428285f2; pgv_pvid=3559226323; fqm_pvqid=3f988bd7-a9ee-40d0-8a31-b30fa147e2ae; ptui_loginuin=1551223499; euin=oKnk7KEs7Kcsoc**; tmeLoginType=2; psrf_access_token_expiresAt=1697784350; pac_uid=1_1055965862; iip=0; psrf_qqaccess_token=8665422AA632F14F5214A73408016E49; psrf_qqrefresh_token=6B3754C0825D9D0C7BCDA00101CEBC43; wxrefresh_token=; wxopenid=; wxunionid=; psrf_qqunionid=B38DC7194B21AC884EAC52F562DD6FB1; psrf_qqopenid=69E7C7C9400E974A85995A96D2C99108; fqm_sessionid=9e5a7505-7845-4a17-9c32-ea187b372df8; pgv_info=ssid=s9948377020; _qpsvr_localtk=0.6710874257497523; login_type=1; uin=1055965862; psrf_musickey_createtime=1690008350; qm_keyst=Q_H_L_510rdGjx0u2wUVeQHyT9QNBYZm19TXOsH-YpJ4M2UynstjPNlLRZrlQ; qqmusic_key=Q_H_L_510rdGjx0u2wUVeQHyT9QNBYZm19TXOsH-YpJ4M2UynstjPNlLRZrlQ"
        );
        api = new QQMusicApi(qqMusicUser.getUserUin(),qqMusicUser.getUserCookie());
        List<Music> songList = getSongList(api);
        songList.forEach(music-> components.add(new MusicButton(music.getName().substring(1,music.getName().length() - 1),music.getUrl().substring(1, music.getUrl().length() - 1))));
    }
    private PlayButton play = new PlayButton();

    public List<Music> getSongList(QQMusicApi api) {
        List<Music> songs = new ArrayList<>();
        Catcher.runCatching(()->{
            for (String diss : api.getDissesID().getContext()) {

                for (Music song : api.getSongsByDissid(diss).getContext()) {
                    Result<String> musicPlayer = (Result<String>) api.getMusicPlayer(song.getUrl().substring(1, song.getUrl().length() - 1));
                    song.setUrl(musicPlayer.getContext());
                    songs.add(song);
                }
            }
        });
        return songs;
    }
    @Override
    public void initGui() {
        super.initGui();
        ScaledResolution sr = new ScaledResolution(mc);
        x = sr.getScaledWidth() / 2 - screenWidth / 2;
        y = sr.getScaledHeight() / 2 - screenHeight / 2;
    }
    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        super.drawScreen(mouseX, mouseY, partialTicks);

        if (isDragging) {
            x = mouseX - dragX;
            y = mouseY - dragY;
        } else if (sizeDragging) {
            screenWidth += mouseX - dragX;
            screenHeight += mouseY - dragY;
            this.dragX = mouseX;
            this.dragY = mouseY;
        }

        RoundedUtil.drawRound(x,y,screenWidth,screenHeight,2,new Color(16, 20, 32));
        container.build(x + 120,y+30,screenWidth - 120,screenHeight - 90,mouseX,mouseY, Theme.buttonTheme);

        if(UIState.button != null){
            if(UIState.button.isPlay()){
                isPlay = true;
                UIState.button.getMediaPlayer().setVolume(0.4);
                UIState.button.getMediaPlayer().play();
            }else {
                isPlay = false;
                UIState.button.getMediaPlayer().pause();
            }
        }
        bottom(mouseX,mouseY);
    }
    public void bottom(int mouseX,int mouseY){
        play.draw(x + screenWidth / 2.0f,y+screenHeight - 40,mouseX,mouseY);
        FontLoaders.music30.drawCenteredStringWithShadow(Icon.PREVIOUS.icon, x + screenWidth / 2.0f - 40,y+screenHeight - 40,Theme.font.getRGB());
        FontLoaders.music30.drawCenteredStringWithShadow(Icon.NEXT.icon, x + screenWidth / 2.0f + 40,y+screenHeight - 40,Theme.font.getRGB());
        FontLoaders.music30.drawCenteredStringWithShadow(Icon.VOLUME.icon, x + screenWidth / 2.0f + 90,y+screenHeight - 40,Theme.font.getRGB());

    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        super.keyTyped(typedChar, keyCode);
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        if (Component.isHover(x, y, screenWidth, 30, mouseX, mouseY) && mouseButton == 0) {
            dragX = mouseX - x;
            dragY = mouseY - y;
            isDragging = true;
        } else if (Component.isHover(x + screenWidth - 20, y + screenHeight - 20, 20, 20, mouseX, mouseY) && mouseButton == 0) {
            dragX = mouseX;
            dragY = mouseY;
            sizeDragging = true;
        }
        play.mouse(mouseButton,MouseType.CLICK);
        container.mouse(mouseButton,MouseType.CLICK);
    }

    @Override
    protected void mouseReleased(int mouseX, int mouseY, int state) {
        super.mouseReleased(mouseX, mouseY, state);
        if (Component.isHover(x, y, screenWidth, 30, mouseX, mouseY) && state == 0) {
            dragX = mouseX - x;
            dragY = mouseY - y;
            isDragging = false;
        } else if (Component.isHover(x + screenWidth - 20, y + screenHeight - 20, 20, 20, mouseX, mouseY) && state == 0) {
            sizeDragging = false;
        }
    }


    @Override
    public void onGuiClosed() {
        super.onGuiClosed();
    }
}

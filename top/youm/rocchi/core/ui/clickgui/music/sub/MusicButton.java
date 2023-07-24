package top.youm.rocchi.core.ui.clickgui.music.sub;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import top.youm.rocchi.core.ui.MouseType;
import top.youm.rocchi.core.ui.Component;
import top.youm.rocchi.core.ui.clickgui.music.MusicPlayerScreen;
import top.youm.rocchi.core.ui.clickgui.music.state.UIState;
import top.youm.rocchi.core.ui.font.FontLoaders;
import top.youm.rocchi.core.ui.theme.Theme;
import top.youm.rocchi.utils.render.RenderUtil;
import top.youm.rocchi.utils.render.RoundedUtil;

import java.awt.*;

public class MusicButton extends Component {
    private Media media;
    private MediaPlayer mediaPlayer;
    private String picture;
    private boolean isPlay = false;
    public MusicButton(String name,String url,String picture) {
        super(name);
        this.media = new Media(url);
        this.mediaPlayer = new MediaPlayer(media);
        this.picture = picture;
        this.width = 70;
        this.height = 70;
    }
    @Override
    public void draw(float xPos, float yPos, int mouseX, int mouseY) {
        this.mouseX = mouseX;this.mouseY = mouseY;
        RoundedUtil.drawRound(x,y,width,height,2,new Color(31,28,31));
        StringBuilder text = new StringBuilder();

//        RenderUtil.drawTexturedRect(x + 2,y + 2 ,width - 2 ,height - 10,mc.mcDataDir+picture+".jpg");

        /*if(FontLoaders.chinese18.getStringWidth(name) >= width){
            text.append(name, 0, 7).append("...");
        }else {
            text.append(name);
        }

        FontLoaders.chinese18.drawStringWithShadow(text.toString(),x + 1 ,y + height - FontLoaders.chinese18.getFontHeight() - 2,-1);
*/    }

    @Override
    public void mouse(int mouseButton, MouseType mouseType) {
        if(mouseButton == 0 && mouseType == MouseType.CLICK && isHover((int) x, (int) y,width,height,mouseX,mouseY)){

            if(UIState.button != null){
                UIState.button.getMediaPlayer().stop();
                UIState.button.setPlay(false);
            }
            if(UIState.button != this) {
                UIState.button = this;
            }
        }
    }

    @Override
    public void input(char typedChar, int keyCode) {

    }

    public Media getMedia() {
        return media;
    }

    public MediaPlayer getMediaPlayer() {
        return mediaPlayer;
    }

    public boolean isPlay() {
        return isPlay;
    }

    public void setPlay(boolean play) {
        isPlay = play;
    }
}

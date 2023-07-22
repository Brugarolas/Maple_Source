package top.youm.rocchi.core.ui.clickgui.music.sub;

import top.youm.rocchi.core.ui.Component;
import top.youm.rocchi.core.ui.MouseType;
import top.youm.rocchi.core.ui.clickgui.music.MusicPlayerScreen;
import top.youm.rocchi.core.ui.font.FontLoaders;
import top.youm.rocchi.core.ui.theme.Icon;
import top.youm.rocchi.core.ui.theme.Theme;

/**
 * @author YouM
 * Created on 2023/7/21
 */
public class PlayButton extends Component {
    private Icon icon = Icon.PLAY;
    public PlayButton() {
        super("Play");
    }

    @Override
    public void draw(float xPos, float yPos, int mouseX, int mouseY) {
        super.draw(xPos, yPos, mouseX, mouseY);
        FontLoaders.music30.drawCenteredStringWithShadow(icon.icon, x,y, Theme.font.getRGB());
    }

    @Override
    public void mouse(int mouseButton, MouseType mouseType) {
        if(mouseButton == 0 && mouseType == MouseType.CLICK) {
            if (Component.isHover((int) (x + MusicPlayerScreen.screenWidth / 2.0f), (int) (y + MusicPlayerScreen.screenHeight - 40), FontLoaders.music30.getStringWidth(Icon.PLAYER.icon) + 3, FontLoaders.music30.getHeight() + 3, mouseX, mouseY))
               icon = MusicPlayerScreen.isPlay ? Icon.PAUSE : Icon.PLAY;
        }
    }

    @Override
    public void input(char typedChar, int keyCode) {

    }
}

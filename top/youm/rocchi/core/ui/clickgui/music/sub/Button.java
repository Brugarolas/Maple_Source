package top.youm.rocchi.core.ui.clickgui.music.sub;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import top.youm.rocchi.core.ui.MouseType;
import top.youm.rocchi.core.ui.Component;
import top.youm.rocchi.utils.render.RenderUtil;
import top.youm.rocchi.utils.render.RoundedUtil;

import java.awt.*;

public class Button extends Component {
    public Button() {
        super("Button");
        this.width = 70;
        this.height = 50;
    }
    @Override
    public void draw(float xPos, float yPos, int mouseX, int mouseY) {
        RoundedUtil.drawRound(x,y,width,height,2,new Color(29, 210, 95));
    }

    @Override
    public void mouse(int mouseButton, MouseType mouseType) {

    }

    @Override
    public void input(char typedChar, int keyCode) {

    }
}

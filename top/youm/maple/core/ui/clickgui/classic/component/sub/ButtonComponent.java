package top.youm.maple.core.ui.clickgui.classic.component.sub;

import top.youm.maple.common.settings.impl.BoolSetting;
import top.youm.maple.core.ui.clickgui.classic.component.Component;
import top.youm.maple.core.ui.clickgui.classic.theme.Theme;
import top.youm.maple.core.ui.clickgui.classic.MouseType;
import top.youm.maple.utils.render.RenderUtil;
import top.youm.maple.utils.render.RoundedUtil;

import java.awt.*;

public class ButtonComponent extends Component {
    private final BoolSetting boolSetting;
    private float animationBool = 10;

    public ButtonComponent(BoolSetting boolSetting) {
        super(boolSetting.getName());
        this.boolSetting = boolSetting;
    }
    @Override
    public void update() {
        this.display = this.boolSetting.canDisplay();
    }
    @Override
    public void draw(float xPos, float yPos, int mouseX, int mouseY) {
        super.draw(xPos,yPos,mouseX,mouseY);
        if (boolSetting.getValue()) {
            if (animationBool > 10) {
                animationBool = animator.animate(10, animationBool, 0.08f);
            }
            RoundedUtil.drawRound(x - 30, y, 20, 6, 3, Theme.theme);
            RenderUtil.drawSmoothCircle(x - animationBool, y + 3, 6, new Color(255,255,255));
        } else {
            if (animationBool < 30) {
                animationBool = animator.animate(30, animationBool, 0.08f);
            }
            RoundedUtil.drawRound(x - 30, y, 20, 6, 3, Theme.enableButton);
            RenderUtil.drawSmoothCircle(x - animationBool, y + 3, 6,new Color(255,255,255));
        }
    }

    @Override
    public void mouse(int mouseButton, MouseType mouseType) {
        if (isHover((int) (x - 30), (int) y, 20, 6, mouseX, mouseY) && mouseButton == 0 && mouseType == MouseType.CLICK) {
            boolSetting.setValue(!boolSetting.getValue());
        }
    }

    @Override
    public void input(char typedChar, int keyCode) {
    }
}

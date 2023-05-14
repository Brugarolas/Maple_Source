package top.youm.rocchi.core.ui.clickgui.components;

import top.youm.rocchi.common.settings.BoolSetting;
import top.youm.rocchi.core.ui.Component;
import top.youm.rocchi.core.ui.Theme;
import top.youm.rocchi.core.ui.clickgui.ClickGuiScreen;
import top.youm.rocchi.core.ui.clickgui.MouseType;
import top.youm.rocchi.utils.AnimationUtils;
import top.youm.rocchi.utils.render.RenderUtil;
import top.youm.rocchi.utils.render.RoundedUtil;

import java.awt.*;


public class BooleanComponent extends Component {
    private BoolSetting setting;
    private float animX = 0;
    int animAlpha = 0;

    public BooleanComponent(BoolSetting setting) {
        super(setting.getName());
        this.setting = setting;
    }
    @Override
    public void draw(float xPos, float yPos,int mouseX, int mouseY) {
        this.x = xPos;this.y = yPos;
        this.mouseX = mouseX;this.mouseY = mouseY;
        if (setting.getValue()) {
            animX = animator.animate(26, animX, 0.08f);
            animAlpha = animator.animate(255, animAlpha, 0.08f);
        } else {
            animX = animator.animate(44, animX, 0.08f);
            animAlpha = animator.animate(0, animAlpha, 0.08f);
        }
        RoundedUtil.drawRoundOutline(ClickGuiScreen.x + ClickGuiScreen.screenWidth - 50, ClickGuiScreen.y + 50 + y + 10, 30, 12, 5, 0.2f, new Color(56, 138, 229, animAlpha), new Color(80, 80, 80, 255));
        RenderUtil.drawCircle(ClickGuiScreen.x + ClickGuiScreen.screenWidth - animX, ClickGuiScreen.y + 55 + y + 11, 5f, Theme.titleColor);
    }

    @Override
    public void mouse(int mouseX, int mouseY, int mouseButton, MouseType mouseType) {
        if(mouseType != MouseType.CLICK){
            if (isHover(ClickGuiScreen.x + ClickGuiScreen.screenWidth - 50, (int) (ClickGuiScreen.y + 50 + y + 10), 24, 12, mouseX, mouseY) && mouseButton == 0) {
                setting.setValue(!setting.getValue());
            }
        }
    }

    public BoolSetting getSetting() {
        return setting;
    }

    public void setSetting(BoolSetting setting) {
        this.setting = setting;
    }
}

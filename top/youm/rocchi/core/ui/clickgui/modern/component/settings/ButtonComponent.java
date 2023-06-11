package top.youm.rocchi.core.ui.clickgui.modern.component.settings;

import top.youm.rocchi.common.settings.BoolSetting;
import top.youm.rocchi.core.ui.clickgui.modern.Component;
import top.youm.rocchi.core.ui.theme.Theme;
import top.youm.rocchi.core.ui.clickgui.old.MouseType;
import top.youm.rocchi.utils.render.RenderUtil;
import top.youm.rocchi.utils.render.RoundedUtil;

public class ButtonComponent extends Component {
    private final BoolSetting boolSetting;
    private float animationBool = 10;

    public ButtonComponent(BoolSetting boolSetting) {
        super(boolSetting.getName());
        this.boolSetting = boolSetting;
    }

    @Override
    public void draw(float xPos, float yPos, int mouseX, int mouseY) {
        this.x = xPos;
        this.y = yPos;
        this.mouseX = mouseX;
        this.mouseY = mouseY;

        if (boolSetting.getValue()) {
            if (animationBool > 10) {
                animationBool = animator.animate(10, animationBool, 0.08f);
            }
            RoundedUtil.drawRound(x - 30, y, 20, 6, 3, Theme.theme);
            RenderUtil.drawCircle(x - animationBool, y + 3, 6, Theme.buttonCircleTheme);
        } else {
            if (animationBool < 30) {
                animationBool = animator.animate(30, animationBool, 0.08f);
            }
            RoundedUtil.drawRound(x - 30, y, 20, 6, 3, Theme.enableButton);
            RenderUtil.drawCircle(x - animationBool, y + 3, 6, Theme.buttonCircleTheme);
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

package top.youm.rocchi.core.ui.clickgui.modern.component.settings;

import top.youm.rocchi.common.settings.ModeSetting;
import top.youm.rocchi.core.ui.clickgui.modern.Component;
import top.youm.rocchi.core.ui.clickgui.modern.theme.Theme;
import top.youm.rocchi.core.ui.clickgui.old.MouseType;
import top.youm.rocchi.core.ui.font.FontLoaders;
import top.youm.rocchi.utils.render.RoundedUtil;

public class DropdownComponent extends Component {
    private final ModeSetting<?> modeSetting;
    public DropdownComponent(ModeSetting<?> modeSetting) {
        super("dropdown");
        this.modeSetting = modeSetting;
    }

    @Override
    public void draw(float xPos, float yPos, int mouseX, int mouseY) {
        this.x = xPos;this.y = yPos;
        this.mouseX = mouseX;this.mouseY = mouseY;
        RoundedUtil.drawRound(x - 160,y - 6,150,14,1, Theme.theme);
        FontLoaders.comfortaaB18.drawStringWithShadow(modeSetting.getValue().name(),x - 85 - FontLoaders.comfortaaB18.getStringWidth(modeSetting.getValue().name()) / 2.0f,y + 14 / 2.0f - FontLoaders.comfortaaB18.getHeight() / 2.0f - 6,Theme.font.getRGB());

    }

    @Override
    public void mouse(int mouseButton, MouseType mouseType) {

    }

    @Override
    public void input(char typedChar, int keyCode) {

    }

}

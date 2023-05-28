package top.youm.rocchi.core.ui.clickgui.modern.component.settings;

import net.minecraft.client.gui.Gui;
import top.youm.rocchi.common.settings.ModeSetting;
import top.youm.rocchi.core.ui.clickgui.modern.Component;
import top.youm.rocchi.core.ui.clickgui.modern.theme.Theme;
import top.youm.rocchi.core.ui.clickgui.old.MouseType;
import top.youm.rocchi.core.ui.font.FontLoaders;
import top.youm.rocchi.utils.render.RenderUtil;
import top.youm.rocchi.utils.render.RoundedUtil;

import java.awt.*;

public class DropdownComponent extends Component {
    private final ModeSetting<?> modeSetting;
    private boolean open;
    private float animation;
    public DropdownComponent(ModeSetting<?> modeSetting) {
        super("dropdown");
        this.modeSetting = modeSetting;
    }

    @Override
    public void draw(float xPos, float yPos, int mouseX, int mouseY) {
        this.x = xPos;this.y = yPos;
        this.mouseX = mouseX;this.mouseY = mouseY;
        if(open){
            animation = animator.animate(0,animation,0.1f);
        }else {
            animation = animator.animate(-5,animation,0.1f);
        }
        if(isHover((int) (x - 160), (int) (y - 6),150,14,mouseX,mouseY) && !open){
            RoundedUtil.drawRound(x - 160,y - 6,150,14,1, Theme.moduleTheme);
        }else {
            RoundedUtil.drawRound(x - 160,y - 6 ,150,14,1, Theme.theme);
        }
        FontLoaders.comfortaaB18.drawStringWithShadow(modeSetting.getValue().name(),x - 85 - FontLoaders.comfortaaB18.getStringWidth(modeSetting.getValue().name()) / 2.0f,y + 14 / 2.0f - FontLoaders.comfortaaB18.getHeight() / 2.0f - 6,Theme.font.getRGB());
    }
    public void render(){
        if(open) {
            int height = 14 * (this.modeSetting.getEnums().length - 1);
            RoundedUtil.drawRound(x - 160, y - 6 + animation + 14, 150, height, 1, Theme.theme);
            RenderUtil.drawRect((int) (x - 140), (int) (y - 5 + animation) + 14, 110, 1, new Color(183, 183, 183).getRGB());
            int offsetY = 0;
            for (Enum<?> e : this.modeSetting.getEnums()) {
                if (!e.name().equals(modeSetting.getValue().name())) {
                    FontLoaders.comfortaaB18.drawStringWithShadow(e.name(), x - 85 - (FontLoaders.comfortaaB18.getStringWidth(e.name()) / 2.0f), y + 14 / 2.0f - FontLoaders.comfortaaB18.getHeight() / 2.0f - 6 + offsetY + 14, Theme.font.getRGB());
                    offsetY += 14;
                }
            }
        }
    }
    @Override
    public void mouse(int mouseButton, MouseType mouseType) {
        if(mouseType == MouseType.CLICK && mouseButton == 0 && isHover((int) (x - 160), (int) (y - 6),150,14,mouseX,mouseY)){
            open = !open;
        }
    }

    @Override
    public void input(char typedChar, int keyCode) {

    }

}

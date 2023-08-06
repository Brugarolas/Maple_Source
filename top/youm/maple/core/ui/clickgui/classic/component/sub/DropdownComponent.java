package top.youm.maple.core.ui.clickgui.classic.component.sub;

import net.minecraft.client.renderer.GlStateManager;
import top.youm.maple.common.settings.impl.ModeSetting;
import top.youm.maple.core.ui.clickgui.classic.component.Component;
import top.youm.maple.core.ui.clickgui.classic.theme.Theme;
import top.youm.maple.core.ui.clickgui.classic.MouseType;
import top.youm.maple.core.ui.font.FontLoaders;
import top.youm.maple.utils.render.RenderUtil;
import top.youm.maple.utils.render.RoundedUtil;

import java.awt.*;

public class DropdownComponent extends Component {
    private final ModeSetting modeSetting;
    private boolean open;
    private float animation;

    public DropdownComponent(ModeSetting modeSetting) {
        super(modeSetting.getName());
        this.modeSetting = modeSetting;
    }
    @Override
    public void update() {
        this.display = this.modeSetting.canDisplay();
    }
    @Override
    public void draw(float xPos, float yPos, int mouseX, int mouseY) {
        super.draw(xPos,yPos,mouseX,mouseY);
        if (open) {
            animation = animator.animate(0, animation, 0.1f);
        } else {
            animation = animator.animate(-12, animation, 0.1f);
        }
        if (isHover((int) (x - 160), (int) (y - 6), 150, 14, mouseX, mouseY) && !open) {
            RoundedUtil.drawRound(x - 160, y - 6, 150, 14, 1, Theme.moduleTheme);
        } else {
            RoundedUtil.drawRound(x - 160, y - 6, 150, 14, 1, Theme.theme);
        }
        FontLoaders.comfortaaB18.drawStringWithShadow(modeSetting.getValue(), x - 85 - FontLoaders.comfortaaB18.getStringWidth(modeSetting.getValue()) / 2.0f, y + 14 / 2.0f - FontLoaders.comfortaaB18.getHeight() / 2.0f - 6, Theme.font.getRGB());
        GlStateManager.pushMatrix();
        if (open) {
            GlStateManager.translate(0.0f,0.0f,2.0f);
            render();
        }
        GlStateManager.translate(0.0f,0.0f,-2.0f);
        GlStateManager.popMatrix();
    }

    public void render() {
        int height = 14 * (this.modeSetting.getModes().length - 1);
        RoundedUtil.drawRound(x - 160, y - 6 + animation + 14, 150, height, 1, Theme.theme);
        RenderUtil.drawRect((int) (x - 140), (int) (y - 5 + animation) + 14, 110, 1, new Color(183, 183, 183).getRGB());
        int offsetY = 0;
        for (String e : this.modeSetting.getModes()) {
            if (!e.equals(modeSetting.getValue())) {
                if (isHover((int) (x - 160), (int) y + 6 + offsetY, 150, 14, mouseX, mouseY)) {
                    RenderUtil.drawRect((int) (x - 160), (int) (y - 5 + animation) + 14 + offsetY, 1, 14, Theme.moduleTheme);
                    RenderUtil.drawRect((int) (x - 12), (int) (y - 5 + animation) + 14 + offsetY, 1, 14, Theme.moduleTheme);
                }
                FontLoaders.comfortaaB18.drawStringWithShadow(e, x - 85 - (FontLoaders.comfortaaB18.getStringWidth(e) / 2.0f), y + 14 / 2.0f - FontLoaders.comfortaaB18.getHeight() / 2.0f - 6 + offsetY + 14 + animation, Theme.font.getRGB());
                offsetY += 14;
            }
        }
    }

    @Override
    public void mouse(int mouseButton, MouseType mouseType) {
        if (mouseType == MouseType.CLICK && mouseButton == 0) {
            if (isHover((int) (x - 160), (int) (y - 6), 150, 14, mouseX, mouseY)) {
                open = !open;
            }
            int offsetY = 0;
            if (open) {
                for (String e : this.modeSetting.getModes()) {
                    if (e.equals(modeSetting.getValue())) {
                        continue;
                    }
                    if (isHover((int) (x - 160), (int) (y + 6) + offsetY, 150, 14, mouseX, mouseY)) {
                        this.modeSetting.setValue(e);
                    }
                    offsetY += 14;
                }
            }
        }
    }

    @Override
    public void input(char typedChar, int keyCode) {

    }

}

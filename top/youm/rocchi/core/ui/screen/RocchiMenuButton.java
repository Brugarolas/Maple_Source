package top.youm.rocchi.core.ui.screen;

import top.youm.rocchi.core.ui.Theme;
import top.youm.rocchi.core.ui.font.CFontRenderer;
import top.youm.rocchi.core.ui.font.FontLoaders;
import top.youm.rocchi.utils.render.RoundedUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;

import java.awt.*;

/**
 * @author You_M
 */
public class RocchiMenuButton extends GuiButton {
    private static final CFontRenderer font = FontLoaders.comfortaaR24;
    private boolean big;
    public float animSize = 1.0f;
    public RocchiMenuButton(int buttonId, int x, int y, String buttonText) {
        super(buttonId, (int) (x - font.getStringWidth(buttonText) / 2.0), y - 5, font.getStringWidth(buttonText) + 10,  font.getHeight() + 10, buttonText);
    }

    public RocchiMenuButton(boolean big,int buttonId, int x, int y,int width, String buttonText) {
        super(buttonId,  (x), y - 5, width,  font.getHeight() + 10, buttonText);
        this.big = big;
    }
    @Override
    public void drawButton(Minecraft mc, int mouseX, int mouseY) {
        this.hovered = mouseX >= this.xPosition && mouseY >= this.yPosition && mouseX < this.xPosition + this.width && mouseY < this.yPosition + this.height;
        RoundedUtil.drawRound(this.xPosition, this.yPosition, this.width, this.height, 2, new Color(255, 255, 255, 44));
        if(big) {
            font.drawCenteredStringWithShadow(this.displayString, this.xPosition + this.width / 2.0f + 5, this.yPosition + 5, Theme.FONT_COLOR.getRGB());
        } else{
            font.drawStringWithShadow(this.displayString, this.xPosition + 5, this.yPosition + 5, Theme.FONT_COLOR.getRGB());
        }
    }
}

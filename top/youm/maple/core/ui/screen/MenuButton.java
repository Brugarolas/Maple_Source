package top.youm.maple.core.ui.screen;

import top.youm.maple.core.ui.theme.Theme;
import top.youm.maple.core.ui.font.CFontRenderer;
import top.youm.maple.core.ui.font.FontLoaders;
import top.youm.maple.utils.AnimationUtils;
import top.youm.maple.utils.render.RenderUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;

import java.awt.*;

/**
 * @author YouM
 */
public class MenuButton extends GuiButton {
    private static final CFontRenderer font = FontLoaders.comfortaaR24;
    public float animation = 0;
    private Color color;
    AnimationUtils animator = new AnimationUtils();
    private String icon;
    public MenuButton(int buttonId, int x, int y, String buttonText,String icon) {
        super(buttonId, (int) (x - font.getStringWidth(buttonText) / 2.0), y - 5, font.getStringWidth(buttonText) + 10 + FontLoaders.icon24.getStringWidth(icon),  font.getHeight() + 10, buttonText);
        this.icon = icon;
    }
    @Override
    public void drawButton(Minecraft mc, int mouseX, int mouseY) {
        this.hovered = mouseX >= this.xPosition && mouseY >= this.yPosition && mouseX < this.xPosition + this.width && mouseY < this.yPosition + this.height;
        if(hovered){
            color = Theme.theme;
            animation = animator.animate(10,animation,0.08f);
        }else {
            color = Theme.font;
            animation = animator.animate(0,animation,0.08f);
        }
        RenderUtil.drawFadeRect(xPosition - 7 - (animation / 2),yPosition + font.getHeight() + 5,width + 10 + animation,1,color,7);
        FontLoaders.icon24.drawStringWithShadow(icon,xPosition - 2,yPosition + 2,color.getRGB());
        FontLoaders.robotoB26.drawStringWithShadow(displayString,xPosition + FontLoaders.icon24.getStringWidth(icon) ,yPosition,color.getRGB());
    }
}

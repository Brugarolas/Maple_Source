package top.youm.rocchi.core.ui.screen;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.util.ResourceLocation;
import top.youm.rocchi.core.ui.font.CFontRenderer;
import top.youm.rocchi.core.ui.font.FontLoaders;
import top.youm.rocchi.core.ui.theme.Theme;
import top.youm.rocchi.utils.AnimationUtils;
import top.youm.rocchi.utils.render.RenderUtil;
import top.youm.rocchi.utils.render.RoundedUtil;

import java.awt.*;

public class IconButton extends GuiButton {
    private static final CFontRenderer font = FontLoaders.comfortaaR24;
    public int animationAlpha = 0;
    private String icon;
    AnimationUtils animator = new AnimationUtils();
    public IconButton(int buttonId, int x, int y, int width, int height,String icon) {
        super(buttonId, x, y, width,  height,"");
        this.icon = icon;
    }
    @Override
    public void drawButton(Minecraft mc, int mouseX, int mouseY) {
        this.hovered = mouseX >= this.xPosition && mouseY >= this.yPosition && mouseX < this.xPosition + this.width && mouseY < this.yPosition + this.height;
        if(hovered){
            animationAlpha = 180;
        }else {
            animationAlpha = 70;
        }
        RoundedUtil.drawRoundOutline(xPosition,yPosition,width,height,2,0.1f,new Color(176, 176, 176, animationAlpha),new Color(200,200,200,170));
        FontLoaders.icon28.drawString(icon,xPosition + 4,yPosition + 6,new Color(230, 230, 230, 255).getRGB());
    }
}
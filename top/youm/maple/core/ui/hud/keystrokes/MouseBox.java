package top.youm.maple.core.ui.hud.keystrokes;

import net.minecraft.client.Minecraft;
import top.youm.maple.core.module.modules.visual.KeyStrokes;
import top.youm.maple.core.ui.hud.KeyStrokesUI;
import top.youm.maple.utils.AnimationUtils;
import top.youm.maple.utils.render.RenderUtil;

import java.awt.*;

/**
 * @author YouM
 */
public class MouseBox {
    private Minecraft mc = Minecraft.getMinecraft();
    public AnimationUtils animationUtils = new AnimationUtils();
    public int animBackgroundColor = 1;
    public Color fontColor;
    public void drawMouseBox(String text,int x, int y, int width, int height, Color color,boolean down){
        if(down){
            animBackgroundColor = animationUtils.animate(255,animBackgroundColor,0.15f);
            fontColor = new Color(0,0,0);
        } else {
            animBackgroundColor = 0;
            fontColor = new Color(255,255,255);
        }
        float center = KeyStrokesUI.y + (KeyStrokesUI.size + KeyStrokesUI.margin) * 2 + (KeyStrokesUI.size - 4) / 2.0f;
        RenderUtil.drawRect(x,y,width,height,new Color(animBackgroundColor,animBackgroundColor,animBackgroundColor,150));
        KeyStrokesUI.drawCenteredStringWithShadow(text,x + 20, center,fontColor);
    }

}

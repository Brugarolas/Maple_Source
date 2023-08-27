package top.youm.maple.core.ui.clickgui.common.sub.button;


import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import top.youm.maple.utils.render.CircleManager;
import top.youm.maple.utils.render.RenderUtil;
import top.youm.maple.utils.render.RoundedUtil;
import top.youm.maple.utils.render.Stencil;

import java.awt.*;

/**
 * @author YouM
 * Created on 2023/8/24
 */
public class RippledButton extends ButtonComponent {
    CircleManager clickCircle = new CircleManager();
    public RippledButton(String name, float width, float height) {
        super(name, width, height);
    }

    @Override
    public void drawComponent() {
        RoundedUtil.drawRound(x,y,width,height,4,new Color(100,100,100));

        Stencil.write(false);
        RoundedUtil.drawRound(x,y,width,height,4,new Color(100,100,100));
        Stencil.erase(true);
        GlStateManager.enableBlend();
        clickCircle.drawCircles();
        Stencil.dispose();
    }

    @Override
    public void onMouseClick(int mouseButton) {
        if(onComponentHover() && mouseButton == 0){
            clickCircle.addCircle(mouseX,mouseY,45, 50, Minecraft.getMinecraft().gameSettings.keyBindAttack.getKeyCode());
        }
    }

    @Override
    public void onMouseRelease(int mouseButton) {

    }

    @Override
    public void update(float x, float y, int mouseX, int mouseY) {
        super.update(x, y, mouseX, mouseY);
        clickCircle.runCircles();
    }
}

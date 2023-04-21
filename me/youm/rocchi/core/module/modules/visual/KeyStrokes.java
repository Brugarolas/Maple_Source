package me.youm.rocchi.core.module.modules.visual;

import com.darkmagician6.eventapi.EventTarget;
import me.youm.rocchi.common.events.Render2DEvent;
import me.youm.rocchi.common.settings.BoolSetting;
import me.youm.rocchi.core.module.Module;
import me.youm.rocchi.core.module.ModuleCategory;
import me.youm.rocchi.core.ui.font.FontLoaders;
import me.youm.rocchi.utils.render.RenderUtil;
import net.minecraft.client.Minecraft;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import java.awt.*;

public class KeyStrokes extends Module {

    public KeyStrokes() {
        super("KeyStrokes", ModuleCategory.VISUAL, Keyboard.KEY_K);
        this.addSetting(ttf);
    }
    private final BoolSetting ttf = new BoolSetting("ttf",true);
    private final int x = 50,y = 50,size = 25,margin = 4;
    @EventTarget
    public void onRender(Render2DEvent event){
        render();
    }

    public void render(){
        renderKey();
        renderMouse();
    }
    public void renderKey(){
        float centerX = (x + size + margin) + (size / 2.0f);
        float centerY = (y + size + margin) + (size / 2.0f);
        RenderUtil.drawRect(x+ size + margin,y,size,size,this.mc.gameSettings.keyBindForward.isKeyDown() ? new Color(255, 255, 255,150) : new Color(0, 0, 0,150));
        RenderUtil.drawRect(x+ size + margin,y+ size + margin,size,size,this.mc.gameSettings.keyBindBack.isKeyDown() ? new Color(255, 255, 255,150) : new Color(0, 0, 0,150));
        RenderUtil.drawRect(x,y+ size + margin,size,size, this.mc.gameSettings.keyBindLeft.isKeyDown() ? new Color(255, 255, 255,150) : new Color(0, 0, 0,150));
        RenderUtil.drawRect(x+ (size + margin) * 2,y+ size + margin,size,size, this.mc.gameSettings.keyBindRight.isKeyDown() ? new Color(255, 255, 255,150) : new Color(0, 0, 0,150));
        this.drawCenteredStringWithShadow("W", centerX,y + (size / 2.0f),new Color(189, 189, 189, 255),ttf.getValue());
        this.drawCenteredStringWithShadow("A",x + (size / 2.0f) , centerY ,new Color(189, 189, 189, 255),ttf.getValue());
        this.drawCenteredStringWithShadow("S", centerX , centerY ,new Color(189, 189, 189, 255),ttf.getValue());
        this.drawCenteredStringWithShadow("D",(x + (size + margin) * 2) + (size / 2.0f) , centerY ,new Color(189, 189, 189, 255),ttf.getValue());
    }
    public void renderMouse(){
        RenderUtil.drawRect(x,y + (size + margin) * 2,40,size - 4, Mouse.isButtonDown(0) ? new Color(255,255,255,150) : new Color(0,0,0,150));
        RenderUtil.drawRect(x + 43,y + (size + margin) * 2,40,size - 4, Mouse.isButtonDown(1) ? new Color(255,255,255,150) : new Color(0,0,0,150));
        float center = y + (size + margin) * 2 + (size - 4) / 2.0f;
        this.drawCenteredStringWithShadow("LMB",x + 20, center,new Color(189, 189, 189, 255),ttf.getValue());
        this.drawCenteredStringWithShadow("RMB",x + 60 + margin, center,new Color(189, 189, 189, 255),ttf.getValue());

    }
    public void drawCenteredStringWithShadow(String text,float x,float y,Color color,boolean ttf){
        if(!ttf)this.mc.fontRendererObj.drawStringWithShadow(text,x - this.mc.fontRendererObj.getStringWidth(text) / 2.0f,y - this.mc.fontRendererObj.FONT_HEIGHT / 2.0f,color.getRGB());
        else FontLoaders.comfortaaR18.drawCenteredStringWithShadow(text,x ,y - 4,color.getRGB());
    }
}

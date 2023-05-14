package top.youm.rocchi.core.module.modules.visual;

import com.darkmagician6.eventapi.EventTarget;
import top.youm.rocchi.common.events.Render2DEvent;
import top.youm.rocchi.common.settings.BoolSetting;
import top.youm.rocchi.core.module.Module;
import top.youm.rocchi.core.module.ModuleCategory;
import top.youm.rocchi.core.module.modules.visual.keystrokes.KeyBox;
import top.youm.rocchi.core.module.modules.visual.keystrokes.MouseBox;
import top.youm.rocchi.core.ui.font.FontLoaders;
import top.youm.rocchi.utils.render.RenderUtil;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import java.awt.*;

public class KeyStrokes extends Module {

    public KeyStrokes() {
        super("KeyStrokes", ModuleCategory.VISUAL, Keyboard.KEY_K);
        this.addSetting(ttf);
        this.forward = new KeyBox(this.mc.gameSettings.keyBindForward);
        this.back = new KeyBox(this.mc.gameSettings.keyBindBack);
        this.left = new KeyBox(this.mc.gameSettings.keyBindLeft);
        this.right = new KeyBox(this.mc.gameSettings.keyBindRight);
    }
    private final BoolSetting ttf = new BoolSetting("ttf-font",true);
    private final int x = 50,y = 50,size = 25,margin = 4;
    private final KeyBox forward,back,left,right;
    private final MouseBox mouseLeft = new MouseBox(),mouseRight = new MouseBox();
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
        this.forward.setKeyBinding(this.mc.gameSettings.keyBindForward);
        this.back.setKeyBinding(this.mc.gameSettings.keyBindBack);
        this.left.setKeyBinding(this.mc.gameSettings.keyBindLeft);
        this.right.setKeyBinding(this.mc.gameSettings.keyBindRight);
        forward.drawKeyBox(x+ size + margin,y,size,size, new Color(255, 255, 255,150));
        back.drawKeyBox(x+ size + margin,y+ size + margin,size,size, new Color(255, 255, 255,150));
        left.drawKeyBox(x,y+ size + margin,size,size, new Color(255, 255, 255,150));
        right.drawKeyBox(x+ (size + margin) * 2,y+ size + margin,size,size, new Color(255, 255, 255,150));
        this.drawCenteredStringWithShadow("W", centerX,y + (size / 2.0f),new Color(189, 189, 189, 255),ttf.getValue());
        this.drawCenteredStringWithShadow("A",x + (size / 2.0f) , centerY ,new Color(189, 189, 189, 255),ttf.getValue());
        this.drawCenteredStringWithShadow("S", centerX , centerY ,new Color(189, 189, 189, 255),ttf.getValue());
        this.drawCenteredStringWithShadow("D",(x + (size + margin) * 2) + (size / 2.0f) , centerY ,new Color(189, 189, 189, 255),ttf.getValue());
    }
    public void renderMouse(){
        mouseLeft.drawMouseBox(x,y + (size + margin) * 2,40,size - 4,new Color(255,255,255,150), Mouse.isButtonDown(0) );
        mouseRight.drawMouseBox(x + 43,y + (size + margin) * 2,40,size - 4,new Color(255,255,255,150), Mouse.isButtonDown(1));
        float center = y + (size + margin) * 2 + (size - 4) / 2.0f;
        this.drawCenteredStringWithShadow("LMB",x + 20, center,new Color(189, 189, 189, 255),ttf.getValue());
        this.drawCenteredStringWithShadow("RMB",x + 60 + margin, center,new Color(189, 189, 189, 255),ttf.getValue());

    }
    public void drawCenteredStringWithShadow(String text,float x,float y,Color color,boolean ttf){
        if(!ttf)this.mc.fontRendererObj.drawStringWithShadow(text,x - this.mc.fontRendererObj.getStringWidth(text) / 2.0f,y - this.mc.fontRendererObj.FONT_HEIGHT / 2.0f,color.getRGB());
        else FontLoaders.comfortaaR18.drawCenteredStringWithShadow(text,x ,y - 4,color.getRGB());
    }

    public void drawKeyBox(int x,int y,int width,int height,Color color){
        RenderUtil.drawRect(x,y,width,height,color);
    }
}

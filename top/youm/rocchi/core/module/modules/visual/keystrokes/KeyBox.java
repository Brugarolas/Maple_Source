package top.youm.rocchi.core.module.modules.visual.keystrokes;

import net.minecraft.client.settings.KeyBinding;
import top.youm.rocchi.utils.AnimationUtils;
import top.youm.rocchi.utils.render.RenderUtil;

import java.awt.*;

public class KeyBox {
    private KeyBinding keyBinding;
    public void drawKeyBox(int x, int y, int width, int height, Color color){
        if(this.keyBinding.isKeyDown()){
            RenderUtil.drawRect(x,y,width,height,new Color(color.getRed(),color.getGreen(),color.getBlue(),150));
        }else{
            RenderUtil.drawRect(x,y,width,height,new Color(0,0,0,150));
        }
    }

    public KeyBox(KeyBinding keyBinding) {
        this.keyBinding = keyBinding;
    }

    public KeyBinding getKeyBinding() {
        return keyBinding;
    }

    public void setKeyBinding(KeyBinding keyBinding) {
        this.keyBinding = keyBinding;
    }
}

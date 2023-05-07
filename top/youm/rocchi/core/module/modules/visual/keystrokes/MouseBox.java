package top.youm.rocchi.core.module.modules.visual.keystrokes;

import top.youm.rocchi.utils.render.RenderUtil;

import java.awt.*;

public class MouseBox {
    public void drawMouseBox(int x, int y, int width, int height, Color color,boolean down){
        if(down)RenderUtil.drawRect(x,y,width,height,new Color(color.getRed(),color.getGreen(),color.getBlue(),150));
        else RenderUtil.drawRect(x,y,width,height,new Color(0,0,0,150));
    }

}

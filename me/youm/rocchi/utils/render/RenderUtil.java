package me.youm.rocchi.utils.render;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;

import java.awt.Color;
public class RenderUtil {
    public static void drawRect(int x,int y,int width,int height,int color){
        Gui.drawRect(x,y,x+width,y+height,color);
    }
    public static void drawRect(int x,int y,int width,int height,Color color){
        drawRect(x,y,width,height,color.getRGB());
    }

}

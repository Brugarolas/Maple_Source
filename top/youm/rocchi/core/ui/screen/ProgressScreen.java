package top.youm.rocchi.core.ui.screen;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.shader.Framebuffer;
import top.youm.rocchi.core.ui.font.FontLoaders;
import top.youm.rocchi.core.ui.theme.Theme;
import top.youm.rocchi.utils.AnimationUtils;
import top.youm.rocchi.utils.TimerUtil;
import top.youm.rocchi.utils.math.MathUtil;
import top.youm.rocchi.utils.render.RenderUtil;

import java.awt.*;
import java.util.Random;

public class ProgressScreen {
    private Framebuffer framebuffer;
    private String text;
    private int progress;
    private int number;
    public void makeProgress(String text){
        this.text = text;
        this.progress += 17;
        drawScreen();
    }

    public ProgressScreen() {
        number = MathUtil.getRandomInRange(1, 3);
    }

    public void drawScreen(){

        ScaledResolution sr = new ScaledResolution(Minecraft.getMinecraft());
        framebuffer = new Framebuffer(Minecraft.getMinecraft().displayWidth, Minecraft.getMinecraft().displayHeight,false);
        framebuffer.framebufferClear();
        framebuffer.bindFramebuffer(true);
        int i = sr.getScaleFactor();
        GlStateManager.matrixMode(5889);
        GlStateManager.loadIdentity();
        GlStateManager.ortho(0.0D, sr.getScaledWidth(), sr.getScaledHeight(), 0.0D, 1000.0D, 3000.0D);
        GlStateManager.matrixMode(5888);
        GlStateManager.loadIdentity();
        GlStateManager.translate(0.0F, 0.0F, -2000.0F);
        GlStateManager.disableLighting();
        GlStateManager.disableFog();
        GlStateManager.disableDepth();
        GlStateManager.enableTexture2D();

        this.draw(sr);

        GlStateManager.disableLighting();
        GlStateManager.disableFog();
        framebuffer.unbindFramebuffer();
        framebuffer.framebufferRender(sr.getScaledWidth() * i, sr.getScaledHeight() * i);
        GlStateManager.enableAlpha();
        GlStateManager.alphaFunc(516, 0.1F);

        Minecraft.getMinecraft().updateDisplay();
    }
    public void draw(ScaledResolution sr){
        RenderUtil.drawTexturedRect(0,0,sr.getScaledWidth(),sr.getScaledHeight(),"background"+number+".png");
        FontLoaders.robotoR34.drawStringWithShadow(text,sr.getScaledWidth() / 2.0 - FontLoaders.robotoR34.getStringWidth(text) / 2.0f, sr.getScaledHeight() - 150, Theme.font.getRGB());
        RenderUtil.drawBorder(sr.getScaledWidth() / 2.0f - 75, sr.getScaledHeight()/2.0f,150,20,2,new Color(233,233,233));
        RenderUtil.drawRect(sr.getScaledWidth() / 2.0f - 71, sr.getScaledHeight() / 2.0f + 6,progress + 0.5f,10,new Color(233,233,233));
    }
}

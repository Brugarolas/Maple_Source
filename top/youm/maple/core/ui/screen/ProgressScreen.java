package top.youm.maple.core.ui.screen;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.shader.Framebuffer;
import org.lwjgl.opengl.GL11;
import top.youm.maple.core.ui.font.FontLoaders;
import top.youm.maple.utils.animation.Animation;
import top.youm.maple.utils.animation.SmoothStepAnimation;
import top.youm.maple.utils.render.RenderUtil;
import top.youm.maple.utils.render.RoundedUtil;

import java.awt.*;

/**
 * @author YouM
 */
public class ProgressScreen {
    private Framebuffer framebuffer;

    public void drawScreen(float prograss){
        ScaledResolution sr = new ScaledResolution(Minecraft.getMinecraft());
        // Create the scale factor
        int scaleFactor = sr.getScaleFactor();
        // Bind the width and height to the framebuffer
        framebuffer = RenderUtil.createFrameBuffer(framebuffer);

        while (!end) {
            framebuffer.framebufferClear();
            framebuffer.bindFramebuffer(true);
            // Create the projected image to be rendered
            GlStateManager.matrixMode(GL11.GL_PROJECTION);
            GlStateManager.loadIdentity();
            GlStateManager.ortho(0.0D, sr.getScaledWidth(), sr.getScaledHeight(), 0.0D, 1000.0D, 3000.0D);
            GlStateManager.matrixMode(GL11.GL_MODELVIEW);
            GlStateManager.loadIdentity();
            GlStateManager.translate(0.0F, 0.0F, -2000.0F);
            GlStateManager.disableLighting();
            GlStateManager.disableFog();
            GlStateManager.disableDepth();
            GlStateManager.enableTexture2D();


            GlStateManager.color(0, 0, 0, 0);
            GlStateManager.enableBlend();
            GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

            draw(sr,prograss);

            // Unbind the width and height as it's no longer needed
            framebuffer.unbindFramebuffer();

            // Render the previously used frame buffer
            framebuffer.framebufferRender(sr.getScaledWidth() * scaleFactor, sr.getScaledHeight() * scaleFactor);
            // Update the users screen
            Minecraft.getMinecraft().updateDisplay();
        }
        end = false;
    }
    private Animation progressAnim;
    private double progress = 0;
    private boolean end = false;
    public void draw(ScaledResolution sr,float progressF){
        float width = sr.getScaledWidth() - 100;
        if (progressAnim == null) {
            progressAnim = new SmoothStepAnimation(500, (progressF * width) - (progress * width));
        }
        RenderUtil.drawTexturedRect(0,0,sr.getScaledWidth(),sr.getScaledHeight(),"background.png");
        FontLoaders.robotoB40.drawCenteredStringWithShadow("Loading Client...",sr.getScaledWidth() / 2.0f,sr.getScaledHeight() - (sr.getScaledHeight() / 10.0f) - 50,-1);
        RoundedUtil.drawRound((sr.getScaledWidth() / 2.0f) - (width / 2.0f),sr.getScaledHeight() - (sr.getScaledHeight() / 10.0f), width, 10,5,new Color(91, 91, 91));
        RoundedUtil.drawRound(sr.getScaledWidth() / 2.0f - width / 2.0f,sr.getScaledHeight() - (sr.getScaledHeight() / 10.0f) , (float)( (progress * width) + (progressAnim.getOutput())), 10,5,new Color(255,255,255));
        if(progressAnim.isDone()) {
            progress = progressF;
            end = true;
            progressAnim = null;
        }
    }

}

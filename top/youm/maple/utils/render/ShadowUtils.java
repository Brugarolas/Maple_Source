package top.youm.maple.utils.render;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.shader.Framebuffer;
import net.minecraft.client.shader.ShaderGroup;
import net.minecraft.util.ResourceLocation;

import java.io.IOException;

import static net.minecraft.client.renderer.GlStateManager.glTexParameteri;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL12.GL_CLAMP_TO_EDGE;

public class ShadowUtils{

    private static Framebuffer initFramebuffer = null;
    private static Framebuffer frameBuffer = null;
    static Framebuffer resultBuffer = null;

    private static ShaderGroup shaderGroup = null;
    private static int lastWidth = 0;
    private static int lastHeight = 0;
    private static float lastStrength = 0F;

    private static final ResourceLocation blurDirectory = new ResourceLocation("Maple/shader/post/shadow.json");

    public static void initShaderIfRequired(ScaledResolution sc, Float strength) throws IOException {
        int width = sc.getScaledWidth();
        int height = sc.getScaledHeight();
        int factor = sc.getScaleFactor();

        if (lastWidth != width || lastHeight != height || initFramebuffer == null || frameBuffer == null || shaderGroup == null) {
            initFramebuffer = new Framebuffer(width * factor, height * factor, true);
            initFramebuffer.setFramebufferColor(0F, 0F, 0F, 0F);
            initFramebuffer.setFramebufferFilter(GL_LINEAR);
            shaderGroup = new ShaderGroup(Minecraft.getMinecraft().getTextureManager(), Minecraft.getMinecraft().getResourceManager(), initFramebuffer, blurDirectory);
            shaderGroup.createBindFramebuffers(width * factor, height * factor);
            frameBuffer = shaderGroup.getMainFramebuffer();
            resultBuffer = shaderGroup.getFramebufferRaw("braindead");
    
            lastWidth = width;
            lastHeight = height;
            lastStrength = strength;
            for (int i = 0;i<=1;i++) {
                shaderGroup.getListShaders().get(i).getShaderManager().getShaderUniform("Radius").set(strength);
            }
        }
        if (lastStrength != strength) {
            lastStrength = strength;
            for (int i = 0;i<=1;i++) {
                shaderGroup.getListShaders().get(i).getShaderManager().getShaderUniform("Radius").set(strength);
            }
        }
    }

    public static void shadow(Float strength, Runnable drawMethod,Runnable cutMethod) {
        if (!OpenGlHelper.isFramebufferEnabled())
            return;

        ScaledResolution sc = new ScaledResolution(Minecraft.getMinecraft());
        float width = sc.getScaledWidth();
        float height = sc.getScaledHeight();
        try {
            initShaderIfRequired(sc, strength);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if(initFramebuffer == null || resultBuffer == null || frameBuffer == null) return;

        Minecraft.getMinecraft().getFramebuffer().unbindFramebuffer();
        initFramebuffer.framebufferClear();
        resultBuffer.framebufferClear();
        initFramebuffer.bindFramebuffer(true);
        drawMethod.run();
        frameBuffer.bindFramebuffer(true);
        shaderGroup.loadShaderGroup(Minecraft.getMinecraft().timer.renderPartialTicks);
        Minecraft.getMinecraft().getFramebuffer().bindFramebuffer(true);

        int fr_width = resultBuffer.framebufferWidth / resultBuffer.framebufferTextureWidth;
        int fr_height = resultBuffer.framebufferHeight / resultBuffer.framebufferTextureHeight;

        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldrenderer = tessellator.getWorldRenderer();

        glPushMatrix();
        GlStateManager.disableLighting();
        GlStateManager.disableAlpha();
        GlStateManager.enableTexture2D();
        GlStateManager.disableDepth();
        GlStateManager.depthMask(false);
        GlStateManager.colorMask(true, true, true, true);

        Stencil.write(false);
        cutMethod.run();
        Stencil.erase(false);

        GlStateManager.enableBlend();
        GlStateManager.blendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        GlStateManager.color(1F, 1F, 1F, 1F);

        resultBuffer.bindFramebufferTexture();
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE);

        worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
        worldrenderer.pos(0.0, height, 0.0).tex(0.0, 0.0).color(255, 255, 255, 255).endVertex();
        worldrenderer.pos(width, height, 0.0).tex(fr_width, 0.0).color(255, 255, 255, 255).endVertex();
        worldrenderer.pos(width, 0.0, 0.0).tex(fr_width, fr_height).color(255, 255, 255, 255).endVertex();
        worldrenderer.pos(0.0, 0.0, 0.0).tex(0.0, fr_height).color(255, 255, 255, 255).endVertex();

        tessellator.draw();
        resultBuffer.unbindFramebufferTexture();

        GlStateManager.disableBlend();
        GlStateManager.enableAlpha();
        GlStateManager.enableDepth();
        GlStateManager.depthMask(true);

        Stencil.dispose();
        glPopMatrix();

        GlStateManager.resetColor();
        GlStateManager.color(1F, 1F, 1F, 1F);
        GlStateManager.enableBlend();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
    }

}
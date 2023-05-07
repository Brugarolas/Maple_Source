package top.youm.rocchi.core.module.modules.visual;


import com.darkmagician6.eventapi.EventTarget;
import top.youm.rocchi.common.events.Render2DEvent;
import top.youm.rocchi.common.events.Render3DEvent;
import top.youm.rocchi.common.settings.BoolSetting;
import top.youm.rocchi.common.settings.NumberSetting;
import top.youm.rocchi.core.module.Module;
import top.youm.rocchi.core.module.ModuleCategory;
import top.youm.rocchi.utils.math.MathUtil;
import top.youm.rocchi.utils.render.ESPUtil;
import top.youm.rocchi.utils.render.RenderUtil;
import top.youm.rocchi.utils.render.gl.ShaderUtil;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.shader.Framebuffer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.player.EntityPlayer;
import org.lwjgl.BufferUtils;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;

import java.awt.*;
import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.opengl.GL20.glUniform1;

@SuppressWarnings("unused")
public class GlowESP extends Module {
    private final NumberSetting radius = new NumberSetting("Radius", 4, 30, 2, 2);
    private final NumberSetting exposure = new NumberSetting("Exposure", 2.2, 3.5, .5, .1);
    private final BoolSetting seperate = new BoolSetting("Seperate Texture", true);

    public GlowESP() {
        super("GlowESP", ModuleCategory.VISUAL, Keyboard.KEY_B);
        this.addSetting(radius,exposure, seperate);
    }

    public static boolean renderNameTags = true;
    private final ShaderUtil outlineShader = new ShaderUtil("Rocchi/shader/outline.frag");
    private final ShaderUtil glowShader = new ShaderUtil("Rocchi/shader/glow.frag");

    public Framebuffer framebuffer;
    public Framebuffer outlineFrameBuffer;
    public Framebuffer glowFrameBuffer;
    private final Frustum frustum = new Frustum();
    private final List<Entity> entities = new ArrayList<>();

    public void createFrameBuffers() {
        framebuffer = RenderUtil.createFrameBuffer(framebuffer);
        outlineFrameBuffer = RenderUtil.createFrameBuffer(outlineFrameBuffer);
        glowFrameBuffer = RenderUtil.createFrameBuffer(glowFrameBuffer);
    }

    @EventTarget
    public void onRender3D(Render3DEvent event){
        createFrameBuffers();
        collectEntities();
        framebuffer.framebufferClear();
        framebuffer.bindFramebuffer(true);
        renderEntities(event.getTicks());
        framebuffer.unbindFramebuffer();
        mc.getFramebuffer().bindFramebuffer(true);
        GlStateManager.disableLighting();
    };

    @EventTarget
    private void onRender(Render2DEvent event){
        ScaledResolution sr = new ScaledResolution(mc);
        if (framebuffer != null && outlineFrameBuffer != null && entities.size() > 0) {
            GlStateManager.enableAlpha();
            GlStateManager.alphaFunc(516, 0.0f);
            GlStateManager.enableBlend();

            OpenGlHelper.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA, GL11.GL_ONE, GL11.GL_ZERO);
            outlineFrameBuffer.framebufferClear();
            outlineFrameBuffer.bindFramebuffer(true);
            outlineShader.init();
            setupOutlineUniforms(0, 1);
            RenderUtil.bindTexture(framebuffer.framebufferTexture);
            ShaderUtil.drawQuads();
            outlineShader.init();
            setupOutlineUniforms(1, 0);
            RenderUtil.bindTexture(framebuffer.framebufferTexture);
            ShaderUtil.drawQuads();
            outlineShader.unload();
            outlineFrameBuffer.unbindFramebuffer();

            GlStateManager.color(1, 1, 1, 1);
            glowFrameBuffer.framebufferClear();
            glowFrameBuffer.bindFramebuffer(true);
            glowShader.init();
            setupGlowUniforms(1, 0);
            RenderUtil.bindTexture(outlineFrameBuffer.framebufferTexture);
            ShaderUtil.drawQuads();
            glowShader.unload();
            glowFrameBuffer.unbindFramebuffer();

            mc.getFramebuffer().bindFramebuffer(true);
            glowShader.init();
            setupGlowUniforms(0, 1);
            if (seperate.getValue()) {
                GL13.glActiveTexture(GL13.GL_TEXTURE16);
                RenderUtil.bindTexture(framebuffer.framebufferTexture);
            }
            GL13.glActiveTexture(GL13.GL_TEXTURE0);
            RenderUtil.bindTexture(glowFrameBuffer.framebufferTexture);
            ShaderUtil.drawQuads();
            glowShader.unload();

        }
    }


    public void setupGlowUniforms(float dir1, float dir2) {
        Color color = getColor();
        glowShader.setUniformi("texture", 0);
        if (seperate.getValue()) {
            glowShader.setUniformi("textureToCheck", 16);
        }
        glowShader.setUniformf("radius", radius.getValue().floatValue());
        glowShader.setUniformf("texelSize", 1.0f / mc.displayWidth, 1.0f / mc.displayHeight);
        glowShader.setUniformf("direction", dir1, dir2);
        glowShader.setUniformf("color", color.getRed() / 255f, color.getGreen() / 255f, color.getBlue() / 255f);
        glowShader.setUniformf("exposure",  (exposure.getValue().floatValue()));
        glowShader.setUniformi("avoidTexture", seperate.getValue() ? 1 : 0);

        final FloatBuffer buffer = BufferUtils.createFloatBuffer(256);
        for (int i = 1; i <= radius.getValue().floatValue(); i++) {
            buffer.put(MathUtil.calculateGaussianValue(i, radius.getValue().floatValue() / 2));
        }
        buffer.rewind();

        glUniform1(glowShader.getUniform("weights"), buffer);
    }


    public void setupOutlineUniforms(float dir1, float dir2) {
        Color color = getColor();
        outlineShader.setUniformi("texture", 0);
        outlineShader.setUniformf("radius", radius.getValue().floatValue() / 1.5f);
        outlineShader.setUniformf("texelSize", 1.0f / mc.displayWidth, 1.0f / mc.displayHeight);
        outlineShader.setUniformf("direction", dir1, dir2);
        outlineShader.setUniformf("color", color.getRed() / 255f, color.getGreen() / 255f, color.getBlue() / 255f);
    }

    public void renderEntities(float ticks) {
        entities.forEach(entity -> {
            renderNameTags = false;
            mc.getRenderManager().renderEntityStaticNoShadow(entity, ticks, false);
            renderNameTags = true;
        });
    }

    private Color getColor() {
        return new Color(21, 165, 255);
    }

    public void collectEntities() {
        entities.clear();
        for (Entity entity : mc.theWorld.getLoadedEntityList()) {
            if (!ESPUtil.isInView(entity)) continue;
            if (entity == mc.thePlayer && mc.gameSettings.thirdPersonView == 0) continue;
            if (entity instanceof EntityAnimal) {
                entities.add(entity);
            }
            if (entity instanceof EntityPlayer) {
                entities.add(entity);
            }
            if (entity instanceof EntityMob) {
                entities.add(entity);
            }
        }
    }

}

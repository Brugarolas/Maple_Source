package top.youm.maple.core.module.modules.combat.killaura;

import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.DefaultPlayerSkin;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;
import top.youm.maple.Maple;
import top.youm.maple.common.events.Render3DEvent;
import top.youm.maple.core.module.modules.combat.Targets;
import top.youm.maple.core.module.modules.visual.HUD;
import top.youm.maple.core.module.modules.world.SafeScaffold;
import top.youm.maple.core.ui.font.FontLoaders;
import top.youm.maple.utils.AnimationUtils;
import top.youm.maple.utils.render.ColorUtil;
import top.youm.maple.utils.render.RenderUtil;

import java.awt.*;

import static org.lwjgl.opengl.GL11.glColor4f;

/**
 * @author YouM
 * Created on 2023/9/30
 */
public class AuraRenderer {
    boolean direction;
    float offsetY = 0;
    public void update(){
        if (direction) {
            offsetY += 0.04f;
            if (2 - offsetY < 0.02) {
                direction = false;
            }
        } else {
            offsetY -= 0.04f;
            if (offsetY < 0.02) {
                direction = true;
            }
        }
    }
    public void render3D(Render3DEvent event){
        if (KillAura.footCircle.getValue()) {
            GlStateManager.pushMatrix();
            GlStateManager.translate(KillAura.mc.thePlayer.lastTickPosX + (KillAura.mc.thePlayer.posX - KillAura.mc.thePlayer.lastTickPosX) * KillAura.mc.timer.renderPartialTicks - KillAura.mc.getRenderManager().renderPosX, KillAura.mc.thePlayer.lastTickPosY + (KillAura.mc.thePlayer.posY - KillAura.mc.thePlayer.lastTickPosY) * KillAura.mc.timer.renderPartialTicks - KillAura.mc.getRenderManager().renderPosY, KillAura.mc.thePlayer.lastTickPosZ + (KillAura.mc.thePlayer.posZ - KillAura.mc.thePlayer.lastTickPosZ) * KillAura.mc.timer.renderPartialTicks - KillAura.mc.getRenderManager().renderPosZ);
            GlStateManager.enableBlend();
            GL11.glEnable(GL11.GL_LINE_SMOOTH);
            GlStateManager.disableDepth();
            GlStateManager.disableTexture2D();
            GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
            GL11.glLineWidth(1.0f);
            GlStateManager.rotate(90F, 1F, 0F, 0F);
            RenderUtil.color(HUD.getHUDThemeColor().getRGB());
            GlStateManager.glBegin(GL11.GL_LINE_STRIP);

            for (int i = 0; i <= 360; i += 2) {
                GL11.glVertex2f((float) Math.cos(i * Math.PI / 180.0) * KillAura.reach.getValue().floatValue(), (float) Math.sin(i * Math.PI / 180.0) * KillAura.reach.getValue().floatValue());
            }
            GlStateManager.glEnd();

            GlStateManager.enableTexture2D();
            GlStateManager.enableDepth();
            GL11.glDisable(GL11.GL_LINE_SMOOTH);
            GlStateManager.disableBlend();
            GlStateManager.popMatrix();
            glColor4f(1f, 1f, 1f, 1f);
            GL11.glLineWidth(2);
        }
        if (Targets.INSTANCE.target == null) {
            return;
        }
        if (Targets.INSTANCE.target.isDead || Targets.INSTANCE.target.getHealth() <= 0 || Maple.getInstance().getModuleManager().getModuleByClass(SafeScaffold.class).isToggle() || KillAura.mc.thePlayer.isDead || KillAura.mc.thePlayer.isSpectator())
            return;
        if (KillAura.scanCircle.getValue()) {
            drawCircle(event);
            drawShadow(event);
        }

    }
    public void drawCircle(Render3DEvent event) {

        GlStateManager.pushMatrix();
        GlStateManager.enableBlend();
        GlStateManager.disableDepth();
        GlStateManager.disableTexture2D();
        GL11.glShadeModel(7425);
        GL11.glLineWidth(1.0f);
        double x = Targets.INSTANCE.target.lastTickPosX + (Targets.INSTANCE.target.posX - Targets.INSTANCE.target.lastTickPosX) * (double) event.getTicks() - KillAura.mc.getRenderManager().viewerPosX;
        double y = Targets.INSTANCE.target.lastTickPosY + (Targets.INSTANCE.target.posY - Targets.INSTANCE.target.lastTickPosY) * (double) event.getTicks() - KillAura.mc.getRenderManager().viewerPosY + offsetY;
        double z = Targets.INSTANCE.target.lastTickPosZ + (Targets.INSTANCE.target.posZ - Targets.INSTANCE.target.lastTickPosZ) * (double) event.getTicks() - KillAura.mc.getRenderManager().viewerPosZ;
        GlStateManager.glBegin(GL11.GL_LINE_STRIP);
        for (int i = 0; i <= 360; i++) {
            double c1 = i * Math.PI * 2 / 360;
            RenderUtil.color(HUD.getHUDThemeColor().getRGB());
            GL11.glVertex3d(x + 0.5 * Math.cos(c1) * 1.2, y, z + 0.5 * Math.sin(c1) * 1.2);
        }

        GlStateManager.glEnd();
        GlStateManager.enableTexture2D();
        GL11.glShadeModel(7424);
        GlStateManager.enableDepth();
        GlStateManager.disableBlend();

        GlStateManager.popMatrix();
        glColor4f(1f, 1f, 1f, 1f);
        GL11.glLineWidth(2);
    }

    public void drawShadow(Render3DEvent event) {
        GlStateManager.pushMatrix();
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glDisable(GL11.GL_DEPTH_TEST);
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glShadeModel(7425);
        double x = Targets.INSTANCE.target.lastTickPosX + (Targets.INSTANCE.target.posX - Targets.INSTANCE.target.lastTickPosX) * (double) event.getTicks() - KillAura.mc.getRenderManager().viewerPosX;
        double y = Targets.INSTANCE.target.lastTickPosY + (Targets.INSTANCE.target.posY - Targets.INSTANCE.target.lastTickPosY) * (double) event.getTicks() - KillAura.mc.getRenderManager().viewerPosY + offsetY;
        double z = Targets.INSTANCE.target.lastTickPosZ + (Targets.INSTANCE.target.posZ - Targets.INSTANCE.target.lastTickPosZ) * (double) event.getTicks() - KillAura.mc.getRenderManager().viewerPosZ;
        GL11.glBegin(GL11.GL_QUAD_STRIP);
        for (int i = 0; i <= 360; i++) {
            double c1 = i * Math.PI * 2 / 360;
            double c2 = (i + 1) * Math.PI * 2 / 360;
            GL11.glColor4f(HUD.red.getValue().floatValue() / 255f, HUD.green.getValue().floatValue() / 255f, HUD.blue.getValue().floatValue() / 255f, 0.8f);
            GL11.glVertex3d(x + 0.5 * Math.cos(c1) * 1.2, y, z + 0.5 * Math.sin(c1) * 1.2);
            GL11.glVertex3d(x + 0.5 * Math.cos(c2) * 1.2, y, z + 0.5 * Math.sin(c2) * 1.2);
            GL11.glColor4f(HUD.red.getValue().floatValue() / 255f, HUD.green.getValue().floatValue() / 255f, HUD.blue.getValue().floatValue() / 255f, 0.4f);
            GL11.glVertex3d(x + 0.5 * Math.cos(c1) * 1.2, y + (direction ? -0.3 : 0.3), z + 0.5 * Math.sin(c1) * 1.2);
            GL11.glVertex3d(x + 0.5 * Math.cos(c2) * 1.2, y + (direction ? -0.3 : 0.3), z + 0.5 * Math.sin(c2) * 1.2);
        }
        GL11.glEnd();
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glShadeModel(7424);
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL11.glDisable(GL11.GL_BLEND);
        GlStateManager.popMatrix();
        glColor4f(1f, 1f, 1f, 1f);
        GL11.glLineWidth(2);
    }
    private final AnimationUtils animator = new AnimationUtils();
    private float size;
    private float headSize = 100;
    private float health = 120;
    public void render2D() {
        if (Targets.INSTANCE.target != null) {
            if (Targets.INSTANCE.target.hurtTime > 4) {
                headSize -= 0.5f;
            } else {
                headSize += 0.5f;
            }
            if (headSize <= 90) headSize = 90;
            if (headSize >= 100) headSize = 100;

            size = animator.animate(100, size, 0.1f);
            health = animator.animate((Targets.INSTANCE.target.getHealth() / Targets.INSTANCE.target.getMaxHealth()) * 120, health, 0.1f);
        } else {
            health = animator.animate(0, health, 0.1f);
            if (health <= 0) {
                size = animator.animate(0, size, 0.1f);
            }
        }

        ScaledResolution sr = new ScaledResolution(KillAura.mc);
        int width = sr.getScaledWidth();
        int height = sr.getScaledHeight();
        int x = width - (width / 3);
        int y = height - (height / 3);
        RenderUtil.scale(x + (200 / 2.0f), y + (40 / 2.0f), size / 100, () -> {
            RenderUtil.drawRect(x, y, 180, 40, new Color(0, 0, 0, 120));
            ResourceLocation locationSkin;
            if (Targets.INSTANCE.target != null) {

                if (KillAura.mc.getNetHandler().getPlayerInfo(Targets.INSTANCE.target.getUniqueID()) != null) {
                    locationSkin = KillAura.mc.getNetHandler().getPlayerInfo(Targets.INSTANCE.target.getUniqueID()).getLocationSkin();
                } else {
                    locationSkin = DefaultPlayerSkin.getDefaultSkinLegacy();
                }
                RenderUtil.drawHead(locationSkin, x + 4, y + 4, 34, 34);
                if (Targets.INSTANCE.target.hurtTime > 4) {
                    RenderUtil.drawRect(x + 4, y + 4, 34, 34, new Color(255, 0, 0, 120));
                }

                RenderUtil.drawRect(x + 50, y + 25, 120, 10, new Color(60, 60, 60).getRGB());
                switch (KillAura.targetUIStyle.getValue()) {
                    case "Normal":
                        Color healthColor = getHealthColor();
                        RenderUtil.drawRect(x + 50, y + 25, (Targets.INSTANCE.target.getHealth() / Targets.INSTANCE.target.getMaxHealth()) * 120, 10, healthColor);
                        break;
                    case "Gradient":
                        RenderUtil.drawGradientRect2(x + 50, y + 25, health, 10, HUD.getHUDThemeColor(), ColorUtil.darker(HUD.getHUDThemeColor(), 0.7f));
                }

                FontLoaders.robotoB32.drawStringWithShadow(Targets.INSTANCE.target.getName(), x + 50, y + 8, -1);
                FontLoaders.comfortaaB22.drawCenteredStringWithShadow((float) (Math.round(Targets.INSTANCE.target.getHealth() * 10)) / 10 + "", x + 110, y + 26, -1);
            }else {
                FontLoaders.comfortaaB30.drawCenteredStringWithShadow("Entity Has been Dead!",x+ 90,y + 22 - (FontLoaders.comfortaaB30.getHeight() / 2.0f),-1);
            }
        });
    }
    private static Color getHealthColor() {
        Color healthColor = new Color(0, 255, 0);
        if ((Targets.INSTANCE.target.getHealth() / Targets.INSTANCE.target.getMaxHealth()) * 120 <= 120 * 0.75f) {
            healthColor = new Color(255, 255, 0);
        }
        if ((Targets.INSTANCE.target.getHealth() / Targets.INSTANCE.target.getMaxHealth()) * 120 <= 120 * 0.50f) {
            healthColor = new Color(255, 136, 0);
        }
        if ((Targets.INSTANCE.target.getHealth() / Targets.INSTANCE.target.getMaxHealth()) * 120 <= 120 * 0.25f) {
            healthColor = new Color(255, 0, 0);
        }
        return healthColor;
    }
}

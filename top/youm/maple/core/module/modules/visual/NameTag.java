package top.youm.maple.core.module.modules.visual;

import com.darkmagician6.eventapi.EventTarget;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.StringUtils;
import org.lwjgl.input.Keyboard;
import org.lwjgl.util.vector.Vector4f;
import top.youm.maple.common.events.Render2DEvent;
import top.youm.maple.common.events.Render3DEvent;
import top.youm.maple.common.settings.impl.BoolSetting;
import top.youm.maple.common.settings.impl.NumberSetting;
import top.youm.maple.core.module.Module;
import top.youm.maple.core.module.ModuleCategory;
import top.youm.maple.core.module.modules.world.Teams;
import top.youm.maple.utils.render.ESPUtil;
import top.youm.maple.utils.render.RenderUtil;

import java.awt.*;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.lwjgl.opengl.GL11.*;

/**
 * @author YouM
 * Created on 2023/8/27
 */
public class NameTag extends Module {
    private final Map<EntityLivingBase, Vector4f> entities = new HashMap<>();
    BoolSetting players = new BoolSetting("Players", true);
    BoolSetting mobs = new BoolSetting("Mobs", true);
    BoolSetting animals = new BoolSetting("Animals", false);

    private NumberSetting scale = new NumberSetting("scale",0.5,1,0.1,0.1);
    public NameTag() {
        super("NameTag", ModuleCategory.VISUAL, Keyboard.KEY_NONE);
        this.addSetting(players,mobs,animals,scale);
        this.isRenderModule = true;
    }
    private boolean shouldRender(Entity entity) {
        if (entity.isDead || entity.isInvisible()) {
            return false;
        }
        if (players.getValue() && entity instanceof EntityPlayer) {
            if (entity == mc.thePlayer) {
                return mc.gameSettings.thirdPersonView != 0;
            }
            return !entity.getDisplayName().getUnformattedText().contains("NPC");
        }
        if (animals.getValue() && entity instanceof EntityAnimal) {
            return true;
        }

        return mobs.getValue() && entity instanceof EntityMob;
    }

    @EventTarget
    public void onRender3D(Render3DEvent event){
        entities.clear();
        for (Entity entity : mc.theWorld.getLoadedEntityList()) {
            if (shouldRender(entity) && ESPUtil.isInView(entity) && entity instanceof EntityLivingBase) {
                entities.put((EntityLivingBase)entity, ESPUtil.getEntityPositionsOn2D(entity));
            }
        }
    }
    private final NumberFormat df = new DecimalFormat("0.#");
    @EventTarget
    public void onRender2D(Render2DEvent event){
        for (EntityLivingBase entity : entities.keySet()) {
            Vector4f pos = entities.get(entity);
            float x = pos.getX(),
                    y = pos.getY(),
                    right = pos.getZ(),
                    bottom = pos.getW();
            FontRenderer font = mc.fontRendererObj;
            float healthValue = entity.getHealth() / entity.getMaxHealth();
            Color healthColor = healthValue > .75 ? new Color(66, 246, 123) : healthValue > .5 ? new Color(228, 255, 105) : healthValue > .35 ? new Color(236, 100, 64) : new Color(255, 65, 68);
            String name = entity.getDisplayName().getFormattedText();
            StringBuilder text = new StringBuilder("§7[§6" + Math.round(mc.thePlayer.getDistanceToEntity(entity)) + "§7] " +  "§c" + name);
            text.append(String.format(" §7[§r%s HP§7]", df.format(entity.getHealth() + entity.getAbsorptionAmount())));
            double fontScale = scale.getValue().doubleValue();
            float middle = x + ((right - x) / 2);
            float textWidth;
            double fontHeight = font.FONT_HEIGHT * fontScale;
            textWidth = font.getStringWidth(text.toString());
            middle -= (textWidth * fontScale) / 2f;
            glPushMatrix();
            glTranslated(middle, y - (fontHeight + 2), 0);
            glScaled(fontScale, fontScale, 1);
            glTranslated(-middle, -(y - (fontHeight + 2)), 0);


            Color backgroundTagColor = new Color(10, 10, 10, 130);

            RenderUtil.drawRect((int) (middle - 3), (int) (y - (fontHeight + 7)), (int) (textWidth + 6),
                    (int) ((fontHeight / fontScale) + 4), backgroundTagColor.getRGB());


            RenderUtil.resetColor();
            mc.fontRendererObj.drawStringWithShadow(text.toString(), (int) middle, (int) (y - (fontHeight + 4)), healthColor.getRGB());

            glPopMatrix();
        }

    }

}

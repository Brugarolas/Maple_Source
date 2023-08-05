package top.youm.maple.core.module.modules.visual;

import com.darkmagician6.eventapi.EventTarget;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.player.EntityPlayer;
import org.lwjgl.input.Keyboard;
import org.lwjgl.util.vector.Vector4f;
import top.youm.maple.common.events.Render2DEvent;
import top.youm.maple.common.events.Render3DEvent;
import top.youm.maple.common.settings.impl.BoolSetting;
import top.youm.maple.core.module.Module;
import top.youm.maple.core.module.ModuleCategory;
import top.youm.maple.utils.render.ESPUtil;
import top.youm.maple.utils.render.RenderUtil;

import java.awt.*;
import java.util.*;

/**
 * @author YouM
 * Created on 2023/7/22
 */
public class ESP extends Module {

    public static BoolSetting tags = new BoolSetting("Tags", true);
    public static BoolSetting armor = new BoolSetting("Armor Bar", true);
    public static BoolSetting health = new BoolSetting("HP Bar", true);

    public static BoolSetting box = new BoolSetting("Box", false);

    public static BoolSetting invisible = new BoolSetting("Invisible", false);
    public static BoolSetting player = new BoolSetting("Player", true);
    public static BoolSetting mob = new BoolSetting("Mob", false);

    private final Map<Entity, Vector4f> entityPosition = new HashMap<>();
    public ESP() {
        super("ESP", ModuleCategory.VISUAL, Keyboard.KEY_NONE);
        this.addSetting(tags,armor,health,invisible,player,mob,box);
    }
    @EventTarget
    public void onRender2D(Render2DEvent event){
        for (Entity entity : entityPosition.keySet()) {
            Vector4f pos = entityPosition.get(entity);
            float x = pos.getX(),
                    y = pos.getY(),
                    right = pos.getZ(),
                    bottom = pos.getW();

            if(box.getValue()){
                RenderUtil.drawBorder(x,y,right,bottom,.5f,new Color(255,255,255));
            }
        }

    }
    @EventTarget
    public void onRender3D(final Render3DEvent event) {
        entityPosition.clear();
        for (Entity entity : mc.theWorld.getLoadedEntityList()) {
            if(!(entity instanceof EntityLivingBase)){
                continue;
            }
            EntityLivingBase target = (EntityLivingBase) entity;
            if (shouldRender(target) && ESPUtil.isInView(target)) {
                entityPosition.put(target, ESPUtil.getEntityPositionsOn2D(target));
            }
        }
    }

    private boolean shouldRender(Entity entity) {
        if (entity.isDead) {
            return false;
        }
        if(!invisible.getValue()){
            return true;
        }
        if (player.getValue() && entity instanceof EntityPlayer) {
            if (entity == mc.thePlayer) {
                return mc.gameSettings.thirdPersonView != 0;
            }
            return !entity.getDisplayName().getUnformattedText().contains("[NPC");
        }

        return mob.getValue() && entity instanceof EntityMob;
    }

}

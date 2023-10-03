package top.youm.maple.core.module.modules.combat;

import com.darkmagician6.eventapi.EventTarget;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.monster.EntitySlime;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.play.server.S18PacketEntityTeleport;
import org.lwjgl.input.Keyboard;
import top.youm.maple.Maple;
import top.youm.maple.common.events.PacketReceiveEvent;
import top.youm.maple.common.settings.impl.CheckBoxSetting;
import top.youm.maple.core.module.Module;
import top.youm.maple.core.module.ModuleCategory;
import top.youm.maple.core.module.modules.combat.killaura.KillAura;
import top.youm.maple.core.module.modules.world.Teams;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * @author YouM
 * Created on 2023/9/29
 */
public class Targets extends Module {
    public static Targets INSTANCE;
    public CheckBoxSetting players = new CheckBoxSetting("Players", true);
    public CheckBoxSetting mobs = new CheckBoxSetting("Mobs", false);
    public CheckBoxSetting animals = new CheckBoxSetting("Animals", false);
    public CheckBoxSetting villager = new CheckBoxSetting("Villager", false);
    public CheckBoxSetting invisible = new CheckBoxSetting("Invisible", false);

    private final List<EntityLivingBase> targets = new ArrayList<>();

    public EntityLivingBase target;

    public Targets() {
        super("Targets", ModuleCategory.COMBAT, Keyboard.KEY_NONE);
        INSTANCE = this;
        this.addSettings(players,mobs,animals,villager,invisible);
    }
    public void sort() {
        clear();
        for (Entity entity : mc.theWorld.getLoadedEntityList()) {
            if (entity instanceof EntityLivingBase) {
                EntityLivingBase entLiving = (EntityLivingBase) entity;
                if (mc.thePlayer.getDistanceToEntity(entLiving) < KillAura.reach.getValue().floatValue() && entLiving != mc.thePlayer && !entLiving.isDead && Targets.INSTANCE.targetCanAttack(entLiving)) {
                    targets.add(entLiving);
                }
            }
        }
        targets.sort(Comparator.comparingDouble(mc.thePlayer::getDistanceToEntity));
    }
    public boolean targetCanAttack(EntityLivingBase livingBase) {
        AntiBot antiBot = Maple.getInstance().getModuleManager().getModuleByClass(AntiBot.class);
        if (livingBase == null) {
            return false;
        }
        if (Teams.isInTeam(livingBase)) return false;
        if (livingBase instanceof EntityPlayer && players.getValue() && !livingBase.isInvisible() && !antiBot.isNPC(livingBase)) {
            return true;
        }
        if (livingBase instanceof EntityPlayer && invisible.getValue() && livingBase.isInvisible()) {
            return true;
        }
        if (mobs.getValue() && (livingBase instanceof EntityMob || livingBase instanceof EntitySlime)) {
            return true;
        }
        if (villager.getValue() && livingBase instanceof EntityVillager) {
            return true;
        }
        return livingBase instanceof EntityAnimal && animals.getValue();
    }
    public boolean removeTarget(){
        if(targets.isEmpty()) return true;
        if (targets.get(0).getHealth() <= 0 || targets.get(0).isDead || mc.thePlayer.isDead || mc.thePlayer.isSpectator()){
            targets.remove(0);
            target = null;
            return true;
        }
        return false;
    }
    @EventTarget
    public void onPacketReceive(PacketReceiveEvent event) {
        if (target != null) {
            if (event.getPacket() instanceof S18PacketEntityTeleport) {
                S18PacketEntityTeleport s18PacketEntityTeleport = (S18PacketEntityTeleport) event.getPacket();
                if (target.getEntityId() == s18PacketEntityTeleport.getEntityId()) {
                    if (s18PacketEntityTeleport.getX() == mc.thePlayer.posX && s18PacketEntityTeleport.getY() == mc.thePlayer.posY && s18PacketEntityTeleport.getZ() == mc.thePlayer.posZ) {
                        targets.remove(target);
                        target = null;
                    }
                }
            }
        }
    }
    public void clear(){
        targets.clear();
        target = null;
    }
    public void setTarget(){
        if(targets.isEmpty()){
            return;
        }
        this.target = targets.get(0);
    }
}

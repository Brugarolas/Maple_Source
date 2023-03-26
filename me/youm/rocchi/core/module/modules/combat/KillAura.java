package me.youm.rocchi.core.module.modules.combat;

import com.darkmagician6.eventapi.EventTarget;
import com.darkmagician6.eventapi.events.Event;
import me.youm.rocchi.common.events.MotionEvent;
import me.youm.rocchi.common.settings.BoolSetting;
import me.youm.rocchi.common.settings.ModeSetting;
import me.youm.rocchi.common.settings.NumberSetting;
import me.youm.rocchi.core.module.Module;
import me.youm.rocchi.core.module.ModuleCategory;
import net.minecraft.entity.Entity;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.MathHelper;
import org.lwjgl.input.Keyboard;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class KillAura extends Module {

    public final ModeSetting<Mode> mode = new ModeSetting<>("mode", Mode.values(), Mode.NCP);
    private final NumberSetting reach = new NumberSetting("reach", 3.2, 10.0, 1.0, 0.1);
    private final NumberSetting cps = new NumberSetting("cps", 8.0, 10.0, 1.0, 0.5);
    public final BoolSetting animal = new BoolSetting("animal", false);
    public final BoolSetting player = new BoolSetting("player", false);
    public final BoolSetting autoBlock = new BoolSetting("auto block", false);
    public final BoolSetting mobs = new BoolSetting("mobs", false);
    public Entity target;
    public List<Entity> targets;

    public KillAura() {
        super("KillAura", ModuleCategory.COMBAT, Keyboard.KEY_R);
    }

    @EventTarget
    public void onMotion(MotionEvent event) {
        this.setSuffixes(this.mode.getValue().name());
        if (this.mc.thePlayer.isDead || this.mc.thePlayer.isSpectator()) return;
        targets = this.loadTargets()
                .stream().sorted(
                        Comparator.comparingDouble(mc.thePlayer::getDistanceToEntity)
                ).collect(Collectors.toList());
        if (event.getState() == Event.State.PRE) {
            target = targets.get(0);
            float[] rotations = getRotationsToEnt(target);
            switch (mode.getValue()){
                case WatchDog:
                    break;
                case AAC:
                    break;
                case Matrix:
                    break;
                default:
                    break;
            }
            event.setYaw(rotations[0]);
            event.setPitch(rotations[1]);
        }
    }

    public List<Entity> loadTargets() {
        return this.mc.theWorld.loadedEntityList.stream().
                filter(
                        entity ->
                                this.mc.thePlayer.getDistanceToEntity(entity) <= this.reach.getValue().floatValue() &&
                                        this.qualifies(entity)
                ).collect(Collectors.toList());
    }

    public boolean qualifies(Entity entity) {
        if (entity == this.mc.thePlayer) return false;
        if (!entity.isEntityAlive() || entity.isDead) return false;
        if (this.animal.getValue() && entity instanceof EntityAnimal) return true;
        if (this.mobs.getValue() && entity instanceof EntityMob) return true;
        return this.player.getValue() && entity instanceof EntityPlayer;
    }

    @Override
    public void onEnable() {

    }

    @Override
    public void onDisable() {
        this.targets.clear();
        this.target = null;
    }

    public enum Mode {
        AAC, Matrix, NCP, WatchDog
    }

    private float[] getRotationsToEnt(Entity ent) {
        final double differenceX = ent.posX - mc.thePlayer.posX;
        final double differenceY = (ent.posY + ent.height) - (mc.thePlayer.posY + mc.thePlayer.height) - 0.5;
        final double differenceZ = ent.posZ - mc.thePlayer.posZ;
        final float rotationYaw = (float) (Math.atan2(differenceZ, differenceX) * 180.0D / Math.PI) - 90.0f;
        final float rotationPitch = (float) (Math.atan2(differenceY, mc.thePlayer.getDistanceToEntity(ent)) * 180.0D
                / Math.PI);
        final float finishedYaw = mc.thePlayer.rotationYaw
                + MathHelper.wrapAngleTo180_float(rotationYaw - mc.thePlayer.rotationYaw);
        final float finishedPitch = mc.thePlayer.rotationPitch
                + MathHelper.wrapAngleTo180_float(rotationPitch - mc.thePlayer.rotationPitch);
        return new float[]{finishedYaw, -MathHelper.clamp_float(finishedPitch, -90, 90)};
    }
}

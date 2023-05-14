package top.youm.rocchi.core.module.modules.combat;

import com.darkmagician6.eventapi.EventTarget;
import com.darkmagician6.eventapi.events.Event;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.network.play.client.C0APacketAnimation;
import net.minecraft.network.play.server.S18PacketEntityTeleport;
import net.minecraft.util.*;
import org.lwjgl.input.Keyboard;
import top.youm.rocchi.common.events.MotionEvent;
import top.youm.rocchi.common.events.PacketReceiveEvent;
import top.youm.rocchi.common.settings.BoolSetting;
import top.youm.rocchi.common.settings.ModeSetting;
import top.youm.rocchi.common.settings.NumberSetting;
import top.youm.rocchi.core.module.Module;
import top.youm.rocchi.core.module.ModuleCategory;
import top.youm.rocchi.core.module.modules.world.Teams;
import top.youm.rocchi.utils.TimerUtil;
import top.youm.rocchi.utils.math.MathUtil;
import top.youm.rocchi.utils.network.PacketUtil;
import net.minecraft.entity.Entity;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemSword;
import net.minecraft.network.play.client.C02PacketUseEntity;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import top.youm.rocchi.utils.player.RotationUtil;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class KillAura extends Module {
    private final NumberSetting reach = new NumberSetting("Reach", 4, 6, 1, 0.1);
    private final NumberSetting mincps = new NumberSetting("Min CPS", 12, 20, 1, 1);
    private final NumberSetting maxcps = new NumberSetting("Max CPS", 12, 20, 1, 1);
    private final ModeSetting<BlockMode> autoBlockMode = new ModeSetting<>("AutoBlock Mode", BlockMode.values(),BlockMode.WatchDog);
    private final BoolSetting autoblock = new BoolSetting("Autoblock", true);

    BoolSetting players = new BoolSetting("Players", true);
    BoolSetting mobs = new BoolSetting("Mobs", false);
    BoolSetting animals = new BoolSetting("Animals", false);
    BoolSetting invisibles = new BoolSetting("Invisibles",false);
    BoolSetting dynamic = new BoolSetting("Dynamic", false);
    BoolSetting resolver = new BoolSetting("Resolver", false);
    BoolSetting smooth = new BoolSetting("Smooth", false);

    BoolSetting ticks = new BoolSetting("Ticks", true);
    BoolSetting invisible = new BoolSetting("Invisible", false);
    BoolSetting nameTags = new BoolSetting("NameTags", false);
    BoolSetting packet = new BoolSetting("Packet", false);

    private final BoolSetting matrix = new BoolSetting("Matrix", false);
    public List<EntityLivingBase> targets = new ArrayList<>();
    public static EntityLivingBase target;
    public static boolean blocking;
    public static boolean attacking;

    private final TimerUtil timer = new TimerUtil();

    public KillAura() {
        super("KillAura", ModuleCategory.COMBAT, Keyboard.KEY_NONE);
        this.addSetting(reach, mincps, maxcps, autoBlockMode,autoblock,players,invisibles,mobs,animals,dynamic,resolver,smooth,ticks,packet,nameTags,invisible,autoblock,matrix);
    }
    @EventTarget
    public void onMotion(MotionEvent event){
        sortTargets();
        this.setSuffixes(autoBlockMode.getValue().name());
//        if (Tenacity.INSTANCE.isToggled(Scaffold.class) || mc.thePlayer.isDead || mc.thePlayer.isSpectator())
//            return;
        if (!targets.isEmpty()) {
            if (event.getState() == Event.State.PRE) {
                target = targets.get(0);
                float[] rotations = getRotationsToEnt(target);
                if (dynamic.getValue()) {
                    rotations[0] += MathUtil.getRandomInRange(-5, 5);
                    rotations[1] += MathUtil.getRandomInRange(-5, 5);
                }
                if (resolver.getValue()) {
                    if (target.posY < 0) {
                        rotations[1] = 1;
                    } else if (target.posY > 255) {
                        rotations[1] = 90;
                    }
                }

                if (smooth.getValue()) {
                    float sens = RotationUtil.getSensitivityMultiplier();

                    rotations[0] = RotationUtil.smoothRotation(mc.thePlayer.rotationYaw, rotations[0], 360);
                    rotations[1] = RotationUtil.smoothRotation(mc.thePlayer.rotationPitch, rotations[1], 90);

                    rotations[0] = Math.round(rotations[0] / sens) * sens;
                    rotations[1] = Math.round(rotations[1] / sens) * sens;

                }

                if (matrix.getValue()) {
                    rotations[0] = rotations[0] + MathUtil.getRandomFloat(1.98f, -1.98f);
                }
                event.setYaw(rotations[0]);
                event.setPitch(rotations[1]);
                RotationUtil.setRotations(rotations);
            }

            if (autoblock.getValue() && mc.thePlayer.getCurrentEquippedItem() != null && mc.thePlayer.getCurrentEquippedItem().getItem() instanceof ItemSword) {
                mc.playerController.syncCurrentPlayItem();
                blocking = true;
                switch (autoBlockMode.getValue()) {
                    case WatchDog:
                        if (event.getState() == Event.State.POST) {
                            if (mc.thePlayer.swingProgressInt == -1) {
                                PacketUtil.sendPacket(new C07PacketPlayerDigging(
                                        C07PacketPlayerDigging.Action.RELEASE_USE_ITEM, new BlockPos(-1, -1, -1), EnumFacing.DOWN));
                            } else if (mc.thePlayer.swingProgressInt == 0) {
                                PacketUtil.sendPacket(new C08PacketPlayerBlockPlacement(
                                        new BlockPos(-1, -1, -1), 255, mc.thePlayer.getHeldItem(), 0, 0, 0));
                            }
                        }
                        break;
                    case Interaction:
                        if (event.getState() == Event.State.POST) {
                            if (blocking) {
                                for (Entity current : targets) {
                                    mc.playerController.interactWithEntitySendPacket(mc.thePlayer, current);
                                }
                                mc.playerController.sendUseItem(mc.thePlayer, mc.theWorld, mc.thePlayer.getHeldItem());
                            } else {
                                PacketUtil.sendPacket(new C08PacketPlayerBlockPlacement(
                                        new BlockPos(-1, -1, -1), 255, mc.thePlayer.getHeldItem(), 0, 0, 0));
                                blocking = false;
                            }
                        }
                        break;
                    case AAC:
                        if (event.getState() == Event.State.POST) {
                            if (blocking) {
                                mc.playerController.sendUseItem(mc.thePlayer, mc.theWorld, mc.thePlayer.getHeldItem());
                            } else {
                                PacketUtil.sendPacket(new C08PacketPlayerBlockPlacement(
                                        new BlockPos(-1, -1, -1), 255, mc.thePlayer.getHeldItem(), 0, 0, 0));
                                blocking = false;
                            }
                        }
                        break;
                }
                PacketUtil.sendPacket(new C0APacketAnimation());
            }
            if (event.getState() == Event.State.PRE) {
                attacking = true;
                if (timer.hasTimeElapsed((1000 / MathUtil.getRandomInRange(mincps.getValue().intValue(), maxcps.getValue().intValue())), true)) {
                    mc.thePlayer.swingItem();
                    mc.thePlayer.sendQueue.addToSendQueue(new C02PacketUseEntity(target, C02PacketUseEntity.Action.ATTACK));
                }
            }
        }
        if (targets.isEmpty()) {
            attacking = false;
            blocking = false;
        }
    };

    @EventTarget
    public void onPacketReceive(PacketReceiveEvent event) {
        if (packet.getValue() && target != null) {
            if (event.getPacket() instanceof S18PacketEntityTeleport) {
                S18PacketEntityTeleport s18PacketEntityTeleport = (S18PacketEntityTeleport) event.getPacket();
                if (target.getEntityId() == s18PacketEntityTeleport.getEntityId()) {
                    if (s18PacketEntityTeleport.getX() == mc.thePlayer.posX && s18PacketEntityTeleport.getY() == mc.thePlayer.posY && s18PacketEntityTeleport.getZ() == mc.thePlayer.posZ) {
                        targets.remove(target);
                    }
                }
            }
        }
    };

    @Override
    public void onDisable() {
        targets.clear();
        blocking = false;
        attacking = false;
    }

    public void sortTargets() {
        targets.clear();
        for (Entity entity : mc.theWorld.getLoadedEntityList()) {
            if (entity instanceof EntityLivingBase) {
                EntityLivingBase entLiving = (EntityLivingBase) entity;
                if (mc.thePlayer.getDistanceToEntity(entLiving) < reach.getValue().floatValue() && entLiving != mc.thePlayer && !entLiving.isDead && isValid(entLiving)) {
                    targets.add(entLiving);
                }
            }
        }
        targets.sort(Comparator.comparingDouble(mc.thePlayer::getDistanceToEntity));
    }

    public boolean isValid(EntityLivingBase entLiving) {
        if (entLiving == null) return false;
        if (Teams.isInTeam(entLiving)) return false;
        if (entLiving instanceof EntityPlayer && players.getValue() && !entLiving.isInvisible()) {
            return true;
        }
        if (entLiving instanceof EntityPlayer && invisibles.getValue() && entLiving.isInvisible()) {
            return true;
        }
        if (entLiving instanceof EntityMob && mobs.getValue()) {
            return true;
        }
        if (ticks.getValue() && entLiving.ticksExisted < 100) {
            return false;
        }
        if (entLiving.getDisplayName() != null && entLiving instanceof EntityPlayer) {
            if (nameTags.getValue() && (entLiving.getDisplayName().getFormattedText().contains("§c") || entLiving.getDisplayName().getFormattedText().contains("§4"))) {
                return false;
            }
        }

        if (!entLiving.canEntityBeSeen(mc.thePlayer) && invisible.getValue()) {
            return false;
        }
        return entLiving instanceof EntityAnimal && animals.getValue();
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
    public enum BlockMode{
        WatchDog,Interaction,AAC
    }
}

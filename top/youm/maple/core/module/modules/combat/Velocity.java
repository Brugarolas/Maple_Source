package top.youm.maple.core.module.modules.combat;

import com.darkmagician6.eventapi.EventTarget;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C0FPacketConfirmTransaction;
import net.minecraft.network.play.server.S12PacketEntityVelocity;
import net.minecraft.network.play.server.S19PacketEntityStatus;
import net.minecraft.network.play.server.S27PacketExplosion;
import org.lwjgl.input.Keyboard;
import top.youm.maple.common.events.PacketReceiveEvent;
import top.youm.maple.common.events.PacketSendEvent;
import top.youm.maple.common.events.TickEvent;
import top.youm.maple.common.events.WorldEvent;
import top.youm.maple.common.settings.impl.BoolSetting;
import top.youm.maple.common.settings.impl.ModeSetting;
import top.youm.maple.common.settings.impl.NumberSetting;
import top.youm.maple.core.module.Module;
import top.youm.maple.core.module.ModuleCategory;
import top.youm.maple.utils.math.MathUtil;
import top.youm.maple.utils.player.MovementUtil;

public class Velocity extends Module {

    private final ModeSetting mode = new ModeSetting("Mode", "Matrix", "Matrix", "Tick", "Stack","Vulcan", "C0F Cancel");
    private final NumberSetting horizontal = new NumberSetting("Horizontal", 0, 100, 0, 1);
    private final NumberSetting vertical = new NumberSetting("Vertical", 0, 100, 0, 1);
    private final NumberSetting chance = new NumberSetting("Chance", 100, 100, 0, 1);
    private final BoolSetting onlyWhileMoving = new BoolSetting("Only while moving", false);
    private final BoolSetting staffCheck = new BoolSetting("Staff check", false);

    public final BoolSetting onSwing = new BoolSetting("Swing", false);
    public final BoolSetting onSprint = new BoolSetting("Sprint", false);
    private long lastDamageTimestamp, lastAlertTimestamp;
    private boolean cancel;
    private int stack;

    public Velocity() {
        super("Velocity", ModuleCategory.COMBAT, Keyboard.KEY_NONE);
        onSprint.addParent(mode,mode->mode.getValue().equals("Vulcan"));
        onSwing.addParent(mode,mode->mode.getValue().equals("Vulcan"));
        this.addSetting(mode, horizontal, vertical, chance, onlyWhileMoving, staffCheck);
    }

    @EventTarget
    public void onPacketSendEvent(PacketSendEvent event) {
        if (mode.getValue().equals("C0F Cancel")) {
            if (event.getPacket() instanceof C0FPacketConfirmTransaction && mc.thePlayer.hurtTime > 0) {
                event.setCancelled(true);
            }
        } else if (mode.getValue().equals("Vulcan")) {
            if (onSwing.getValue() || onSprint.getValue() && !mc.thePlayer.isSwingInProgress) return;

            if (event.getPacket() instanceof C0FPacketConfirmTransaction && mc.thePlayer.hurtTime > 0) {
                event.setCancelled(true);
            }
        }
    }

    @EventTarget
    public void onPacketReceiveEvent(PacketReceiveEvent e) {
        if ((onlyWhileMoving.getValue() && !MovementUtil.isMoving()) || (chance.getValue().intValue() != 100 && MathUtil.getRandomInRange(0, 100) > chance.getValue().intValue()))
            return;
        Packet<?> packet = e.getPacket();
        switch (mode.getValue()) {
            case "C0F Cancel":
                if (packet instanceof S12PacketEntityVelocity) {
                    S12PacketEntityVelocity s12 = (S12PacketEntityVelocity) e.getPacket();
                    if (mc.thePlayer != null && s12.getEntityID() == mc.thePlayer.getEntityId()) {
                        e.setCancelled(true);
                    }
                }
                if (packet instanceof S27PacketExplosion) {
                    e.setCancelled(true);
                }
                break;
            case "Stack":
                if (packet instanceof S12PacketEntityVelocity) {
                    S12PacketEntityVelocity s12 = (S12PacketEntityVelocity) packet;
                    cancel = !cancel;
                    if (cancel) {
                        e.setCancelled(true);
                    }
                }
                if (packet instanceof S27PacketExplosion) {
                    e.setCancelled(true);
                }
                break;
            case "Matrix":
                if (packet instanceof S12PacketEntityVelocity) {
                    S12PacketEntityVelocity s12 = (S12PacketEntityVelocity) e.getPacket();
                    if (mc.thePlayer != null && s12.getEntityID() == mc.thePlayer.getEntityId()) {
                        s12.motionX *= 5 / 100.0;
                        s12.motionZ *= 5 / 100.0;
                        s12.motionY *= 100 / 100.0;
                    }
                }
                break;
            case "Vulcan":
                vulcanVelocity(e);
        }
    }
    public void vulcanVelocity(PacketReceiveEvent event){
        if (mc.thePlayer.isSwingInProgress || mc.thePlayer.isSprinting() && !mc.thePlayer.isSwingInProgress) return;

        final Packet<?> p = event.getPacket();

        final double horizontal = this.horizontal.getValue().doubleValue();
        final double vertical = this.vertical.getValue().doubleValue();

        if (p instanceof S12PacketEntityVelocity) {
            final S12PacketEntityVelocity wrapper = (S12PacketEntityVelocity) p;

            if (wrapper.getEntityID() == mc.thePlayer.getEntityId()) {
                if (horizontal == 0 && vertical == 0) {
                    event.setCancelled(true);
                    return;
                }

                wrapper.motionX *= horizontal / 100;
                wrapper.motionY *= vertical / 100;
                wrapper.motionZ *= horizontal / 100;

                event.setPacket(wrapper);
            }
        }

        if (p instanceof S27PacketExplosion) {
            final S27PacketExplosion wrapper = (S27PacketExplosion) p;

            if (horizontal == 0 && vertical == 0) {
                event.setCancelled(true);
                return;
            }

            wrapper.posX *= horizontal / 100;
            wrapper.posY *= vertical / 100;
            wrapper.posZ *= horizontal / 100;

            event.setPacket(wrapper);
        }
    }

    @EventTarget
    public void onWorldEvent(WorldEvent event) {
        stack = 0;
    }

    private boolean cancel(PacketReceiveEvent e) {
        if (staffCheck.getValue() && System.currentTimeMillis() - lastDamageTimestamp > 500) {
            return true;
        }
        if (horizontal.getValue().doubleValue() == 0 && vertical.getValue().doubleValue() == 0) {
            e.setCancelled(true);
            return true;
        }
        return false;
    }
    @EventTarget
    public void onTick(TickEvent event){
        this.setSuffixes(mode.getValue());
    }
}

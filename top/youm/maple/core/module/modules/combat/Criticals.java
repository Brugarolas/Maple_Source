package top.youm.maple.core.module.modules.combat;


import com.darkmagician6.eventapi.EventTarget;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C18PacketSpectate;
import org.lwjgl.input.Keyboard;
import top.youm.maple.Maple;
import top.youm.maple.common.events.MotionEvent;
import top.youm.maple.common.settings.impl.ModeSetting;
import top.youm.maple.common.settings.impl.NumberSetting;
import top.youm.maple.core.module.Module;
import top.youm.maple.core.module.ModuleCategory;
import top.youm.maple.core.module.modules.movement.Step;
import top.youm.maple.utils.network.PacketUtil;

import java.util.UUID;

/**
 * @author YouM
 */
public final class Criticals extends Module {
    private final ModeSetting mode = new ModeSetting("Mode","Watchdog","Watchdog", "Packet", "Dev");
    private final NumberSetting delay = new NumberSetting("Delay", 1, 20, 0, 1);

    public Criticals() {
        super("Criticals", ModuleCategory.COMBAT, Keyboard.KEY_NONE);
        this.addSetting(mode, delay);
    }

    @EventTarget
    public void onMotionEvent(MotionEvent e) {
        this.setSuffixes(mode.getValue());
        switch (mode.getValue()) {
            case "Watchdog":
                if (KillAura.attacking && e.isOnGround() && !Maple.getInstance().getModuleManager().getModuleByClass(Step.class).isToggle()) {
                    if (KillAura.target != null && KillAura.target.hurtTime >= delay.getValue().intValue()) {
                        for (double offset : new double[]{0.06f, 0.01f}) {
                            mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY + offset + (Math.random() * 0.001), mc.thePlayer.posZ, false));
                        }
                    }
                }
                break;
            case "Packet":
                if (mc.objectMouseOver.entityHit != null && mc.thePlayer.onGround) {
                    if (mc.objectMouseOver.entityHit.hurtResistantTime > delay.getValue().intValue()) {
                        for (double offset : new double[]{0.006253453, 0.002253453, 0.001253453}) {
                            mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY + offset, mc.thePlayer.posZ, false));
                        }
                    }
                }
                break;
            default:
                if (mc.objectMouseOver.entityHit != null && mc.thePlayer.onGround) {
                    if (mc.objectMouseOver.entityHit.hurtResistantTime > delay.getValue().intValue()) {
                        for (double offset : new double[]{0.06253453, 0.02253453, 0.001253453, 0.0001135346}) {
                            mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY + offset, mc.thePlayer.posZ, false));
                            PacketUtil.sendPacketNoEvent(new C18PacketSpectate(UUID.randomUUID()));
                        }
                    }
                }
        }
    }
}

package top.youm.maple.core.module.modules.combat;

import com.darkmagician6.eventapi.EventTarget;
import net.minecraft.network.play.client.C0BPacketEntityAction;
import org.lwjgl.input.Keyboard;
import top.youm.maple.common.events.AttackEvent;
import top.youm.maple.core.module.Module;
import top.youm.maple.core.module.ModuleCategory;
import top.youm.maple.utils.network.PacketUtil;

public final class SuperKB extends Module {

    public SuperKB() {
        super("SuperKB", ModuleCategory.COMBAT, Keyboard.KEY_NONE);
    }

    @EventTarget
    public void onAttackEvent(AttackEvent event) {
        if (event.getTargetEntity() != null) {
            if (mc.thePlayer.isSprinting())
                PacketUtil.sendPacketNoEvent(new C0BPacketEntityAction(mc.thePlayer, C0BPacketEntityAction.Action.STOP_SPRINTING));

            PacketUtil.sendPacketNoEvent(new C0BPacketEntityAction(mc.thePlayer, C0BPacketEntityAction.Action.START_SPRINTING));
            PacketUtil.sendPacketNoEvent(new C0BPacketEntityAction(mc.thePlayer, C0BPacketEntityAction.Action.STOP_SPRINTING));
            PacketUtil.sendPacketNoEvent(new C0BPacketEntityAction(mc.thePlayer, C0BPacketEntityAction.Action.START_SPRINTING));
        }
    }
}
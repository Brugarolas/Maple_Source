package top.youm.maple.core.module.modules.world;

import com.darkmagician6.eventapi.EventTarget;
import net.minecraft.network.play.client.C0BPacketEntityAction;
import net.minecraft.network.play.client.C17PacketCustomPayload;
import org.lwjgl.input.Keyboard;
import top.youm.maple.common.events.PacketReceiveEvent;
import top.youm.maple.common.events.TickEvent;
import top.youm.maple.common.settings.impl.SelectButtonSetting;
import top.youm.maple.core.module.Module;
import top.youm.maple.core.module.ModuleCategory;

/**
 * @author YouM
 * Created on 2023/7/24
 */
public class Disabler extends Module {
    private SelectButtonSetting mode = new SelectButtonSetting("Mode","Vulcan","Vulcan","NCP","WatchDog");
    public Disabler() {
        super("Disabler", ModuleCategory.WORLD, Keyboard.KEY_NONE);
    }

    @EventTarget
    public void onTick(TickEvent event){
        this.setSuffixes(mode.getValue());
        if(mc.thePlayer == null) return;
        switch (mode.getValue()){
            case "Vulcan":
                mc.getNetHandler().addToSendQueue(new C0BPacketEntityAction(mc.thePlayer, C0BPacketEntityAction.Action.START_SPRINTING));
                mc.getNetHandler().addToSendQueue(new C0BPacketEntityAction(mc.thePlayer, C0BPacketEntityAction.Action.STOP_SPRINTING));
                break;
            case "NCP":
                break;
            default:

        }
    }
    @EventTarget
    public void onPacketReceive(PacketReceiveEvent event){
        switch (mode.getValue()){
            case "Vulcan":
                if (event.getPacket() instanceof C17PacketCustomPayload) {
                    event.setCancelled(true);
                }
                break;
            case "NCP":
                break;
            default:
        }

    }

}

package top.youm.maple.core.module.modules.player;

import com.darkmagician6.eventapi.EventTarget;
import com.darkmagician6.eventapi.events.Event;
import top.youm.maple.common.events.MotionEvent;
import top.youm.maple.common.events.TickEvent;
import top.youm.maple.common.settings.impl.SelectButtonSetting;
import top.youm.maple.core.module.Module;
import top.youm.maple.core.module.ModuleCategory;
import top.youm.maple.utils.network.PacketUtil;
import net.minecraft.network.play.client.C03PacketPlayer;
import org.lwjgl.input.Keyboard;

/**
 * @author YouM
 */
@SuppressWarnings("unused")
public final class NoFall extends Module {

    private final SelectButtonSetting mode = new SelectButtonSetting("Mode","Vanilla","Vanilla","Packet","Edit");
    @EventTarget
    public void onTick(TickEvent event){
        this.setSuffixes(mode.getValue());
    }
    @EventTarget
    public void onMotion(MotionEvent event) {
        if (event.getState() == Event.State.PRE) {
            if (mc.thePlayer.fallDistance > 3.0) {
                switch (mode.getValue()) {
                    case "Vanilla":
                        event.setOnGround(true);
                        break;
                    case "Packet":
                        PacketUtil.sendPacket(new C03PacketPlayer(true));
                        break;
                }
                mc.thePlayer.fallDistance = 0;
            }
        }
    }
    public NoFall() {
        super("No Fall", ModuleCategory.PLAYER, Keyboard.KEY_NONE);
        this.addSetting(mode);
    }

}

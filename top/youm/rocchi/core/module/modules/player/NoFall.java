package top.youm.rocchi.core.module.modules.player;

import com.darkmagician6.eventapi.EventTarget;
import com.darkmagician6.eventapi.events.Event;
import top.youm.rocchi.common.events.MotionEvent;
import top.youm.rocchi.common.settings.ModeSetting;
import top.youm.rocchi.core.module.Module;
import top.youm.rocchi.core.module.ModuleCategory;
import top.youm.rocchi.utils.network.PacketUtil;
import net.minecraft.network.play.client.C03PacketPlayer;
import org.lwjgl.input.Keyboard;

@SuppressWarnings("unused")
public final class NoFall extends Module {

    private final ModeSetting<Mode> mode = new ModeSetting<>("Mode",Mode.values(),Mode.Vanilla);
    @EventTarget
    public void onMotion(MotionEvent event) {
        if (event.getState() == Event.State.PRE) {
            if (mc.thePlayer.fallDistance > 3.0) {
                switch (mode.getValue()) {
                    case Vanilla:
                        event.setOnGround(true);
                        break;
                    case Packet:
                        PacketUtil.sendPacket(new C03PacketPlayer(true));
                        break;
                }
                mc.thePlayer.fallDistance = 0;
            }
        }
    };


    public NoFall() {
        super("NoFall", ModuleCategory.PLAYER, Keyboard.KEY_NONE);
        this.addSetting(mode);
    }

    public enum Mode{
        Vanilla,Packet,Edit
    }
}

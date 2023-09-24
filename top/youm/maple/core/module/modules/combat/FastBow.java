package top.youm.maple.core.module.modules.combat;

import com.darkmagician6.eventapi.EventTarget;
import net.minecraft.item.ItemBow;
import net.minecraft.network.play.client.C03PacketPlayer;
import org.lwjgl.input.Keyboard;
import top.youm.maple.common.events.MotionEvent;
import top.youm.maple.common.settings.impl.ModeSetting;
import top.youm.maple.core.module.Module;
import top.youm.maple.core.module.ModuleCategory;

/**
 * @author YouM
 */
public class FastBow extends Module {
    public static ModeSetting mode = new ModeSetting("Mode","NCP", "NCP","Vanilla");

    public FastBow(){
        super("Fast Bow", ModuleCategory.COMBAT, Keyboard.KEY_NONE);
        this.addSetting(mode);
    }

    @EventTarget
    public void onTick(MotionEvent event) {
        this.setSuffixes(mode.getValue());
        if (mc.thePlayer.getItemInUseDuration() >= 15 || mode.getValue().equals("Vanilla")) {
            if (mc.thePlayer.onGround && mc.thePlayer.getItemInUse().getItem() instanceof ItemBow) {
                for (int i = 0; i < (mode.getValue().equals("Vanilla") ? 20 : 8); ++i)
                    mc.getNetHandler().addToSendQueue(new C03PacketPlayer(mc.thePlayer.onGround));
                mc.playerController.onStoppedUsingItem(mc.thePlayer);
            }
        }
    }

}
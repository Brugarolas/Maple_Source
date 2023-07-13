package top.youm.rocchi.core.module.modules.combat;

import com.darkmagician6.eventapi.EventTarget;
import net.minecraft.item.ItemBow;
import net.minecraft.network.play.client.C03PacketPlayer;
import org.lwjgl.input.Keyboard;
import top.youm.rocchi.common.events.MotionEvent;
import top.youm.rocchi.common.settings.impl.ModeSetting;
import top.youm.rocchi.core.module.Module;
import top.youm.rocchi.core.module.ModuleCategory;

/**
 * @author YouM
 */
public class FastBow extends Module {
    public static ModeSetting<FastBowMode> mode = new ModeSetting("Mode",FastBowMode.values(),FastBowMode.NCP);

    public FastBow(){
        super("Fast-bow", ModuleCategory.COMBAT, Keyboard.KEY_NONE);
        this.addSetting(mode);
    }

    @EventTarget
    public void onTick(MotionEvent event) {
        if (this.mc.thePlayer.getItemInUseDuration() >= 15 || mode.getValue() == FastBowMode.Vanilla) {
            if (this.mc.thePlayer.onGround && this.mc.thePlayer.getItemInUse().getItem() instanceof ItemBow) {
                for (int i = 0; i < (mode.getValue() == FastBowMode.Vanilla ? 20 : 8); ++i)
                    this.mc.getNetHandler().addToSendQueue(new C03PacketPlayer(this.mc.thePlayer.onGround));
                this.mc.playerController.onStoppedUsingItem(this.mc.thePlayer);
            }
        }
    }
    public enum FastBowMode{
        NCP, Vanilla
    }
}
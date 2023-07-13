package top.youm.rocchi.core.module.modules.player;

import com.darkmagician6.eventapi.EventTarget;
import top.youm.rocchi.common.events.MotionEvent;
import top.youm.rocchi.core.module.Module;
import top.youm.rocchi.core.module.ModuleCategory;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import org.lwjgl.input.Keyboard;

/**
 * @author YouM
 */
public class NoSlow extends Module {
    public NoSlow() {
        super("NoSlow", ModuleCategory.MOVEMENT, Keyboard.KEY_NONE);
    }

    @EventTarget
    public void onMotion(MotionEvent motion){
        if(mc.thePlayer.isUsingItem()) {
            if(mc.thePlayer.ticksExisted % 3 == 0) {
                mc.thePlayer.sendQueue.addToSendQueue(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.RELEASE_USE_ITEM, BlockPos.ORIGIN, EnumFacing.DOWN));
            }else{
                mc.thePlayer.sendQueue.addToSendQueue(new C08PacketPlayerBlockPlacement(mc.thePlayer.getHeldItem()));
            }
        }
    }
}

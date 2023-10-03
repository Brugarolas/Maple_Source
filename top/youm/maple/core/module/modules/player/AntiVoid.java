package top.youm.maple.core.module.modules.player;

import com.darkmagician6.eventapi.EventTarget;
import net.minecraft.block.Block;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import org.lwjgl.input.Keyboard;
import top.youm.maple.common.events.MotionEvent;
import top.youm.maple.common.events.TPEvent;
import top.youm.maple.common.events.TickEvent;
import top.youm.maple.common.settings.impl.SelectButtonSetting;
import top.youm.maple.common.settings.impl.SliderSetting;
import top.youm.maple.core.module.Module;
import top.youm.maple.core.module.ModuleCategory;

/**
 * @author YouM
 * Created on 2023/8/16
 */
public class AntiVoid extends Module {
    private final SliderSetting distance = new SliderSetting("Distance", 1, 10, 0 , 1);
    private final SelectButtonSetting mode = new SelectButtonSetting("mode","Vulcan","Position","Vulcan");
    private boolean teleported;
    public AntiVoid() {
        super("AntiVoid", ModuleCategory.PLAYER, Keyboard.KEY_NONE);
        this.addSettings(distance);
    }
    @EventTarget
    public void onTick(TickEvent event){
        this.setSuffixes(mode.getValue());
    }
    @EventTarget
    public void onMotion(MotionEvent event){
        if (mode.getValue().equals("Vulcan")) {
            if (mc.thePlayer.fallDistance > distance.getValue().floatValue()) {
                event.setPosY(event.getPosY() - event.getPosY() % 0.015625);
                event.setOnGround(true);
                mc.thePlayer.motionY = -0.08D;
                mc.thePlayer.motionX = 0;
                mc.thePlayer.motionZ = 0;
            }

            if (teleported) {
                mc.thePlayer.motionX = 0;
                mc.thePlayer.motionZ = 0;
                teleported = false;
            }
        } else {
            if (mc.thePlayer.fallDistance > distance.getValue().doubleValue() && !isBlockUnder(mc.thePlayer.posY + mc.thePlayer.getEyeHeight())) {
                event.setPosY(event.getPosY() + mc.thePlayer.fallDistance);
            }
        }

    }
    @EventTarget
    public void onTP(TPEvent event){
        if (mode.getValue().equals("Vulcan") && mc.thePlayer.fallDistance > distance.getValue().floatValue()) {
            teleported = true;
        }

    }
    public boolean isBlockUnder(final double height) {
        return isBlockUnder(height, true);
    }

    public boolean isBlockUnder(final double height, final boolean boundingBox) {
        if (boundingBox) {
            for (int offset = 0; offset < height; offset += 2) {
                final AxisAlignedBB bb = mc.thePlayer.getEntityBoundingBox().offset(0, -offset, 0);

                if (!mc.theWorld.getCollidingBoundingBoxes(mc.thePlayer, bb).isEmpty()) {
                    return true;
                }
            }
        } else {
            for (int offset = 0; offset < height; offset++) {
                if (blockRelativeToPlayer(0, -offset, 0).isFullBlock()) {
                    return true;
                }
            }
        }
        return false;
    }
    public Block blockRelativeToPlayer(final double offsetX, final double offsetY, final double offsetZ) {
        return mc.theWorld.getBlockState(new BlockPos(mc.thePlayer).add(offsetX, offsetY, offsetZ)).getBlock();
    }
}

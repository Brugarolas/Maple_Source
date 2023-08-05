package top.youm.maple.core.module.modules.player;

import top.youm.maple.common.settings.impl.BoolSetting;
import top.youm.maple.core.module.Module;
import top.youm.maple.core.module.ModuleCategory;
import net.minecraft.item.ItemBlock;
import org.lwjgl.input.Keyboard;

/**
 * @author YouM
 */
public class SafeWalk extends Module {
    public SafeWalk() {
        super("Safe Walk", ModuleCategory.PLAYER, Keyboard.KEY_NONE);
    }
    private final BoolSetting blocksOnly = new BoolSetting("Blocks only", false);
    private boolean canSafeWalk() {
        if (!blocksOnly.getValue()) return true;
        return mc.thePlayer != null && mc.thePlayer.getCurrentEquippedItem() != null && mc.thePlayer.getCurrentEquippedItem().getItem() instanceof ItemBlock;
    }
    public boolean start(){
        return canSafeWalk();
    }

}

package me.youm.rocchi.core.module.modules.player;

import me.youm.rocchi.common.settings.BoolSetting;
import me.youm.rocchi.core.module.Module;
import me.youm.rocchi.core.module.ModuleCategory;
import net.minecraft.item.ItemBlock;
import org.lwjgl.input.Keyboard;

public class SafeWalk extends Module {
    public SafeWalk() {
        super("SafeWalk", ModuleCategory.PLAYER, Keyboard.KEY_T);
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

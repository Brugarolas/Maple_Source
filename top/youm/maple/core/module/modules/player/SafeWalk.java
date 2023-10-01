package top.youm.maple.core.module.modules.player;

import com.darkmagician6.eventapi.EventTarget;
import top.youm.maple.common.events.SafeWalkEvent;
import top.youm.maple.common.settings.impl.CheckBoxSetting;
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
        this.addSetting(blocksOnly);
    }
    private final CheckBoxSetting blocksOnly = new CheckBoxSetting("Blocks only", false);
    @EventTarget
    public void onSafeWalkEvent(SafeWalkEvent event) {
        event.setSafe(true);
    }
    private boolean canSafeWalk() {
        return mc.thePlayer != null && mc.thePlayer.getCurrentEquippedItem() != null && mc.thePlayer.getCurrentEquippedItem().getItem() instanceof ItemBlock;
    }

}

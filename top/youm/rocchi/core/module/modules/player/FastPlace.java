package top.youm.rocchi.core.module.modules.player;

import com.darkmagician6.eventapi.EventTarget;
import top.youm.rocchi.common.events.MotionEvent;
import top.youm.rocchi.common.settings.BoolSetting;
import top.youm.rocchi.common.settings.NumberSetting;
import top.youm.rocchi.core.module.Module;
import top.youm.rocchi.core.module.ModuleCategory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemEgg;
import net.minecraft.item.ItemSnowball;
import org.lwjgl.input.Keyboard;

public class FastPlace extends Module {
    private final BoolSetting blocks = new BoolSetting("Blocks", true);
    private final BoolSetting projectiles = new BoolSetting("Projectiles", true);
    private final NumberSetting ticks = new NumberSetting("Ticks", 0, 4, 0, 1);
    public FastPlace() {
        super("FastPlace", ModuleCategory.PLAYER, Keyboard.KEY_M);
        this.addSetting(ticks, blocks, projectiles);
    }
    @EventTarget
    public void onMotion(MotionEvent event){
        if (canFastPlace()) {
            mc.rightClickDelayTimer = Math.min(0, ticks.getValue().intValue());
        }
    }
    private boolean canFastPlace() {
        if (mc.thePlayer == null || mc.thePlayer.getCurrentEquippedItem() == null || mc.thePlayer.getCurrentEquippedItem().getItem() == null)
            return false;
        Item heldItem = mc.thePlayer.getCurrentEquippedItem().getItem();
        return (blocks.getValue() && heldItem instanceof ItemBlock) || (projectiles.getValue() && (heldItem instanceof ItemSnowball || heldItem instanceof ItemEgg));
    }

    @Override
    public void onDisable() {
        mc.rightClickDelayTimer = 4;
    }
}

package top.youm.rocchi.core.module.modules.player;

import com.darkmagician6.eventapi.EventTarget;
import com.darkmagician6.eventapi.events.Event;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.util.MovingObjectPosition;
import org.lwjgl.input.Keyboard;
import top.youm.rocchi.common.events.MotionEvent;
import top.youm.rocchi.common.settings.impl.BoolSetting;
import top.youm.rocchi.core.module.Module;
import top.youm.rocchi.core.module.ModuleCategory;
import top.youm.rocchi.core.module.modules.combat.KillAura;
import top.youm.rocchi.utils.player.InventoryUtil;

public class AutoTool extends Module {

    private final BoolSetting autoSword = new BoolSetting("AutoSword", true);

    public AutoTool() {
        super("AutoTool", ModuleCategory.PLAYER, Keyboard.KEY_NONE);
        this.addSetting(autoSword);
    }

    @EventTarget
    public void onMotion(MotionEvent e) {
        if (e.getState() == Event.State.PRE) {
            if (mc.objectMouseOver != null && mc.gameSettings.keyBindAttack.isKeyDown()) {
                MovingObjectPosition objectMouseOver = mc.objectMouseOver;
                if (objectMouseOver.entityHit != null) {
                    switchSword();
                } else if (objectMouseOver.getBlockPos() != null) {
                    Block block = mc.theWorld.getBlockState(objectMouseOver.getBlockPos()).getBlock();
                    updateItem(block);
                }
            } else if (KillAura.target != null) {
                switchSword();
            }
        }
    }

    private void updateItem(Block block) {
        float strength = 1.0F;
        int bestItem = -1;
        for (int i = 0; i < 9; i++) {
            ItemStack itemStack = mc.thePlayer.inventory.mainInventory[i];
            if (itemStack == null) {
                continue;
            }
            float strVsBlock = itemStack.getStrVsBlock(block);
            if (strVsBlock > strength) {
                strength = strVsBlock;
                bestItem = i;
            }
        }
        if (bestItem != -1) {
            mc.thePlayer.inventory.currentItem = bestItem;
        }
    }

    private void switchSword() {
        if (!autoSword.getValue()) return;
        float damage = 1;
        int bestItem = -1;
        for (int i = 0; i < 9; i++) {
            ItemStack is = mc.thePlayer.inventory.mainInventory[i];
            if (is != null && is.getItem() instanceof ItemSword && InventoryUtil.getSwordStrength(is) > damage) {
                damage = InventoryUtil.getSwordStrength(is);
                bestItem = i;
            }
        }
        if (bestItem != -1) {
            mc.thePlayer.inventory.currentItem = bestItem;
        }
    }

}

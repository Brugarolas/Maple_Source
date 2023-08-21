package top.youm.maple.core.module.modules.movement;

import com.darkmagician6.eventapi.EventTarget;
import com.darkmagician6.eventapi.events.Event;
import top.youm.maple.common.events.MotionEvent;
import top.youm.maple.common.settings.impl.ModeSetting;
import top.youm.maple.core.module.Module;
import top.youm.maple.core.module.ModuleCategory;
import top.youm.maple.utils.TimerUtil;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.client.settings.KeyBinding;
import org.lwjgl.input.Keyboard;

import java.util.Arrays;
import java.util.List;

/**
 * @author YouM
 */
@SuppressWarnings("unused")
public final class InventoryMove extends Module {
    private final TimerUtil delayTimer = new TimerUtil();
    private final ModeSetting mode = new ModeSetting("Mode","Vanilla","Vanilla","Delay");
    private static final List<KeyBinding> keys = Arrays.asList(
            mc.gameSettings.keyBindForward,
            mc.gameSettings.keyBindBack,
            mc.gameSettings.keyBindLeft,
            mc.gameSettings.keyBindRight,
            mc.gameSettings.keyBindJump
    );

    public static void updateStates() {
        if (mc.currentScreen != null) {
            keys.forEach(k -> KeyBinding.setKeyBindState(k.getKeyCode(),GameSettings.isKeyDown(k)));
        }
    }
    @EventTarget
    public void onMotion(MotionEvent event) {
        switch (mode.getValue()) {
            case "Vanilla":
                if (event.getState() == Event.State.PRE && mc.currentScreen instanceof GuiContainer) {
                    updateStates();
                }
                break;
            case "Delay":
                if (event.getState() == Event.State.PRE && mc.currentScreen instanceof GuiContainer) {
                    if (delayTimer.hasTimeElapsed(100)) {
                        updateStates();
                        delayTimer.reset();
                    }
                }
                break;
        }
    };


    public InventoryMove() {
        super("Inventory Move", ModuleCategory.MOVEMENT, Keyboard.KEY_NONE);
        this.addSetting(mode);
    }
}

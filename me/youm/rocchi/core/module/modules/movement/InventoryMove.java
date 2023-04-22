package me.youm.rocchi.core.module.modules.movement;

import com.darkmagician6.eventapi.EventTarget;
import com.darkmagician6.eventapi.events.Event;
import me.youm.rocchi.common.events.MotionEvent;
import me.youm.rocchi.common.settings.ModeSetting;
import me.youm.rocchi.core.module.Module;
import me.youm.rocchi.core.module.ModuleCategory;
import me.youm.rocchi.core.module.modules.player.NoFall;
import me.youm.rocchi.utils.TimerUtil;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.network.play.server.S2DPacketOpenWindow;
import net.minecraft.network.play.server.S2EPacketCloseWindow;
import org.lwjgl.input.Keyboard;

import java.util.Arrays;
import java.util.List;

@SuppressWarnings("unused")
public final class InventoryMove extends Module {
    private final TimerUtil delayTimer = new TimerUtil();
    private final ModeSetting<Mode> mode = new ModeSetting<>("Mode",Mode.values(),Mode.Vanilla);
    private final List<KeyBinding> keys = Arrays.asList(
            mc.gameSettings.keyBindForward,
            mc.gameSettings.keyBindBack,
            mc.gameSettings.keyBindLeft,
            mc.gameSettings.keyBindRight,
            mc.gameSettings.keyBindJump
    );

    public void updateStates() {
        if (mc.currentScreen != null) {
            keys.forEach(k -> KeyBinding.setKeyBindState(k.getKeyCode(),GameSettings.isKeyDown(k)));
        }
    }
    @EventTarget
    public void onMotion(MotionEvent event) {
        switch (mode.getValue()) {
            case Vanilla:
                if (event.getState() == Event.State.PRE && mc.currentScreen instanceof GuiContainer) {
                    updateStates();
                }
                break;
            case Delay:
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
        super("InventoryMove", ModuleCategory.MOVEMENT, Keyboard.KEY_NONE);
        this.addSetting(mode);
    }
    public enum Mode{
        Vanilla,Delay
    }
}

package me.youm.rocchi.core.module.modules.player;

import com.darkmagician6.eventapi.EventTarget;
import me.youm.rocchi.common.events.MotionEvent;
import me.youm.rocchi.common.settings.NumberSetting;
import me.youm.rocchi.core.module.Module;
import me.youm.rocchi.core.module.ModuleCategory;
import org.lwjgl.input.Keyboard;

public final class Timer extends Module {

    private final NumberSetting amount = new NumberSetting("Amount", 1, 10, 0.1, 0.1);
    @EventTarget
    public void onMotion(MotionEvent motionEvent) {
        this.mc.timer.timerSpeed = amount.getValue().floatValue();
    }

    public Timer() {
        super("Timer", ModuleCategory.PLAYER, Keyboard.KEY_B);
        this.addSetting(amount);
    }

    @Override
    public void onDisable() {
        mc.timer.timerSpeed = 1.0F;
    }

}
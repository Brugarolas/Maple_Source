package top.youm.maple.core.module.modules.combat;


import com.darkmagician6.eventapi.EventTarget;
import org.lwjgl.input.Keyboard;
import top.youm.maple.common.events.MotionEvent;
import top.youm.maple.common.events.TickEvent;
import top.youm.maple.common.settings.impl.SelectButtonSetting;
import top.youm.maple.common.settings.impl.SliderSetting;
import top.youm.maple.core.module.Module;
import top.youm.maple.core.module.ModuleCategory;

/**
 * @author YouM
 */
public final class Criticals extends Module {
    private final SelectButtonSetting mode = new SelectButtonSetting("Mode","Watchdog","Watchdog", "Packet", "Dev","NoGround");
    private final SliderSetting delay = new SliderSetting("Delay", 1, 20, 0, 1);

    public Criticals() {
        super("Criticals", ModuleCategory.COMBAT, Keyboard.KEY_NONE);
        this.addSetting(mode, delay);
    }
    @EventTarget
    public void onTick(TickEvent event){
        this.setSuffixes(mode.getValue());
    }
    @EventTarget
    public void onMotionEvent(MotionEvent e) {
    }
}

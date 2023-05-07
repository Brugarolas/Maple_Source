package top.youm.rocchi.core.module.modules.visual;

import com.darkmagician6.eventapi.EventTarget;
import top.youm.rocchi.common.events.MotionEvent;
import top.youm.rocchi.common.settings.ModeSetting;
import top.youm.rocchi.core.module.Module;
import top.youm.rocchi.core.module.ModuleCategory;
import org.lwjgl.input.Keyboard;

public class Animations extends Module {
    public ModeSetting<Mode> mode = new ModeSetting<>("mode",Mode.values(),Mode.Exhibition);
    public Animations() {
        super("Animations", ModuleCategory.VISUAL, Keyboard.KEY_N);
        this.addSetting(mode);
    }
    @EventTarget
    public void onMotion(MotionEvent event){
        this.setSuffixes(this.mode.getValue().name());
    }
    public enum Mode{
        OLD,SIGMA,Smooth,ETB,Exhibition,Slide,Position
    }

}

package top.youm.maple.core.module.modules.visual;

import com.darkmagician6.eventapi.EventTarget;
import top.youm.maple.common.events.MotionEvent;
import top.youm.maple.common.settings.impl.ModeSetting;
import top.youm.maple.core.module.Module;
import top.youm.maple.core.module.ModuleCategory;
import org.lwjgl.input.Keyboard;

public class Animations extends Module {
    public ModeSetting mode = new ModeSetting("Mode","OLD","OLD","SIGMA","Smooth","ETB","Exhibition","Slide","Position");
    public Animations() {
        super("Animations", ModuleCategory.VISUAL, Keyboard.KEY_NONE);
        this.addSetting(mode);
    }
    @EventTarget
    public void onMotion(MotionEvent event){
        this.setSuffixes(this.mode.getValue());
    }

}

package top.youm.maple.core.module.modules.visual;

import com.darkmagician6.eventapi.EventTarget;
import top.youm.maple.common.events.TickEvent;
import top.youm.maple.common.settings.impl.SelectButtonSetting;
import top.youm.maple.core.module.Module;
import top.youm.maple.core.module.ModuleCategory;
import org.lwjgl.input.Keyboard;

public class Animations extends Module {
    public SelectButtonSetting mode = new SelectButtonSetting("Mode","OLD","OLD","SIGMA","Smooth","ETB","Exhibition","Slide","Position");
    public Animations() {
        super("Animations", ModuleCategory.VISUAL, Keyboard.KEY_NONE);
        this.addSettings(mode);
    }
    @EventTarget
    public void onTick(TickEvent event){
        this.setSuffixes(mode.getValue());
    }

}

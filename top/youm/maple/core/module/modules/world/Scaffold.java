package top.youm.maple.core.module.modules.world;

import com.darkmagician6.eventapi.EventTarget;
import org.lwjgl.input.Keyboard;
import top.youm.maple.common.events.MotionEvent;
import top.youm.maple.common.settings.impl.CheckBoxSetting;
import top.youm.maple.common.settings.impl.SelectButtonSetting;
import top.youm.maple.common.settings.impl.SliderSetting;
import top.youm.maple.core.module.Module;
import top.youm.maple.core.module.ModuleCategory;

/**
 * @author YouM
 * Created on 2023/10/2
 */
public class Scaffold extends Module {
    public CheckBoxSetting count = new CheckBoxSetting("Count",true);
    public SelectButtonSetting countStyle = new SelectButtonSetting("Count Style","Maple","Maple","Old");
    public SelectButtonSetting rotation = new SelectButtonSetting("Rotation","Telly","Telly","NCP","Legit");
    public SliderSetting tellyDelayTick = new SliderSetting("Delay Tick",10,100,1,1);
    public Scaffold() {
        super("Scaffold", ModuleCategory.WORLD, Keyboard.KEY_NONE);
    }
    @EventTarget
    public void onMotion(MotionEvent event){

    }
}

package top.youm.maple.core.module.modules.movement;

import com.darkmagician6.eventapi.EventTarget;
import org.omg.CORBA.INITIALIZE;
import top.youm.maple.common.events.MoveEvent;
import top.youm.maple.common.settings.impl.BoolSetting;
import top.youm.maple.core.module.Module;
import top.youm.maple.core.module.ModuleCategory;
import net.minecraft.client.settings.KeyBinding;
import org.lwjgl.input.Keyboard;

/**
 * @author YouM
 */
public class Sprint extends Module {
    public static Sprint INSTANCE;
    public Sprint() {
        super("Sprint", ModuleCategory.MOVEMENT, Keyboard.KEY_NONE);
        INSTANCE = this;
    }
    @EventTarget
    public void onMove(MoveEvent event){
        KeyBinding.setKeyBindState(this.mc.gameSettings.keyBindSprint.getKeyCode(),true);
    }
/*
    @Override
    public void onEnable() {

        if (mc.thePlayer != null) {
            KeyBinding.setKeyBindState(this.mc.gameSettings.keyBindSprint.getKeyCode(), true);
            mc.thePlayer.setSprinting(true);
        }
    }

    @Override
    public void onDisable() {

        if (mc.thePlayer != null) {
            KeyBinding.setKeyBindState(this.mc.gameSettings.keyBindSprint.getKeyCode(), false);
            mc.thePlayer.setSprinting(false);
        }
    }*/
}

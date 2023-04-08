package me.youm.rocchi.core.module.modules.movement;

import com.darkmagician6.eventapi.EventTarget;
import me.youm.rocchi.common.events.MoveEvent;
import me.youm.rocchi.common.settings.BoolSetting;
import me.youm.rocchi.core.module.Module;
import me.youm.rocchi.core.module.ModuleCategory;
import net.minecraft.client.settings.KeyBinding;
import org.lwjgl.input.Keyboard;


public class Sprint extends Module {
    public Sprint() {
        super("Sprint", ModuleCategory.MOVEMENT, Keyboard.KEY_T);
    }
    public BoolSetting safe = new BoolSetting("safe",true);
    @EventTarget
    public void onMove(MoveEvent event){
        if(safe.getValue()){
            KeyBinding.setKeyBindState(this.mc.gameSettings.keyBindSprint.getKeyCode(),true);
        }else {
            this.mc.thePlayer.setSprinting(true);
        }
    }
}

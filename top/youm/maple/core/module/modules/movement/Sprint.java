package top.youm.maple.core.module.modules.movement;

import com.darkmagician6.eventapi.EventTarget;
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
    public Sprint() {
        super("Sprint", ModuleCategory.MOVEMENT, Keyboard.KEY_NONE);
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

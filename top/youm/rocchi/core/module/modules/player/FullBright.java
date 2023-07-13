package top.youm.rocchi.core.module.modules.player;

import top.youm.rocchi.core.module.Module;
import top.youm.rocchi.core.module.ModuleCategory;
import org.lwjgl.input.Keyboard;

/**
 * @author YouM
 */
public class FullBright extends Module {

    public FullBright() {
        super("FullBright", ModuleCategory.PLAYER, Keyboard.KEY_NONE);
    }
    private float gamma;
    @Override
    public void onEnable() {
        this.gamma = this.mc.gameSettings.gammaSetting;
        this.mc.gameSettings.gammaSetting = 300;
    }
//    @EventTarget
//    public void onMotion(MotionEvent motion){
//        this.mc.gameSettings.gammaSetting = 300
//    }
    @Override
    public void onDisable() {
        this.mc.gameSettings.gammaSetting = gamma;
    }
}

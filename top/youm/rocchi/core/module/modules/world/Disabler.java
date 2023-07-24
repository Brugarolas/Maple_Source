package top.youm.rocchi.core.module.modules.world;

import org.lwjgl.input.Keyboard;
import top.youm.rocchi.common.settings.impl.BoolSetting;
import top.youm.rocchi.core.module.Module;
import top.youm.rocchi.core.module.ModuleCategory;

/**
 * @author YouM
 * Created on 2023/7/24
 */
public class Disabler extends Module {
    public static Disabler INSTANCE;
    public BoolSetting killaura = new BoolSetting("Killaura",true);
    public Disabler() {
        super("Disabler", ModuleCategory.WORLD, Keyboard.KEY_NONE);
        this.addSetting(killaura);
        INSTANCE = this;
    }
}

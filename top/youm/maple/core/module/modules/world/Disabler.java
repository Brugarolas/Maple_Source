package top.youm.maple.core.module.modules.world;

import org.lwjgl.input.Keyboard;
import top.youm.maple.common.settings.impl.BoolSetting;
import top.youm.maple.core.module.Module;
import top.youm.maple.core.module.ModuleCategory;

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

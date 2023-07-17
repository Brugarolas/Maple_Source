package top.youm.rocchi.core.module.modules.world;

import org.lwjgl.input.Keyboard;
import top.youm.rocchi.core.module.Module;
import top.youm.rocchi.core.module.ModuleCategory;

/**
 * @author YouM
 * Created on 2023/7/14
 */
public class God extends Module {
    public God() {
        super("God", ModuleCategory.WORLD, Keyboard.KEY_NONE);
    }
}

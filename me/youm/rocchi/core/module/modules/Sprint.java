package me.youm.rocchi.core.module.modules;

import me.youm.rocchi.core.module.Module;
import me.youm.rocchi.core.module.ModuleCategory;
import org.lwjgl.input.Keyboard;

public class Sprint extends Module {
    public Sprint() {
        super("Sprint", ModuleCategory.MOVEMENT, Keyboard.KEY_NONE);
    }

}

package top.youm.maple.core.module.modules.visual;

import org.lwjgl.input.Keyboard;
import top.youm.maple.core.module.Module;
import top.youm.maple.core.module.ModuleCategory;

/**
 * @author YouM
 * Created on 2023/7/22
 */
public class ESP extends Module {

    public ESP() {
        super("ESP", ModuleCategory.VISUAL, Keyboard.KEY_NONE);
    }

}

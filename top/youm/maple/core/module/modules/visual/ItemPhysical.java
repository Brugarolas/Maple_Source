package top.youm.maple.core.module.modules.visual;

import org.lwjgl.input.Keyboard;
import top.youm.maple.core.module.Module;
import top.youm.maple.core.module.ModuleCategory;

/**
 * @author YouM
 * Created on 2023/8/27
 */
public class ItemPhysical extends Module {
    public ItemPhysical() {
        super("Item Physical", ModuleCategory.VISUAL, Keyboard.KEY_NONE);
        this.isRenderModule = true;
    }
}

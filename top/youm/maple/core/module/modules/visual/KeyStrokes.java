package top.youm.maple.core.module.modules.visual;

import org.lwjgl.input.Keyboard;
import top.youm.maple.core.module.Module;
import top.youm.maple.core.module.ModuleCategory;

/**
 * @author YouM
 */
public class KeyStrokes extends Module {
    public KeyStrokes() {
        super("Key Strokes", ModuleCategory.VISUAL, Keyboard.KEY_NONE);
    }

}

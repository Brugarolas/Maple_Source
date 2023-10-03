package top.youm.maple.core.module.modules.world;

import org.lwjgl.input.Keyboard;
import top.youm.maple.core.module.Module;
import top.youm.maple.core.module.ModuleCategory;

/**
 * @author YouM
 * Created on 2023/10/2
 */
public class HitEffect extends Module {

    public HitEffect() {
        super("HitEffect", ModuleCategory.WORLD, Keyboard.KEY_NONE);
    }

    
}

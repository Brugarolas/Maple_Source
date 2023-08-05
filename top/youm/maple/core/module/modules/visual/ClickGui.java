package top.youm.maple.core.module.modules.visual;

import top.youm.maple.core.module.Module;
import top.youm.maple.core.module.ModuleCategory;
import top.youm.maple.core.ui.clickgui.classic.ClassicClickGUI;
import org.lwjgl.input.Keyboard;

/**
 * @author YouM
 */
public class ClickGui extends Module {
    public ClickGui() {
        super("ClickGui", ModuleCategory.VISUAL, Keyboard.KEY_RSHIFT);
        this.setToggle(false);
    }
    @Override
    public void onEnable(){
        this.mc.displayGuiScreen(new ClassicClickGUI());
    }
    @Override
    public void onDisable() {
        this.mc.displayGuiScreen(null);
    }
}

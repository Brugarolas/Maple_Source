package top.youm.rocchi.core.module.modules.visual;

import top.youm.rocchi.core.module.Module;
import top.youm.rocchi.core.module.ModuleCategory;
import top.youm.rocchi.core.ui.clickgui.modern.ModernClickGUI;
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
        this.mc.displayGuiScreen(new ModernClickGUI());
    }
    @Override
    public void onDisable() {
        this.mc.displayGuiScreen(null);
    }
}

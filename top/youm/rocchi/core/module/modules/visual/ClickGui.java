package top.youm.rocchi.core.module.modules.visual;

import top.youm.rocchi.core.module.Module;
import top.youm.rocchi.core.module.ModuleCategory;
import top.youm.rocchi.core.ui.clickgui.modern.Screen;
import top.youm.rocchi.core.ui.clickgui.old.ClickGuiScreen;
import org.lwjgl.input.Keyboard;

public class ClickGui extends Module {
    public ClickGui() {
        super("ClickGui", ModuleCategory.VISUAL, Keyboard.KEY_RSHIFT);
    }
    private final ClickGuiScreen clickGui = new ClickGuiScreen();
    public final Screen screen = new Screen();
    @Override
    public void onEnable(){
        this.mc.displayGuiScreen(screen);
    }

    @Override
    public void onDisable() {
        this.mc.displayGuiScreen(null);
    }
}

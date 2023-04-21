package me.youm.rocchi.core.module.modules.visual;

import me.youm.rocchi.core.module.Module;
import me.youm.rocchi.core.module.ModuleCategory;
import me.youm.rocchi.core.ui.clickgui.ClickGuiScreen;
import org.lwjgl.input.Keyboard;

public class ClickGui extends Module {
    public ClickGui() {
        super("ClickGui", ModuleCategory.VISUAL, Keyboard.KEY_RSHIFT);
    }
    private final ClickGuiScreen clickGui = new ClickGuiScreen();
    @Override
    public void onEnable(){
        this.mc.displayGuiScreen(clickGui);
    }

    @Override
    public void onDisable() {
        this.mc.displayGuiScreen(null);
    }
}

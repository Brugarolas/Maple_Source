package top.youm.rocchi.core.module.modules.visual;

import top.youm.rocchi.common.settings.ModeSetting;
import top.youm.rocchi.core.module.Module;
import top.youm.rocchi.core.module.ModuleCategory;
import top.youm.rocchi.core.ui.clickgui.modern.ModernClickGUI;
import top.youm.rocchi.core.ui.clickgui.old.ClickGuiScreen;
import org.lwjgl.input.Keyboard;

public class ClickGui extends Module {
    ModeSetting<ClickGUI> clickGUIModeSetting = new ModeSetting<>("Mode", ClickGUI.values(), ClickGUI.MODERN);
    public ClickGui() {
        super("ClickGui", ModuleCategory.VISUAL, Keyboard.KEY_RSHIFT);
        this.addSetting(clickGUIModeSetting);
    }
    @Override
    public void onEnable(){
        switch (clickGUIModeSetting.getValue()){
            case MODERN:
                this.mc.displayGuiScreen(new ModernClickGUI());
                break;
            case OLD:
                this.mc.displayGuiScreen(new ClickGuiScreen());
                break;
            default:
                this.mc.displayGuiScreen(null);
        }
    }
    @Override
    public void onDisable() {
        this.mc.displayGuiScreen(null);
    }

    public enum ClickGUI{
        MODERN,OLD
    }
}

package top.youm.maple.core.module.modules.visual;

import com.darkmagician6.eventapi.EventTarget;
import top.youm.maple.common.events.TickEvent;
import top.youm.maple.common.settings.impl.ColorThemeSetting;
import top.youm.maple.common.settings.impl.ModeSetting;
import top.youm.maple.core.module.Module;
import top.youm.maple.core.module.ModuleCategory;
import top.youm.maple.core.ui.clickgui.classic.ClassicClickGUI;
import org.lwjgl.input.Keyboard;
import top.youm.maple.core.ui.clickgui.classic.theme.ColorTheme;
import top.youm.maple.core.ui.clickgui.classic.theme.Theme;
import top.youm.maple.core.ui.clickgui.common.MapleClickGUI;

import java.awt.*;
import java.util.HashMap;

/**
 * @author YouM
 */
public class ClickGui extends Module {
    public ModeSetting mode = new ModeSetting("mode","Classic","Classic","Common");
    public ColorThemeSetting colorTheme = new ColorThemeSetting("theme",Theme.themes);
    public ClickGui() {
        super("ClickGui", ModuleCategory.VISUAL, Keyboard.KEY_RSHIFT);
        this.setToggle(false);
        this.addSetting(colorTheme,mode);
        this.isRenderModule = true;
    }
    @Override
    public void onEnable(){
        if (mode.getValue().equals("Classic")) {
            mc.displayGuiScreen(new ClassicClickGUI());
        } else {
            mc.displayGuiScreen(new MapleClickGUI());
        }
    }
    @EventTarget
    public void onTick(TickEvent event){
        this.setSuffixes(mode.getValue());
    }

    @Override
    public void onDisable() {
        mc.displayGuiScreen(null);
    }
}

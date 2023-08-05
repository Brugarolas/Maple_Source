package top.youm.maple.core.module.modules.visual;

import com.darkmagician6.eventapi.EventTarget;
import top.youm.maple.common.events.Render2DEvent;
import top.youm.maple.common.settings.impl.BoolSetting;
import top.youm.maple.common.settings.impl.ModeSetting;
import top.youm.maple.common.settings.impl.NumberSetting;
import top.youm.maple.common.settings.Setting;
import top.youm.maple.core.module.Module;
import top.youm.maple.core.module.ModuleCategory;
import top.youm.maple.core.ui.hud.ModuleListUI;
import org.lwjgl.input.Keyboard;
import top.youm.maple.core.ui.theme.Theme;

/**
 * @author YouM
 */
public class ModuleList extends Module {
    public ModeSetting mode = new ModeSetting("mode","Fade","Static","Rainbow","Fade","Gradient");
    public BoolSetting font = new BoolSetting("ttf-font",false);
    public BoolSetting rect = new BoolSetting("rect",true);

    public BoolSetting border = new BoolSetting("border",false);
    public BoolSetting sidebar = new BoolSetting("sidebar",true);
    public ModeSetting borderMode = new ModeSetting("Mode","Border","Shadow","Border");
    public NumberSetting shadowStrength = new NumberSetting("Strength",10,30,1,1);
    public NumberSetting rectAlpha = new NumberSetting("alpha",100,255,0,1);
    public NumberSetting speed = new NumberSetting("speed",10,30,1,1);

    public NumberSetting red = new NumberSetting("Red Gradient",Theme.theme.getRed(),255,0,1);
    public NumberSetting green = new NumberSetting("Green Gradient",Theme.theme.getGreen(),255,0,1);
    public NumberSetting blue = new NumberSetting("Blue Gradient",Theme.theme.getBlue(),255,0,1);

    private final ModuleListUI moduleListUI = new ModuleListUI();
    public ModuleList() {
        super("Module List", ModuleCategory.VISUAL, Keyboard.KEY_NONE);
        borderMode.addParent(border, Setting::getValue);
        shadowStrength.addParent(border,Setting::getValue);
        red.addParent(mode,modeSetting -> modeSetting.getValue().equals("Gradient"));
        green.addParent(mode,modeSetting -> modeSetting.getValue().equals("Gradient"));
        blue.addParent(mode,modeSetting -> modeSetting.getValue().equals("Gradient"));

        this.addSetting(mode,font,rect,sidebar,border,borderMode,shadowStrength,rectAlpha,speed,red,green,blue);
    }
    @EventTarget
    public void onRender2D(Render2DEvent event){
        moduleListUI.draw();
    }
}

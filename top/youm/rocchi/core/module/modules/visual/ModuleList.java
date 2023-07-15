package top.youm.rocchi.core.module.modules.visual;

import com.darkmagician6.eventapi.EventTarget;
import top.youm.rocchi.common.events.Render2DEvent;
import top.youm.rocchi.common.settings.impl.BoolSetting;
import top.youm.rocchi.common.settings.impl.ModeSetting;
import top.youm.rocchi.common.settings.impl.NumberSetting;
import top.youm.rocchi.common.settings.Setting;
import top.youm.rocchi.core.module.Module;
import top.youm.rocchi.core.module.ModuleCategory;
import top.youm.rocchi.core.ui.hud.ModuleListUI;
import org.lwjgl.input.Keyboard;
import top.youm.rocchi.core.ui.theme.Theme;

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

    public NumberSetting red = new NumberSetting("red", Theme.theme.getRed(),255,0,1);
    public NumberSetting green = new NumberSetting("green",Theme.theme.getGreen(),255,0,1);
    public NumberSetting blue = new NumberSetting("blue",Theme.theme.getBlue(),255,0,1);

    public NumberSetting red2 = new NumberSetting("red2",Theme.theme.getRed(),255,0,1);
    public NumberSetting green2 = new NumberSetting("green2",Theme.theme.getGreen(),255,0,1);
    public NumberSetting blue2 = new NumberSetting("blue2",Theme.theme.getBlue(),255,0,1);

    private final ModuleListUI moduleListUI = new ModuleListUI();
    public ModuleList() {
        super("Module List", ModuleCategory.VISUAL, Keyboard.KEY_NONE);
        borderMode.addParent(border, Setting::getValue);
        shadowStrength.addParent(border,Setting::getValue);
        red.addParent(mode,modeSetting -> modeSetting.getValue().equals("Static") || modeSetting.getValue().equals("Fade") || modeSetting.getValue().equals("Gradient"));
        green.addParent(mode,modeSetting -> modeSetting.getValue().equals("Static") || modeSetting.getValue().equals("Fade") || modeSetting.getValue().equals("Gradient"));
        blue.addParent(mode,modeSetting -> modeSetting.getValue().equals("Static") || modeSetting.getValue().equals("Fade") || modeSetting.getValue().equals("Gradient"));
        red2.addParent(mode,modeSetting -> modeSetting.getValue().equals("Static") || modeSetting.getValue().equals("Gradient"));
        green2.addParent(mode,modeSetting -> modeSetting.getValue().equals("Static") || modeSetting.getValue().equals("Gradient"));
        blue2.addParent(mode,modeSetting -> modeSetting.getValue().equals("Static") || modeSetting.getValue().equals("Gradient"));


        this.addSetting(mode,font,rect,sidebar,border,borderMode,shadowStrength,rectAlpha,speed,red,green,blue,red2,green2,blue2);
    }
    @EventTarget
    public void onRender2D(Render2DEvent event){

        moduleListUI.draw();
    }
}

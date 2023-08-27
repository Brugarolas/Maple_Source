package top.youm.maple.core.module.modules.visual;


import top.youm.maple.common.settings.impl.BoolSetting;
import top.youm.maple.common.settings.impl.ModeSetting;
import top.youm.maple.common.settings.impl.NumberSetting;
import top.youm.maple.common.settings.Setting;
import top.youm.maple.core.module.Module;
import top.youm.maple.core.module.ModuleCategory;
import org.lwjgl.input.Keyboard;
import top.youm.maple.core.ui.clickgui.classic.theme.Theme;

/**
 * @author YouM
 */
public class ModuleList extends Module {
    public ModeSetting mode = new ModeSetting("Color Mode","Fade","Static","Rainbow","Fade","Gradient");
    public BoolSetting font = new BoolSetting("TTF-Font",false);
    public BoolSetting rect = new BoolSetting("Rect",true);

    public BoolSetting border = new BoolSetting("Border",false);
    public BoolSetting sidebar = new BoolSetting("sidebar",true);
    public BoolSetting noRender = new BoolSetting("noRender",false);
    public BoolSetting edge = new BoolSetting("edge",false);
    public ModeSetting borderMode = new ModeSetting("Mode","Border","Shadow","Border");
    public NumberSetting rectPadding = new NumberSetting("Padding",10,10,4,1);
    public NumberSetting shadowStrength = new NumberSetting("Strength",10,30,1,1);
    public NumberSetting rectAlpha = new NumberSetting("alpha",100,255,0,1);
    public NumberSetting speed = new NumberSetting("Speed",10,30,1,1);
    public NumberSetting red = new NumberSetting("Red Gradient",Theme.theme.getRed(),255,0,1);
    public NumberSetting green = new NumberSetting("Green Gradient",Theme.theme.getGreen(),255,0,1);
    public NumberSetting blue = new NumberSetting("Blue Gradient",Theme.theme.getBlue(),255,0,1);
    public ModuleList() {
        super("Module List", ModuleCategory.VISUAL, Keyboard.KEY_NONE);
        borderMode.addParent(border, Setting::getValue);
        shadowStrength.addParent(border,Setting::getValue);
        rectAlpha.addParent(rect,Setting::getValue);
        rectPadding.addParent(rect,Setting::getValue);
        red.addParent(mode,modeSetting -> modeSetting.getValue().equals("Gradient"));
        green.addParent(mode,modeSetting -> modeSetting.getValue().equals("Gradient"));
        blue.addParent(mode,modeSetting -> modeSetting.getValue().equals("Gradient"));
        this.addSetting(mode,font,noRender,rect,edge,rectPadding,sidebar,border,borderMode,shadowStrength,rectAlpha,speed,red,green,blue);
        this.isRenderModule = true;
    }
}

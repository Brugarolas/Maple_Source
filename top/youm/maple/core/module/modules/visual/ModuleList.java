package top.youm.maple.core.module.modules.visual;


import top.youm.maple.common.settings.impl.CheckBoxSetting;
import top.youm.maple.common.settings.impl.SelectButtonSetting;
import top.youm.maple.common.settings.impl.SliderSetting;
import top.youm.maple.common.settings.Setting;
import top.youm.maple.core.module.Module;
import top.youm.maple.core.module.ModuleCategory;
import org.lwjgl.input.Keyboard;
import top.youm.maple.core.ui.clickgui.classic.theme.Theme;

/**
 * @author YouM
 */
public class ModuleList extends Module {
    public SelectButtonSetting mode = new SelectButtonSetting("Color Mode","Fade","Static","Rainbow","Fade","Gradient");
    public CheckBoxSetting font = new CheckBoxSetting("TTF-Font",false);
    public CheckBoxSetting rect = new CheckBoxSetting("Rect",true);

    public CheckBoxSetting border = new CheckBoxSetting("Border",false);
    public CheckBoxSetting sidebar = new CheckBoxSetting("sidebar",true);
    public CheckBoxSetting noRender = new CheckBoxSetting("noRender",false);
    public CheckBoxSetting edge = new CheckBoxSetting("edge",false);
    public SelectButtonSetting borderMode = new SelectButtonSetting("Mode","Border","Shadow","Border","Glow");
    public SliderSetting rectPadding = new SliderSetting("Padding",10,10,4,1);
    public SliderSetting shadowStrength = new SliderSetting("Strength",10,30,1,1);
    public SliderSetting rectAlpha = new SliderSetting("alpha",100,255,0,1);
    public SliderSetting speed = new SliderSetting("Speed",10,30,1,1);
    public SliderSetting red = new SliderSetting("Red Gradient",Theme.theme.getRed(),255,0,1);
    public SliderSetting green = new SliderSetting("Green Gradient",Theme.theme.getGreen(),255,0,1);
    public SliderSetting blue = new SliderSetting("Blue Gradient",Theme.theme.getBlue(),255,0,1);
    public ModuleList() {
        super("Module List", ModuleCategory.VISUAL, Keyboard.KEY_NONE);
        borderMode.addParent(border, Setting::getValue);
        shadowStrength.addParent(border,Setting::getValue);
        rectAlpha.addParent(rect,Setting::getValue);
        rectPadding.addParent(rect,Setting::getValue);
        red.addParent(mode,modeSetting -> modeSetting.getValue().equals("Gradient"));
        green.addParent(mode,modeSetting -> modeSetting.getValue().equals("Gradient"));
        blue.addParent(mode,modeSetting -> modeSetting.getValue().equals("Gradient"));
        this.addSettings(mode,font,noRender,rect,edge,rectPadding,sidebar,border,borderMode,shadowStrength,rectAlpha,speed,red,green,blue);
        this.isRenderModule = true;
    }
}

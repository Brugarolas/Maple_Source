package top.youm.rocchi.core.module.modules.visual;

import com.darkmagician6.eventapi.EventTarget;
import top.youm.rocchi.common.events.Render2DEvent;
import top.youm.rocchi.common.settings.BoolSetting;
import top.youm.rocchi.common.settings.ModeSetting;
import top.youm.rocchi.common.settings.NumberSetting;
import top.youm.rocchi.core.module.Module;
import top.youm.rocchi.core.module.ModuleCategory;
import top.youm.rocchi.core.ui.hud.ModuleListUI;
import org.lwjgl.input.Keyboard;


public class ModuleList extends Module {
    public ModeSetting<Mode> mode = new ModeSetting<>("mode",Mode.values(),Mode.FADE);
    public BoolSetting font = new BoolSetting("ttf-font",false);
    public BoolSetting rect = new BoolSetting("rect",true);
    public BoolSetting shadow = new BoolSetting("shadow",false);
    public BoolSetting border = new BoolSetting("border",true);
    public NumberSetting rectAlpha = new NumberSetting("alpha",100,255,0,1);
    public NumberSetting speed = new NumberSetting("speed",10,100,0,1);
    private final ModuleListUI moduleListUI = new ModuleListUI();
    public ModuleList() {
        super("ModuleList", ModuleCategory.VISUAL, Keyboard.KEY_U);
        this.addSetting(mode,font,rect,shadow,border,rectAlpha,speed);
    }
    @EventTarget
    public void onRender2D(Render2DEvent event){
        moduleListUI.draw();
    }
    public enum Mode{
        RAINBOW,FADE
    }
}

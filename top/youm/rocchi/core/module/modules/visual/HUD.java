package top.youm.rocchi.core.module.modules.visual;

import com.darkmagician6.eventapi.EventTarget;
import top.youm.rocchi.Rocchi;
import top.youm.rocchi.common.events.Render2DEvent;
import top.youm.rocchi.common.settings.BoolSetting;
import top.youm.rocchi.core.module.Module;
import top.youm.rocchi.core.module.ModuleCategory;
import top.youm.rocchi.core.ui.TabUI;
import top.youm.rocchi.core.ui.Theme;
import top.youm.rocchi.core.ui.font.FontLoaders;
import org.lwjgl.input.Keyboard;

public class HUD extends Module {
    public BoolSetting ttf_font = new BoolSetting("ttf-font",false);
    public BoolSetting notification = new BoolSetting("notification",true);
    public TabUI tabUI = new TabUI();
    public HUD() {
        super("HUD", ModuleCategory.VISUAL, Keyboard.KEY_V);
        this.setToggle(true);
        this.addSetting(ttf_font);
    }
    @EventTarget
    public void onRender(Render2DEvent event){
        if (notification.getValue()){
        }
        if(this.ttf_font.getValue()){
            FontLoaders.robotoB32.drawStringWithShadow(Rocchi.getInstance().NAME,5,5, Theme.titleColor.getRGB());
        }else {
            this.mc.fontRendererObj.drawStringWithShadow(Rocchi.getInstance().NAME,5,5, Theme.titleColor.getRGB());
        }
    }
}

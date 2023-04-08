package me.youm.rocchi.core.module.modules.client;

import com.darkmagician6.eventapi.EventTarget;
import me.youm.rocchi.Rocchi;
import me.youm.rocchi.common.events.Render2DEvent;
import me.youm.rocchi.common.settings.BoolSetting;
import me.youm.rocchi.core.module.Module;
import me.youm.rocchi.core.module.ModuleCategory;
import me.youm.rocchi.core.ui.TabUI;
import me.youm.rocchi.core.ui.Theme;
import me.youm.rocchi.core.ui.font.FontLoaders;
import me.youm.rocchi.utils.render.RenderUtil;
import org.lwjgl.input.Keyboard;

import java.awt.*;

public class HUD extends Module {
    public BoolSetting ttf_font = new BoolSetting("ttf-font",false);
    public TabUI tabUI = new TabUI();
    public HUD() {
        super("HUD", ModuleCategory.CLIENT, Keyboard.KEY_V);
        this.setToggle(true);
        this.addSetting(ttf_font);
    }
    @EventTarget
    public void onRender(Render2DEvent event){
        if(this.ttf_font.getValue()){
            FontLoaders.comfortaaB18.drawStringWithShadow(Rocchi.getInstance().NAME,5,5, Theme.titleColor.getRGB());
        }else {
            this.mc.fontRendererObj.drawStringWithShadow(Rocchi.getInstance().NAME,5,5, Theme.titleColor.getRGB());
        }
    }
}

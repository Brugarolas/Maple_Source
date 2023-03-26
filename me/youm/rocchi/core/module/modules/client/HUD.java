package me.youm.rocchi.core.module.modules.client;

import com.darkmagician6.eventapi.EventTarget;
import me.youm.rocchi.Rocchi;
import me.youm.rocchi.common.events.Render2DEvent;
import me.youm.rocchi.common.settings.BoolSetting;
import me.youm.rocchi.core.module.Module;
import me.youm.rocchi.core.module.ModuleCategory;
import me.youm.rocchi.core.module.ModuleManager;
import me.youm.rocchi.core.ui.TabUI;
import me.youm.rocchi.core.ui.Theme;
import me.youm.rocchi.utils.render.RenderUtil;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import org.lwjgl.input.Keyboard;

import java.awt.*;

public class HUD extends Module {
    public BoolSetting ttf_font = new BoolSetting("ttf-font",false);
    TabUI tabUI = new TabUI();
    public HUD() {
        super("HUD", ModuleCategory.CLIENT, Keyboard.KEY_U);
        this.setToggle(true);
        this.addSetting(ttf_font);
    }
    @EventTarget
    public void onRender(Render2DEvent event){
        this.renderTitle();
        this.renderTabUI();
    }
    public void renderTitle(){
        GlStateManager.pushMatrix();
        GlStateManager.scale(1.2,1.2,1.2);
        this.mc.fontRendererObj.drawStringWithShadow(Rocchi.getInstance().NAME,5,5, Theme.titleColor.getRGB());
        GlStateManager.popMatrix();
    }
    public void renderTabUI(){
        tabUI.render();
    }

}

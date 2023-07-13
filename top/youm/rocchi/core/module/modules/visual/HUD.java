package top.youm.rocchi.core.module.modules.visual;

import com.darkmagician6.eventapi.EventTarget;
import net.minecraft.client.renderer.GlStateManager;
import top.youm.rocchi.Rocchi;
import top.youm.rocchi.common.events.Render2DEvent;
import top.youm.rocchi.common.settings.impl.BoolSetting;
import top.youm.rocchi.common.settings.impl.ColorSetting;
import top.youm.rocchi.core.module.Module;
import top.youm.rocchi.core.module.ModuleCategory;
import top.youm.rocchi.core.ui.theme.Theme;
import top.youm.rocchi.core.ui.font.FontLoaders;
import org.lwjgl.input.Keyboard;

import java.awt.*;

/**
 * @author YouM
 */
public class HUD extends Module {
    public BoolSetting ttf_font = new BoolSetting("ttf-font",false);
    public BoolSetting notification = new BoolSetting("notification",true);
    public ColorSetting colorSetting = new ColorSetting("theme color",new Color(188, 81, 188));

    public HUD() {
        super("HUD", ModuleCategory.VISUAL, Keyboard.KEY_NONE);
        this.setToggle(true);
        this.addSetting(ttf_font,notification,colorSetting);
    }
    @EventTarget
    public void onRender(Render2DEvent event){

        if(this.ttf_font.getValue()){

            FontLoaders.robotoR22.drawStringWithShadow(Rocchi.getInstance().NAME,5,5, Theme.theme.getRGB());

        }else {
            this.mc.fontRendererObj.drawStringWithShadow(Rocchi.getInstance().NAME,5,5, Theme.theme.getRGB());

        }
    }
}

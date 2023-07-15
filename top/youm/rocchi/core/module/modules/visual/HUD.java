package top.youm.rocchi.core.module.modules.visual;

import com.darkmagician6.eventapi.EventTarget;
import net.minecraft.client.renderer.GlStateManager;
import top.youm.rocchi.Rocchi;
import top.youm.rocchi.common.events.Render2DEvent;
import top.youm.rocchi.common.settings.impl.BoolSetting;
import top.youm.rocchi.common.settings.impl.ColorSetting;
import top.youm.rocchi.common.settings.impl.NumberSetting;
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
    public NumberSetting red = new NumberSetting("red",Theme.theme.getRed(),255,0,1);
    public NumberSetting green = new NumberSetting("green",Theme.theme.getGreen(),255,0,1);
    public NumberSetting blue = new NumberSetting("blue",Theme.theme.getBlue(),255,0,1);
    public HUD() {
        super("HUD", ModuleCategory.VISUAL, Keyboard.KEY_NONE);
        this.setToggle(true);
        this.addSetting(ttf_font,notification,red,green,blue);
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

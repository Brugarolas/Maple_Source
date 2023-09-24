package top.youm.maple.core.module.modules.visual;

import com.darkmagician6.eventapi.EventTarget;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import top.youm.maple.Maple;
import top.youm.maple.common.events.Render2DEvent;
import top.youm.maple.common.settings.impl.BoolSetting;
import top.youm.maple.common.settings.impl.ModeSetting;
import top.youm.maple.common.settings.impl.NumberSetting;
import top.youm.maple.core.module.Module;
import top.youm.maple.core.module.ModuleCategory;
import top.youm.maple.core.ui.clickgui.classic.theme.Theme;
import top.youm.maple.core.ui.font.FontLoaders;
import org.lwjgl.input.Keyboard;
import top.youm.maple.core.ui.hud.components.HUDComponent;
import top.youm.maple.core.ui.hud.components.KeyStrokesUI;
import top.youm.maple.core.ui.hud.components.ModuleListUI;
import top.youm.maple.core.ui.hud.components.StatisticsUI;
import top.youm.maple.core.ui.hud.components.noti.NotificationManager;
import top.youm.maple.utils.render.ColorUtil;
import top.youm.maple.utils.render.GradientUtil;
import top.youm.maple.utils.render.RenderUtil;

import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static org.lwjgl.opengl.GL11.GL_GREATER;

/**
 * @author YouM
 */
public class HUD extends Module {
    private final Map<Module, HUDComponent> ModuleUI = new HashMap<>();
    public static BoolSetting ttf_font = new BoolSetting("TTF-Font",false);
    public static ModeSetting mode = new ModeSetting("Mode","CSGO","CSGO","Text","Tenacity");
    public static BoolSetting notification = new BoolSetting("Notification",true);

    public static NumberSetting red = new NumberSetting("red",Theme.theme.getRed(),255,0,1);
    public static NumberSetting green = new NumberSetting("green",Theme.theme.getGreen(),255,0,1);
    public static NumberSetting blue = new NumberSetting("blue",Theme.theme.getBlue(),255,0,1);
    public HUD() {
        super("HUD", ModuleCategory.VISUAL, Keyboard.KEY_NONE);
        this.setToggle(true);
        this.addSetting(ttf_font,mode,notification,red,green,blue);
        this.isRenderModule = true;
    }
    public void initRenderModule(){
        this.ModuleUI.put(Maple.getInstance().getModuleManager().getModuleByClass(ModuleList.class),new ModuleListUI());
        this.ModuleUI.put(Maple.getInstance().getModuleManager().getModuleByClass(KeyStrokes.class),new KeyStrokesUI());
        this.ModuleUI.put(Maple.getInstance().getModuleManager().getModuleByClass(Statistics.class),new StatisticsUI());
    }
    @EventTarget
    public void onRender(Render2DEvent event){
        ScaledResolution sr = new ScaledResolution(mc);
        String date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        String text = " | " + Maple.getInstance().VERSION + " | " + mc.thePlayer.getName() + " | " + date;
        switch (mode.getValue()){
            case "CSGO":
                RenderUtil.drawFadeRect(2,3,FontLoaders.title.getStringWidth(Maple.getInstance().NAME + text) + 4,1,HUD.getHUDThemeColor(),5);

                if(ttf_font.getValue()){
                    RenderUtil.drawRect(2,4,FontLoaders.title.getStringWidth(Maple.getInstance().NAME + text) + 4,FontLoaders.title.getHeight() + 4,new Color(0,0,0,150));
                }else{
                    RenderUtil.drawRect(2,4,mc.fontRendererObj.getStringWidth(Maple.getInstance().NAME + text) + 4, mc.fontRendererObj.FONT_HEIGHT + 4,new Color(0,0,0,150));
                }
                if(ttf_font.getValue()){
                    FontLoaders.title.drawStringWithShadow(Maple.getInstance().NAME,5,5, HUD.getHUDThemeColor().getRGB());
                    FontLoaders.title.drawStringWithShadow(text,5 + FontLoaders.title.getStringWidth(Maple.getInstance().NAME),5, -1);
                }else {
                    mc.fontRendererObj.drawStringWithShadow(Maple.getInstance().NAME,5,5, HUD.getHUDThemeColor().getRGB());
                    mc.fontRendererObj.drawStringWithShadow(text,5 + mc.fontRendererObj.getStringWidth(Maple.getInstance().NAME),5, -1);
                }
                break;
            case "Text":
                if(ttf_font.getValue()){
                    FontLoaders.title.drawStringWithShadow(Maple.getInstance().NAME.substring(0,1),5,5, HUD.getHUDThemeColor().getRGB());
                    FontLoaders.title.drawStringWithShadow(Maple.getInstance().NAME.substring(1),5 + FontLoaders.title.getStringWidth(Maple.getInstance().NAME.substring(0,1)),5, -1);
                }else {
                    mc.fontRendererObj.drawStringWithShadow(Maple.getInstance().NAME.substring(0,1),5,5, HUD.getHUDThemeColor().getRGB());
                    mc.fontRendererObj.drawStringWithShadow(Maple.getInstance().NAME.substring(1),5 + mc.fontRendererObj.getStringWidth(Maple.getInstance().NAME.substring(0,1)),5, -1);
                }
                break;
            case "Tenacity":
                GradientUtil.applyGradientHorizontal(5, 5, FontLoaders.tenacity.getStringWidth(Maple.getInstance().NAME), 20, 1, HUD.getHUDThemeColor(), ColorUtil.staticFade(HUD.getHUDThemeColor()), () -> {
                    RenderUtil.setAlphaLimit(0);
                    FontLoaders.tenacity.drawStringWithShadow(Maple.getInstance().NAME, 5, 5, 0);
                });
                break;
        }


        NotificationManager.draw(sr.getScaledWidth(),sr.getScaledHeight() - (sr.getScaledHeight() / 10.0f));
        ModuleUI.forEach((module, hudComponent) -> {
            if(module.isToggle()){
                hudComponent.draw(module);
            }
        });
    }
    public static Color getHUDThemeColor(){
        return new Color(red.getValue().intValue(),green.getValue().intValue(),blue.getValue().intValue());
    }
}

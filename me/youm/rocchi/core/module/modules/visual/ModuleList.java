package me.youm.rocchi.core.module.modules.visual;

import com.darkmagician6.eventapi.EventTarget;
import com.mojang.realmsclient.gui.ChatFormatting;
import me.youm.rocchi.Rocchi;
import me.youm.rocchi.common.events.Render2DEvent;
import me.youm.rocchi.common.settings.BoolSetting;
import me.youm.rocchi.common.settings.NumberSetting;
import me.youm.rocchi.core.module.Module;
import me.youm.rocchi.core.module.ModuleCategory;
import me.youm.rocchi.core.ui.font.FontLoaders;
import me.youm.rocchi.utils.AnimationUtils;
import me.youm.rocchi.utils.TimerUtil;
import me.youm.rocchi.utils.render.ColorUtil;
import me.youm.rocchi.utils.render.RenderUtil;
import net.minecraft.client.gui.ScaledResolution;
import org.lwjgl.input.Keyboard;

import java.awt.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class ModuleList extends Module {
    public BoolSetting rainbow = new BoolSetting("rainbow",true);
    public BoolSetting font = new BoolSetting("ttf-font",false);
    public BoolSetting rect = new BoolSetting("rect",true);
    public BoolSetting shadow = new BoolSetting("shadow",false);
    public NumberSetting rectAlpha = new NumberSetting("alpha",100,255,0,1);
    public NumberSetting speed = new NumberSetting("speed",20,100,0,1);
    TimerUtil timer = new TimerUtil();
    public ModuleList() {
        super("ArrayList", ModuleCategory.VISUAL, Keyboard.KEY_U);
    }
    @EventTarget
    public void onRender2D(Render2DEvent event){
        List<Module> modules = Rocchi.getInstance().getModuleManager().modules;
        ScaledResolution sr = new ScaledResolution(this.mc);
        int width = sr.getScaledWidth();
        modules.sort(
                (module1,module2) ->
                        this.mc.fontRendererObj.getStringWidth(
                                module2.getSuffixes().isEmpty() ?
                                        module2.getName() :
                                        module2.getName() + " - " + module2.getSuffixes()
                        )
                                - this.mc.fontRendererObj.getStringWidth(
                                module1.getSuffixes().isEmpty() ?
                                        module1.getName() :
                                        module1.getName() + " - " + module1.getSuffixes()
                        )
        );
        int offsetY = 0;
        int index = 0;
        for (Module module : modules){
            String text = module.getName() + (module.getSuffixes().isEmpty() ? "" : " - " + module.getSuffixes());
            if (!module.isToggle()) {
                module.animX = AnimationUtils.animateF(width + 5, module.animX, 0.2f);
            }else {
                module.animX = AnimationUtils.animateF(width - this.mc.fontRendererObj.getStringWidth(text), module.animX, 0.2f);
                module.animY = AnimationUtils.animateF(offsetY,module.animY,0.3f);
                offsetY += 12;
            }
            if(this.rect.getValue())RenderUtil.drawRect((int) (module.animX - 5), (int) (2 + module.animY),this.mc.fontRendererObj.getStringWidth(text) + 3,this.mc.fontRendererObj.FONT_HEIGHT + 3,new Color(0,0,0,this.rectAlpha.getValue().intValue()));
            this.mc.fontRendererObj.drawStringWithShadow(text, module.animX - 3,  (4 + module.animY), rainbow.getValue() ? ColorUtil.rainbow(speed.getValue().intValue(),10 * index,1f, 0.8f,1f).getRGB() : new Color(220,220,220,255).getRGB());
            index++;
        }
    }
}

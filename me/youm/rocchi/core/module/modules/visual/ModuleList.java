package me.youm.rocchi.core.module.modules.visual;

import com.darkmagician6.eventapi.EventTarget;
import me.youm.rocchi.Rocchi;
import me.youm.rocchi.common.events.Render2DEvent;
import me.youm.rocchi.core.module.Module;
import me.youm.rocchi.core.module.ModuleCategory;
import net.minecraft.client.gui.ScaledResolution;
import org.lwjgl.input.Keyboard;

import java.awt.*;
import java.util.Comparator;
import java.util.List;

public class ModuleList extends Module {

    public ModuleList() {
        super("ArrayList", ModuleCategory.VISUAL, Keyboard.KEY_U);
        this.setToggle(true);
    }
    @EventTarget
    public void onRender2D(Render2DEvent event){
        List<Module> modules = Rocchi.getInstance().getModuleManager().modules;
        ScaledResolution sr = new ScaledResolution(this.mc);
        int width = sr.getScaledWidth() ,height = sr.getScaledHeight();
        modules.sort((module1,module2) -> this.mc.fontRendererObj.getStringWidth(module2.getName() + " - " + module2.getSuffixes()) - this.mc.fontRendererObj.getStringWidth(module1.getName() + " - " + module1.getSuffixes()));
        int offsetY = 0;
        for (Module module : modules){
            if(module.isToggle()){
                String text = module.getName() + (module.getSuffixes().equals("") ? "" : " - " + module.getSuffixes());
                this.mc.fontRendererObj.drawStringWithShadow(text,width - 3 - this.mc.fontRendererObj.getStringWidth(text),3 + offsetY, new Color(220,220,220,255).getRGB());
                offsetY += this.mc.fontRendererObj.FONT_HEIGHT + 2;
            }
        }
    }
}

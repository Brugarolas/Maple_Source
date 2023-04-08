package me.youm.rocchi.core.ui.hud;

import com.mojang.realmsclient.gui.ChatFormatting;
import me.youm.rocchi.Rocchi;
import me.youm.rocchi.core.module.Module;
import me.youm.rocchi.core.module.modules.visual.ModuleList;
import me.youm.rocchi.utils.AnimationUtils;
import me.youm.rocchi.utils.render.ColorUtil;
import me.youm.rocchi.utils.render.RenderUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class ModuleListUI implements HUDComponent {

    @Override
    public void draw(){
        ModuleList UIModule = Rocchi.getInstance().getModuleManager().getModuleByClass(ModuleList.class);
        ScaledResolution sr = new ScaledResolution(Minecraft.getMinecraft());
        int width = sr.getScaledWidth();
        List<Module> modules = new ArrayList<>();

        for(Module m : Rocchi.getInstance().getModuleManager().modules){
            if(!m.isToggle() && m.wasRemoved)
                continue;
            modules.add(m);
        }

        modules.sort(
                (module1,module2) ->
                        Minecraft.getMinecraft().fontRendererObj.getStringWidth(
                                module2.getSuffixes().isEmpty() ?
                                        module2.getName() :
                                        module2.getName() + " - " + module2.getSuffixes()
                        )
                                - Minecraft.getMinecraft().fontRendererObj.getStringWidth(
                                module1.getSuffixes().isEmpty() ?
                                        module1.getName() :
                                        module1.getName() + " - " + module1.getSuffixes()
                        )
        );
        int offsetY = 0;
        int index = 0;
        for (Module module : modules){
            String text = module.getName() + (module.getSuffixes().isEmpty() ? "" : ChatFormatting.BLUE + " - " + ChatFormatting.WHITE + module.getSuffixes());

            if(module.isToggle()){
                module.wasRemoved = false;
                module.animX = AnimationUtils.animateF(width - Minecraft.getMinecraft().fontRendererObj.getStringWidth(text), module.animX, 0.2f);
                module.animY = AnimationUtils.animateF(offsetY,module.animY,0.3f);
                offsetY += 12;
            }else {
                module.animX = AnimationUtils.animateF(width + 6, module.animX, 0.2f);

                if (module.animX >= width - 10) module.wasRemoved = true;
            }
            if(UIModule.rect.getValue()){
                RenderUtil.drawRect((int) (module.animX - 5), (int) (2 + module.animY),Minecraft.getMinecraft().fontRendererObj.getStringWidth(text) + 3,Minecraft.getMinecraft().fontRendererObj.FONT_HEIGHT + 3,new Color(0,0,0,UIModule.rectAlpha.getValue().intValue()));
            }
            switch (UIModule.mode.getValue()){
                case FADE:
                    if(UIModule.border.getValue() && module.isToggle()){
                        RenderUtil.drawRect((int) (module.animX - 5) - 1, (int) (2 + module.animY),1,Minecraft.getMinecraft().fontRendererObj.FONT_HEIGHT + 3, ColorUtil.fade(UIModule.speed.getValue().intValue(),70 * index,new Color(24, 165, 255), 1f));
                        RenderUtil.drawRect((int) (module.animX + Minecraft.getMinecraft().fontRendererObj.getStringWidth(text) - 3),(int) (2 + module.animY ),1,Minecraft.getMinecraft().fontRendererObj.FONT_HEIGHT + 3,ColorUtil.fade(UIModule.speed.getValue().intValue(),70 * index,new Color(24, 165, 255), 1f));
                        if (module != modules.get(0)) {
                            int differ = Minecraft.getMinecraft().fontRendererObj.getStringWidth(
                                    modules.get(index - 1).getName()
                                            + (modules.get(index - 1).getSuffixes().isEmpty() ?
                                            "" :
                                            ChatFormatting.BLUE + " - " + ChatFormatting.WHITE + modules.get(index - 1).getSuffixes())
                            ) -
                                    Minecraft.getMinecraft().fontRendererObj.getStringWidth(
                                            module.getName()
                                                    + (module.getSuffixes().isEmpty() ?
                                                    "" :
                                                    ChatFormatting.BLUE +" - " + ChatFormatting.WHITE + module.getSuffixes())
                                    );
                            RenderUtil.drawRect((int) (module.animX - 6) - differ,(int) (2 + module.animY),differ,1,ColorUtil.fade(UIModule.speed.getValue().intValue(),70 * index,new Color(24, 165, 255), 1f));
                            if(index == modules.size() - 1){
                                RenderUtil.drawRect((int) (module.animX - 6),(int) (2 + module.animY + Minecraft.getMinecraft().fontRendererObj.FONT_HEIGHT + 3),Minecraft.getMinecraft().fontRendererObj.getStringWidth(text) + 4,1,ColorUtil.fade(UIModule.speed.getValue().intValue(),70 * index,new Color(24, 165, 255), 1f));
                            }
                        }else {
                            RenderUtil.drawRect((int) (module.animX - 6),(int) (2 + module.animY),Minecraft.getMinecraft().fontRendererObj.getStringWidth(text) + 4,1,ColorUtil.fade(UIModule.speed.getValue().intValue(),70 * index,new Color(24, 165, 255), 1f));
                        }
                    }
                    Minecraft.getMinecraft().fontRendererObj.drawStringWithShadow(text, module.animX - 3,  (4 + module.animY),ColorUtil.fade(UIModule.speed.getValue().intValue(),70 * index,new Color(24, 165, 255), 1f).getRGB());
                    break;
                case RAINBOW:
                    if(UIModule.border.getValue() && module.isToggle()){
                        RenderUtil.drawRect((int) (module.animX - 5) - 1, (int) (2 + module.animY),1,Minecraft.getMinecraft().fontRendererObj.FONT_HEIGHT + 3,ColorUtil.rainbow(UIModule.speed.getValue().intValue(),10 * index,1f, 0.8f,1f).getRGB());
                        RenderUtil.drawRect((int) (module.animX + Minecraft.getMinecraft().fontRendererObj.getStringWidth(text) - 3),(int) (2 + module.animY ),1,Minecraft.getMinecraft().fontRendererObj.FONT_HEIGHT + 3,ColorUtil.rainbow(UIModule.speed.getValue().intValue(),10 * index,1f, 0.8f,1f).getRGB());
                        if (module != modules.get(0)) {
                            int differ = Minecraft.getMinecraft().fontRendererObj.getStringWidth(
                                    modules.get(index - 1).getName()
                                            + (modules.get(index - 1).getSuffixes().isEmpty() ?
                                            "" :
                                            ChatFormatting.BLUE + " - " + ChatFormatting.WHITE + modules.get(index - 1).getSuffixes())
                            ) -
                                    Minecraft.getMinecraft().fontRendererObj.getStringWidth(
                                            module.getName()
                                                    + (module.getSuffixes().isEmpty() ?
                                                    "" :
                                                    ChatFormatting.BLUE +" - " + ChatFormatting.WHITE + module.getSuffixes())
                                    );
                            RenderUtil.drawRect((int) (module.animX - 6) - differ,(int) (2 + module.animY),differ,1,ColorUtil.rainbow(UIModule.speed.getValue().intValue(),10 * index,1f, 0.8f,1f).getRGB());
                            if(index == modules.size() - 1){
                                RenderUtil.drawRect((int) (module.animX - 6),(int) (2 + module.animY + Minecraft.getMinecraft().fontRendererObj.FONT_HEIGHT + 3),Minecraft.getMinecraft().fontRendererObj.getStringWidth(text) + 4,1,ColorUtil.rainbow(UIModule.speed.getValue().intValue(),10 * index,1f, 0.8f,1f).getRGB());
                            }
                        }else {
                            RenderUtil.drawRect((int) (module.animX - 6),(int) (2 + module.animY),Minecraft.getMinecraft().fontRendererObj.getStringWidth(text) + 4,1,ColorUtil.rainbow(UIModule.speed.getValue().intValue(),10 * index,1f, 0.8f,1f).getRGB());
                        }
                    }
                    Minecraft.getMinecraft().fontRendererObj.drawStringWithShadow(text, module.animX - 3,  (4 + module.animY),ColorUtil.rainbow(UIModule.speed.getValue().intValue(),10 * index,1f, 0.8f,1f).getRGB());
                    break;
                default:
            }
            index++;
        }
    }
}

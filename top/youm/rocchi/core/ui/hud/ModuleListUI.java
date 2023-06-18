package top.youm.rocchi.core.ui.hud;

import com.mojang.realmsclient.gui.ChatFormatting;
import top.youm.rocchi.Rocchi;
import top.youm.rocchi.core.module.Module;
import top.youm.rocchi.core.module.modules.visual.ModuleList;
import top.youm.rocchi.core.ui.font.CFontRenderer;
import top.youm.rocchi.core.ui.font.FontLoaders;
import top.youm.rocchi.core.ui.theme.Theme;
import top.youm.rocchi.utils.AnimationUtils;
import top.youm.rocchi.utils.TimerUtil;
import top.youm.rocchi.utils.render.ColorUtil;
import top.youm.rocchi.utils.render.RenderUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class ModuleListUI implements HUDComponent {
    private final AnimationUtils animator = new AnimationUtils();
    @Override
    public void draw(){
        ModuleList UIModule = Rocchi.getInstance().getModuleManager().getModuleByClass(ModuleList.class);
        ScaledResolution sr = new ScaledResolution(Minecraft.getMinecraft());
        int width = sr.getScaledWidth();
        List<Module> modules = new ArrayList<>();
        CFontRenderer comfortaaR18 = FontLoaders.comfortaaB22;
        for(Module m : Rocchi.getInstance().getModuleManager().modules) {
            if (!m.isToggle() && m.wasRemoved)
                continue;
            modules.add(m);
        }
        boolean font = UIModule.font.getValue();
        modules.sort(
                (module1,module2) -> font ?
                        comfortaaR18.getStringWidth(
                                module2.getSuffixes().isEmpty() ?
                                        module2.getName() :
                                        module2.getName() + " - " + module2.getSuffixes()
                        )
                                - comfortaaR18.getStringWidth(
                                module1.getSuffixes().isEmpty() ?
                                        module1.getName() :
                                        module1.getName() + " - " + module1.getSuffixes()
                        )
    :
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
                module.animY = animator.animate(offsetY,module.animY,0.2f);
                module.animX = animator.animate(width - (font ? comfortaaR18.getStringWidth(text) : (font ? comfortaaR18.getStringWidth(text) : Minecraft.getMinecraft().fontRendererObj.getStringWidth(text))), module.animX, 0.08f);
                module.wasRemoved = false;
                offsetY += font ? comfortaaR18.getHeight() + 3 : 12;
            }else {
                module.animX = animator.animate(width + 6, module.animX, 0.08f);
                if (module.animX >= width) module.wasRemoved = true;
            }
            if(UIModule.rect.getValue()){
                RenderUtil.drawRect((int) (module.animX - 7), (int) (2 + module.animY), font ?  comfortaaR18.getStringWidth(text) + 5:Minecraft.getMinecraft().fontRendererObj.getStringWidth(text) + 5, font ? comfortaaR18.getHeight() + 3 : Minecraft.getMinecraft().fontRendererObj.FONT_HEIGHT + 3,new Color(0,0,0,UIModule.rectAlpha.getValue().intValue()));
            }
            switch (UIModule.mode.getValue()){

                case FADE:
                    if(UIModule.border.getValue() && module.isToggle()){
                        RenderUtil.drawRect((int) (module.animX - 5) - 3, (int) (2 + module.animY),1,(font ?  comfortaaR18.getHeight() + 3 : Minecraft.getMinecraft().fontRendererObj.FONT_HEIGHT + 3), ColorUtil.fade(UIModule.speed.getValue().intValue(),30 * index, Theme.theme, 1f));
                        RenderUtil.drawRect((int) (module.animX + (font ?comfortaaR18.getStringWidth(text) : Minecraft.getMinecraft().fontRendererObj.getStringWidth(text)) - 3),(int) (2 + module.animY ),1,(font ?  comfortaaR18.getHeight() + 3 : Minecraft.getMinecraft().fontRendererObj.FONT_HEIGHT + 3),ColorUtil.fade(UIModule.speed.getValue().intValue(),30 * index,Theme.theme, 1f));
                        if (module != modules.get(0)) {
                            int differ = font ?
                                    comfortaaR18.getStringWidth(
                                            modules.get(index - 1).getName()
                                                    + (modules.get(index - 1).getSuffixes().isEmpty() ?
                                                    "" :
                                                    ChatFormatting.BLUE + " - " + ChatFormatting.WHITE + modules.get(index - 1).getSuffixes())
                                    ) - comfortaaR18.getStringWidth(
                                            module.getName()
                                                    + (module.getSuffixes().isEmpty() ?
                                                    "" :
                                                    ChatFormatting.BLUE +" - " + ChatFormatting.WHITE + module.getSuffixes())
                                    ) :
                                    Minecraft.getMinecraft().fontRendererObj.getStringWidth(
                                            modules.get(index - 1).getName()
                                                    + (modules.get(index - 1).getSuffixes().isEmpty() ?
                                                    "" :
                                                    ChatFormatting.BLUE + " - " + ChatFormatting.WHITE + modules.get(index - 1).getSuffixes())
                                    ) - Minecraft.getMinecraft().fontRendererObj.getStringWidth(
                                            module.getName()
                                                    + (module.getSuffixes().isEmpty() ?
                                                    "" :
                                                    ChatFormatting.BLUE +" - " + ChatFormatting.WHITE + module.getSuffixes())
                                    );
                            RenderUtil.drawRect((int) (module.animX - 8) - differ,(int) (2 + module.animY),differ,1,ColorUtil.fade(UIModule.speed.getValue().intValue(),30 * index,Theme.theme, 1f));
                            if(index == modules.size() - 1){
                                RenderUtil.drawRect((int) (module.animX - 8),(int) (2 + module.animY + (font ?  comfortaaR18.getHeight() + 3 : Minecraft.getMinecraft().fontRendererObj.FONT_HEIGHT + 3)),(font ? comfortaaR18.getStringWidth(text) : Minecraft.getMinecraft().fontRendererObj.getStringWidth(text)) + 6,1,ColorUtil.fade(UIModule.speed.getValue().intValue(),30 * index,Theme.theme, 1f));
                            }
                        }else {
                            RenderUtil.drawRect((int) (module.animX - 8),(int) (2 + module.animY),(font ? comfortaaR18.getStringWidth(text) : Minecraft.getMinecraft().fontRendererObj.getStringWidth(text)) + 6,1,ColorUtil.fade(UIModule.speed.getValue().intValue(),30 * index,Theme.theme, 1f));
                        }
                    }
                    if(font){
                        comfortaaR18.drawStringWithShadow(text, module.animX - 5, (4 + module.animY), ColorUtil.fade(UIModule.speed.getValue().intValue(), 30 * index, Theme.theme, 1f).getRGB());
                    }else {
                        Minecraft.getMinecraft().fontRendererObj.drawStringWithShadow(text, module.animX - 5, (4 + module.animY), ColorUtil.fade(UIModule.speed.getValue().intValue(), 30 * index, Theme.theme, 1f).getRGB());
                    }
                    break;
                case RAINBOW:
                    if(UIModule.border.getValue() && module.isToggle()){
                        RenderUtil.drawRect((int) (module.animX - 5) - 3, (int) (2 + module.animY),1,(font ?  comfortaaR18.getHeight() + 3 : Minecraft.getMinecraft().fontRendererObj.FONT_HEIGHT + 3),ColorUtil.rainbow(UIModule.speed.getValue().intValue(),10 * index,1f, 0.8f,1f).getRGB());
                        RenderUtil.drawRect((int) (module.animX + (font ? comfortaaR18.getStringWidth(text) : Minecraft.getMinecraft().fontRendererObj.getStringWidth(text)) - 3),(int) (2 + module.animY ),1,(font ?  comfortaaR18.getHeight() + 3 : Minecraft.getMinecraft().fontRendererObj.FONT_HEIGHT + 3),ColorUtil.rainbow(UIModule.speed.getValue().intValue(),10 * index,1f, 0.8f,1f).getRGB());
                        if (module != modules.get(0)) {
                            int differ = font ?
                                    comfortaaR18.getStringWidth(
                                            modules.get(index - 1).getName()
                                                    + (modules.get(index - 1).getSuffixes().isEmpty() ?
                                                    "" :
                                                    ChatFormatting.BLUE + " - " + ChatFormatting.WHITE + modules.get(index - 1).getSuffixes())
                                    ) - comfortaaR18.getStringWidth(
                                            module.getName()
                                                    + (module.getSuffixes().isEmpty() ?
                                                    "" :
                                                    ChatFormatting.BLUE +" - " + ChatFormatting.WHITE + module.getSuffixes())
                                    ) :
                                    Minecraft.getMinecraft().fontRendererObj.getStringWidth(
                                            modules.get(index - 1).getName()
                                                    + (modules.get(index - 1).getSuffixes().isEmpty() ?
                                                    "" :
                                                    ChatFormatting.BLUE + " - " + ChatFormatting.WHITE + modules.get(index - 1).getSuffixes())
                                    ) - Minecraft.getMinecraft().fontRendererObj.getStringWidth(
                                            module.getName()
                                                    + (module.getSuffixes().isEmpty() ?
                                                    "" :
                                                    ChatFormatting.BLUE +" - " + ChatFormatting.WHITE + module.getSuffixes())
                                    );
                            RenderUtil.drawRect((int) (module.animX - 8) - differ,(int) (2 + module.animY),differ,1,ColorUtil.rainbow(UIModule.speed.getValue().intValue(),10 * index,1f, 0.8f,1f).getRGB());
                            if(index == modules.size() - 1){
                                RenderUtil.drawRect((int) (module.animX - 8),(int) (2 + module.animY + (font ?  comfortaaR18.getHeight() + 3 : Minecraft.getMinecraft().fontRendererObj.FONT_HEIGHT + 3)),(font ? comfortaaR18.getStringWidth(text) : Minecraft.getMinecraft().fontRendererObj.getStringWidth(text)) + 6,1,ColorUtil.rainbow(UIModule.speed.getValue().intValue(),10 * index,1f, 0.8f,1f).getRGB());
                            }
                        }else {
                            RenderUtil.drawRect((int) (module.animX - 8),(int) (2 + module.animY),(font ? comfortaaR18.getStringWidth(text) : Minecraft.getMinecraft().fontRendererObj.getStringWidth(text)) + 6,1,ColorUtil.rainbow(UIModule.speed.getValue().intValue(),10 * index,1f, 0.8f,1f).getRGB());
                        }
                    }
                    if(font){
                        comfortaaR18.drawStringWithShadow(text, module.animX - 5,  (4 + module.animY),ColorUtil.rainbow(UIModule.speed.getValue().intValue(),10 * index,1f, 0.8f,1f).getRGB());
                    }else {
                        Minecraft.getMinecraft().fontRendererObj.drawStringWithShadow(text, module.animX - 5,  (4 + module.animY),ColorUtil.rainbow(UIModule.speed.getValue().intValue(),10 * index,1f, 0.8f,1f).getRGB());
                    }
                    break;
                default:
                    Color textcolor = ColorUtil.interpolateColorsBackAndForth(UIModule.speed.getValue().intValue(),50*index,Theme.theme,Theme.theme2,true);

                    if(UIModule.border.getValue() && module.isToggle()){
                        RenderUtil.drawRect((int) (module.animX - 5) - 3, (int) (2 + module.animY),1,(font ?  comfortaaR18.getHeight() + 3 : Minecraft.getMinecraft().fontRendererObj.FONT_HEIGHT + 3),textcolor);
                        RenderUtil.drawRect((int) (module.animX + (font ? comfortaaR18.getStringWidth(text) : Minecraft.getMinecraft().fontRendererObj.getStringWidth(text)) - 3),(int) (2 + module.animY ),1,(font ?  comfortaaR18.getHeight() + 3 : Minecraft.getMinecraft().fontRendererObj.FONT_HEIGHT + 3),textcolor);
                        if (module != modules.get(0)) {
                            int differ = font ?
                                    comfortaaR18.getStringWidth(
                                            modules.get(index - 1).getName()
                                                    + (modules.get(index - 1).getSuffixes().isEmpty() ?
                                                    "" :
                                                    ChatFormatting.BLUE + " - " + ChatFormatting.WHITE + modules.get(index - 1).getSuffixes())
                                    ) - comfortaaR18.getStringWidth(
                                            module.getName()
                                                    + (module.getSuffixes().isEmpty() ?
                                                    "" :
                                                    ChatFormatting.BLUE +" - " + ChatFormatting.WHITE + module.getSuffixes())
                                    ) :
                                    Minecraft.getMinecraft().fontRendererObj.getStringWidth(
                                            modules.get(index - 1).getName()
                                                    + (modules.get(index - 1).getSuffixes().isEmpty() ?
                                                    "" :
                                                    ChatFormatting.BLUE + " - " + ChatFormatting.WHITE + modules.get(index - 1).getSuffixes())
                                    ) - Minecraft.getMinecraft().fontRendererObj.getStringWidth(
                                            module.getName()
                                                    + (module.getSuffixes().isEmpty() ?
                                                    "" :
                                                    ChatFormatting.BLUE +" - " + ChatFormatting.WHITE + module.getSuffixes())
                                    );
                            RenderUtil.drawRect((int) (module.animX - 8) - differ,(int) (2 + module.animY),differ,1,textcolor);
                            if(index == modules.size() - 1){
                                RenderUtil.drawRect((int) (module.animX - 8),(int) (2 + module.animY + (font ?  comfortaaR18.getHeight() + 3 : Minecraft.getMinecraft().fontRendererObj.FONT_HEIGHT + 3)),(font ? comfortaaR18.getStringWidth(text) : Minecraft.getMinecraft().fontRendererObj.getStringWidth(text)) + 6,1,textcolor);
                            }
                        }else {
                            RenderUtil.drawRect((int) (module.animX - 8),(int) (2 + module.animY),(font ? comfortaaR18.getStringWidth(text) : Minecraft.getMinecraft().fontRendererObj.getStringWidth(text)) + 6,1,textcolor);
                        }
                    }
                    if(font){
                        comfortaaR18.drawStringWithShadow(text, module.animX - 5,  (4 + module.animY),textcolor.getRGB());
                    }else {
                        Minecraft.getMinecraft().fontRendererObj.drawStringWithShadow(text, module.animX - 5,  (4 + module.animY),textcolor.getRGB());
                    }
            }
            index++;
        }
    }
}

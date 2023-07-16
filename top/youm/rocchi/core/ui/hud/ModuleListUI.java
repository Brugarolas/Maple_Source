package top.youm.rocchi.core.ui.hud;

import com.mojang.realmsclient.gui.ChatFormatting;
import net.minecraft.client.renderer.GlStateManager;
import top.youm.rocchi.Rocchi;
import top.youm.rocchi.core.module.Module;
import top.youm.rocchi.core.module.modules.visual.ModuleList;
import top.youm.rocchi.core.ui.font.CFontRenderer;
import top.youm.rocchi.core.ui.font.FontLoaders;
import top.youm.rocchi.core.ui.theme.Theme;
import top.youm.rocchi.utils.AnimationUtils;
import top.youm.rocchi.utils.render.ColorUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import top.youm.rocchi.utils.render.ShadowUtils;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static top.youm.rocchi.utils.render.RenderUtil.*;

/**
 * @author YouM
 */
public class ModuleListUI implements HUDComponent {
    private final AnimationUtils animator = new AnimationUtils();
    //default color
    private Color color = Theme.theme;

    //render modules list
    @Override
    public void draw() {
        ModuleList UIModule = Rocchi.getInstance().getModuleManager().getModuleByClass(ModuleList.class);

        ScaledResolution sr = new ScaledResolution(Minecraft.getMinecraft());

        int width = sr.getScaledWidth();

        List<Module> modules = new ArrayList<>();

        CFontRenderer fontRenderer = FontLoaders.rise20;

        for (Module m : Rocchi.getInstance().getModuleManager().modules) {
            if (!m.isToggle() && m.wasRemoved)
                continue;
            modules.add(m);
        }
        boolean font = UIModule.font.getValue();

        //sort module
        modules.sort(
                (module1, module2) -> font ?
                        fontRenderer.getStringWidth(
                                module2.getSuffixes().isEmpty() ?
                                        module2.getName() :
                                        module2.getName() + " " + module2.getSuffixes()
                        )
                                - fontRenderer.getStringWidth(
                                module1.getSuffixes().isEmpty() ?
                                        module1.getName() :
                                        module1.getName() + " " + module1.getSuffixes()
                        )
                        :
                        Minecraft.getMinecraft().fontRendererObj.getStringWidth(
                                module2.getSuffixes().isEmpty() ?
                                        module2.getName() :
                                        module2.getName() + " " + module2.getSuffixes()
                        )
                                - Minecraft.getMinecraft().fontRendererObj.getStringWidth(
                                module1.getSuffixes().isEmpty() ?
                                        module1.getName() :
                                        module1.getName() + " " + module1.getSuffixes()
                        )
        );
        int offsetY = 0;
        int index = 0;

        for (Module module : modules) {
            String text =
                    module.getName() + (
                            module.getSuffixes().isEmpty() ?
                                    "" :
                                    ChatFormatting.BLUE + " " + ChatFormatting.WHITE + module.getSuffixes()
                    );
            //animation
            if (module.isToggle()) {
                module.animY = animator.animate(offsetY + 10, module.animY, 0.2f);
                module.animX = animator.animate(width - (font ? fontRenderer.getStringWidth(text) : Minecraft.getMinecraft().fontRendererObj.getStringWidth(text)) - 10, module.animX, 0.08f);
                module.wasRemoved = false;
                offsetY += font ? fontRenderer.getHeight() + 3 : 12;
            } else {
                module.animX = animator.animate(width + 6, module.animX, 0.08f);
                if (module.animX >= width) module.wasRemoved = true;
            }

            //render rect
            if (UIModule.rect.getValue()) {
                drawRect((int) (module.animX - 7), (int) (2 + module.animY), font ? fontRenderer.getStringWidth(text) + 5 : Minecraft.getMinecraft().fontRendererObj.getStringWidth(text) + 5, font ? fontRenderer.getHeight() + 3 : Minecraft.getMinecraft().fontRendererObj.FONT_HEIGHT + 4, new Color(0, 0, 0, UIModule.rectAlpha.getValue().intValue()));
            }

            //update color
            switch (UIModule.mode.getValue()) {
                case "Fade":
                    color = ColorUtil.fade(UIModule.speed.getValue().intValue(), 30 * index, new Color(UIModule.red.getValue().intValue(),UIModule.green.getValue().intValue(),UIModule.blue.getValue().intValue()), 1f);
                    break;
                case "Rainbow":
                    color = ColorUtil.rainbow(UIModule.speed.getValue().intValue(), 10 * index, 1f, 0.8f, 1f);
                    break;
                case "Static":
                    color = new Color(UIModule.red.getValue().intValue(),UIModule.green.getValue().intValue(),UIModule.blue.getValue().intValue());
                    break;
                default:
                    color = ColorUtil.interpolateColorsBackAndForth(UIModule.speed.getValue().intValue(), 50 * index, new Color(UIModule.red.getValue().intValue(),UIModule.green.getValue().intValue(),UIModule.blue.getValue().intValue()), new Color(UIModule.red2.getValue().intValue(),UIModule.green2.getValue().intValue(),UIModule.blue2.getValue().intValue()), true);
            }

            //render border
            if (UIModule.border.getValue() && module.isToggle()) {
                if (UIModule.borderMode.getValue().equals("Border")) {
                    drawRect((int) (module.animX - 5) - 3, (int) (2 + module.animY), 1, (font ? fontRenderer.getHeight() + 3 : Minecraft.getMinecraft().fontRendererObj.FONT_HEIGHT + 3), color.getRGB());
                    drawRect((int) (module.animX + (font ? fontRenderer.getStringWidth(text) : Minecraft.getMinecraft().fontRendererObj.getStringWidth(text)) - 3), (int) (2 + module.animY), 1, (font ? fontRenderer.getHeight() + 3 : Minecraft.getMinecraft().fontRendererObj.FONT_HEIGHT + 3), color.getRGB());
                    if (module != modules.get(0)) {
                        int differ = font ?
                                fontRenderer.getStringWidth(
                                        modules.get(index - 1).getName()
                                                + (modules.get(index - 1).getSuffixes().isEmpty() ?
                                                "" :
                                                ChatFormatting.BLUE + " " + ChatFormatting.WHITE + modules.get(index - 1).getSuffixes())
                                ) - fontRenderer.getStringWidth(
                                        module.getName()
                                                + (module.getSuffixes().isEmpty() ?
                                                "" :
                                                ChatFormatting.BLUE + " " + ChatFormatting.WHITE + module.getSuffixes())
                                ) :
                                Minecraft.getMinecraft().fontRendererObj.getStringWidth(
                                        modules.get(index - 1).getName()
                                                + (modules.get(index - 1).getSuffixes().isEmpty() ?
                                                "" :
                                                ChatFormatting.BLUE + " " + ChatFormatting.WHITE + modules.get(index - 1).getSuffixes())
                                ) - Minecraft.getMinecraft().fontRendererObj.getStringWidth(
                                        module.getName()
                                                + (module.getSuffixes().isEmpty() ?
                                                "" :
                                                ChatFormatting.BLUE + " " + ChatFormatting.WHITE + module.getSuffixes())
                                );
                        drawRect((int) (module.animX - 8) - differ, (int) (2 + module.animY), differ, 1, color.getRGB());
                        if (index == modules.size() - 1) {
                            drawRect((int) (module.animX - 8), (int) (2 + module.animY + (font ? fontRenderer.getHeight() + 3 : Minecraft.getMinecraft().fontRendererObj.FONT_HEIGHT + 3)), (font ? fontRenderer.getStringWidth(text) : Minecraft.getMinecraft().fontRendererObj.getStringWidth(text)) + 6, 1, color.getRGB());
                        }
                    } else {
                        drawRect((int) (module.animX - 8), (int) (2 + module.animY), (font ? fontRenderer.getStringWidth(text) : Minecraft.getMinecraft().fontRendererObj.getStringWidth(text)) + 6, 1, color.getRGB());
                    }
                }
            }

            //render font
            if (font) {
                fontRenderer.drawStringWithShadow(text, module.animX - 5 , (4 + module.animY), color.getRGB());
            } else {
                Minecraft.getMinecraft().fontRendererObj.drawStringWithShadow(text, module.animX - 5, (4 + module.animY), color.getRGB());
            }
            //increments
            index++;
        }
        if(UIModule.borderMode.getValue().equals("Shadow")){

            ShadowUtils.shadow(UIModule.shadowStrength.getValue().floatValue(),()-> {
                for (Module module : modules) {
                    String context =
                            module.getName() + (
                                    module.getSuffixes().isEmpty() ?
                                            "" :
                                            ChatFormatting.BLUE + " " + ChatFormatting.WHITE + module.getSuffixes()
                            );
                    GlStateManager.pushMatrix();
                    newDrawRect(
                            (module.animX - 7),
                            (2 + module.animY),
                            (module.animX - 7) + (font ? fontRenderer.getStringWidth(context) + 5 : Minecraft.getMinecraft().fontRendererObj.getStringWidth(context) + 5),
                            (2 + module.animY) + (font ? fontRenderer.getHeight() + 3 : Minecraft.getMinecraft().fontRendererObj.FONT_HEIGHT + 3),
                            new Color(0,0,0).getRGB()
                    );

                    GlStateManager.popMatrix();
                }
            },()-> {
                for (Module module : modules) {
                    String context =
                            module.getName() + (
                                    module.getSuffixes().isEmpty() ?
                                            "" :
                                            ChatFormatting.BLUE + " " + ChatFormatting.WHITE + module.getSuffixes()
                            );
                    GlStateManager.pushMatrix();
                    newDrawRect(
                            (module.animX - 7),
                            (2 + module.animY),
                            (int) (module.animX - 7) + (font ? fontRenderer.getStringWidth(context) + 5 : Minecraft.getMinecraft().fontRendererObj.getStringWidth(context) + 5),
                            (int) (2 + module.animY) + (font ? fontRenderer.getHeight() + 3 : Minecraft.getMinecraft().fontRendererObj.FONT_HEIGHT + 3) + 1,
                            new Color(0, 0, 0, 0).getRGB()
                    );
                    GlStateManager.popMatrix();
                }
            });

        }
        int indexSide = 0;
        for (Module module : modules) {
            Color sidebar;
            switch (UIModule.mode.getValue()) {
                case "Fade":
                    sidebar = ColorUtil.fade(UIModule.speed.getValue().intValue(), 30 * indexSide, new Color(UIModule.red.getValue().intValue(),UIModule.green.getValue().intValue(),UIModule.blue.getValue().intValue()), 1f);
                    break;
                case "Rainbow":
                    sidebar = ColorUtil.rainbow(UIModule.speed.getValue().intValue(), 10 * indexSide, 1f, 0.8f, 1f);
                    break;
                case "Static":
                    sidebar = new Color(UIModule.red.getValue().intValue(),UIModule.green.getValue().intValue(),UIModule.blue.getValue().intValue());
                    break;
                default:
                    sidebar = ColorUtil.interpolateColorsBackAndForth(UIModule.speed.getValue().intValue(), 50 * indexSide, new Color(UIModule.red.getValue().intValue(),UIModule.green.getValue().intValue(),UIModule.blue.getValue().intValue()), new Color(UIModule.red2.getValue().intValue(),UIModule.green2.getValue().intValue(),UIModule.blue2.getValue().intValue()), true);
            }
            if(UIModule.sidebar.getValue()){
                String context =
                        module.getName() + (
                                module.getSuffixes().isEmpty() ?
                                        "" :
                                        ChatFormatting.BLUE + " " + ChatFormatting.WHITE + module.getSuffixes()
                        );
                drawRoundedRect((int) (module.animX + (font ? fontRenderer.getStringWidth(context) : Minecraft.getMinecraft().fontRendererObj.getStringWidth(context)) - 3) + 1.5f, (int) (2 + module.animY) + 1.5f, 1.5f, (font ? fontRenderer.getHeight() + 3 : Minecraft.getMinecraft().fontRendererObj.FONT_HEIGHT + 3) - 2f,1f, sidebar);
                indexSide++;
            }
        }
    }
}
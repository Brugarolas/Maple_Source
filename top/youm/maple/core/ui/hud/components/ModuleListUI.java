package top.youm.maple.core.ui.hud.components;

import com.mojang.realmsclient.gui.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import top.youm.maple.Maple;
import top.youm.maple.core.module.Module;
import top.youm.maple.core.module.modules.visual.HUD;
import top.youm.maple.core.module.modules.visual.ModuleList;
import top.youm.maple.core.ui.clickgui.classic.theme.Theme;
import top.youm.maple.core.ui.font.CFontRenderer;
import top.youm.maple.core.ui.font.FontLoaders;
import top.youm.maple.utils.AnimationUtils;
import top.youm.maple.utils.render.ColorUtil;
import top.youm.maple.utils.render.ShadowUtils;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

import static top.youm.maple.utils.render.RenderUtil.*;

/**
 * @author YouM
 */
public class ModuleListUI implements HUDComponent<ModuleList> {
    private final AnimationUtils animator = new AnimationUtils();
    //default color
    private Color color = Theme.theme;

    //render modules list
    @Override
    public void draw(ModuleList UIModule) {
        ScaledResolution sr = new ScaledResolution(Minecraft.getMinecraft());

        int width = sr.getScaledWidth();

        List<Module> modules = new ArrayList<>();

        CFontRenderer fontRenderer = FontLoaders.moduleList;

        for (Module m : Maple.getInstance().getModuleManager().modules) {
            if ((UIModule.noRender.getValue() && m.isRenderModule) || (!m.isToggle() && m.wasRemoved))
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

                module.animY = animator.animate(offsetY + (UIModule.edge.getValue() ? 10 : -2), module.animY, 0.2f);
                module.animX = animator.animate(width - (font ? fontRenderer.getStringWidth(text) : Minecraft.getMinecraft().fontRendererObj.getStringWidth(text)) - (UIModule.edge.getValue() ?  12 : -1), module.animX, 0.08f);
                module.wasRemoved = false;
                offsetY += font ? fontRenderer.getHeight() + 3 : 12;
            } else {
                module.animX = animator.animate(width + 6, module.animX, 0.08f);
                if (module.animX >= width) module.wasRemoved = true;
            }

            //render rect
            if (UIModule.rect.getValue()) {
                if (UIModule.border.getValue() && UIModule.borderMode.getValue().equals("Shadow")) {
                    drawRect(
                            (int) (module.animX - UIModule.rectPadding.getValue().floatValue()),
                            (int) (2 + module.animY),
                            font ? fontRenderer.getStringWidth(text) + (UIModule.rectPadding.getValue().floatValue() + UIModule.rectPadding.getValue().floatValue() - 7) + 1
                                    : Minecraft.getMinecraft().fontRendererObj.getStringWidth(text) + 1 + (UIModule.rectPadding.getValue().floatValue() + UIModule.rectPadding.getValue().floatValue() - 7),
                            font ? fontRenderer.getHeight() + 3
                                    : Minecraft.getMinecraft().fontRendererObj.FONT_HEIGHT + 3,
                            new Color(0, 0, 0, UIModule.rectAlpha.getValue().intValue())
                    );
                } else {
                    drawRect(
                            (int) (module.animX + 1 - UIModule.rectPadding.getValue().floatValue()),
                            (int) (2 + module.animY),
                            font ? fontRenderer.getStringWidth(text) + (UIModule.rectPadding.getValue().floatValue() + UIModule.rectPadding.getValue().floatValue() - 7)
                                    : Minecraft.getMinecraft().fontRendererObj.getStringWidth(text) + 1 + (UIModule.rectPadding.getValue().floatValue() + UIModule.rectPadding.getValue().floatValue() - 7),
                            font ? fontRenderer.getHeight() + 3
                                    : Minecraft.getMinecraft().fontRendererObj.FONT_HEIGHT + 3,
                            new Color(0, 0, 0, UIModule.rectAlpha.getValue().intValue())
                    );
                }
            }

            //update color
            switch (UIModule.mode.getValue()) {
                case "Fade":
                    color = ColorUtil.fade(UIModule.speed.getValue().intValue(), 30 * index, HUD.getHUDThemeColor(), 1f);
                    break;
                case "Rainbow":
                    color = ColorUtil.rainbow(UIModule.speed.getValue().intValue(), 10 * index, 1f, 0.8f, 1f);
                    break;
                case "Static":
                    color = HUD.getHUDThemeColor();
                    break;
                default:
                    color = ColorUtil.interpolateColorsBackAndForth(UIModule.speed.getValue().intValue(), 60 * index, HUD.getHUDThemeColor(), new Color(UIModule.red.getValue().intValue(), UIModule.green.getValue().intValue(), UIModule.blue.getValue().intValue()), true);
            }

            //render border
            if (UIModule.border.getValue() && module.isToggle()) {
                if (UIModule.borderMode.getValue().equals("Border")) {
                    drawRect((int) (module.animX - UIModule.rectPadding.getValue().intValue()), (int) (2 + module.animY), 1, (font ? fontRenderer.getHeight() + 3 : Minecraft.getMinecraft().fontRendererObj.FONT_HEIGHT + 3), color.getRGB());
                    drawRect((int) (module.animX + (font ? fontRenderer.getStringWidth(text) - 1 : Minecraft.getMinecraft().fontRendererObj.getStringWidth(text))) + UIModule.rectPadding.getValue().intValue() - 5, (int) (2 + module.animY), 1, (font ? fontRenderer.getHeight() + 3 : Minecraft.getMinecraft().fontRendererObj.FONT_HEIGHT + 3), color.getRGB());
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
                        drawRect((int) (module.animX - UIModule.rectPadding.getValue().intValue()) - differ, (int) (2 + module.animY), differ, 1, color.getRGB());
                        if (index == modules.size() - 1) {
                            drawRect((int) (module.animX - UIModule.rectPadding.getValue().intValue()), (int) (2 + module.animY + (font ? fontRenderer.getHeight() + 3 : Minecraft.getMinecraft().fontRendererObj.FONT_HEIGHT + 3)), (font ? fontRenderer.getStringWidth(text) : Minecraft.getMinecraft().fontRendererObj.getStringWidth(text) + 1) + (UIModule.rectPadding.getValue().intValue() + UIModule.rectPadding.getValue().intValue() - 5), 1, color.getRGB());
                        }
                    } else {
                        drawRect((int) (module.animX - UIModule.rectPadding.getValue().intValue()), (int) (2 + module.animY), (font ? fontRenderer.getStringWidth(text) : Minecraft.getMinecraft().fontRendererObj.getStringWidth(text)) + (UIModule.rectPadding.getValue().intValue() + UIModule.rectPadding.getValue().intValue() - 5), 1, color.getRGB());
                    }
                }
            }

            //render font
            if (font) {
                fontRenderer.drawStringWithShadow(text, module.animX - 3, (4 + module.animY), color.getRGB());
            } else {
                Minecraft.getMinecraft().fontRendererObj.drawStringWithShadow(text, module.animX - 2, (4 + module.animY), color.getRGB());
            }
            //increments
            index++;
        }
        if (UIModule.border.getValue() && UIModule.borderMode.getValue().equals("Shadow")) {
            ShadowUtils.shadow(UIModule.shadowStrength.getValue().floatValue(), () -> {
                for (Module module : modules) {
                    String context =
                            module.getName() + (
                                    module.getSuffixes().isEmpty() ?
                                            "" :
                                            ChatFormatting.BLUE + " " + ChatFormatting.WHITE + module.getSuffixes()
                            );
                    GlStateManager.pushMatrix();
                    drawRect(
                            (int) (module.animX - UIModule.rectPadding.getValue().floatValue()),
                            (int) (2 + module.animY),
                            font ? fontRenderer.getStringWidth(context) + (UIModule.rectPadding.getValue().floatValue() + UIModule.rectPadding.getValue().floatValue() - 7) + 1
                                    : Minecraft.getMinecraft().fontRendererObj.getStringWidth(context) + 1 + (UIModule.rectPadding.getValue().floatValue() + UIModule.rectPadding.getValue().floatValue() - 7),
                            font ? fontRenderer.getHeight() + 3
                                    : Minecraft.getMinecraft().fontRendererObj.FONT_HEIGHT + 3,
                            new Color(0, 0, 0, 180)
                    );

                    GlStateManager.popMatrix();
                }
            }, () -> {
                for (Module module : modules) {
                    String context =
                            module.getName() + (
                                    module.getSuffixes().isEmpty() ?
                                            "" :
                                            ChatFormatting.BLUE + " " + ChatFormatting.WHITE + module.getSuffixes()
                            );
                    GlStateManager.pushMatrix();
                    drawRect(
                            (int) (module.animX - UIModule.rectPadding.getValue().floatValue()),
                            (int) (2 + module.animY),
                            font ? fontRenderer.getStringWidth(context) + (UIModule.rectPadding.getValue().floatValue() + UIModule.rectPadding.getValue().floatValue() - 7) + 1
                                    : Minecraft.getMinecraft().fontRendererObj.getStringWidth(context) + 1 + (UIModule.rectPadding.getValue().floatValue() + UIModule.rectPadding.getValue().floatValue() - 7),
                            font ? fontRenderer.getHeight() + 3
                                    : Minecraft.getMinecraft().fontRendererObj.FONT_HEIGHT + 3,
                            new Color(0, 0, 0, 180)
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
                    sidebar = ColorUtil.fade(UIModule.speed.getValue().intValue(), 30 * indexSide, HUD.getHUDThemeColor(), 1f);
                    break;
                case "Rainbow":
                    sidebar = ColorUtil.rainbow(UIModule.speed.getValue().intValue(), 10 * indexSide, 1f, 0.8f, 1f);
                    break;
                case "Static":
                    sidebar = HUD.getHUDThemeColor();
                    break;
                default:
                    sidebar = ColorUtil.interpolateColorsBackAndForth(UIModule.speed.getValue().intValue(), 60 * indexSide, HUD.getHUDThemeColor(), new Color(UIModule.red.getValue().intValue(), UIModule.green.getValue().intValue(), UIModule.blue.getValue().intValue()), true);
            }
            if (UIModule.sidebar.getValue()) {
                String context =
                        module.getName() + (
                                module.getSuffixes().isEmpty() ?
                                        "" :
                                        ChatFormatting.BLUE + " " + ChatFormatting.WHITE + module.getSuffixes()
                        );
                drawRect((int) (module.animX + (font ? fontRenderer.getStringWidth(context) : Minecraft.getMinecraft().fontRendererObj.getStringWidth(context)) - 3) + UIModule.rectPadding.getValue().floatValue() - 3, (int) (module.animY) + 2, 1f, (font ? fontRenderer.getHeight() + 3 : Minecraft.getMinecraft().fontRendererObj.FONT_HEIGHT + 3), sidebar);
                indexSide++;
            }
        }
    }
}
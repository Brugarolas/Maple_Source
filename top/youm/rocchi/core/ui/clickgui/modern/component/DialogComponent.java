package top.youm.rocchi.core.ui.clickgui.modern.component;

import net.minecraft.client.gui.GuiOptions;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import top.youm.rocchi.Rocchi;
import top.youm.rocchi.core.module.Module;
import top.youm.rocchi.core.module.modules.visual.ClickGui;
import top.youm.rocchi.core.ui.clickgui.modern.Screen;
import top.youm.rocchi.core.ui.clickgui.modern.theme.Theme;
import top.youm.rocchi.core.ui.clickgui.old.MouseType;
import top.youm.rocchi.core.ui.font.FontLoaders;
import top.youm.rocchi.utils.render.RoundedUtil;

import java.awt.*;
import java.io.IOException;

import static top.youm.rocchi.core.ui.clickgui.modern.component.Component.isHover;

public class DialogComponent extends GuiScreen {
    private Module module;
    private int dialogWidth;
    private int dialogHeight;
    private GuiScreen father;
    public DialogComponent(GuiScreen father,Module module, int width, int height) {
        this.dialogHeight = height;
        this.dialogWidth = width;
        this.module = module;
        this.father = father;
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        ScaledResolution sr = new ScaledResolution(mc);
        float x = sr.getScaledWidth() / 2.0f - this.dialogWidth / 2.0f;
        float y = sr.getScaledHeight() / 2.0f - this.dialogHeight / 2.0f;
        RoundedUtil.drawRound(x,y,dialogWidth,dialogHeight,3, Theme.background);
        FontLoaders.robotoR22.drawStringWithShadow("X",x+width - FontLoaders.robotoR22.getStringWidth("X"),y,Theme.font.getRGB());
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        ScaledResolution sr = new ScaledResolution(mc);
        float x = sr.getScaledWidth() / 2.0f - this.dialogWidth / 2.0f;
        float y = sr.getScaledHeight() / 2.0f - this.dialogHeight / 2.0f;
        if (isHover((int) (x + dialogWidth - FontLoaders.robotoR22.getStringWidth("X") - 4), (int) (y + 2), FontLoaders.robotoR22.getStringWidth("X") + 4, FontLoaders.robotoR22.getHeight() + 2, mouseX, mouseY) && mouseButton == 0) {
            ClickGui moduleByClass = Rocchi.getInstance().getModuleManager().getModuleByClass(ClickGui.class);
            moduleByClass.screen.updateDialog(null);
        }
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException {
    }

    public Module getModule() {
        return module;
    }

    public void setModule(Module module) {
        this.module = module;
    }
}

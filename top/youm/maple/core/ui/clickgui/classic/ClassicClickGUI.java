package top.youm.maple.core.ui.clickgui.classic;

import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import org.lwjgl.input.Keyboard;
import top.youm.maple.Maple;
import top.youm.maple.core.module.Module;
import top.youm.maple.core.module.ModuleCategory;
import top.youm.maple.core.module.modules.visual.ClickGui;
import top.youm.maple.core.ui.clickgui.classic.component.Component;
import top.youm.maple.core.ui.clickgui.classic.component.CategoryComponent;
import top.youm.maple.core.ui.clickgui.classic.component.ModuleComponent;
import top.youm.maple.core.ui.clickgui.classic.state.UIState;
import top.youm.maple.core.ui.font.FontLoaders;
import top.youm.maple.core.ui.clickgui.classic.theme.Theme;
import top.youm.maple.utils.render.RenderUtil;
import top.youm.maple.utils.render.RoundedUtil;
import top.youm.maple.utils.render.ShadowUtils;

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ClassicClickGUI extends GuiScreen {
    public static int x, y;
    public static int screenWidth = 450, screenHeight = 260;
    public static int navbarWidth = 110;
    public int dragX, dragY;
    public boolean isDragging = false;
    public boolean sizeDragging = false;
    private final List<CategoryComponent> categoryButtons = new ArrayList<>();
    private final List<ModuleComponent> moduleComponents = new ArrayList<>();
    public CategoryComponent currentComponent;
    public ClassicClickGUI() {
        for (ModuleCategory value : ModuleCategory.values()) {
            CategoryComponent categoryComponent = new CategoryComponent(value);
            categoryComponent.setModuleComponents(moduleComponents);
            categoryButtons.add(categoryComponent);
        }
        for (Module value : Maple.getInstance().getModuleManager().modules) {
            moduleComponents.add(new ModuleComponent(value));
        }
    }
    @Override
    public void initGui() {
        super.initGui();
        ScaledResolution sr = new ScaledResolution(mc);
        x = sr.getScaledWidth() / 2 - screenWidth / 2;
        y = sr.getScaledHeight() / 2 - screenHeight / 2;
    }
    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        // scale and move the screen
        super.drawScreen(mouseX, mouseY, partialTicks);
        if (isDragging) {
            x = mouseX - dragX;
            y = mouseY - dragY;
        }else if (sizeDragging) {
            screenWidth += mouseX - dragX;
            if(screenWidth < 280){
                screenWidth = 280;
            }else {
                this.dragX = mouseX;
            }
            screenHeight += mouseY - dragY;
            if(screenHeight < 180){
                screenHeight = 180;
            }else {
                this.dragY = mouseY;
            }
        }
        //draw background
        ShadowUtils.shadow(8.0f,
                ()->RenderUtil.drawRoundedRect(x, y, screenWidth, screenHeight, 2, new Color(0,0,0))
                ,()->RenderUtil.drawRoundedRect(x, y, screenWidth, screenHeight, 2, new Color(0,0,0))
        );
        RoundedUtil.drawRound(x + .625f, y + 40.625f, screenWidth - 0.25f, screenHeight - 40, 2, Theme.background);
        RenderUtil.drawRect(x, y + 29, screenWidth + 1.25f, 12, Theme.background);
        /* top menu */
        topRouter(mouseX, mouseY);
        /* navbar menu */
        navbar(mouseX, mouseY);
        /* modules list */
        RenderUtil.startGlScissor(x + navbarWidth, y + 30, screenWidth - navbarWidth - 10, screenHeight - 30);
        currentComponent.moduleMenu(mouseX, mouseY);
        RenderUtil.stopGlScissor();
        if (UIState.settingFocused) {
            UIState.dialog.draw(0, 0, mouseX, mouseY);
        }
    }

    public void topRouter(int mouseX, int mouseY) {
        RoundedUtil.drawRound(x + 0.625f, y + 0.625f, screenWidth - 0.25f, 20, 2, Theme.theme);
        RenderUtil.drawRect(x, y + 20, screenWidth + 1.25f, 10, Theme.theme);
        FontLoaders.neverlose.drawStringWithShadow(Maple.getInstance().NAME, x + 10, y + 6, -1);
    }
    public void navbar(int mouseX, int mouseY) {
        RenderUtil.drawGradientRect(x + navbarWidth - 2,y + 30,0.5f,screenHeight - 30,new Color(50,50,50),new Color(70,70,70));
        int yOffset = 0;
        for (CategoryComponent categoryButton : categoryButtons) {
            categoryButton.draw(x + 15, y + 35 + yOffset, mouseX, mouseY);
            yOffset += 30;
            if (UIState.currentCategory == categoryButton.category) {
                this.currentComponent = categoryButton;
            }
        }
    }
    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        if (!UIState.settingFocused) {
            if (keyCode == Keyboard.KEY_RSHIFT || keyCode == Keyboard.KEY_ESCAPE) {
                this.onGuiClosed();
                mc.displayGuiScreen(null);
                ClickGui clickGui = Maple.getInstance().getModuleManager().getModuleByClass(ClickGui.class);
                clickGui.setToggle(false);
            }
            for (ModuleComponent moduleComponent : moduleComponents) {
                moduleComponent.input(typedChar, keyCode);
            }
        } else {
            UIState.dialog.input(typedChar, keyCode);
        }
    }
    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        ScaledResolution sr = new ScaledResolution(mc);
        float dialogX = sr.getScaledWidth() / 2.0f - 400 / 2.0f;
        float dialogY = sr.getScaledHeight() / 2.0f - 350 / 2.0f;
        if (!UIState.settingFocused) {
            if (Component.isHover(x, y, screenWidth, 30, mouseX, mouseY) && mouseButton == 0) {
                dragX = mouseX - x;
                dragY = mouseY - y;
                isDragging = true;
            } else if (Component.isHover(x + screenWidth - 10, y + screenHeight - 10, 10, 10, mouseX, mouseY) && mouseButton == 0 ) {
                dragX = mouseX;
                dragY = mouseY;
                sizeDragging = true;
            }
            for (CategoryComponent categoryButton : categoryButtons) {
                categoryButton.mouse(mouseButton, MouseType.CLICK);
            }
            if (Component.isHover(x + navbarWidth, y + 30, screenWidth - navbarWidth - 10, screenHeight - 30, mouseX, mouseY)) {
                for (ModuleComponent moduleComponent : moduleComponents) {
                    if (moduleComponent.getModule().getCategory() != UIState.currentCategory) {
                        continue;
                    }
                    moduleComponent.mouse(mouseButton, MouseType.CLICK);
                }
            }
        } else {
            if (Component.isHover((int) dialogX, (int) dialogY, 400, 350, mouseX, mouseY)) {
                UIState.dialog.mouse(mouseButton, MouseType.CLICK);
            }
        }
    }

    @Override
    protected void mouseReleased(int mouseX, int mouseY, int state) {
        super.mouseReleased(mouseX, mouseY, state);
        if (!UIState.settingFocused) {
            if (Component.isHover(x, y, screenWidth, 30, mouseX, mouseY) && state == 0) {
                dragX = mouseX - x;
                dragY = mouseY - y;
                isDragging = false;
            } else if (Component.isHover(x + screenWidth - 10, y + screenHeight - 10, 10, 10, mouseX, mouseY) && state == 0) {
                dragX = mouseX;
                dragY = mouseY;
                sizeDragging = false;
            }else if(state == 0 && sizeDragging){
                sizeDragging = false;
            }
        } else {
            UIState.dialog.mouse(state, MouseType.RELEASED);
        }
    }

}

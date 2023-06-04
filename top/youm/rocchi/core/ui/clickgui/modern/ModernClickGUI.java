package top.youm.rocchi.core.ui.clickgui.modern;

import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import org.lwjgl.input.Keyboard;
import top.youm.rocchi.Rocchi;
import top.youm.rocchi.core.module.Module;
import top.youm.rocchi.core.module.ModuleCategory;
import top.youm.rocchi.core.module.modules.visual.ClickGui;
import top.youm.rocchi.core.ui.clickgui.modern.component.CategoryComponent;
import top.youm.rocchi.core.ui.clickgui.modern.component.ModuleComponent;
import top.youm.rocchi.core.ui.clickgui.modern.state.UIState;
import top.youm.rocchi.core.ui.clickgui.modern.theme.Theme;
import top.youm.rocchi.core.ui.clickgui.old.MouseType;
import top.youm.rocchi.utils.render.RenderUtil;
import top.youm.rocchi.utils.render.RoundedUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ModernClickGUI extends GuiScreen {
    public static int x, y;
    public static int screenWidth = 450, screenHeight = 260;
    public int dragX, dragY;
    public boolean isDragging = false;
    private final List<CategoryComponent> categoryButtons = new ArrayList<>();
    private final List<ModuleComponent> moduleComponents = new ArrayList<>();
    public CategoryComponent currentComponent;

    public ModernClickGUI() {
        for (ModuleCategory value : ModuleCategory.values()) {
            CategoryComponent categoryComponent = new CategoryComponent(value);
            categoryComponent.setModuleComponents(moduleComponents);
            categoryButtons.add(categoryComponent);
        }
        for (Module value : Rocchi.getInstance().getModuleManager().modules) {
            moduleComponents.add(new ModuleComponent(value));
        }
    }

    @Override
    public void initGui() {
        super.initGui();
        ScaledResolution sr = new ScaledResolution(this.mc);
        x = sr.getScaledWidth() / 2 - screenWidth / 2;
        y = sr.getScaledHeight() / 2 - screenHeight / 2;
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        super.drawScreen(mouseX, mouseY, partialTicks);
        if (isDragging) {
            x = mouseX - dragX;
            y = mouseY - dragY;
        }
        RoundedUtil.drawRound(x, y, screenWidth, screenHeight, 2, Theme.background);
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
        RoundedUtil.drawRound(x, y, screenWidth, 20, 2, Theme.theme);
        RenderUtil.drawRect(x - 1, y + 19, screenWidth + 2, 10, Theme.theme);
    }

    public static int navbarWidth = 110;

    public void navbar(int mouseX, int mouseY) {
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
                ClickGui clickGui = Rocchi.getInstance().getModuleManager().getModuleByClass(ClickGui.class);
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
            }
        } else {
            UIState.dialog.mouse(state, MouseType.RELEASED);
        }
    }

}

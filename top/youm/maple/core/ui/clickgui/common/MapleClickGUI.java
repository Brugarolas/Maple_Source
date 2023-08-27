package top.youm.maple.core.ui.clickgui.common;

import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import top.youm.maple.core.module.ModuleCategory;
import top.youm.maple.core.module.modules.visual.HUD;
import top.youm.maple.core.ui.clickgui.common.sub.button.ButtonComponent;
import top.youm.maple.core.ui.clickgui.common.sub.button.ModuleCategoryButton;
import top.youm.maple.core.ui.clickgui.common.sub.button.RippledButton;
import top.youm.maple.core.ui.font.FontLoaders;
import top.youm.maple.utils.AnimationUtils;
import top.youm.maple.utils.render.RenderUtil;
import top.youm.maple.utils.render.RoundedUtil;

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author YouM
 * Created on 2023/8/22
 */
public class MapleClickGUI extends GuiScreen {
    RippledButton rippledButton = new RippledButton("test",80,25);

    public List<ModuleCategoryButton> categories = new ArrayList<>();
    public static float x, y, dragX, dragY;
    private boolean dragging;
    private final float screenWidth = 500;
    private final float screenHeight = 300;
    public static ModuleCategory category = ModuleCategory.COMBAT;
    public ModuleCategoryButton categoryButton;
    private final AnimationUtils animator = new AnimationUtils();

    public MapleClickGUI() {
        for (ModuleCategory moduleCategory : ModuleCategory.values()) {
            ModuleCategoryButton moduleCategoryButton = new ModuleCategoryButton(moduleCategory);
            if (moduleCategoryButton.category == category) {
                this.categoryButton = moduleCategoryButton;
            }
            categories.add(moduleCategoryButton);
        }
    }

    @Override
    public void initGui() {
        super.initGui();
        ScaledResolution sr = new ScaledResolution(mc);
        x = (sr.getScaledWidth() - this.screenWidth) / 2.0f;
        y = (sr.getScaledHeight() - this.screenHeight) / 2.0f;
    }

    float positionY;

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        super.drawScreen(mouseX, mouseY, partialTicks);
        if (dragging) {
            x = mouseX - dragX;
            y = mouseY - dragY;
        }
        drawBackground(mouseX, mouseY);

        int offsetY = 24;
        for (ModuleCategoryButton categoryButton : categories) {
            if (category == categoryButton.category) {
                this.categoryButton = categoryButton;
            }
            categoryButton.offsetY = offsetY;
            categoryButton.update((x - 20) + (45 / 2.0f), y + ((screenHeight - 230) / 2.0f) + offsetY, mouseX, mouseY);
            categoryButton.drawComponent();
            offsetY += (FontLoaders.commonIcon.getHeight() + 24);
        }
        if(categoryButton != null){
            positionY = animator.animate(categoryButton.offsetY, positionY, 0.1f);
            RoundedUtil.drawRound((x - 20) + (45 / 2.0f) - 22, y + ((screenHeight - 230) / 2.0f) + positionY - 2, 2, 21, 1, HUD.getHUDThemeColor());
        }
        rippledButton.update(x + 100,y + 70,mouseX,mouseY);
        rippledButton.drawComponent();

    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        super.keyTyped(typedChar, keyCode);
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        if (ButtonComponent.isHovered(x + 50, y, screenWidth, 55, mouseX, mouseY) && mouseButton == 0) {
            dragX = mouseX - x;
            dragY = mouseY - y;
            dragging = true;
        }
        if (ButtonComponent.isHovered(x - 20, y + (screenHeight - 230) / 2.0f, 45, 230, mouseX, mouseY)) {
            for (ModuleCategoryButton categoryButton : categories) {
                categoryButton.onMouseClick(mouseButton);
            }
        }
        rippledButton.onMouseClick(mouseButton);
    }

    @Override
    protected void mouseReleased(int mouseX, int mouseY, int state) {
        super.mouseReleased(mouseX, mouseY, state);
        if (ButtonComponent.isHovered(x + 50, y, screenWidth, 55, mouseX, mouseY) && state == 0) {
            dragX = mouseX - x;
            dragY = mouseY - y;
            dragging = false;
        }
    }


    public void drawBackground(int mouseX, int mouseY) {

        RoundedUtil.drawRound(x - 20, y + (screenHeight - 230) / 2.0f, 45, 230, 24, new Color(70, 70, 70));
        RoundedUtil.drawRound(x + 50, y, screenWidth, screenHeight, 10, new Color(55, 55, 55));
        RoundedUtil.drawRound(x + 50, y, screenWidth, 50, 10, new Color(83, 83, 83));
        RenderUtil.drawRect(x + 50 - 0.625f, y + 20, screenWidth + 2, 35, new Color(83, 83, 83));

    }

    @Override
    public void onGuiClosed() {
        super.onGuiClosed();
    }
}

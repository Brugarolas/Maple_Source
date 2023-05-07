package top.youm.rocchi.core.ui.clickgui;

import net.minecraft.client.renderer.GlStateManager;
import org.lwjgl.input.Mouse;
import top.youm.rocchi.Rocchi;
import top.youm.rocchi.core.module.Module;
import top.youm.rocchi.core.module.ModuleCategory;
import top.youm.rocchi.core.module.modules.visual.ClickGui;
import top.youm.rocchi.core.ui.clickgui.components.CategoryButton;
import top.youm.rocchi.core.ui.clickgui.components.ModuleComponent;
import top.youm.rocchi.core.ui.font.CFontRenderer;
import top.youm.rocchi.core.ui.font.FontLoaders;
import top.youm.rocchi.utils.AnimationUtils;
import top.youm.rocchi.utils.render.RenderUtil;
import top.youm.rocchi.utils.render.RoundedUtil;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import org.lwjgl.input.Keyboard;

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("all")
public class ClickGuiScreen extends GuiScreen {
    public static int x,y;
    public static int screenWidth = 440, screenHeight = 250;
    public static int navbarWidht = 90,navbarHeight = 180;
    public static final int margin = 20;
    public static int menuWidht = screenWidth - 120,menuHeight = 180;
    public static int dragX, dragY;
    public static boolean isDragging = false;
    public static ModuleCategory moduleCategory = null;
    private final List<CategoryButton> categoryButtons = new ArrayList<>();
    private final List<ModuleComponent> moduleComponents = new ArrayList<>();
    private final CFontRenderer font = FontLoaders.comfortaaR30;
    public static Module settingOpen;
    public static float animCategory = 0;
    public ClickGuiScreen(){
        for (ModuleCategory category : ModuleCategory.values()) {
            categoryButtons.add(new CategoryButton(category));
        }
        for (Module module : Rocchi.getInstance().getModuleManager().modules) {
            moduleComponents.add(new ModuleComponent(module));
        }
    }
    @Override
    public void initGui() {
        super.initGui();
        ScaledResolution sr = new ScaledResolution(this.mc);
        this.x = sr.getScaledWidth() / 2 - screenWidth / 2;this.y = sr.getScaledHeight() / 2 - screenHeight / 2;
    }
    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        super.drawScreen(mouseX, mouseY, partialTicks);
        render(mouseX,mouseY,partialTicks);
    }
    public static int wheel = 0;
    public ModuleComponent component;
    public void render(int mouseX, int mouseY, float partialTicks){
        if (isDragging) {
            x = mouseX - dragX;
            y = mouseY - dragY;
        }
        ScaledResolution sr = new ScaledResolution(this.mc);
        /* background */
        RoundedUtil.drawRound(x,y, screenWidth, screenHeight,2,new Color(58, 58, 58, 255));

        /* title */
        FontLoaders.robotoB40.drawStringWithShadow(Rocchi.getInstance().NAME, x + margin,y + margin,-1);
        /* module category */
        float xOffset = 0;

        for (CategoryButton categoryButton : categoryButtons) {
            categoryButton.draw(x + screenWidth - 340 + xOffset,y + 20);
            xOffset += 64;
        }
        RoundedUtil.drawRoundOutline(x + screenWidth - 340,y + 20,320,20,2,1,new Color(0,0,0,0),new Color(80, 80, 80,255));

        /* navbar */
        RoundedUtil.drawRoundOutline(x + 10,y + margin + 30,navbarWidht,navbarHeight,2,1,new Color(0,0,0,0),new Color(80, 80, 80,255));
        RenderUtil.startGlScissor(x + 10,y + margin + 30,navbarWidht,navbarHeight);
        int real = Mouse.getDWheel();
        int offsetY = 0;
        for (ModuleComponent moduleComponent : moduleComponents) {
            if(moduleComponent.getModule().getCategory() != this.moduleCategory){
                continue;
            }
            moduleComponent.draw(x + 55,(y + 59 - FontLoaders.robotoR22.getHeight() / 2) + offsetY + wheel);
            if(moduleComponent.getModule().equals(settingOpen)){
                component = moduleComponent;
            }
            offsetY += 22;

        }
        RenderUtil.stopGlScissor();

        if (real != 0 && Mouse.hasWheel() && isHover(x + 10,y + margin + 30,navbarWidht,navbarHeight,mouseX,mouseY)) {
            if (real > 0) {
                wheel = AnimationUtils.animateI(wheel + 50, wheel, 0.08f);
            } else {
                wheel = AnimationUtils.animateI(wheel - 50, wheel, 0.08f);
            }
        }
        if(moduleCategory != null){
            /* main menu*/
            RoundedUtil.drawRoundOutline(x + 110,y + 50,menuWidht,menuHeight,2,1,new Color(0,0,0,0),new Color(80, 80, 80,255));
        }else {
            font.drawStringWithShadow("Welcome!!",x + screenWidth / 2.0f + font.getStringWidth("Welcome!!") / 2.0f + 10,y + screenHeight / 2.0f - font.getHeight() , -1);
        }
        if(component != null){
            RenderUtil.startGlScissor(x + 110,y + margin + 30,menuWidht,menuHeight);
            component.renderSetting();
            RenderUtil.stopGlScissor();
        }

    }
    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        if(keyCode == Keyboard.KEY_RSHIFT || keyCode == 1){
            this.onGuiClosed();
            ClickGui clickGui = Rocchi.getInstance().getModuleManager().getModuleByClass(ClickGui.class);
            clickGui.setToggle(false);
        }
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        if (isHover(x, y, screenWidth, 20, mouseX, mouseY) && mouseButton == 0) {
            dragX = mouseX - x;
            dragY = mouseY - y;
            isDragging = true;
        }
        componentsClick(mouseX,mouseY,mouseButton);
    }

    @Override
    protected void mouseReleased(int mouseX, int mouseY, int state) {
        super.mouseReleased(mouseX, mouseY, state);
        if (isHover(x, y, screenWidth, 20, mouseX, mouseY) && state == 0) {
            dragX = mouseX - x;
            dragY = mouseY - y;
            isDragging = false;
        }
    }

    private boolean isHover(int x, int y, int width, int height, int mouseX, int mouseY){
        return mouseX >= x && mouseX <= x + width && mouseY > y && mouseY <= y + height;
    }

    public void componentsClick(int mouseX, int mouseY,int mouseButton){
        for (CategoryButton categoryButton : this.categoryButtons) {
            categoryButton.mouse(mouseX,mouseY,mouseButton);
        }
        for (ModuleComponent moduleComponent : moduleComponents) {
            if(moduleComponent.getModule().getCategory() == this.moduleCategory){
                moduleComponent.mouse(mouseX,mouseY,mouseButton);
            }
        }
    }

}

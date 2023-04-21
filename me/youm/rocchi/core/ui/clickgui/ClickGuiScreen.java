package me.youm.rocchi.core.ui.clickgui;

import me.youm.rocchi.Rocchi;
import me.youm.rocchi.core.module.ModuleCategory;
import me.youm.rocchi.core.module.modules.visual.ClickGui;
import me.youm.rocchi.core.ui.IComponent;
import me.youm.rocchi.core.ui.Theme;
import me.youm.rocchi.core.ui.clickgui.components.CategoryButton;
import me.youm.rocchi.core.ui.font.CFontRenderer;
import me.youm.rocchi.core.ui.font.FontLoaders;
import me.youm.rocchi.utils.render.RenderUtil;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import org.lwjgl.input.Keyboard;

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("all")
public class ClickGuiScreen extends GuiScreen {
    private int x,y;
    private int width = 440;
    private int height = 250;
    public static ModuleCategory moduleCategory = null;

    private final List<CategoryButton> categoryButtons = new ArrayList<>();

    public final String title = "Welcome! This is " + Rocchi.getInstance().NAME + " Client's Menu";
    private final CFontRenderer font = FontLoaders.comfortaaR30;
    private boolean full;

    public ClickGuiScreen(){
        for (ModuleCategory category : ModuleCategory.values()) {
            categoryButtons.add(new CategoryButton(category));
        }
    }
    @Override
    public void initGui() {
        super.initGui();
        ScaledResolution sr = new ScaledResolution(this.mc);
        this.x = sr.getScaledWidth() / 2 - width / 2;this.y = sr.getScaledHeight() / 2 - height / 2;
    }
    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        super.drawScreen(mouseX, mouseY, partialTicks);
        ScaledResolution sr = new ScaledResolution(this.mc);
        if(full){
            this.x = 0;this.y = 0;
            this.width = sr.getScaledWidth();
            this.height = sr.getScaledHeight();
        }else {
            this.x = sr.getScaledWidth() / 2 - width / 2;this.y = sr.getScaledHeight() / 2 - height / 2;
            this.width = 440;
            this.height = 250;
        }
        RenderUtil.drawRect(x,y,width,height,new Color(58, 58, 58, 255));
        float xOffset = 0;
        for (CategoryButton categoryButton : categoryButtons) {
            categoryButton.draw(x + 20 + xOffset,y + 20);
            xOffset += FontLoaders.comfortaaR22.getStringWidth(categoryButton.category.name()) + 40;
        }

        if(moduleCategory == null){
            font.drawStringWithShadow(title,x + width / 2.0f - font.getStringWidth(title) / 2.0f,y + height / 2.0f - font.getHeight() , Theme.fontColor.getRGB());
        }
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        if(keyCode == Keyboard.KEY_RSHIFT || keyCode == 1){
            this.onGuiClosed();
            ClickGui clickGui = Rocchi.getInstance().getModuleManager().getModuleByClass(ClickGui.class);
            clickGui.setToggle(false);
            if (this.mc.currentScreen == null) this.mc.setIngameFocus();
        }
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        ScaledResolution sr = new ScaledResolution(this.mc);
        if(isHover(x,y,width,20,mouseX,mouseY) && mouseButton == 0 && !full){
            full = true;
        }
        if(isHover(0,0,sr.getScaledWidth(),20,mouseX,mouseY) && mouseButton == 0 && full)full = false;
        if(mouseButton == 0){
            componentsClick(mouseX,mouseY,mouseButton);
        }
    }
    private boolean isHover(int x, int y, int width, int height, int mouseX, int mouseY){
        return mouseX >= x && mouseX <= x + width && mouseY > y && mouseY <= y + height;
    }

    public void componentsClick(int mouseX, int mouseY,int mouseButton){
        for (CategoryButton categoryButton : this.categoryButtons) {
            categoryButton.mouse(mouseX,mouseY,mouseButton);
        }
    }

}

package me.youm.rocchi.core.ui.clickgui;

import me.youm.rocchi.Rocchi;
import me.youm.rocchi.core.module.ModuleCategory;
import me.youm.rocchi.core.module.modules.visual.ClickGui;
import me.youm.rocchi.core.ui.IComponent;
import me.youm.rocchi.core.ui.Theme;
import me.youm.rocchi.core.ui.clickgui.components.CategoryButton;
import me.youm.rocchi.core.ui.font.CFontRenderer;
import me.youm.rocchi.core.ui.font.FontLoaders;
import me.youm.rocchi.utils.animations.Animation;
import me.youm.rocchi.utils.animations.Direction;
import me.youm.rocchi.utils.animations.impl.EaseBackIn;
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
    private Animation openingAnimation;
    private int x,y;
    private int width = 440;
    private int height = 250;
    public static int dragX, dragY;
    public static boolean isDragging = false;
    public static ModuleCategory moduleCategory = null;
    private final List<CategoryButton> categoryButtons = new ArrayList<>();

    public final String title = "Welcome! This is " + Rocchi.getInstance().NAME + " Client's Menu";
    private final CFontRenderer font = FontLoaders.comfortaaR30;

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
        openingAnimation = new EaseBackIn(400, .4f, 2f);
    }
    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        super.drawScreen(mouseX, mouseY, partialTicks);

        ScaledResolution sr = new ScaledResolution(this.mc);

        if (openingAnimation.isDone() && openingAnimation.getDirection().equals(Direction.BACKWARDS)) {
            mc.displayGuiScreen(null);
            return;
        }

        RenderUtil.scale(sr.getScaledWidth() / 2f, sr.getScaledHeight() / 2f, (float) (openingAnimation.getOutput() + 0.6),()->{
            render(mouseX,mouseY,partialTicks);
        });

    }
    public void render(int mouseX, int mouseY, float partialTicks){
        if (isDragging) {
            x = mouseX - dragX;
            y = mouseY - dragY;
        }
        ScaledResolution sr = new ScaledResolution(this.mc);

        this.width = 440;
        this.height = 250;

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
            openingAnimation.setDirection(Direction.BACKWARDS);
            this.onGuiClosed();
            ClickGui clickGui = Rocchi.getInstance().getModuleManager().getModuleByClass(ClickGui.class);
            clickGui.setToggle(false);
        }
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        if (isHover(x, y, width, 20, mouseX, mouseY) && mouseButton == 0) {
            dragX = mouseX - x;
            dragY = mouseY - y;
            isDragging = true;
        }
        ScaledResolution sr = new ScaledResolution(this.mc);
        if(mouseButton == 0){
            componentsClick(mouseX,mouseY,mouseButton);
        }
    }

    @Override
    protected void mouseReleased(int mouseX, int mouseY, int state) {
        super.mouseReleased(mouseX, mouseY, state);
        if (isHover(x, y, width, 20, mouseX, mouseY) && state == 0) {
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
    }

}

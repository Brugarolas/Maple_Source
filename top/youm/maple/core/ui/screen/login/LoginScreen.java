package top.youm.maple.core.ui.screen.login;

import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.input.Keyboard;
import top.youm.maple.core.ui.clickgui.classic.component.Component;
import top.youm.maple.utils.render.Circle;
import top.youm.maple.utils.render.CircleManager;
import top.youm.maple.utils.render.RenderUtil;
import top.youm.maple.utils.render.Stencil;
import top.youm.maple.utils.render.gl.ShaderUtil;

import java.awt.*;
import java.io.IOException;

/**
 * @author YouM
 * Created on 2023/8/27
 */
public class LoginScreen extends GuiScreen {
    public static LoginTextField userNameField = new LoginTextField(4, mc.fontRendererObj, 0, 0, 180, 25);

    private LoginButton loginButton = new LoginButton();
    private final ShaderUtil backgroundShader = new ShaderUtil("Maple/shader/background.frag");
    private long initTime = System.currentTimeMillis();

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {

        ScaledResolution sr = new ScaledResolution(mc);
        drawBackground();
        userNameField.xPosition = (int) ((sr.getScaledWidth() - userNameField.getWidth()) / 2.0f);
        userNameField.yPosition = (int) ((sr.getScaledHeight() - 50) / 2.0f);
        userNameField.drawTextBox();


        loginButton.update((sr.getScaledWidth() - loginButton.width) / 2.0f,(sr.getScaledHeight()) / 2.0f + 20,mouseX,mouseY);
        loginButton.drawComponent();
        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        userNameField.textboxKeyTyped(typedChar,keyCode);
    }
    public void drawBackground(){
        backgroundShader.init();
        backgroundShader.setUniformf("time",(System.currentTimeMillis() - initTime) / 1000f);
        backgroundShader.setUniformf("resolution",this.width / 2.0f,this.height / 2.0f);
        ShaderUtil.drawQuads();
        backgroundShader.unload();
    }
    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        loginButton.onMouseClick(mouseButton);
    }

    @Override
    protected void mouseReleased(int mouseX, int mouseY, int state) {
        super.mouseReleased(mouseX, mouseY, state);
    }


    @Override
    public void initGui() {
        super.initGui();
        initTime = System.currentTimeMillis();
        userNameField.setFocused(true);
        Keyboard.enableRepeatEvents(true);
    }

    @Override
    public void updateScreen() {
        userNameField.updateCursorCounter();

    }
}

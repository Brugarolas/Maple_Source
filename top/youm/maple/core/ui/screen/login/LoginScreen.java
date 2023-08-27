package top.youm.maple.core.ui.screen.login;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.*;
import net.minecraft.client.renderer.GlStateManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.input.Keyboard;
import top.youm.maple.Account;
import top.youm.maple.Maple;
import top.youm.maple.utils.math.RsaUtil;
import top.youm.maple.utils.render.RenderUtil;

import java.io.IOException;
import java.util.Map;

/**
 * @author YouM
 * Created on 2023/8/27
 */
public class LoginScreen extends GuiScreen {
    private GuiTextField userNameField;
    public Logger logger = LogManager.getLogger();
    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        this.drawDefaultBackground();
        RenderUtil.scale(this.width / 2.0f,this.height / 3.5f,1.5f,()->{
            mc.fontRendererObj.drawCenteredString("Maple Client HWID Login",this.width / 2.0f,this.height / 3.5f,-1);
        });
        if (this.userNameField.getText().isEmpty()) {
            this.drawString(mc.fontRendererObj, "Your HWID", userNameField.xPosition + (userNameField.getWidth() / 2) - 92, userNameField.yPosition + (20 - mc.fontRendererObj.FONT_HEIGHT) / 2, -7829368);
        }

        userNameField.drawTextBox();
        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        userNameField.textboxKeyTyped(typedChar,keyCode);
    }

    @Override
    public void initGui() {
        ScaledResolution sr = new ScaledResolution(mc);
        this.buttonList.add(new GuiButton(0, width / 2 - 50, (sr.getScaledHeight()) / 2,100,20, "Login"));
        userNameField = new GuiTextField(1, mc.fontRendererObj, (int) ((sr.getScaledWidth() - 200) / 2.0f), (sr.getScaledHeight() - 50) / 2, 200, 20);
        userNameField.setMaxStringLength(500);
        userNameField.setFocused(true);
        Keyboard.enableRepeatEvents(true);
    }

    @Override
    protected void actionPerformed(GuiButton button) throws IOException {
        super.actionPerformed(button);
        if(button.id == 0){
            Map<String, String> keys = RsaUtil.generateKey();
            String privateKeyStr = keys.get("privateKeyStr");
            String text = userNameField.getText();
            try {
                String decrypt = RsaUtil.decryptByPrivateKey(text, privateKeyStr);
                for (float accountNum : RsaUtil.accounts) {
                    if (accountNum == Float.parseFloat(decrypt) + 114514) {
                        Account account = new Account("0");
                        account.setUid(String.valueOf(accountNum));
                        Maple.getInstance().account = account;
                        mc.displayGuiScreen(new GuiMainMenu());
                    }
                }
            } catch (Exception e) {
                logger.warn("failed uid,not to login");
            }
        }
    }

    @Override
    public void updateScreen() {
        userNameField.updateCursorCounter();
    }
}

package top.youm.maple.core.ui.screen.login;

import net.minecraft.client.gui.GuiMainMenu;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import top.youm.maple.Account;
import top.youm.maple.Maple;
import top.youm.maple.core.ui.clickgui.classic.theme.Theme;
import top.youm.maple.core.ui.clickgui.common.sub.button.ButtonComponent;
import top.youm.maple.core.ui.font.FontLoaders;
import top.youm.maple.utils.math.RsaUtil;
import top.youm.maple.utils.render.RenderUtil;

import java.awt.*;
import java.util.Base64;
import java.util.Map;

/**
 * @author YouM
 * Created on 2023/8/27
 */
public class LoginButton extends ButtonComponent {
    public LoginButton() {
        super("Login", 80, 20);
    }
    private static final Logger logger = LogManager.getLogger();
    @Override
    public void drawComponent() {
        if(onComponentHover()){
            RenderUtil.drawRect(x,y,width,height, new Color(Theme.themes.get("Blue").getTheme().getRed() - 30,Theme.themes.get("Blue").getTheme().getGreen()- 30,Theme.themes.get("Blue").getTheme().getBlue()- 30));
        }else{
            RenderUtil.drawRect(x,y,width,height, Theme.themes.get("Blue").getTheme());
        }

        FontLoaders.robotoR22.drawCenteredStringWithShadow(this.getName(),x + (width / 2.0f),y + FontLoaders.robotoR22.getHeight() / 2.0f,-1);

    }

    @Override
    public void onMouseClick(int mouseButton) {
        if(onComponentHover() && mouseButton == 0){
            this.disable = true;
            Map<String, String> keys = RsaUtil.generateKey();
            String privateKeyStr = keys.get("privateKeyStr");
            String text = LoginScreen.userNameField.getText();
            try {
                String decrypt = RsaUtil.decryptByPrivateKey(text, privateKeyStr);
                for (float accountNum : RsaUtil.accounts) {
                    if (accountNum == Float.parseFloat(decrypt) + 114514) {
                        Account account = new Account();
                        account.uid = String.valueOf(accountNum);
                        Maple.getInstance().account = account;
                        this.mc.displayGuiScreen(new GuiMainMenu());
                    }
                }
            } catch (Exception e) {
                logger.warn("failed uid,not to login");
            }

        }
    }

    @Override
    public void onMouseRelease(int mouseButton) {

    }
}

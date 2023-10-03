package top.youm.maple.core.ui.clickgui.classic.component;

import org.lwjgl.input.Keyboard;
import top.youm.maple.core.module.Module;
import top.youm.maple.core.ui.clickgui.classic.ClassicClickGUI;
import top.youm.maple.core.ui.clickgui.classic.state.UIState;
import top.youm.maple.core.ui.clickgui.classic.theme.Theme;
import top.youm.maple.core.ui.clickgui.classic.MouseType;
import top.youm.maple.core.ui.font.FontLoaders;
import top.youm.maple.utils.render.RenderUtil;
import top.youm.maple.utils.render.RoundedUtil;

import java.awt.*;

public class ModuleComponent extends Component {
    private Module module;
    public float animation = 30;
    public DialogComponent dialog;
    private int animAlpha = 0;
    public ModuleComponent(Module module) {
        super(module.getName());
        this.module = module;
        this.width = ClassicClickGUI.screenWidth - ClassicClickGUI.navbarWidth - 15;
        this.height = 28;
        this.dialog = new DialogComponent(module);
    }

    @Override
    public void draw(float xPos, float yPos, int mouseX, int mouseY) {
        super.draw(xPos,yPos,mouseX,mouseY);
        this.width = ClassicClickGUI.screenWidth - ClassicClickGUI.navbarWidth - 15;

        if(componentHover()){
            animAlpha = animator.animate(255,animAlpha,0.1f);
            RoundedUtil.drawRoundOutline(x + 3, y, width, height, 3,0.5f, Theme.moduleTheme,new Color(255,255,255,animAlpha));
        }else {
            animAlpha = animator.animate(0,animAlpha,0.15f);
            RoundedUtil.drawRound(x + 3, y, width, height, 3, Theme.moduleTheme);
        }
        FontLoaders.comfortaaB18.drawStringWithShadow(module.getName(), x + 7, y + height / 2.0f - FontLoaders.comfortaaB18.getHeight() / 2.0f, Theme.font.getRGB());
        if (module.isEnabled()) {
            if (animation >= 10) {
                animation = animator.animate(10, animation, 0.08f);
            }
            RoundedUtil.drawRound(x + width - 30, y + height / 2.0f - 3, 20, 6, 3, Theme.theme);
        } else {
            if (animation <= 30) {
                animation = animator.animate(30, animation, 0.08f);
            }
            RoundedUtil.drawRound(x + width - 30, y + height / 2.0f - 3, 20, 6, 3, Theme.enableButton);
        }
        RenderUtil.drawSmoothCircle(x + width - animation, y + height / 2.0f, 6, new Color(255,255,255));

        RoundedUtil.drawRound((x + width - 70 - 4),  (y + height / 2.0f - 4), 28, 8,2,Theme.theme);
        if(this.equals(UIState.focusKey)){
            FontLoaders.comfortaaB18.drawCenteredStringWithShadow("...",(x + width - 70)+ 10,y + height / 2.0f - FontLoaders.comfortaaB18.getHeight() / 2.0f +1 ,Theme.font.getRGB());
        }else {
            FontLoaders.comfortaaB18.drawCenteredStringWithShadow(Keyboard.getKeyName(this.module.getKey()),(x + width - 70)+10,y + height / 2.0f- FontLoaders.comfortaaB18.getHeight() / 2.0f+1,Theme.font.getRGB());
        }
    }

    @Override
    public void mouse(int mouseButton, MouseType mouseType) {
        if(mouseType == MouseType.CLICK){
            if (isHover((int) (x), (int) (y), width, height, mouseX, mouseY)) {
                if (mouseButton == 1 && !this.dialog.getSubComponents().isEmpty()) {
                    UIState.settingFocused = true;
                    UIState.dialog = this.dialog;
                }
            }
            if (isHover((int) (x + width - 70 - 4), (int) (y + height / 2.0f - 4), 28, 8, mouseX, mouseY)) {
                if (mouseButton == 0) UIState.focusKey = this;
            }
            if(isHover((int) (x + width - 32), (int) (y + height / 2.0f - 3), 24, 8,mouseX,mouseY)){
                if (mouseButton == 0) module.toggled();
            }
        }
    }

    @Override
    public void input(char typedChar, int keyCode) {
        if (this.equals(UIState.focusKey)) {
            if(keyCode != Keyboard.KEY_END)
                this.module.setKey(keyCode);
            else
                this.module.setKey(Keyboard.KEY_NONE);
            UIState.focusKey = null;
        }
    }

    public Module getModule() {
        return module;
    }

    public void setModule(Module module) {
        this.module = module;
    }

}

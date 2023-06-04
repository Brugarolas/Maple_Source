package top.youm.rocchi.core.ui.clickgui.modern.component;

import org.lwjgl.input.Keyboard;
import top.youm.rocchi.core.module.Module;
import top.youm.rocchi.core.ui.clickgui.modern.Component;
import top.youm.rocchi.core.ui.clickgui.modern.ModernClickGUI;
import top.youm.rocchi.core.ui.clickgui.modern.state.UIState;
import top.youm.rocchi.core.ui.clickgui.modern.theme.Theme;
import top.youm.rocchi.core.ui.clickgui.old.MouseType;
import top.youm.rocchi.core.ui.font.FontLoaders;
import top.youm.rocchi.utils.render.RenderUtil;
import top.youm.rocchi.utils.render.RoundedUtil;

public class ModuleComponent extends Component {
    private Module module;
    public float animation = 30;
    public DialogComponent dialog;
    public ModuleComponent(Module module) {
        super(module.getName());
        this.module = module;
        this.width = ModernClickGUI.screenWidth - ModernClickGUI.navbarWidth - 15;
        this.height = 25;
        this.dialog = new DialogComponent(module);
    }

    @Override
    public void draw(float xPos, float yPos, int mouseX, int mouseY) {
        this.x = xPos;
        this.y = yPos;
        this.mouseX = mouseX;
        this.mouseY = mouseY;
        RoundedUtil.drawRound(x + 3, y, width, height, 11, Theme.moduleTheme);
        FontLoaders.comfortaaB18.drawStringWithShadow(module.getName(), x + 7, y + height / 2.0f - FontLoaders.comfortaaB18.getHeight() / 2.0f, Theme.font.getRGB());
        if (module.isToggle()) {
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
        RenderUtil.drawCircle(x + width - animation, y + height / 2.0f, 6, Theme.buttonCircleTheme);

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
                if (mouseButton == 1) {
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
            this.module.setKey(keyCode);
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

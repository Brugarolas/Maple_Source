package top.youm.rocchi.core.ui.clickgui.modern.component;

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
        RoundedUtil.drawRound(x + width - 30, y + height / 2.0f - 3, 20, 6, 3, Theme.enableButton);
        if (module.isToggle()) {
            if (animation >= 10) {
                animation = animator.animate(10, animation, 0.08f);
            }
            RenderUtil.drawCircle(x + width - animation, y + height / 2.0f, 6, Theme.theme);
        } else {
            if (animation <= 30) {
                animation = animator.animate(30, animation, 0.08f);
            }
            RenderUtil.drawCircle(x + width - animation, y + height / 2.0f, 6, Theme.buttonCircleTheme);
        }
    }

    @Override
    public void mouse(int mouseButton, MouseType mouseType) {
        if (isHover((int) (x), (int) (y), width, height, mouseX, mouseY) && mouseType == MouseType.CLICK) {
            if (mouseButton == 0) module.toggled();
            else if (mouseButton == 1) {
                UIState.settingFocused = true;
                UIState.dialog = this.dialog;
            }
        }
    }

    @Override
    public void input(char typedChar, int keyCode) {
    }

    public Module getModule() {
        return module;
    }

    public void setModule(Module module) {
        this.module = module;
    }

}

package top.youm.rocchi.core.ui.clickgui.modern.component;

import org.lwjgl.input.Mouse;
import top.youm.rocchi.core.module.ModuleCategory;
import top.youm.rocchi.core.ui.Component;
import top.youm.rocchi.core.ui.clickgui.modern.ModernClickGUI;
import top.youm.rocchi.core.ui.clickgui.modern.animation.Animation;
import top.youm.rocchi.core.ui.clickgui.modern.animation.Direction;
import top.youm.rocchi.core.ui.clickgui.modern.animation.SmoothStepAnimation;
import top.youm.rocchi.core.ui.clickgui.modern.state.UIState;
import top.youm.rocchi.core.ui.theme.Icon;
import top.youm.rocchi.core.ui.theme.Theme;
import top.youm.rocchi.core.ui.MouseType;
import top.youm.rocchi.core.ui.font.FontLoaders;
import top.youm.rocchi.utils.math.MathUtil;
import top.youm.rocchi.utils.render.RoundedUtil;

import java.util.ArrayList;
import java.util.List;

import static top.youm.rocchi.core.ui.clickgui.modern.ModernClickGUI.navbarWidth;


public class CategoryComponent extends Component {
    public final ModuleCategory category;
    private float scroll;
    private float maxScroll;
    private float minScroll;
    private float rawScroll;
    private Animation scrollAnimation;
    private List<ModuleComponent> moduleComponents = new ArrayList<>();
    private float animation;

    public CategoryComponent(ModuleCategory category) {
        super(category.name().substring(0, 1).toUpperCase() + category.name().substring(1).toLowerCase());
        this.scrollAnimation = new SmoothStepAnimation(0, 0.0, Direction.BACKWARDS);
        this.category = category;
        this.maxScroll = Float.MAX_VALUE;
        this.minScroll = 0.0f;
        this.width = 80;
        this.height = 20;
    }

    @Override
    public void draw(float xPos, float yPos, int mouseX, int mouseY) {
        super.draw(xPos,yPos,mouseX,mouseY);
        if (UIState.currentCategory == category) {
            RoundedUtil.drawRound(x, y, width, height, 2, Theme.theme);
            animation = animator.animate(7, animation, 0.08f);
        } else if (componentHover()) {
            RoundedUtil.drawRound(x, y, width, height, 2, Theme.themeHover);
        }
        if (UIState.currentCategory != category) {
            if (componentHover()) {
                animation = animator.animate(7, animation, 0.08f);
            } else {
                animation = animator.animate(0, animation, 0.08f);
            }
        }
        FontLoaders.icon28.drawStringWithShadow(renderIcon().icon, xPos + 2 + animation, y + height / 2.0f - FontLoaders.robotoR22.getHeight() / 2.0f, Theme.font.getRGB());
        FontLoaders.robotoB22.drawStringWithShadow(name, xPos + 1 + animation + FontLoaders.icon28.getStringWidth(renderIcon().icon) + 3, y + height / 2.0f - FontLoaders.robotoR22.getHeight() / 2.0f, Theme.font.getRGB());

    }
    public Icon renderIcon(){
        switch (this.category){
            case COMBAT:
                return Icon.COMBAT;
            case MOVEMENT:
                return Icon.MOVEMENT;
            case VISUAL:
                return Icon.VISUAL;
            case WORLD:
                return Icon.WORLD;
            default:
                return Icon.PLAYER;
        }
    }
    public void moduleMenu(int mouseX, int mouseY) {
        if (!UIState.settingFocused && isHover((ModernClickGUI.x + ModernClickGUI.navbarWidth), (ModernClickGUI.y + 30), ModernClickGUI.screenWidth - ModernClickGUI.navbarWidth - 10, ModernClickGUI.screenHeight - 30, mouseX, mouseY)) {
            this.onScroll(30);
        }
        int yOffset = 0;
        for (ModuleComponent moduleComponent : moduleComponents) {
            if (moduleComponent.getModule().getCategory() != this.category) {
                continue;
            }
            moduleComponent.draw(ModernClickGUI.x + navbarWidth, (ModernClickGUI.y + 35 + yOffset + getScroll()), mouseX, mouseY);
            yOffset += 35;
        }
    }

    @Override
    public void mouse(int mouseButton, MouseType mouseType) {
        if (mouseType == MouseType.CLICK) {
            if (componentHover() && mouseButton == 0) {
                UIState.currentCategory = this.category;
            }
        }
    }

    @Override
    public void input(char typedChar, int keyCode) {

    }

    public float getScroll() {
        return (float) MathUtil.roundToHalf(scroll());
    }

    public void onScroll(final int ms) {
        this.scroll = (float) (this.rawScroll - this.scrollAnimation.getOutput());
        this.rawScroll += Mouse.getDWheel() / 4.0f;
        this.rawScroll = Math.max(Math.min(this.minScroll, this.rawScroll), -this.maxScroll);
        this.scrollAnimation = new SmoothStepAnimation(ms, this.rawScroll - this.scroll, Direction.BACKWARDS);
    }

    public float scroll() {
        return this.scroll = (float) (this.rawScroll - this.scrollAnimation.getOutput());
    }

    public List<ModuleComponent> getModuleComponents() {
        return moduleComponents;
    }

    public void setModuleComponents(List<ModuleComponent> moduleComponents) {
        this.moduleComponents = moduleComponents;
    }
}

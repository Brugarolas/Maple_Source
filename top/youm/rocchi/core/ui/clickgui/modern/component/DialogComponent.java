package top.youm.rocchi.core.ui.clickgui.modern.component;

import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import top.youm.rocchi.common.settings.*;
import top.youm.rocchi.common.settings.impl.BoolSetting;
import top.youm.rocchi.common.settings.impl.ModeSetting;
import top.youm.rocchi.common.settings.impl.NumberSetting;
import top.youm.rocchi.core.module.Module;
import top.youm.rocchi.core.ui.Component;
import top.youm.rocchi.core.ui.clickgui.modern.animation.Animation;
import top.youm.rocchi.core.ui.clickgui.modern.animation.Direction;
import top.youm.rocchi.core.ui.clickgui.modern.animation.SmoothStepAnimation;
import top.youm.rocchi.core.ui.clickgui.modern.component.settings.*;
import top.youm.rocchi.core.ui.clickgui.modern.state.UIState;
import top.youm.rocchi.core.ui.theme.Theme;
import top.youm.rocchi.core.ui.MouseType;
import top.youm.rocchi.core.ui.font.FontLoaders;
import top.youm.rocchi.utils.math.MathUtil;
import top.youm.rocchi.utils.render.RenderUtil;
import top.youm.rocchi.utils.render.RoundedUtil;

import java.util.ArrayList;
import java.util.List;

public class DialogComponent extends Component {
    private float scroll;
    private float maxScroll;
    private float minScroll;
    private float rawScroll;
    private Animation scrollAnimation;
    private Module module;
    private List<Component> subComponents = new ArrayList<>();
    public DialogComponent(Module module) {
        super(module.getName());
        this.scrollAnimation = new SmoothStepAnimation(0, 0.0, Direction.BACKWARDS);
        this.module = module;
        this.maxScroll = Float.MAX_VALUE;
        this.minScroll = 0.0f;
        this.width = 300;
        this.height = 300;
        for (Setting<?> setting : module.getSettings()) {
            if(setting instanceof BoolSetting){
                subComponents.add(new ButtonComponent((BoolSetting) setting));
            } else if (setting instanceof NumberSetting) {
                subComponents.add(new SliderComponent((NumberSetting) setting));
            }else if (setting instanceof ModeSetting<?>) {
                subComponents.add(new DropdownComponent((ModeSetting<?>) setting));
            }
        }
        this.y = -350;
    }
    boolean isDone = false;
    @Override
    public void draw(float xPos,float yPos,int mouseX, int mouseY) {
        ScaledResolution sr = new ScaledResolution(mc);
        this.x = sr.getScaledWidth() / 2.0f - this.width / 2.0f;
        this.mouseX = mouseX;this.mouseY = mouseY;
        if (isHover((int) x, (int) y, width, height, mouseX, mouseY)) {
            this.onScroll(30);
        }
        GlStateManager.pushMatrix();
        if(!isDone){
            y = (float) animator.animate(sr.getScaledHeight() / 2.0 - this.height / 2.0f,y,0.09f);
            if(y >= (sr.getScaledHeight() / 2.0 - this.height / 2.0f) ){
                isDone = true;
            }
        }else {
            y = (float) (sr.getScaledHeight() / 2.0 - this.height / 2.0f);
        }

        RoundedUtil.drawRound(x,y,width,height,3, Theme.background);
        FontLoaders.robotoR22.drawStringWithShadow(name,x + 5,y + 5,Theme.font.getRGB());
        RenderUtil.startGlScissor((int) x, (int) y + FontLoaders.robotoR22.getHeight() + 5,width,height - (FontLoaders.robotoR22.getHeight() + 5));
        int offsetY = 5;
        if(this.subComponents.isEmpty()){
            FontLoaders.robotoR34.drawCenteredStringWithShadow("No Setting",x + width / 2.0f,y + height / 2.0f- FontLoaders.robotoR34.getHeight() / 2.0f,Theme.font.getRGB());
        }
        for (Component component : subComponents) {
            component.update();
            if(!component.isDisplay()){
               continue;
            }
            FontLoaders.comfortaaB24.drawStringWithShadow(component.getName(),x + 5,y + 16 + offsetY + getScroll(),Theme.font.getRGB());
            component.draw(x + width,y + 25 - 3 + offsetY + getScroll(),mouseX,mouseY);
            offsetY += 30;
        }
        RenderUtil.stopGlScissor();
        GlStateManager.popMatrix();
    }

    @Override
    public void mouse(int mouseButton, MouseType mouseType){
        for (Component component : subComponents) {
            component.mouse(mouseButton,mouseType);
        }
    }

    @Override
    public void input(char typedChar, int keyCode) {
        if(keyCode == Keyboard.KEY_ESCAPE){
            UIState.settingFocused = false;
        }
    }

    public Module getModule() {
        return module;
    }

    public void setModule(Module module) {
        this.module = module;
    }
    public float getScroll() {
        return (float) MathUtil.roundToHalf(scroll());
    }

    public void onScroll(final int ms) {
        this.scroll = (float) (this.rawScroll - this.scrollAnimation.getOutput());
        this.rawScroll += Mouse.getDWheel() / 4.0f;
        this.rawScroll = Math.max(Math.min((this.minScroll + 15.0f), this.rawScroll), -this.maxScroll);
        this.scrollAnimation = new SmoothStepAnimation(ms, this.rawScroll - this.scroll, Direction.BACKWARDS);
    }

    public float scroll() {
        return this.scroll = (float) (this.rawScroll - this.scrollAnimation.getOutput());
    }

    public List<Component> getSubComponents() {
        return subComponents;
    }
}

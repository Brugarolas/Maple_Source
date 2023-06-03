package top.youm.rocchi.core.ui.clickgui.modern.component;

import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;
import top.youm.rocchi.common.settings.BoolSetting;
import top.youm.rocchi.common.settings.ModeSetting;
import top.youm.rocchi.common.settings.NumberSetting;
import top.youm.rocchi.common.settings.Setting;
import top.youm.rocchi.core.module.Module;
import top.youm.rocchi.core.ui.clickgui.modern.Component;
import top.youm.rocchi.core.ui.clickgui.modern.component.settings.ButtonComponent;
import top.youm.rocchi.core.ui.clickgui.modern.component.settings.DropdownComponent;
import top.youm.rocchi.core.ui.clickgui.modern.component.settings.SliderComponent;
import top.youm.rocchi.core.ui.clickgui.modern.state.UIState;
import top.youm.rocchi.core.ui.clickgui.modern.theme.Theme;
import top.youm.rocchi.core.ui.clickgui.old.MouseType;
import top.youm.rocchi.core.ui.font.FontLoaders;
import top.youm.rocchi.utils.render.RenderUtil;
import top.youm.rocchi.utils.render.RoundedUtil;

import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.opengl.GL11.*;


public class DialogComponent extends Component {
    private Module module;
    private float animationNumber;
    public float percentBar;
    private List<Component> subComponents = new ArrayList<>();
    public DialogComponent(Module module) {
        super(module.getName());
        this.module = module;
        this.width = 400;
        this.height = 350;
        for (Setting<?> setting : module.getSettings()) {
            if(setting instanceof BoolSetting){
                subComponents.add(new ButtonComponent((BoolSetting) setting));
            } else if (setting instanceof NumberSetting) {
                subComponents.add(new SliderComponent((NumberSetting) setting));
            }else if (setting instanceof ModeSetting<?>) {
                subComponents.add(new DropdownComponent((ModeSetting<?>) setting));
            }
        }
    }

    @Override
    public void draw(float xPos,float yPos,int mouseX, int mouseY) {
        ScaledResolution sr = new ScaledResolution(mc);
        this.x = sr.getScaledWidth() / 2.0f - this.width / 2.0f;
        this.y = sr.getScaledHeight() / 2.0f - this.height / 2.0f;
        this.mouseX = mouseX;this.mouseY = mouseY;
        RoundedUtil.drawRound(x,y,width,height,3, Theme.background);
        FontLoaders.robotoR22.drawStringWithShadow(name,x + 5,y + 5,Theme.font.getRGB());
        RenderUtil.startGlScissor((int) x, (int) y,width,height);
        int offsetY = 0;
        for (Component component : subComponents) {
            component.draw(x + width,y+25-3 + offsetY,mouseX,mouseY);
            GlStateManager.enableDepth();
            GL11.glClear(GL_DEPTH_BUFFER_BIT);
            glDepthMask(false);
            if(component instanceof DropdownComponent){
                ((DropdownComponent)component).render();
            }
            GlStateManager.disableDepth();
            offsetY+=30;
        }
        RenderUtil.stopGlScissor();
    }

    @Override
    public void mouse(int mouseButton, MouseType mouseType){
        ScaledResolution sr = new ScaledResolution(mc);
        float x = sr.getScaledWidth() / 2.0f - this.width / 2.0f;
        float y = sr.getScaledHeight() / 2.0f - this.height / 2.0f;
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
}

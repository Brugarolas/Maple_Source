package top.youm.rocchi.core.ui.clickgui.modern.component;

import org.lwjgl.opencl.CL;
import top.youm.rocchi.Rocchi;
import top.youm.rocchi.core.module.Module;
import top.youm.rocchi.core.module.modules.visual.ClickGui;
import top.youm.rocchi.core.ui.clickgui.modern.Screen;
import top.youm.rocchi.core.ui.clickgui.modern.theme.Theme;
import top.youm.rocchi.core.ui.clickgui.old.MouseType;
import top.youm.rocchi.core.ui.font.FontLoaders;
import top.youm.rocchi.utils.render.RoundedUtil;

public class ModuleComponent extends Component{
    private Module module;
    public ModuleComponent(Module module) {
        super(module.getName());
        this.module = module;
        this.width = Screen.screenWidth - Screen.navbarWidth - 15;
        this.height = 25;
    }

    @Override
    public void draw(float xPos, float yPos, int mouseX, int mouseY) {
        this.x = xPos;this.y = yPos;
        this.mouseX = mouseX;this.mouseY = mouseY;
        RoundedUtil.drawRound(x,y,width,height,3, Theme.moduleTheme);
        FontLoaders.comfortaaB18.drawStringWithShadow(module.getName(),x + 4,y + height / 2.0f - FontLoaders.comfortaaB18.getHeight() / 2.0f,Theme.font.getRGB());
    }

    @Override
    public void mouse(int mouseButton, MouseType mouseType) {
        if(mouseType == MouseType.CLICK && mouseButton == 1){
            ClickGui moduleByClass = Rocchi.getInstance().getModuleManager().getModuleByClass(ClickGui.class);
            moduleByClass.screen.updateDialog(this.module);
        }
    }

    public Module getModule() {
        return module;
    }

    public void setModule(Module module) {
        this.module = module;
    }
}

package top.youm.rocchi.core.ui.clickgui.modern.component;

import com.darkmagician6.eventapi.events.Event;
import top.youm.rocchi.core.module.ModuleCategory;
import top.youm.rocchi.core.ui.clickgui.modern.state.UIState;
import top.youm.rocchi.core.ui.clickgui.modern.theme.Theme;
import top.youm.rocchi.core.ui.clickgui.old.ClickGuiScreen;
import top.youm.rocchi.core.ui.clickgui.old.MouseType;
import top.youm.rocchi.core.ui.font.FontLoaders;
import top.youm.rocchi.utils.render.RoundedUtil;


public class CategoryComponent extends Component{
    public final ModuleCategory category;
    public CategoryComponent(ModuleCategory category) {
        super(category.name().substring(0, 1).toUpperCase() + category.name().substring(1).toLowerCase());
        this.category = category;
        this.width = 80;
        this.height = 20;
    }
    @Override
    public void draw(float xPos, float yPos,int mouseX, int mouseY) {
        this.x = xPos;this.y = yPos;
        this.mouseX = mouseX;this.mouseY = mouseY;
        if(UIState.currentCategory == category){
            RoundedUtil.drawRound(x,y,width,height,2, Theme.theme);
        }else if(componentHover()){
            RoundedUtil.drawRound(x,y,width,height,2, Theme.themeHover);
        }
        FontLoaders.robotoB22.drawCenteredStringWithShadow(name,xPos + width / 2.0f,y + height / 2.0f - FontLoaders.robotoR22.getHeight() / 2.0f,Theme.font.getRGB());
    }

    @Override
    public void mouse(int mouseButton, MouseType mouseType) {
        if(mouseType == MouseType.CLICK) {
            if (componentHover() && mouseButton == 0) {
                UIState.currentCategory = this.category;
            }
        }
    }

}

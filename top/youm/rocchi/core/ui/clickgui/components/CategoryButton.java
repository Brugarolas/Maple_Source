package top.youm.rocchi.core.ui.clickgui.components;

import top.youm.rocchi.core.module.ModuleCategory;
import top.youm.rocchi.core.ui.Component;
import top.youm.rocchi.core.ui.clickgui.ClickGuiScreen;
import top.youm.rocchi.core.ui.clickgui.MouseType;
import top.youm.rocchi.core.ui.font.FontLoaders;
import top.youm.rocchi.utils.AnimationUtils;
import top.youm.rocchi.utils.render.RenderUtil;

import java.awt.*;


public class CategoryButton extends Component {
    public final ModuleCategory category;
    public int wheel = 0;
    public boolean isThis = false;
    public CategoryButton(ModuleCategory category) {
        super(category.name().substring(0, 1).toUpperCase() + category.name().substring(1).toLowerCase());
        this.category = category;
    }
    @Override
    public void draw(float xPos, float yPos,int mouseX, int mouseY) {
        this.x = xPos;this.y = yPos;
        this.mouseX = mouseX;this.mouseY = mouseY;
        if (ClickGuiScreen.moduleCategory == this.category) {
            ClickGuiScreen.animCategory = animator.animate(x,ClickGuiScreen.animCategory,0.08f);
            isThis = ClickGuiScreen.isDragging;
            RenderUtil.drawRect(isThis ? (int) x :(int) ClickGuiScreen.animCategory, (int) y, 64, 20, new Color(181, 70, 255));
        }
        FontLoaders.comfortaaR22.drawStringWithShadow(this.name, x + 32 - FontLoaders.comfortaaR22.getStringWidth(this.name) / 2.0, y + 10 - FontLoaders.comfortaaR22.getHeight() / 2.0f, new Color(248, 248, 248).getRGB());
    }

    @Override
    public void mouse(int mouseX, int mouseY,int mouseButton, MouseType mouseType) {
        if(mouseType != MouseType.CLICK) {
            if (isHover((int) x, (int) y, 64, 20, this.mouseX, this.mouseY) && mouseButton == 0) {
                ClickGuiScreen.moduleCategory = this.category;
                ClickGuiScreen.wheel = 0;
            }
        }
    }

}

package top.youm.maple.core.ui.clickgui.common.sub.button;

import net.minecraft.client.Minecraft;
import top.youm.maple.core.ui.clickgui.classic.ClassicClickGUI;
import top.youm.maple.utils.AnimationUtils;

import static top.youm.maple.core.ui.clickgui.classic.ClassicClickGUI.screenHeight;

/**
 * @author YouM
 * Created on 2023/8/22
 */
public abstract class ButtonComponent {
    protected Minecraft mc = Minecraft.getMinecraft();
    public float x, y, width, height,mouseX,mouseY;
    private String name;
    public float offsetY;
    protected AnimationUtils animator = new AnimationUtils();
    public boolean disable = false;
    public ButtonComponent(String name, float width, float height) {
        this.name = name;
        this.width = width;
        this.height = height;
    }

    public abstract void drawComponent() ;

    public abstract void onMouseClick(int mouseButton);

    public abstract void onMouseRelease(int mouseButton);

    public void update(float x,float y,int mouseX,int mouseY){
        this.x = x;
        this.y = y;
        this.mouseX = mouseX;
        this.mouseY = mouseY;

    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public static boolean isHovered(float x, float y, float width, float height, float mouseX, float mouseY) {
        return mouseX >= x && mouseY >= y && mouseX <= x + width && mouseY <= y + height;
    }
    protected boolean onComponentHover() {
        return isHovered(x,y,width,height,mouseX,mouseY);
    }
}

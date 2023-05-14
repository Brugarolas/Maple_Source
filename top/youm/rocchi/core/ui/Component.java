package top.youm.rocchi.core.ui;

import net.minecraft.client.Minecraft;
import top.youm.rocchi.core.ui.clickgui.MouseType;
import top.youm.rocchi.utils.AnimationUtils;

public abstract class Component {
    protected Minecraft mc = Minecraft.getMinecraft();
    protected final String name;
    protected int width,height;
    protected float x,y;
    protected int mouseX,mouseY;
    protected AnimationUtils animator = new AnimationUtils();

    public Component(String name) {
        this.name = name;
    }

    protected boolean isHover(int x, int y, int width, int height, int mouseX, int mouseY){
        return mouseX >= x && mouseX <= x + width && mouseY > y && mouseY <= y + height;
    }
    public abstract void draw(float xPos,float yPos,int mouseX, int mouseY);
    public abstract void mouse(int mouseX, int mouseY,int mouseButton, MouseType mouseType);

    public String getName() {
        return name;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }
}

package top.youm.rocchi.core.ui;

import net.minecraft.client.Minecraft;
import top.youm.rocchi.utils.AnimationUtils;

public abstract class Component {
    protected Minecraft mc = Minecraft.getMinecraft();
    protected final String name;
    protected int width, height;
    protected float x, y;
    protected int mouseX, mouseY;
    protected AnimationUtils animator = new AnimationUtils();

    public Component(String name) {
        this.name = name;
    }
    public void draw(float xPos, float yPos, int mouseX, int mouseY){
        this.x = xPos;
        this.y = yPos;
        this.mouseX = mouseX;
        this.mouseY = mouseY;
    }
    public abstract void mouse(int mouseButton, MouseType mouseType);
    public abstract void input(char typedChar, int keyCode);
    public static boolean isHover(int x, int y, int width, int height, int mouseX, int mouseY) {
        return mouseX >= x && mouseX <= x + width && mouseY > y && mouseY <= y + height;
    }

    protected boolean componentHover() {
        return isHover((int) this.x, (int) this.y, this.width, this.height, this.mouseX, this.mouseY);
    }

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

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }
}

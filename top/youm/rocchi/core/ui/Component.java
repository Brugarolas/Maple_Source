package top.youm.rocchi.core.ui;

import net.minecraft.client.Minecraft;

public abstract class Component {
    protected Minecraft mc = Minecraft.getMinecraft();
    protected final String name;
    protected float x,y;
    protected int mouseX,mouseY;

    public Component(String name) {
        this.name = name;
    }

    protected boolean isHover(int x, int y, int width, int height, int mouseX, int mouseY){
        return mouseX >= x && mouseX <= x + width && mouseY > y && mouseY <= y + height;
    }
    public abstract void draw(float xPos,float yPos);
    public abstract void mouse(int mouseX, int mouseY,int mouseButton);

    public String getName() {
        return name;
    }
}

package top.youm.rocchi.core.ui.clickgui.music.layout.container;

import top.youm.rocchi.core.ui.Component;
import top.youm.rocchi.core.ui.MouseType;
import top.youm.rocchi.core.ui.clickgui.music.layout.Layout;
import top.youm.rocchi.utils.render.RenderUtil;
import top.youm.rocchi.utils.render.RoundedUtil;

import java.awt.*;
import java.util.List;
import java.util.Objects;

public class Container extends Component{
    private Layout layout;
    private float ContainerX, ContainerY,maxWidth,maxHeight,gutter;
    private List<? extends Component> components;
    private float currentX,currentY;
    private Color color;
    public Container(Layout layout,List<? extends Component> components,float gutter) {
        super("Container");
        this.gutter = gutter;
        this.layout = layout;
        this.components = components;
    }
    public void build(int x, int y, float maxWidth, float maxHeight,int mouseX,int mouseY, Color color){
        this.ContainerX = x;this.ContainerY = y;
        this.maxWidth = maxWidth;this.maxHeight = maxHeight;
        this.currentX = x + gutter;
        this.currentY = y + gutter;
        this.color = color;
        this.draw(x,y, mouseX,  mouseY);
        if (Objects.requireNonNull(layout) == Layout.Row) {
            for (Component component : components) {
                this.rowSort(component);
            }
            this.currentX = x + gutter;
        }
    }
    public void rowSort(Component component){
        /*
            判断当前的的宽度是否 >= 最大宽度
            "currentX - x + gutter" 当前的currentX位置 - (最开始的位置x + 间隔gutter)
         */
        if((currentX - ContainerX) + component.getWidth() >= maxWidth){
            currentX = ContainerX + gutter; // currentX 设置回最开始的位置
            currentY += component.getHeight() + gutter;// currentY 加上 组件的宽度和 间隔 gutter
        }
        component.setX(currentX);
        component.setContainerY(currentY);
        currentX += component.getWidth() + gutter;
    }

    public List<? extends Component> getComponents() {
        return components;
    }

    public void setComponents(List<? extends Component> components) {
        this.components = components;
    }

    public float getMaxHeight() {
        return maxHeight;
    }

    public void setMaxHeight(float maxHeight) {
        this.maxHeight = maxHeight;
    }
    @Override
    public void draw(float xPos, float yPos, int mouseX, int mouseY) {
        super.draw(xPos, yPos, mouseX, mouseY);
        RenderUtil.startGlScissor((int) xPos, (int) yPos,(int) maxWidth,(int) maxHeight);
        RoundedUtil.drawRound(xPos,yPos,maxWidth,maxHeight,2, color);
        for (Component component : components) {
            component.draw(0,0,mouseX,mouseY);
        }
        RenderUtil.stopGlScissor();
    }
    @Override
    public void mouse(int mouseButton, MouseType mouseType) {
        if(isHover((int) x, (int) y, (int) maxWidth, (int) maxHeight,mouseX,mouseY)) {
            for (Component component : components) {
                component.mouse(mouseButton, mouseType);
            }
        }
    }

    @Override
    public void input(char typedChar, int keyCode) {

    }

    public float getContainerX() {
        return ContainerX;
    }

    public void setContainerX(float ContainerX) {
        this.ContainerX = ContainerX;
    }

    public float getContainerY() {
        return ContainerY;
    }

    public void setContainerY(float containerY) {
        this.ContainerY = containerY;
    }
}

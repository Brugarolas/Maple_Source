package top.youm.rocchi.core.ui.clickgui.music.layout.container;

import top.youm.rocchi.core.ui.Component;
import top.youm.rocchi.core.ui.clickgui.music.layout.Layout;

import java.util.List;
import java.util.Objects;

public class Container {
    private Layout layout;
    private float x,y,maxWidth,maxHeight,gutter;
    private List<? extends Component> components;
    private float currentX,currentY;
    public Container(Layout layout,List<? extends Component> components,float gutter) {
        this.gutter = gutter;
        this.layout = layout;
        this.components = components;
    }
    public void build(int x,int y,float maxWidth,float maxHeight){
        this.x = x;this.y = y;
        this.maxWidth = maxWidth;this.maxHeight = maxHeight;
        this.currentX = x;
        this.currentY = y;
        if (Objects.requireNonNull(layout) == Layout.Row) {
            for (Component component : components) {
                this.rowSort(component);
            }
            this.currentX = x;
        } else {
            for (Component component : components) {
                this.columnSort(component);
            }
            this.currentY = y;
        }
    }
    public void rowSort(Component component){
        /*
            判断当前的的宽度是否 >= 最大宽度
            "currentX - x + gutter" 当前的currentX位置 - (最开始的位置x + 间隔gutter)
         */
        if(currentX - x + gutter >= maxWidth){
            currentX = x; // currentX 设置回最开始的位置
            currentY += component.getHeight() + gutter;// currentY 加上 组件的宽度和 间隔 gutter
        }
        component.setX(currentX);
        component.setY(currentY);
        currentX += component.getWidth() + gutter;
    }
    public void columnSort(Component component){

    }
    public Layout getLayout() {
        return layout;
    }

    public void setLayout(Layout layout) {
        this.layout = layout;
    }

    public List<? extends Component> getComponents() {
        return components;
    }

    public void setComponents(List<? extends Component> components) {
        this.components = components;
    }

    public float getGutter() {
        return gutter;
    }

    public void setGutter(float gutter) {
        this.gutter = gutter;
    }

    public float getMaxWidth() {
        return maxWidth;
    }

    public void setMaxWidth(float maxWidth) {
        this.maxWidth = maxWidth;
    }

    public float getMaxHeight() {
        return maxHeight;
    }

    public void setMaxHeight(float maxHeight) {
        this.maxHeight = maxHeight;
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

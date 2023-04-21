package me.youm.rocchi.core.ui;

public interface IComponent {
    default boolean isHover(int x, int y, int width, int height, int mouseX, int mouseY){
        return mouseX >= x && mouseX <= x + width && mouseY > y && mouseY <= y + height;
    }
    void draw(float xPos,float yPos);
    void mouse(int mouseX, int mouseY,int mouseButton);

}

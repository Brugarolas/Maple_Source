package top.youm.rocchi.core.ui.clickgui.old.components;


import org.lwjgl.input.Mouse;
import top.youm.rocchi.common.settings.NumberSetting;
import top.youm.rocchi.core.ui.Component;
import top.youm.rocchi.core.ui.Theme;
import top.youm.rocchi.core.ui.clickgui.old.ClickGuiScreen;
import top.youm.rocchi.core.ui.clickgui.old.MouseType;
import top.youm.rocchi.core.ui.font.FontLoaders;
import top.youm.rocchi.utils.render.RenderUtil;
import top.youm.rocchi.utils.render.RoundedUtil;

import java.awt.*;


public class NumberComponent extends Component {
    private NumberSetting setting;
    public float percentBar;
    public boolean dragging;

    public NumberComponent(NumberSetting setting) {
        super(setting.getName());
        this.setting = setting;
        this.width = 130;
        this.height = 2;
    }
    private float animX;
    @Override
    public void draw(float xPos, float yPos,int mouseX, int mouseY) {
        this.x = xPos;this.y = yPos;
        this.mouseX = mouseX;this.mouseY = mouseY;
        drawSlider();
    }
    public void drawSlider(){
        this.percentBar = this.width * ((setting.getValue().floatValue() - setting.getMin().floatValue()) / (setting.getMax().floatValue() - setting.getMin().floatValue()));

        animX = animator.animate( percentBar,animX,0.05f);
        RoundedUtil.drawRound(ClickGuiScreen.x + ClickGuiScreen.screenWidth - 150, (int) (ClickGuiScreen.y + 50 + y + 10),this.width,2,2,new Color(231, 231, 231));
        RoundedUtil.drawRound(ClickGuiScreen.x + ClickGuiScreen.screenWidth - 150, (int) (ClickGuiScreen.y + 50 + y + 10),animX,2,2, Theme.titleColor);
        RenderUtil.drawCircle(ClickGuiScreen.x + ClickGuiScreen.screenWidth - 150 + animX, ClickGuiScreen.y + 50 + y + 11,5,Theme.titleColor);

        if (isHover(ClickGuiScreen.x + ClickGuiScreen.screenWidth - 150, (int) (ClickGuiScreen.y + 50 + y + 8), width, 5, mouseX, mouseY) ) {
            setValue();
        }
        FontLoaders.comfortaaT18.drawStringWithShadow(String.valueOf(setting.getValue().floatValue()),ClickGuiScreen.x + ClickGuiScreen.screenWidth - 150 + 55 ,(int) (ClickGuiScreen.y + 50 + y),new Color(255, 255, 255).getRGB());

    }
    public void setValue(){
        if(Mouse.isButtonDown(0)){
            float min = setting.getMin().floatValue();
            float max = setting.getMax().floatValue();
            float inc = setting.getInc().floatValue();
            float valAbs = mouseX - (ClickGuiScreen.x + ClickGuiScreen.screenWidth - 150);
            float perc = valAbs / this.width;
            perc =  Math.min(Math.max(0.0f, perc), 1.0f);
            float valRel = (max - min) * perc;
            float val = min + valRel;
            val = Math.round(val * (1 / inc)) / (1 / inc);
            setting.setValue(val);
        }
    }
    @Override
    public void mouse(int mouseX, int mouseY, int mouseButton, MouseType mouseType) {}

    public void setSetting(NumberSetting setting) {
        this.setting = setting;
    }

    public NumberSetting getSetting() {
        return setting;
    }
}

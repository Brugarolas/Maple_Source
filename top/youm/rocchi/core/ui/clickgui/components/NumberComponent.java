package top.youm.rocchi.core.ui.clickgui.components;

import net.minecraft.client.renderer.entity.Render;
import net.minecraft.network.Packet;
import top.youm.rocchi.common.settings.NumberSetting;
import top.youm.rocchi.core.ui.Component;
import top.youm.rocchi.core.ui.Theme;
import top.youm.rocchi.core.ui.clickgui.ClickGuiScreen;
import top.youm.rocchi.core.ui.clickgui.MouseType;
import top.youm.rocchi.core.ui.font.FontLoaders;
import top.youm.rocchi.utils.AnimationUtils;
import top.youm.rocchi.utils.render.RenderUtil;
import top.youm.rocchi.utils.render.RoundedUtil;

import java.awt.*;
import java.math.BigDecimal;
import java.math.RoundingMode;


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
    private int animX;
    @Override
    public void draw(float xPos, float yPos) {
        this.x = xPos;this.y = yPos;

        this.percentBar = this.width * (setting.getValue().floatValue() - setting.getMin().floatValue()) / (setting.getMax().floatValue() - setting.getMin().floatValue());
        drawSlider();
    }
    public void drawSlider(){
        animX = AnimationUtils.animateI((int) percentBar,animX,0.08f);

        RoundedUtil.drawRound(ClickGuiScreen.x + ClickGuiScreen.screenWidth - 150, (int) (ClickGuiScreen.y + 50 + y + 10),130,2,2,new Color(231, 231, 231));
        RoundedUtil.drawRound(ClickGuiScreen.x + ClickGuiScreen.screenWidth - 150, (int) (ClickGuiScreen.y + 50 + y + 10),animX,2,2, Theme.titleColor);
        RenderUtil.drawCircle(ClickGuiScreen.x + ClickGuiScreen.screenWidth - 150 + animX, ClickGuiScreen.y + 50 + y + 11,5,Theme.titleColor);
        if(dragging){
            setValue();
            RenderUtil.drawRect(ClickGuiScreen.x + ClickGuiScreen.screenWidth - 150 + 55,(int) (ClickGuiScreen.y + 50 + y + 10) - 17,20,7,-1);
            FontLoaders.comfortaaT18.drawStringWithShadow(String.valueOf(setting.getValue().floatValue()),ClickGuiScreen.x + ClickGuiScreen.screenWidth - 150 + 55 ,(int) (ClickGuiScreen.y + 50 + y + 10) - 10,new Color(0,0,0).getRGB());
        }
    }
    public void setValue(){
        float min = setting.getMin().floatValue();
        float max = setting.getMax().floatValue();
        float inc = setting.getInc().floatValue();
        float valAbs = mouseX - (ClickGuiScreen.x + ClickGuiScreen.screenWidth - 150);
        float perc = valAbs / (((ClickGuiScreen.x + ClickGuiScreen.screenWidth - 20) - (ClickGuiScreen.x + ClickGuiScreen.screenWidth - 150 )));
        perc =  Math.min(Math.max(0.0f, perc), 1.0f);
        float valRel = (max - min) * perc;
        float val = min + valRel;
        val = Math.round(val * (1.0f / inc)) / (1.0f / inc);
        BigDecimal b = new BigDecimal(val);
        double f1 = b.setScale(2, RoundingMode.HALF_UP).doubleValue();
        setting.setValue(f1);
    }
    @Override
    public void mouse(int mouseX, int mouseY, int mouseButton, MouseType mouseType) {
        this.mouseX = mouseX;this.mouseY = mouseY;
        if (isHover(ClickGuiScreen.x + ClickGuiScreen.screenWidth - 150, (int) (ClickGuiScreen.y + 50 + y + 8), width, 5, mouseX, mouseY) && mouseButton == 0 && mouseType == MouseType.CLICK) {
            dragging = true;
        }else {
            if(mouseType == MouseType.RELEASED){
                dragging =false;
            }
        }
    }

    public void setSetting(NumberSetting setting) {
        this.setting = setting;
    }

    public NumberSetting getSetting() {
        return setting;
    }
}

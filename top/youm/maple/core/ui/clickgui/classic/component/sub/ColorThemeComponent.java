package top.youm.maple.core.ui.clickgui.classic.component.sub;

import top.youm.maple.common.settings.impl.ColorThemeSetting;
import top.youm.maple.core.module.modules.visual.ClickGui;
import top.youm.maple.core.ui.clickgui.classic.MouseType;
import top.youm.maple.core.ui.clickgui.classic.component.Component;
import top.youm.maple.core.ui.clickgui.classic.theme.ColorTheme;
import top.youm.maple.core.ui.clickgui.classic.theme.Theme;
import top.youm.maple.utils.render.RoundedUtil;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author YouM
 * Created on 2023/8/27
 */
public class ColorThemeComponent extends Component {
    private final ColorThemeSetting setting;
    private final List<ThemeBox> themeBoxes = new ArrayList<>();
    public ColorThemeComponent(ColorThemeSetting setting) {
        super("themes");
        this.setting = setting;
        this.width = 80;
        this.height = 60;
        setting.getValue().forEach((name,color)->{
            themeBoxes.add(new ThemeBox(color.getTheme(),name));
        });
    }

    @Override
    public void update() {
        this.display = this.setting.canDisplay();
    }

    @Override
    public void draw(float xPos, float yPos, int mouseX, int mouseY) {
        super.draw(xPos, yPos, mouseX, mouseY);
        float offsetX = 0;
        for (ThemeBox themeBox : themeBoxes) {
            if(isHover((int) (xPos + offsetX), (int) yPos,width,height,mouseX,mouseY)){
                themeBox.updateAlpha(true);
                RoundedUtil.drawRoundOutline(xPos + offsetX,yPos,this.width,this.height,1,0.5f,themeBox.color,new Color(255,255,255,themeBox.animAlpha));
            }else{
                themeBox.updateAlpha(false);
                RoundedUtil.drawRound(xPos + offsetX,yPos,this.width,this.height,1,themeBox.color);
            }
            offsetX += 20 + width;
        }

    }

    @Override
    public void mouse(int mouseButton, MouseType mouseType) {
        float offsetX = 0;
        for (Map.Entry<String, ColorTheme> themeEntry : setting.getValue().entrySet()) {
            if(isHover((int) (x + offsetX), (int) y,width,height,mouseX,mouseY) && mouseType == MouseType.CLICK){
                this.setting.setCurrentTheme(themeEntry.getValue());
                setTheme();
            }
            offsetX += 20 + width;
        }
    }

    @Override
    public void input(char typedChar, int keyCode) {

    }
    public void setTheme(){
        Theme.theme = setting.getCurrentTheme().getTheme();
        Theme.themeHover = setting.getCurrentTheme().getThemeHover();
        Theme.moduleTheme = setting.getCurrentTheme().getModuleTheme();
        Theme.enableButton = setting.getCurrentTheme().getEnableButton();
    }
    class ThemeBox{
        public int animAlpha;
        public Color color;
        public String name;

        public ThemeBox(Color color, String name) {
            this.color = color;
            this.name = name;
        }

        public void updateAlpha(boolean fadeIn){
            if(fadeIn){
                animAlpha = animator.animate(255,animAlpha,0.1f);
            }else{
                animAlpha = animator.animate(0,animAlpha,0.1f);
            }
        }

    }
}

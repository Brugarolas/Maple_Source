package top.youm.rocchi.core.ui.clickgui.old.components;

import top.youm.rocchi.common.settings.ModeSetting;
import top.youm.rocchi.core.ui.Component;
import top.youm.rocchi.core.ui.Theme;
import top.youm.rocchi.core.ui.clickgui.old.ClickGuiScreen;
import top.youm.rocchi.core.ui.clickgui.old.MouseType;
import top.youm.rocchi.core.ui.font.FontLoaders;
import top.youm.rocchi.utils.render.RenderUtil;

import java.awt.*;


public class ModeComponent extends Component {
    public ModeSetting<Enum<?>> setting;
    private boolean open;
    public ModeComponent(ModeSetting<Enum<?>> setting) {
        super(setting.getName());
        this.setting = setting;
    }

    @Override
    public void draw(float xPos, float yPos,int mouseX, int mouseY) {
        this.x = xPos;this.y = yPos;
        this.mouseX = mouseX;this.mouseY = mouseY;
        RenderUtil.drawRect(ClickGuiScreen.x + ClickGuiScreen.screenWidth - 100, (int) (ClickGuiScreen.y + 50 + y + 10),80,12,new Color(85, 178, 255));
        FontLoaders.comfortaaT18.drawString(setting.getValue().name(),ClickGuiScreen.x + ClickGuiScreen.screenWidth - 100 + (80/2.0f)-(FontLoaders.comfortaaT18.getStringWidth(setting.getValue().name()) / 2.0f),(ClickGuiScreen.y + 50 + y + 10) + 8 - FontLoaders.comfortaaT18.getHeight() / 2.0f,Theme.FONT_COLOR.getRGB());
        if(isHover(ClickGuiScreen.x + ClickGuiScreen.screenWidth - 100, (int) (ClickGuiScreen.y + 50 + y + 10),80,12,mouseX,mouseY)){
            FontLoaders.comfortaaT18.drawStringWithShadow(setting.getValue().name(),ClickGuiScreen.x + ClickGuiScreen.screenWidth - 100 + (80/2.0f)-(FontLoaders.comfortaaT18.getStringWidth(setting.getValue().name()) / 2.0f),(ClickGuiScreen.y + 50 + y + 10) + 8 - FontLoaders.comfortaaT18.getHeight() / 2.0f,-1);
        }

//        else {
//            int heightOffset = 0;
//            for (Enum<?> anEnum : setting.getEnums()) {
//                if(anEnum.name().equals(setting.getValue().name())){
//                    continue;
//                }
//                FontLoaders.comfortaaT18.drawString(anEnum.name(),ClickGuiScreen.x + ClickGuiScreen.screenWidth - 80 + 60/2.0f-FontLoaders.comfortaaT18.getStringWidth(setting.getValue().name()),ClickGuiScreen.y + 50 + y + 20 - FontLoaders.comfortaaT18.getHeight() / 2.0f + heightOffset,Theme.FONT_COLOR.getRGB());
//                heightOffset += 20;
//            }
//            RoundedUtil.drawRoundOutline(ClickGuiScreen.x + ClickGuiScreen.screenWidth - 80,ClickGuiScreen.y + 50 + y + 10,60,20 + heightOffset,1,1,new Color(85, 178, 255), Theme.FONT_COLOR);
//
//            if(isHover(ClickGuiScreen.x + ClickGuiScreen.screenWidth - 80,ClickGuiScreen.y + 50 + (int)y + 10,60,20,mouseX,mouseY)){
//                FontLoaders.comfortaaT18.drawStringWithShadow(setting.getValue().name(),ClickGuiScreen.x + ClickGuiScreen.screenWidth - 80 + 60/2.0f-FontLoaders.comfortaaT18.getStringWidth(setting.getValue().name()),ClickGuiScreen.y + 50 + y + 20 - FontLoaders.comfortaaT18.getHeight() / 2.0f,new Color(255,255,255).getRGB());
//            }
//        }

    }

    @Override
    public void mouse(int mouseX, int mouseY, int mouseButton, MouseType mouseType) {
        if(mouseType != MouseType.CLICK) {
            if (isHover(ClickGuiScreen.x + ClickGuiScreen.screenWidth - 100, (int) (ClickGuiScreen.y + 50 + y + 10), 80, 12, mouseX, mouseY) && mouseButton == 0) {

                Enum<?> current = setting.getValue();
                setting.setValueEnum(setting.getEnums()[current.ordinal() - 1 < 0 ? setting.getEnums().length - 1 : current.ordinal() - 1]);

            }
        }
    }
}

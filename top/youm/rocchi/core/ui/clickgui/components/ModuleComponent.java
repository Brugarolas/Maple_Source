package top.youm.rocchi.core.ui.clickgui.components;

import top.youm.rocchi.Rocchi;
import top.youm.rocchi.common.settings.BoolSetting;
import top.youm.rocchi.common.settings.ModeSetting;
import top.youm.rocchi.common.settings.NumberSetting;
import top.youm.rocchi.common.settings.Setting;
import top.youm.rocchi.core.module.Module;
import top.youm.rocchi.core.ui.Component;
import top.youm.rocchi.core.ui.clickgui.ClickGuiScreen;
import top.youm.rocchi.core.ui.clickgui.MouseType;
import top.youm.rocchi.core.ui.font.FontLoaders;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.util.ResourceLocation;
import top.youm.rocchi.utils.render.RoundedUtil;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;



public class ModuleComponent extends Component {
    private Module module;
    public int wheel;
    private final List<Component> subComponents = new ArrayList<>();
    public ModuleComponent(Module module) {
        super(module.getName());
        this.module = module;
        for (Setting<?> setting : module.getSettings()) {
            if (setting instanceof BoolSetting) {
                subComponents.add(new BooleanComponent((BoolSetting) setting));
            }else if(setting instanceof ModeSetting<?>){
                subComponents.add(new ModeComponent((ModeSetting<Enum<?>>)setting));
            }else if(setting instanceof NumberSetting){
                subComponents.add(new NumberComponent((NumberSetting) setting));
            }
        }
    }
    @Override
    public void draw(float xPos, float yPos,int mouseX, int mouseY) {
        this.x = xPos;this.y = yPos;
        this.mouseX = mouseX;this.mouseY = mouseY;
        if(isHover((int) (x - 44), (int) (y - 8 + FontLoaders.robotoR22.getHeight() / 2.0f),88,18,mouseX,mouseY)){
            RoundedUtil.drawRound((int) (x - 44), (int) (y - 8 + FontLoaders.robotoR22.getHeight() / 2.0f),88,18,4,new Color(55, 128, 255));
        } else if(module.isToggle()){
            RoundedUtil.drawRound((int) (x - 44), (int) (y - 8 + FontLoaders.robotoR22.getHeight() / 2.0f),88,18,4,new Color(55, 128, 255));
        } else {
            RoundedUtil.drawRound((int) (x - 44), (int) (y - 8 + FontLoaders.robotoR22.getHeight() / 2.0f),88,18,4,new Color(175, 212, 255));
        }
        FontLoaders.robotoR22.drawCenteredStringWithShadow(module.getName(),x,y,-1);

    }
    public void renderSetting(){
        if(this.module.equals(ClickGuiScreen.settingOpen)){
            int offsetY = 0;
            if(subComponents.size() == 0){
                FontLoaders.comfortaaR24.drawStringWithShadow("No setting in this module", (ClickGuiScreen.x + 110 + ClickGuiScreen.menuWidht / 2.0f) - (FontLoaders.comfortaaR24.getStringWidth("No setting in this module") / 2.0f) + 10,ClickGuiScreen.y + ClickGuiScreen.screenHeight / 2.0f - FontLoaders.comfortaaR24.getHeight() ,-1);
            }else {
                for (Component subComponent : subComponents) {
                    FontLoaders.comfortaaR20.drawStringWithShadow(subComponent.getName(), ClickGuiScreen.x + ClickGuiScreen.screenWidth - ClickGuiScreen.menuWidht + 2,ClickGuiScreen.y + 62 + offsetY + wheel ,-1);
                    subComponent.draw(0,offsetY + wheel,mouseX,mouseY);
                    offsetY += 25;
                }
            }
        }

    }
    @Override
    public void mouse(int mouseX, int mouseY, int mouseButton,MouseType mouseType) {
        if(mouseType != MouseType.CLICK){
            if(isHover((int) x - 45, (int) y - 5,90,20,mouseX,mouseY) ){
                if(mouseButton == 0) {
                    Rocchi.getInstance().getModuleManager().modules.forEach(module -> {
                        if (module.getName().equals(this.module.getName())) {
                            module.setToggle(!module.isToggle());
                            this.mc.getSoundHandler().playSound(PositionedSoundRecord.create(new ResourceLocation("gui.button.press"), 1.0F));
                        }
                    });
                } else if(mouseButton == 1){
                    this.mc.getSoundHandler().playSound(PositionedSoundRecord.create(new ResourceLocation("gui.button.press"), 1.0F));
                    if(!this.module.equals(ClickGuiScreen.settingOpen)){
                        ClickGuiScreen.settingOpen = this.module;
                    }
                }

            }
        }
        for (Component subComponent : subComponents) {
            if(this.module.equals(ClickGuiScreen.settingOpen)){
                subComponent.mouse(mouseX,mouseY,mouseButton,mouseType);
            }
        }
    }

    public Module getModule() {
        return module;
    }

    public void setModule(Module module) {
        this.module = module;
    }

}

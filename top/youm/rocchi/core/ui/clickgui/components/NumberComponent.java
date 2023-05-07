package top.youm.rocchi.core.ui.clickgui.components;

import top.youm.rocchi.common.settings.NumberSetting;
import top.youm.rocchi.core.ui.Component;


public class NumberComponent extends Component {
    private NumberSetting setting;
    public NumberComponent(NumberSetting setting) {
        super(setting.getName());
        this.setting = setting;
    }

    @Override
    public void draw(float xPos, float yPos) {

    }

    @Override
    public void mouse(int mouseX, int mouseY, int mouseButton) {

    }

    public void setSetting(NumberSetting setting) {
        this.setting = setting;
    }

    public NumberSetting getSetting() {
        return setting;
    }
}

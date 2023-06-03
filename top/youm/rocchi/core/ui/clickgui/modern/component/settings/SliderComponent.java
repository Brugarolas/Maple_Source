package top.youm.rocchi.core.ui.clickgui.modern.component.settings;


import org.lwjgl.input.Mouse;
import top.youm.rocchi.common.events.PacketReceiveEvent;
import top.youm.rocchi.common.settings.NumberSetting;
import top.youm.rocchi.core.ui.clickgui.modern.Component;
import top.youm.rocchi.core.ui.clickgui.modern.theme.Theme;
import top.youm.rocchi.core.ui.clickgui.old.MouseType;
import top.youm.rocchi.core.ui.font.FontLoaders;
import top.youm.rocchi.utils.render.RenderUtil;
import top.youm.rocchi.utils.render.RoundedUtil;

public class SliderComponent extends Component {
    private final NumberSetting numberSetting;
    private float percentBar;
    private float animation;
    private boolean dragging;
    public SliderComponent(NumberSetting numberSetting) {
        super(numberSetting.getName());
        this.numberSetting = numberSetting;
    }

    @Override
    public void draw(float xPos, float yPos, int mouseX, int mouseY) {
        this.x = xPos;this.y = yPos;
        this.mouseX = mouseX;this.mouseY = mouseY;
        this.percentBar = 200 * ((numberSetting.getValue().floatValue() - numberSetting.getMin().floatValue()) / (numberSetting.getMax().floatValue() - numberSetting.getMin().floatValue()));
        boolean hover = isHover((int) (x - 210), (int) y, 200, 6, mouseX, mouseY);
        animation = animator.animate(percentBar,animation,0.08f);
        RoundedUtil.drawRound(x - 210,y,200,5,3,Theme.enableButton);
        RoundedUtil.drawRound(x - 210,y,animation,5,3,Theme.theme);
        RenderUtil.drawCircle(x - 210 + animation, y + 2,hover ? 7 : 6, Theme.theme);
        if(hover && dragging){
            setValue(numberSetting);
        }
    }
    public void setValue(NumberSetting setting){
        float min = setting.getMin().floatValue();
        float max = setting.getMax().floatValue();
        float inc = setting.getInc().floatValue();
        float valAbs = mouseX - (x - 210);
        float perc = valAbs / 200;
        perc =  Math.min(Math.max(0.0f, perc), 1.0f);
        float valRel = (max - min) * perc;
        float val = min + valRel;
        val = Math.round(val * (1 / inc)) / (1 / inc);
        setting.setValue(val);
    }
    @Override
    public void mouse(int mouseButton, MouseType mouseType) {
        if (mouseButton == 0) {
            dragging = mouseType == MouseType.CLICK;
        }
    }

    @Override
    public void input(char typedChar, int keyCode) {

    }
}

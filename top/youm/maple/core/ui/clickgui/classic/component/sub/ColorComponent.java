package top.youm.maple.core.ui.clickgui.classic.component.sub;

import top.youm.maple.common.settings.impl.ColorSetting;
import top.youm.maple.core.ui.Component;
import top.youm.maple.core.ui.MouseType;
import top.youm.maple.utils.render.RenderUtil;

import java.awt.*;

public class ColorComponent extends Component {
    private final ColorSetting colorSetting;
    private int alpha;
    private final Color color;
    private boolean draggingAlpha;

    public ColorComponent(ColorSetting setting) {
        super(setting.getName());
        this.colorSetting = setting;
        this.height = 50;
        this.color = setting.getValue();
    }

    @Override
    public void draw(float xPos, float yPos, int mouseX, int mouseY) {
        super.draw(xPos,yPos,mouseX,mouseY);
        RenderUtil.drawGradientRect(x - 100, y - 6 + 14, 90, height, color, new Color(255, 255, 255), new Color(0, 0, 0), new Color(0, 0, 0));
        RenderUtil.drawGradientRect2(x - 100, y - 6 + 34 + height, x - 100 + 90, y - 6 + 34 + height + 8, new Color(0, 0, 0, 0).getRGB(), color.getRGB());
        RenderUtil.drawRect(x - 100 + 90 * ((float) alpha / 255), y - 6 + 34 + height, 3, 8, new Color(172, 172, 172));
        if (isHover((int) (x - 100), (int) (y - 6 + 34 + height), 90, 8, mouseX, mouseY) && draggingAlpha) {
            setValue();
        }
    }

    public void setValue() {
        float min = 0;
        float max = 255;
        float inc = 1;
        float valAbs = mouseX - (x - 100);
        float perc = valAbs / 90;
        perc = Math.min(Math.max(0.0f, perc), 1.0f);
        float valRel = (max - min) * perc;
        float val = min + valRel;
        val = Math.round(val * (1 / inc)) / (1 / inc);
        alpha = (int) val;
        this.colorSetting.setValue(new Color(colorSetting.getValue().getRed(), colorSetting.getValue().getGreen(), colorSetting.getValue().getBlue(), alpha));
    }

    @Override
    public void mouse(int mouseButton, MouseType mouseType) {
        if (mouseButton == 0) {
            draggingAlpha = mouseType == MouseType.CLICK;
        }
    }

    @Override
    public void input(char typedChar, int keyCode) {

    }
}

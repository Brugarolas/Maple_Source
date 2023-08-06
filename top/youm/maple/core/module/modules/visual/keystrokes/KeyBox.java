package top.youm.maple.core.module.modules.visual.keystrokes;

import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import org.lwjgl.input.Keyboard;
import top.youm.maple.core.module.modules.visual.KeyStrokes;
import top.youm.maple.utils.AnimationUtils;
import top.youm.maple.utils.render.RenderUtil;

import java.awt.*;

/**
 * @author YouM
 */
public class KeyBox {
    private KeyBinding keyBinding;
    private final Minecraft mc = Minecraft.getMinecraft();
    public AnimationUtils animationUtils = new AnimationUtils();
    public int animBackgroundColor = 1;
    public Color fontColor;

    public void drawKeyBox(int x, int y, int width, int height, Color color) {
        if (this.keyBinding.isKeyDown()) {
            animBackgroundColor = animationUtils.animate(255, animBackgroundColor, 0.15f);
            fontColor = new Color(0, 0, 0);
        } else {
            this.animBackgroundColor = 0;
            fontColor = new Color(255, 255, 255);
        }
        RenderUtil.drawRect(x, y, width, height, new Color(animBackgroundColor, animBackgroundColor, animBackgroundColor, 200));
        KeyStrokes.drawCenteredStringWithShadow(Keyboard.getKeyName(keyBinding.getKeyCode()).toUpperCase(), x + (width / 2.0f), y + (KeyStrokes.size / 2.0f), fontColor);
    }

    public KeyBox(KeyBinding keyBinding) {
        this.keyBinding = keyBinding;
    }

    public KeyBinding getKeyBinding() {
        return keyBinding;
    }

    public void setKeyBinding(KeyBinding keyBinding) {
        this.keyBinding = keyBinding;
    }
}

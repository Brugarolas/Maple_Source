package top.youm.maple.core.module.modules.visual;

import com.darkmagician6.eventapi.EventTarget;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import top.youm.maple.common.events.Render2DEvent;
import top.youm.maple.core.module.Module;
import top.youm.maple.core.module.ModuleCategory;
import top.youm.maple.core.module.modules.visual.keystrokes.KeyBox;
import top.youm.maple.core.module.modules.visual.keystrokes.MouseBox;

import java.awt.*;

/**
 * @author YouM
 */
public class KeyStrokes extends Module {

    public KeyStrokes() {
        super("KeyStrokes", ModuleCategory.VISUAL, Keyboard.KEY_NONE);
        this.forward = new KeyBox(mc.gameSettings.keyBindForward);
        this.back = new KeyBox(mc.gameSettings.keyBindBack);
        this.left = new KeyBox(mc.gameSettings.keyBindLeft);
        this.right = new KeyBox(mc.gameSettings.keyBindRight);
    }

    public static final int x = 50, y = 50, size = 25, margin = 4;
    private final KeyBox forward, back, left, right;
    private final MouseBox mouseLeft = new MouseBox(), mouseRight = new MouseBox();

    @EventTarget
    public void onRender(Render2DEvent event) {
        render();
    }

    public static float centerX, centerY;

    public void render() {
        renderKey();
        renderMouse();
    }

    public void renderKey() {
        centerX = (x + size + margin) + (size / 2.0f);
        centerY = (y + size + margin) + (size / 2.0f);
        this.forward.setKeyBinding(mc.gameSettings.keyBindForward);
        this.back.setKeyBinding(mc.gameSettings.keyBindBack);
        this.left.setKeyBinding(mc.gameSettings.keyBindLeft);
        this.right.setKeyBinding(mc.gameSettings.keyBindRight);
        forward.drawKeyBox(x + size + margin, y, size, size, new Color(255, 255, 255, 150));
        back.drawKeyBox(x + size + margin, y + size + margin, size, size, new Color(255, 255, 255, 150));
        left.drawKeyBox(x, y + size + margin, size, size, new Color(255, 255, 255, 150));
        right.drawKeyBox(x + (size + margin) * 2, y + size + margin, size, size, new Color(255, 255, 255, 150));
    }

    public void renderMouse() {
        mouseLeft.drawMouseBox("LMB", x, y + (size + margin) * 2, 40, size - 4, new Color(255, 255, 255, 150), Mouse.isButtonDown(0));
        mouseRight.drawMouseBox("RMB", x + 43, y + (size + margin) * 2, 40, size - 4, new Color(255, 255, 255, 150), Mouse.isButtonDown(1));
    }

    public static void drawCenteredStringWithShadow(String text, float x, float y, Color color) {
        mc.fontRendererObj.drawStringWithShadow(text, x - mc.fontRendererObj.getStringWidth(text) / 2.0f, y - mc.fontRendererObj.FONT_HEIGHT / 2.0f, color.getRGB());
    }

}

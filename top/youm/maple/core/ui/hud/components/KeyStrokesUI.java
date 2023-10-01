package top.youm.maple.core.ui.hud.components;

import net.minecraft.client.gui.ScaledResolution;
import org.lwjgl.input.Mouse;
import top.youm.maple.core.module.modules.visual.KeyStrokes;
import top.youm.maple.core.ui.hud.components.keystrokes.KeyBox;
import top.youm.maple.core.ui.hud.components.keystrokes.MouseBox;

import java.awt.*;

/**
 * @author YouM
 * Created on 2023/8/10
 */
public class KeyStrokesUI implements HUDComponent<KeyStrokes>{
    public static final int size = 25, margin = 2;
    public static int x = 50, y = 50;
    private final KeyBox forward, back, left, right;
    private final MouseBox mouseLeft = new MouseBox(), mouseRight = new MouseBox();
    public KeyStrokesUI(){
        this.forward = new KeyBox(mc.gameSettings.keyBindForward);
        this.back = new KeyBox(mc.gameSettings.keyBindBack);
        this.left = new KeyBox(mc.gameSettings.keyBindLeft);
        this.right = new KeyBox(mc.gameSettings.keyBindRight);
    }
    @Override
    public void draw(KeyStrokes keyStrokes) {
        ScaledResolution sr = new ScaledResolution(mc);
        x = sr.getScaledWidth() - (sr.getScaledWidth() / 3);
        y = sr.getScaledHeight() - 100;
        renderKey();
        renderMouse();
    }
    public static float centerX, centerY;
    public void renderKey() {
        centerX = (x + size + margin) + (size / 2.0f);
        centerY = (y + size + margin) + (size / 2.0f);
        this.forward.setKeyBinding(mc.gameSettings.keyBindForward);
        this.back.setKeyBinding(mc.gameSettings.keyBindBack);
        this.left.setKeyBinding(mc.gameSettings.keyBindLeft);
        this.right.setKeyBinding(mc.gameSettings.keyBindRight);
        forward.drawKeyBox(x + size + margin, y, size, size, new Color(255, 255, 255, 120));
        back.drawKeyBox(x + size + margin, y + size + margin, size, size, new Color(255, 255, 255, 120));
        left.drawKeyBox(x, y + size + margin, size, size, new Color(255, 255, 255, 120));
        right.drawKeyBox(x + (size + margin) * 2, y + size + margin, size, size, new Color(255, 255, 255, 120));
    }

    public void renderMouse() {
        mouseLeft.drawMouseBox("LMB", x, y + (size + margin) * 2, 38, size - 4, new Color(255, 255, 255, 120), Mouse.isButtonDown(0));
        mouseRight.drawMouseBox("RMB", x + 41, y + (size + margin) * 2, 38, size - 4, new Color(255, 255, 255, 120), Mouse.isButtonDown(1));
    }

    public static void drawCenteredStringWithShadow(String text, float x, float y, Color color) {
        mc.fontRendererObj.drawStringWithShadow(text, x - mc.fontRendererObj.getStringWidth(text) / 2.0f, y - mc.fontRendererObj.FONT_HEIGHT / 2.0f, color.getRGB());
    }
}

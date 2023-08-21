package top.youm.maple.core.ui.hud;

import net.minecraft.client.Minecraft;

/**
 * @author YouM
 */
public interface HUDComponent {
    Minecraft mc = Minecraft.getMinecraft();
    void draw();

}

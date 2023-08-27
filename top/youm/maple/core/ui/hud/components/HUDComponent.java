package top.youm.maple.core.ui.hud.components;

import net.minecraft.client.Minecraft;
import top.youm.maple.core.module.Module;

/**
 * @author YouM
 */
public interface HUDComponent<M extends Module> {
    Minecraft mc = Minecraft.getMinecraft();
    void draw(M mod);

}

package top.youm.maple.core.module.modules.world;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import org.lwjgl.input.Keyboard;
import top.youm.maple.core.module.Module;
import top.youm.maple.core.module.ModuleCategory;


/**
 * @author YouM
 */
@SuppressWarnings("unused")
public class Teams extends Module {

    private static Teams INSTANCE;
    public static Minecraft mc = Minecraft.getMinecraft();

    public Teams() {
        super("Teams", ModuleCategory.WORLD, Keyboard.KEY_NONE);
        INSTANCE = this;
    }

    public static boolean isInTeam(Entity entity) {
        if (INSTANCE == null || !INSTANCE.isEnabled() || entity == null) return false;
        if (!(entity instanceof EntityPlayer)) return false;

        return mc.thePlayer.getTeam() != null
                && ((EntityPlayer) entity).getTeam() != null
                && mc.thePlayer.getTeam().isSameTeam(((EntityPlayer) entity).getTeam());
    }
}

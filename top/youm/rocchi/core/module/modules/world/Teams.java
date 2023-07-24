package top.youm.rocchi.core.module.modules.world;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import org.lwjgl.input.Keyboard;
import top.youm.rocchi.common.settings.impl.BoolSetting;
import top.youm.rocchi.core.module.Module;
import top.youm.rocchi.core.module.ModuleCategory;


/**
 * @author YouM
 */
@SuppressWarnings("unused")
public class Teams extends Module {

    private static Teams INSTANCE;
    public static Minecraft mc = Minecraft.getMinecraft();
    private static final BoolSetting color = new BoolSetting("Color", true);
    private static final BoolSetting scoreboard = new BoolSetting("ScoreBoard", false);
    private static final BoolSetting armor = new BoolSetting("Armor", true);

    public Teams() {
        super("Teams", ModuleCategory.WORLD, Keyboard.KEY_NONE);
        this.addSetting(color, armor, scoreboard);
        INSTANCE = this;
    }

    public static boolean isInTeam(Entity entity) {
        if (INSTANCE == null || !INSTANCE.isToggle() || entity == null) return false;
        if (!(entity instanceof EntityPlayer)) return false;

        boolean flag = scoreboard.getValue()
                && mc.thePlayer.getTeam() != null
                && ((EntityPlayer) entity).getTeam() != null
                && mc.thePlayer.getTeam().isSameTeam(((EntityPlayer) entity).getTeam());

        if (!flag && color.getValue() && mc.thePlayer.getDisplayName() != null && entity.getDisplayName() != null) {
            String targetName = entity.getDisplayName().getFormattedText().replace("§r", "");
            String clientName = mc.thePlayer.getDisplayName().getFormattedText().replace("§r", "");
            flag = targetName.startsWith(String.valueOf(clientName.charAt(0)));
        }
        return flag;
    }
}

package top.youm.rocchi.core.module.modules.world;

import com.darkmagician6.eventapi.EventTarget;
import net.minecraft.client.gui.GuiPlayerTabOverlay;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import org.lwjgl.input.Keyboard;
import top.youm.rocchi.common.events.TickEvent;
import top.youm.rocchi.common.settings.impl.BoolSetting;
import top.youm.rocchi.common.settings.impl.ModeSetting;
import top.youm.rocchi.core.module.Module;
import top.youm.rocchi.core.module.ModuleCategory;
import top.youm.rocchi.utils.player.Helper;

import java.util.ArrayList;
import java.util.List;
/**
 * @author YouM
 *
 */
public class AntiBot extends Module {
    ModeSetting mode = new ModeSetting("Mode","Advanced","Classic","Advanced","BMC");
    BoolSetting pingCheck = new BoolSetting("Ping Check",true);
    public AntiBot() {
        super("AntiBot", ModuleCategory.WORLD, Keyboard.KEY_NONE);
    }
    @EventTarget
    public void onUpdate(TickEvent event){
    }

    public boolean isNPC(EntityLivingBase entity) {
        if (entity == null) {
            return true;
        }
        if (entity == mc.thePlayer) {
            return true;
        }
        if (!(entity instanceof EntityPlayer)) {
            return false;
        }
        if (entity.isPlayerSleeping()) {
            return true;
        }
        if (pingCheck.getValue()) {
            if (mc.getNetHandler().getPlayerInfo(entity.getUniqueID()).getResponseTime() == 0) {
                return true;
            }
        }
        return (mode.getValue().equals("Classic") || mode.getValue().equals("Advanced")) && (entity).ticksExisted <= 80;
    }
    public boolean isServerBot(Entity entity) {
        if (Helper.onServer("hypixel")) {
            if (entity.getDisplayName().getFormattedText().startsWith("§") && !entity.isInvisible() && !entity.getDisplayName().getFormattedText().toLowerCase().contains("npc")) {
                return false;
            }
            return true;
        }
        return false;
    }
    private List<EntityPlayer> getTabPlayerList() {
        ArrayList<EntityPlayer> list = new ArrayList<>();
        List<NetworkPlayerInfo> players = GuiPlayerTabOverlay.field_175252_a.sortedCopy(mc.thePlayer.sendQueue.getPlayerInfoMap());
        for (NetworkPlayerInfo info : players) {
            if (info == null) {
                continue;
            }
            list.add(mc.theWorld.getPlayerEntityByName(info.getGameProfile().getName()));
        }
        return list;
    }
}



package top.youm.maple.core.module.modules.player;

import com.darkmagician6.eventapi.EventTarget;
import com.darkmagician6.eventapi.events.Event;
import net.minecraft.item.ItemBow;
import net.minecraft.network.play.client.C09PacketHeldItemChange;
import top.youm.maple.Maple;
import top.youm.maple.common.events.MotionEvent;
import top.youm.maple.common.events.SlowDownEvent;
import top.youm.maple.common.events.TickEvent;
import top.youm.maple.common.settings.impl.ModeSetting;
import top.youm.maple.core.module.Module;
import top.youm.maple.core.module.ModuleCategory;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import org.lwjgl.input.Keyboard;
import top.youm.maple.core.module.modules.combat.KillAura;
import top.youm.maple.utils.BadPacketsComponent;
import top.youm.maple.utils.SlotComponent;
import top.youm.maple.utils.network.PacketUtil;
import top.youm.maple.utils.player.MovementUtil;

import java.util.Objects;

/**
 * @author YouM
 */
public class NoSlow extends Module {
    private int disable;
    private ModeSetting mode = new ModeSetting("Mode","New NCP","New NCP","Other");
    public NoSlow() {
        super("No Slow", ModuleCategory.MOVEMENT, Keyboard.KEY_NONE);
        this.addSetting(mode);
    }
    @EventTarget
    public void onTick(TickEvent event){
        this.setSuffixes(mode.getValue());
    }
    @EventTarget
    public void onSlowDown(SlowDownEvent event){
        event.setCancelled(true);
    }
    @EventTarget
    public void onMotion(MotionEvent motion){
        switch (mode.getValue()){
            case "New NCP":
                this.disable++;
                if (mc.thePlayer.isUsingItem() && this.disable > 10 && !BadPacketsComponent.bad(false,
                        true, true, false, false) && !(Objects.requireNonNull(SlotComponent.getItemStack()).getItem() instanceof ItemBow) && KillAura.target == null) {
                    PacketUtil.send(new C09PacketHeldItemChange(SlotComponent.getItemIndex() % 8 + 1));
                    PacketUtil.send(new C09PacketHeldItemChange(SlotComponent.getItemIndex()));
                }
                break;
            case "Other":
                if (MovementUtil.isMoving() && mc.thePlayer.isUsingItem()) {
                    if (motion.getState() == Event.State.PRE) {
                        PacketUtil.sendPacket(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.RELEASE_USE_ITEM, BlockPos.ORIGIN, EnumFacing.DOWN));
                    } else {
                        PacketUtil.sendPacket(new C08PacketPlayerBlockPlacement(mc.thePlayer.getCurrentEquippedItem()));
                    }
                }
        }

    }
}

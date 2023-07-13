package top.youm.rocchi.core.module.modules.player;

import com.darkmagician6.eventapi.EventTarget;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.network.play.client.C03PacketPlayer;
import org.lwjgl.input.Keyboard;
import top.youm.rocchi.common.events.MotionEvent;
import top.youm.rocchi.common.events.PacketSendEvent;
import top.youm.rocchi.core.module.Module;
import top.youm.rocchi.core.module.ModuleCategory;

/**
 * @author YouM
 */
@SuppressWarnings("unused")
public final class Freecam extends Module {

    private EntityOtherPlayerMP copy;
    private double x, y, z;

    @Override
    public void onEnable() {
        if(this.mc.thePlayer != null){
            this.copy = new EntityOtherPlayerMP(this.mc.theWorld, this.mc.thePlayer.getGameProfile());
            this.copy.clonePlayer(this.mc.thePlayer, true);
            this.copy.setLocationAndAngles(this.mc.thePlayer.posX, this.mc.thePlayer.posY, this.mc.thePlayer.posZ, this.mc.thePlayer.rotationYaw, this.mc.thePlayer.rotationPitch);
            this.copy.rotationYawHead = this.mc.thePlayer.rotationYawHead;
            this.copy.setEntityId(-1337);
            this.copy.setSneaking(this.mc.thePlayer.isSneaking());
            this.mc.theWorld.addEntityToWorld(this.copy.getEntityId(), this.copy);
            this.x = this.mc.thePlayer.posX;
            this.y = this.mc.thePlayer.posY;
            this.z = this.mc.thePlayer.posZ;
        }
    }
    @EventTarget
    public void onMotion(MotionEvent event){
        this.mc.thePlayer.capabilities.isFlying = true;
        this.mc.thePlayer.noClip = true;
        this.mc.thePlayer.capabilities.setFlySpeed(0.1f);
        event.setCancelled(true);
    };
    @EventTarget
    public void onSendPacket(PacketSendEvent event ){
        if (event.getPacket() instanceof C03PacketPlayer) {
            event.setCancelled(true);
        }
    };

    @Override
    public void onDisable() {
        if(this.mc.thePlayer != null) {
            this.mc.thePlayer.setSpeed(0.0);
            this.mc.thePlayer.setLocationAndAngles(this.copy.posX, this.copy.posY, this.copy.posZ, this.copy.rotationYaw, this.copy.rotationPitch);
            this.mc.thePlayer.rotationYawHead = this.copy.rotationYawHead;
            this.mc.theWorld.removeEntityFromWorld(this.copy.getEntityId());
            this.mc.thePlayer.setSneaking(this.copy.isSneaking());
            this.copy = null;
            this.mc.renderGlobal.loadRenderers();
            this.mc.thePlayer.setPosition(this.x, this.y, this.z);
            this.mc.getNetHandler().addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(this.mc.thePlayer.posX, this.mc.thePlayer.posY + 0.01, this.mc.thePlayer.posZ, this.mc.thePlayer.onGround));
            this.mc.thePlayer.capabilities.isFlying = false;
            this.mc.thePlayer.noClip = false;
            this.mc.theWorld.removeEntityFromWorld(-1);
        }
    }

    public Freecam() {
        super("Freecam", ModuleCategory.PLAYER, Keyboard.KEY_NONE);
    }
}

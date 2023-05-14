package top.youm.rocchi.core.module.modules.movement;

import com.darkmagician6.eventapi.EventTarget;
import com.darkmagician6.eventapi.events.Event;
import net.minecraft.network.play.client.*;
import net.minecraft.potion.Potion;
import org.lwjgl.input.Keyboard;
import top.youm.rocchi.common.events.MotionEvent;
import top.youm.rocchi.common.settings.BoolSetting;
import top.youm.rocchi.common.settings.ModeSetting;
import top.youm.rocchi.common.settings.NumberSetting;
import top.youm.rocchi.core.module.Module;
import top.youm.rocchi.core.module.ModuleCategory;
import top.youm.rocchi.utils.TimerUtil;
import top.youm.rocchi.utils.player.RotationUtil;
import top.youm.rocchi.utils.player.ScaffoldUtil;

@SuppressWarnings("unused")
public class Scaffold extends Module {

    private ScaffoldUtil.BlockCache blockCache, lastBlockCache;
    private ModeSetting<PlaceType> placetype = new ModeSetting<>("Place Type", PlaceType.values(), PlaceType.PRE);
    public static NumberSetting extend = new NumberSetting("Extend", 0, 6, 0, 0.01);
    public static BoolSetting sprint = new BoolSetting("Sprint", false);
    private BoolSetting tower = new BoolSetting("Tower", false);
    private NumberSetting towerTimer = new NumberSetting("Tower Timer Boost", 1.2, 5, 0.1, 0.1);
    private BoolSetting swing = new BoolSetting("Swing", false);
    private float rotations[];
    private TimerUtil timer = new TimerUtil();

    public Scaffold() {
        super("Scaffold", ModuleCategory.MOVEMENT, Keyboard.KEY_NONE);
        this.addSetting(placetype, extend, sprint, tower, towerTimer, swing);
    }
    @EventTarget
    public void onMotion(MotionEvent event){

        if(event.getState() == Event.State.PRE) {

            // Rotations
            if(lastBlockCache != null) {
                rotations = RotationUtil.getFacingRotations2(lastBlockCache.getPosition().getX(), lastBlockCache.getPosition().getY(), lastBlockCache.getPosition().getZ());
                mc.thePlayer.renderYawOffset = rotations[0];
                mc.thePlayer.rotationYawHead = rotations[0];
                event.setYaw(rotations[0]);
                event.setPitch(81);
                mc.thePlayer.rotationPitchHead = 81;
            } else {
                event.setPitch(81);
                event.setYaw(mc.thePlayer.rotationYaw + 180);
                mc.thePlayer.rotationPitchHead = 81;
                mc.thePlayer.renderYawOffset = mc.thePlayer.rotationYaw + 180;
                mc.thePlayer.rotationYawHead = mc.thePlayer.rotationYaw + 180;
            }

            // Speed 2 Slowdown
            if(mc.thePlayer.isPotionActive(Potion.moveSpeed.id)){
                mc.thePlayer.motionX *= 0.66;
                mc.thePlayer.motionZ *= 0.66;
            }

            // Setting Block Cache
            blockCache = ScaffoldUtil.grab();
            if (blockCache != null) {
                lastBlockCache = ScaffoldUtil.grab();
            }else{
                return;
            }

            // Setting Item Slot (Pre)
            int slot = ScaffoldUtil.grabBlockSlot();
            if(slot == -1) return;

            // Setting Slot
            mc.thePlayer.sendQueue.addToSendQueue(new C09PacketHeldItemChange(slot));

            // Placing Blocks (Pre)
            if(placetype.getValue() == PlaceType.PRE){
                if (blockCache == null) return;
                mc.playerController.onPlayerRightClick(mc.thePlayer, mc.theWorld, mc.thePlayer.inventory.getStackInSlot(slot), lastBlockCache.position, lastBlockCache.facing, ScaffoldUtil.getHypixelVec3(lastBlockCache));
                if(swing.getValue()){
                    mc.thePlayer.swingItem();
                }
                mc.thePlayer.sendQueue.addToSendQueue(new C0APacketAnimation());
                blockCache = null;
            }
        }else{

            // Tower
            if(tower.getValue()) {
                if(mc.gameSettings.keyBindJump.isKeyDown()) {
                    mc.timer.timerSpeed = towerTimer.getValue().floatValue();
                    if(mc.thePlayer.motionY < 0) {
                        mc.thePlayer.jump();
                    }
                }else{
                    mc.timer.timerSpeed = 1;
                }
            }

            // Setting Item Slot (Post)
            int slot = ScaffoldUtil.grabBlockSlot();
            if(slot == -1) return;

            // Placing Blocks (Post)
            if(placetype.getValue() == PlaceType.POST){
                if (blockCache == null) return;
                mc.playerController.onPlayerRightClick(mc.thePlayer, mc.theWorld, mc.thePlayer.inventory.getStackInSlot(slot), lastBlockCache.position, lastBlockCache.facing, ScaffoldUtil.getHypixelVec3(lastBlockCache));
                if(swing.getValue()){
                    mc.thePlayer.swingItem();
                }
                mc.thePlayer.sendQueue.addToSendQueue(new C0APacketAnimation());
                blockCache = null;
            }
        }
    };

    @Override
    public void onDisable() {
        if(this.mc.thePlayer != null) {
            mc.thePlayer.sendQueue.addToSendQueue(new C09PacketHeldItemChange(mc.thePlayer.inventory.currentItem));
        }
    }

    @Override
    public void onEnable() {
        lastBlockCache = null;
    }
    private enum PlaceType{
        PRE,POST
    }
}

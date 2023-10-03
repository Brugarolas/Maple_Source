package top.youm.maple.core.module.modules.movement;

import com.darkmagician6.eventapi.EventTarget;
import top.youm.maple.common.events.MotionEvent;
import top.youm.maple.common.events.TickEvent;
import top.youm.maple.common.settings.impl.CheckBoxSetting;
import top.youm.maple.common.settings.impl.SliderSetting;
import top.youm.maple.core.module.Module;
import top.youm.maple.core.module.ModuleCategory;
import net.minecraft.network.play.client.C03PacketPlayer;
import org.lwjgl.input.Keyboard;

/**
 * @author YouM
 */
public class Step extends Module {
    private SliderSetting height = new SliderSetting("Height", 1.0, 10.0, 1.0, 0.5);
    private CheckBoxSetting ncp = new CheckBoxSetting("NCP", false);
    public Step() {
        super("Step", ModuleCategory.MOVEMENT, Keyboard.KEY_NONE);
        this.addSettings(ncp,height);
    }
    @EventTarget
    public void onTick(TickEvent event){
        this.setSuffixes(ncp.getValue() ? ncp.getName() : "");
    }
    @EventTarget
    private void onMotion(MotionEvent e) {
        if (this.ncp.getValue()) {
            this.mc.thePlayer.stepHeight = 0.6f;
            if (this.mc.thePlayer.isCollidedHorizontally && this.mc.thePlayer.onGround && this.mc.thePlayer.isCollidedVertically && this.mc.thePlayer.isCollided) {
                this.mc.thePlayer.setSprinting(true);
                this.mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(this.mc.thePlayer.posX, this.mc.thePlayer.posY + 0.42, this.mc.thePlayer.posZ, this.mc.thePlayer.onGround));
                this.mc.thePlayer.setSprinting(true);
                this.mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(this.mc.thePlayer.posX, this.mc.thePlayer.posY + 0.753, this.mc.thePlayer.posZ, this.mc.thePlayer.onGround));
                this.mc.thePlayer.setSprinting(true);
                this.mc.thePlayer.setPosition(this.mc.thePlayer.posX, this.mc.thePlayer.posY + 0.42, this.mc.thePlayer.posZ);
                this.mc.timer.timerSpeed = 0.5f;
                this.mc.thePlayer.setSprinting(true);
                new Thread(() -> {
                    try {
                        Thread.sleep(100L);
                    }
                    catch (InterruptedException ex) {
                        ex.printStackTrace();
                    }
                    Step.this.mc.timer.timerSpeed = 1.0f;
                }).start();
            }
        } else {
            this.mc.thePlayer.stepHeight = 1.0f;
        }
    }
}

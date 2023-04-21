package me.youm.rocchi.core.module.modules.movement;

import com.darkmagician6.eventapi.EventTarget;
import me.youm.rocchi.common.events.MotionEvent;
import me.youm.rocchi.core.module.Module;
import me.youm.rocchi.core.module.ModuleCategory;
import org.lwjgl.input.Keyboard;

public class Speed extends Module {
    public Speed() {
        super("Speed", ModuleCategory.MOVEMENT, Keyboard.KEY_V);
    }
    private float speed;
    @EventTarget
    public void onMotion(MotionEvent motion){
        if (mc.thePlayer.movementInput.moveForward != 0F || mc.thePlayer.movementInput.moveStrafe != 0F) {
            if (mc.thePlayer.onGround && mc.thePlayer.motionY < 0.003) {
                mc.thePlayer.jump();
                mc.timer.timerSpeed = 1.0f;
            }
            if (mc.thePlayer.motionY > 0.003) {
                mc.thePlayer.motionX *= speed;
                mc.thePlayer.motionZ *= speed;
                mc.timer.timerSpeed = 1.05f;
            }
            speed = 1.0002f;
        }
    }
}

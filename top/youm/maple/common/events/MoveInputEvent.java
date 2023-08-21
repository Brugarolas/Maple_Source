package top.youm.maple.common.events;

import com.darkmagician6.eventapi.events.Event;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import top.youm.maple.utils.player.MovementUtil;

/**
 * @author YouM
 * Created on 2023/8/12
 */
public class MoveInputEvent extends Event {
    private float strafe, forward, friction, yaw, pitch;

    public void applyMotion(double speed, float strafeMotion) {
        float remainder = 1 - strafeMotion;
        EntityPlayerSP player = Minecraft.getMinecraft().thePlayer;
        strafeMotion *= 0.91;
        if (player.onGround) {
            MovementUtil.setSpeed(speed);
        } else {
            player.motionX = player.motionX * strafeMotion;
            player.motionZ = player.motionZ * strafeMotion;
            friction = (float) speed * remainder;
        }
    }

    public MoveInputEvent(float strafe, float forward, float friction, float yaw, float pitch) {
        this.strafe = strafe;
        this.forward = forward;
        this.friction = friction;
        this.yaw = yaw;
        this.pitch = pitch;
    }

    public float getStrafe() {
        return strafe;
    }

    public void setStrafe(float strafe) {
        this.strafe = strafe;
    }

    public float getForward() {
        return forward;
    }

    public void setForward(float forward) {
        this.forward = forward;
    }

    public float getFriction() {
        return friction;
    }

    public void setFriction(float friction) {
        this.friction = friction;
    }

    public float getYaw() {
        return yaw;
    }

    public void setYaw(float yaw) {
        this.yaw = yaw;
    }

    public float getPitch() {
        return pitch;
    }

    public void setPitch(float pitch) {
        this.pitch = pitch;
    }
}

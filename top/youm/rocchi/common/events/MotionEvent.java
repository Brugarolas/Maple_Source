package top.youm.rocchi.common.events;

import com.darkmagician6.eventapi.events.Event;

/**
 * @author YouM
 * when player int minecraft motion invoke this event
 */
public class MotionEvent extends Event {
    //player position X,position Y,position Z,
    private double posX,posY,posZ;
    //player head pitch angle yaw angle
    private float pitch,yaw;
    //player is on ground
    private boolean onGround;

    public MotionEvent(double posX, double posY, double posZ, float yaw, float pitch, boolean onGround) {
        this.posX = posX;
        this.posY = posY;
        this.posZ = posZ;
        this.yaw = yaw;
        this.pitch = pitch;
        this.onGround = onGround;
    }

    public double getPosX() {
        return posX;
    }

    public void setPosX(double posX) {
        this.posX = posX;
    }

    public double getPosY() {
        return posY;
    }

    public void setPosY(double posY) {
        this.posY = posY;
    }

    public double getPosZ() {
        return posZ;
    }

    public void setPosZ(double posZ) {
        this.posZ = posZ;
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

    public boolean isOnGround() {
        return onGround;
    }

    public void setOnGround(boolean onGround) {
        this.onGround = onGround;
    }
}

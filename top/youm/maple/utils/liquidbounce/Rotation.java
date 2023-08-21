package top.youm.maple.utils.liquidbounce;

/**
 * @author YouM
 * Created on 2023/8/12
 */
public class Rotation {
    private float yaw,pitch;

    public Rotation(float yaw, float pitch) {
        this.yaw = yaw;
        this.pitch = pitch;
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

package top.youm.maple.common.events;

import com.darkmagician6.eventapi.events.Event;

/**
 * @author YouM
 * Created on 2023/8/12
 */
public class JumpFixEvent extends Event {
    private float yaw;

    public JumpFixEvent(float yaw) {
        this.yaw = yaw;
    }

    public float getYaw() {
        return yaw;
    }

    public void setYaw(float yaw) {
        this.yaw = yaw;
    }
}

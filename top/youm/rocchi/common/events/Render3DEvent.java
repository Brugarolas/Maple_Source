package top.youm.rocchi.common.events;

import com.darkmagician6.eventapi.events.Event;
/**
 * @author YouM
 * when render 3D entity or other item invoke this event
 */
public class Render3DEvent extends Event {
    //render tick
    private float ticks;

    public Render3DEvent(float ticks) {
        this.ticks = ticks;
    }

    public float getTicks() {
        return ticks;
    }

    public void setTicks(float ticks) {
        this.ticks = ticks;
    }
}

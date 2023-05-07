package top.youm.rocchi.common.events;

import com.darkmagician6.eventapi.events.Event;

public class Render3DEvent extends Event {
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

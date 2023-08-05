package top.youm.maple.common.events;

import com.darkmagician6.eventapi.events.Event;
/**
 * @author YouM
 * when render overlay invoke this event
 */
public class Render2DEvent extends Event {
    //render tick
    private float partialTicks;

    public Render2DEvent(float partialTicks) {
        this.partialTicks = partialTicks;
    }

    public float getPartialTicks() {
        return partialTicks;
    }

    public void setPartialTicks(float partialTicks) {
        this.partialTicks = partialTicks;
    }
}

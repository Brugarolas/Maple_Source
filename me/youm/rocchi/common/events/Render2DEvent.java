package me.youm.rocchi.common.events;

import com.darkmagician6.eventapi.events.Event;

public class Render2DEvent extends Event {
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

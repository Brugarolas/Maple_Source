package top.youm.maple.common.events;

import com.darkmagician6.eventapi.events.Event;

public class SafeWalkEvent extends Event {
    private boolean safe;

    public boolean isSafe() {
        return this.safe;
    }
    public void setSafe(boolean safe) {
        this.safe = safe;
    }

}

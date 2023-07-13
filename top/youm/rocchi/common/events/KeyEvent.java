package top.youm.rocchi.common.events;

import com.darkmagician6.eventapi.events.Event;
/**
 * @author YouM
 * when player in minecraft input key invoke this event
 */
public class KeyEvent extends Event {
    private int key;

    public KeyEvent(int key) {
        this.key = key;
    }

    public int getKey() {
        return key;
    }

    public void setKey(int key) {
        this.key = key;
    }
}

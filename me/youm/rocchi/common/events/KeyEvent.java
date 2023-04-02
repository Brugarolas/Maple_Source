package me.youm.rocchi.common.events;

import com.darkmagician6.eventapi.events.Event;
import me.youm.rocchi.core.module.Module;
import me.youm.rocchi.core.module.ModuleCategory;

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

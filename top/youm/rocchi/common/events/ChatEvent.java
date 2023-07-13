package top.youm.rocchi.common.events;

import com.darkmagician6.eventapi.events.Event;

/**
 * @author YouM
 * when have minecraft chat message send to player invoke this event
 */
public class ChatEvent extends Event {
    //chat message
    private String message;

    public ChatEvent(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}

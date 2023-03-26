package com.darkmagician6.eventapi.events;

/**
 * The most basic form of an event.
 * You have to implement this interface in order for the EventAPI to recognize the event.
 *
 * @author DarkMagician6
 * @since July 30, 2013
 */
public class Event {

    private State state = State.PRE;
    private boolean cancelled;

    public boolean isCancelled() {
        return cancelled;
    }

    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

    public enum State{
        PRE,POST
    }
}

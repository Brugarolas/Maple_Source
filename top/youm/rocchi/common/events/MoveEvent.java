package top.youm.rocchi.common.events;

import com.darkmagician6.eventapi.events.Event;

/**
 * @author YouM
 * when player in minecraft move invoke this event
 */
public class MoveEvent extends Event {
    //player position X,position Y,position Z
    private double x, y, z;
    public MoveEvent(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }
    public double getX() {
        return x;
    }
    public void setX(double x) {
        this.x = x;
    }
    public double getY() {
        return y;
    }
    public void setY(double y) {
        this.y = y;
    }
    public double getZ() {
        return z;
    }
    public void setZ(double z) {
        this.z = z;
    }

}

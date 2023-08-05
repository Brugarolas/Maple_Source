package top.youm.maple.common.events;

import com.darkmagician6.eventapi.events.Event;
import net.minecraft.network.Packet;

/**
 * @author YouM
 * when server send packets to player invoke this event
 */
public class PacketReceiveEvent extends Event {
    //server send to player packets type
    private Packet<?> packet;

    public PacketReceiveEvent(Packet<?> packet) {
        this.packet = packet;
    }


    public Packet<?> getPacket() {
        return packet;
    }


    public void setPacket(Packet<?> packet) {
        this.packet = packet;
    }

}

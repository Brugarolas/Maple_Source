package top.youm.maple.common.events;

import com.darkmagician6.eventapi.events.Event;
import net.minecraft.network.Packet;

/**
 * @author YouM
 * when player send packet to server in minecraft invoke this event
 */
public class PacketSendEvent extends Event {
    //player send to server packets type
    private Packet<?> packet;

    public PacketSendEvent(Packet<?> packet) {
        this.packet = packet;
    }


    public Packet<?> getPacket() {
        return packet;
    }


    public void setPacket(Packet<?> packet) {
        this.packet = packet;
    }

}

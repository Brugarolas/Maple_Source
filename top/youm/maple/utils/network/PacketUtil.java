package top.youm.maple.utils.network;

import net.minecraft.client.Minecraft;
import net.minecraft.network.Packet;

public class PacketUtil {
    public static void sendPacket(Packet<?> packet, boolean silent) {
        if (Minecraft.getMinecraft().thePlayer != null) {
            Minecraft.getMinecraft().getNetHandler().getNetworkManager().sendPacket(packet, silent);
        }
    }

    public static void sendPacketNoEvent(Packet<?> packet) {
        sendPacket(packet, true);
    }

    public static void sendPacket(Packet<?> packet) {
        sendPacket(packet, false);
    }
    public static void send(final Packet<?> packet) {
        if (Minecraft.getMinecraft().thePlayer != null) {
            Minecraft.getMinecraft().getNetHandler().addToSendQueue(packet);
        }
    }
    public void sendNoEvent(final Packet<?> packet) {
        Minecraft.getMinecraft().getNetHandler().addToSendQueueUnregistered(packet);
    }


}

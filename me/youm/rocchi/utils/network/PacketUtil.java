package me.youm.rocchi.utils.network;

import net.minecraft.client.Minecraft;
import net.minecraft.network.Packet;

public class PacketUtil {
    public static void sendPacket(Packet<?> packet){
        Minecraft.getMinecraft().thePlayer.sendQueue.addToSendQueue(packet);
    }
}

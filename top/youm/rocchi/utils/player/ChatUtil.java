package top.youm.rocchi.utils.player;

import net.minecraft.client.Minecraft;

public class ChatUtil {
    public static Minecraft mc = Minecraft.getMinecraft();
    public static void send(String message) {
        if (mc.thePlayer != null) {
            mc.thePlayer.sendChatMessage(message);
        }
    }

}

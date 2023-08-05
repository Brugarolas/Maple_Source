package top.youm.maple.utils.player;

import net.minecraft.client.Minecraft;
@SuppressWarnings("all")
public class Helper {
    public static Minecraft mc = Minecraft.getMinecraft();
    public static boolean onServer(String server) {
        if (!mc.isSingleplayer() && mc.getCurrentServerData().serverIP.toLowerCase().contains(server)) {
            return true;
        }
        return false;
    }
}

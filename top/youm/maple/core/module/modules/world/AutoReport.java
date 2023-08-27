package top.youm.maple.core.module.modules.world;

import com.darkmagician6.eventapi.EventTarget;
import net.minecraft.network.play.client.C01PacketChatMessage;
import net.minecraft.network.play.server.S02PacketChat;
import org.lwjgl.input.Keyboard;
import top.youm.maple.common.events.PacketReceiveEvent;
import top.youm.maple.core.module.Module;
import top.youm.maple.core.module.ModuleCategory;
import top.youm.maple.utils.player.ChatUtil;

/**
 * @author YouM
 * Created on 2023/8/27
 */
public class AutoReport extends Module {
    public AutoReport() {
        super("Auto Report", ModuleCategory.WORLD, Keyboard.KEY_NONE);
    }

    @EventTarget
    public void onPacketReceive(PacketReceiveEvent event){
        if (event.getPacket() instanceof S02PacketChat) {
            String message = ((S02PacketChat) event.getPacket()).getChatComponent().getFormattedText();
            System.out.println(message);
        }
    }
}

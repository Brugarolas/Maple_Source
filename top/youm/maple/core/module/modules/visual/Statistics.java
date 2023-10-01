package top.youm.maple.core.module.modules.visual;

import com.darkmagician6.eventapi.EventTarget;
import net.minecraft.network.play.server.S02PacketChat;
import org.lwjgl.input.Keyboard;
import top.youm.maple.common.events.PacketReceiveEvent;
import top.youm.maple.common.settings.impl.CheckBoxSetting;
import top.youm.maple.core.module.Module;
import top.youm.maple.core.module.ModuleCategory;

/**
 * @author YouM
 * Created on 2023/8/21
 */
public class Statistics extends Module {
    public CheckBoxSetting shadow = new CheckBoxSetting("Shadow",true);
    public int wins,kills,death;
    public Statistics() {
        super("Statistics", ModuleCategory.VISUAL, Keyboard.KEY_NONE);
        this.addSetting(shadow);
        this.isRenderModule = true;
    }
    @EventTarget
    public void onPacketReceive(PacketReceiveEvent event){
        if (event.getPacket() instanceof S02PacketChat) {
            String message = ((S02PacketChat) (event.getPacket())).getChatComponent().getFormattedText();
            if (message.contains("§ewas killed by §9" + mc.thePlayer.getName())) {
                System.out.println(message);
                kills++;
            } else if (message.contains("&6for wining the game")) {
                wins++;
            }else if(message.contains("&6You") && message.contains("§9" + mc.thePlayer.getName() + "§ewas killed by")){
                death++;
            }
        }
    }

}

package top.youm.maple.core.module.modules.visual;

import com.darkmagician6.eventapi.EventTarget;
import net.minecraft.network.play.server.S02PacketChat;
import org.lwjgl.input.Keyboard;
import top.youm.maple.common.events.PacketReceiveEvent;
import top.youm.maple.common.events.Render2DEvent;
import top.youm.maple.core.module.Module;
import top.youm.maple.core.module.ModuleCategory;
import top.youm.maple.core.ui.font.FontLoaders;
import top.youm.maple.utils.render.RoundedUtil;

import java.awt.*;

/**
 * @author YouM
 * Created on 2023/8/21
 */
public class Statistics extends Module {
    private int wins,kills,death,width = 170,height = 75;
    public Statistics() {
        super("Statistics", ModuleCategory.VISUAL, Keyboard.KEY_NONE);
    }
    @EventTarget
    public void onRender2D(Render2DEvent event){
        RoundedUtil.drawRound(5,50,width,height,2,true,new Color(0,0,0,130));
        FontLoaders.robotoB26.drawCenteredStringWithShadow("Statistics",5 + (width / 2.0f),50 + 2,-1);
        RoundedUtil.drawRound(20,50 + FontLoaders.robotoB26.getHeight() + 4,140,1,1,true,HUD.getHUDThemeColor());
        FontLoaders.aovel22.drawString("Kills:",8,60 + FontLoaders.robotoB26.getHeight(),-1);
        FontLoaders.aovel22.drawString("Wins:",8,64 + FontLoaders.robotoB26.getHeight() + FontLoaders.aovel22.getHeight(),-1);
        FontLoaders.aovel22.drawString("Deaths:",8,68 + FontLoaders.robotoB26.getHeight() + FontLoaders.aovel22.getHeight() * 2,-1);
        FontLoaders.aovel22.drawString("Server IP:",8,72 + FontLoaders.robotoB26.getHeight() + FontLoaders.aovel22.getHeight() * 3,-1);

        FontLoaders.aovel22.drawString(String.valueOf(kills),width - 2 - FontLoaders.aovel22.getStringWidth(String.valueOf(kills)),60 + FontLoaders.robotoB26.getHeight(),-1);
        FontLoaders.aovel22.drawString(String.valueOf(wins),width - 2 - FontLoaders.aovel22.getStringWidth(String.valueOf(wins)),64 + FontLoaders.robotoB26.getHeight() + FontLoaders.aovel22.getHeight(),-1);
        FontLoaders.aovel22.drawString(String.valueOf(death),width - 2 - FontLoaders.aovel22.getStringWidth(String.valueOf(death)),68 + FontLoaders.robotoB26.getHeight() + FontLoaders.aovel22.getHeight() * 2,-1);
        if(mc.isSingleplayer()){
            FontLoaders.aovel22.drawString("Single Player",width - 2 - FontLoaders.aovel22.getStringWidth("Single Player"),72 + FontLoaders.robotoB26.getHeight() + FontLoaders.aovel22.getHeight() * 3,-1);
        }else{
            FontLoaders.aovel22.drawString(mc.getCurrentServerData().serverIP,width - 2 - FontLoaders.aovel22.getStringWidth(mc.getCurrentServerData().serverIP.toLowerCase()),74 + FontLoaders.robotoB26.getHeight() + FontLoaders.aovel22.getHeight() * 3,-1);
        }

    }
    @EventTarget
    public void onPacketReceive(PacketReceiveEvent event){
        if (event.getPacket() instanceof S02PacketChat) {
            String message = ((S02PacketChat) (event.getPacket())).getChatComponent().getFormattedText();
            if (message.contains("was killed by " + mc.thePlayer.getName())) {
                kills++;
            } else if (message.contains("for wining the game")) {
                wins++;
            }else if(message.contains(mc.thePlayer.getName() + " was killed by")){
                death++;
            }
        }
    }

}

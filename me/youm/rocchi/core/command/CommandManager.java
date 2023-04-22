package me.youm.rocchi.core.command;

import com.darkmagician6.eventapi.EventManager;
import com.darkmagician6.eventapi.EventTarget;
import me.youm.rocchi.Rocchi;
import me.youm.rocchi.common.events.ChatEvent;
import me.youm.rocchi.core.command.commands.BindCommand;
import me.youm.rocchi.core.command.commands.SendCommand;
import me.youm.rocchi.utils.player.ChatUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.EnumChatFormatting;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class CommandManager {
    public List<ICommand> commands = new ArrayList<>();
    public final String prefix = "*";
    public void initialize(){
        commands.add(new SendCommand());
        commands.add(new BindCommand());
        EventManager.register(this);
    }

    public boolean execute(String message){
        if (!message.startsWith(prefix)) {
            return false;
        }
        String[] context =  message.substring(1).split(" ");
        System.out.println(Arrays.toString(context));
        for (ICommand command : commands) {
            if(context[0].equals("help")){
                helperSend("command: " +command.getName() + " usage: "+ command.getUsage(),State.NONE);
            }else if(context[0].equals(command.getName())){
                return command.execute(context);
            }
        }
        return false;
    }
    @EventTarget
    public void onChat(ChatEvent event){
       execute(event.getMessage());
    }
    public static void helperSend(String message,State state){

        Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentTranslation("["+ EnumChatFormatting.BLUE + Rocchi.getInstance().NAME + EnumChatFormatting.WHITE +"]: " + state.color + (state == State.NONE ? state.color : "") + " " + message));
    }
    public enum State{

        NONE(EnumChatFormatting.WHITE),Info(EnumChatFormatting.BLUE),Warn(EnumChatFormatting.YELLOW),Error(EnumChatFormatting.RED),Debug(EnumChatFormatting.GOLD);
        public final EnumChatFormatting color;

        State(EnumChatFormatting color) {
            this.color = color;
        }
    }
}

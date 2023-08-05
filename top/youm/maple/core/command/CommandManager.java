package top.youm.maple.core.command;

import com.darkmagician6.eventapi.EventManager;
import com.darkmagician6.eventapi.EventTarget;
import top.youm.maple.Maple;
import top.youm.maple.common.events.ChatEvent;
import top.youm.maple.core.command.commands.BindCommand;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.EnumChatFormatting;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author YouM
 * manage commands
 */
public class CommandManager {
    public List<Command> commands = new ArrayList<>();
    public final String prefix = "*";
    public void initialize(){
        commands.add(new BindCommand());
        EventManager.register(this);
    }

    /**
     * execute command
     * @param message command context
     * @return is success
     */
    public boolean execute(String message){
        if (!message.startsWith(prefix)) {
            return false;
        }
        String[] context = message.substring(1).split(" ");
        System.out.println(Arrays.toString(context));
        for (Command command : commands) {
            if(context[0].equals("help")){
                helperSend("command: " +command.getName() + " usage: "+ command.getUsage(),State.NONE);
            }else if(context[0].equals(command.getName())){
                return command.execute(context);
            }
        }
        return false;
    }

    /**
     * receive chat message
     */
    @EventTarget
    public void onChat(ChatEvent event){
       execute(event.getMessage());
    }

    /**
     * send message to player
     * @param message client's message
     * @param state message type
     */
    public static void helperSend(String message,State state){
        Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentTranslation("["+ EnumChatFormatting.BLUE + Maple.getInstance().NAME + EnumChatFormatting.WHITE +"]: " + state.color + (state == State.NONE ? state.color : "") + " " + message));
    }
    public enum State{
        NONE(EnumChatFormatting.WHITE),Info(EnumChatFormatting.BLUE),Warn(EnumChatFormatting.YELLOW),Error(EnumChatFormatting.RED),Debug(EnumChatFormatting.GOLD);
        public final EnumChatFormatting color;

        State(EnumChatFormatting color) {
            this.color = color;
        }
    }
}

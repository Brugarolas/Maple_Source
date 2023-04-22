package me.youm.rocchi.core.command.commands;

import me.youm.rocchi.Rocchi;
import me.youm.rocchi.core.command.ICommand;
import org.apache.commons.lang3.StringUtils;

public class SendCommand extends ICommand {
    public SendCommand() {
        super("send", "*send <player name> <message>");
    }
    @Override
    public boolean execute(String ...args) {
        String receiver = args[1];
        if(!StringUtils.isBlank(receiver) && StringUtils.isBlank(args[2])){
            Rocchi.getInstance().getClient().getPlayer().sendMessage(receiver,args[2]);
        }
        return true;
    }
}

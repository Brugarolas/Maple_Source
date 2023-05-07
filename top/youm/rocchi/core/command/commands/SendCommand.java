package top.youm.rocchi.core.command.commands;

import top.youm.rocchi.Rocchi;
import org.apache.commons.lang3.StringUtils;
import top.youm.rocchi.core.command.Command;

public class SendCommand extends Command {
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

package top.youm.maple.core.command.commands;

import org.lwjgl.input.Keyboard;
import top.youm.maple.Maple;
import top.youm.maple.core.command.Command;
import top.youm.maple.core.command.CommandManager;
import top.youm.maple.core.module.Module;

/**
 * @author YouM
 * Created on 2023/9/29
 */
public class UnBindCommand extends Command {
    public UnBindCommand() {
        super("unbind", "*unbind <module name>");
    }

    @Override
    public boolean execute(String... args) {
        for (Module module : Maple.getInstance().getModuleManager().modules) {
            if(args[1].equalsIgnoreCase(module.getName().replace(" ",""))){
                module.setKey(Keyboard.KEY_NONE);
                CommandManager.helperSend("UnBind Successed! module: " + module.getName(), CommandManager.State.Info);
                return true;
            }
        }
        CommandManager.helperSend("UnBind Filed!",CommandManager.State.Error);
        return false;
    }
}

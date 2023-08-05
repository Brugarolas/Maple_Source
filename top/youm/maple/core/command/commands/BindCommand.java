package top.youm.maple.core.command.commands;

import top.youm.maple.Maple;
import top.youm.maple.core.command.Command;
import top.youm.maple.core.module.Module;
import org.lwjgl.input.Keyboard;

/**
 * @author YouM
 * can bind your keyborad to toggle prescribed module
 */
public class BindCommand extends Command {
    public BindCommand() {
        super("bind", "*bind <module name> <key name>");
    }

    @Override
    public boolean execute(String... args) {
        for (Module module : Maple.getInstance().getModuleManager().modules) {
            if(args[1].equalsIgnoreCase(module.getName())){
                module.setKey(Keyboard.getKeyIndex(args[2].toUpperCase()));
                return true;
            }
        }
        return false;
    }
}

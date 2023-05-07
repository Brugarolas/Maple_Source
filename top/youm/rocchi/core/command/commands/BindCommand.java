package top.youm.rocchi.core.command.commands;

import top.youm.rocchi.Rocchi;
import top.youm.rocchi.core.command.Command;
import top.youm.rocchi.core.module.Module;
import org.lwjgl.input.Keyboard;

public class BindCommand extends Command {
    public BindCommand() {
        super("bind", "*bind <module name> <key name>");
    }

    @Override
    public boolean execute(String... args) {
        for (Module module : Rocchi.getInstance().getModuleManager().modules) {
            if(args[1].equalsIgnoreCase(module.getName())){
                module.setKey(Keyboard.getKeyIndex(args[2].toUpperCase()));
                return true;
            }
        }
        return false;
    }
}

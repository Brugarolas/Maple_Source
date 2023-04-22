package me.youm.rocchi.core.command.commands;

import me.youm.rocchi.Rocchi;
import me.youm.rocchi.core.command.ICommand;
import me.youm.rocchi.core.module.Module;
import org.lwjgl.input.Keyboard;

public class BindCommand extends ICommand {
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

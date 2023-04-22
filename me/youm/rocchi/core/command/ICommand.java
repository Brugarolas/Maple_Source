package me.youm.rocchi.core.command;

public abstract class ICommand {
    private String name;
    private String usage;

    public ICommand(String name, String usage) {
        this.name = name;
        this.usage = usage;
    }

    public abstract boolean execute(String ...args);

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUsage() {
        return usage;
    }

    public void setUsage(String usage) {
        this.usage = usage;
    }
}

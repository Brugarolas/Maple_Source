package top.youm.rocchi.core.config;

import java.io.File;

/**
 * @author YouM
 */
public abstract class Config {
    private final String name;
    protected String context;
    protected File file;
    public Config(String name) {
        this.name = name;
    }

    public abstract void loadConfig();
    public abstract void saveConfig();

    public String getName() {
        return name;
    }

    public String getContext() {
        return context;
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }
}

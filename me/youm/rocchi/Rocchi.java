package me.youm.rocchi;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import me.youm.rocchi.common.settings.SettingManager;
import me.youm.rocchi.core.command.CommandManager;
import me.youm.rocchi.core.config.ConfigManager;
import me.youm.rocchi.core.module.ModuleManager;
import me.youm.rocchi.irc.client.Client;
import net.minecraft.command.server.CommandMessage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.opengl.Display;

/**
 * @author You_M
 */
@SuppressWarnings("all")
public class Rocchi {
    private static final Rocchi instance = new Rocchi();
    public static Gson gson = new GsonBuilder().setPrettyPrinting().create();
    public static Rocchi getInstance() {
        return instance;
    }
    public static Logger log = LogManager.getLogger();
    private final Client client = new Client();
    public String NAME = "Rocchi";
    public String VERSION = "alpha-0.1";
    public String dev = "YouM";
    private ModuleManager moduleManager;
    private SettingManager settingManager;
    private ConfigManager configManager;
    private CommandManager commandManager;
    public void startGame(){
        moduleManager = new ModuleManager();
        settingManager = new SettingManager();
        configManager = new ConfigManager();
        commandManager = new CommandManager();

        moduleManager.initialize();
        configManager.initialize();
        commandManager.initialize();

        log.info("developer: " + dev.toString());
        Display.setTitle(NAME + " | " + VERSION);

        configManager.load();
    }
    public void shutDownGame(){
        log.info("Thank you to play Rocchi client. Goodbye");
        configManager.save();
    }
    public ModuleManager getModuleManager() {
        return moduleManager;
    }

    public SettingManager getSettingManager() {
        return settingManager;
    }

    public ConfigManager getConfigManager() {
        return configManager;
    }

    public Client getClient() {
        return client;
    }
}

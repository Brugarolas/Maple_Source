package top.youm.rocchi;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.minecraft.client.Minecraft;
import top.youm.rocchi.common.settings.SettingManager;
import top.youm.rocchi.core.command.CommandManager;
import top.youm.rocchi.core.config.ConfigManager;
import top.youm.rocchi.core.module.ModuleManager;
import top.youm.rocchi.irc.client.Client;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.opengl.Display;
import top.youm.rocchi.utils.math.MathUtil;

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
    public String username = "YouM"+ MathUtil.getRandomInRange(1,999);;
    private ModuleManager moduleManager;
    private SettingManager settingManager;
    private ConfigManager configManager;
    private CommandManager commandManager;
    public void startGame(){

        moduleManager = new ModuleManager();
        settingManager = new SettingManager();
        moduleManager.initialize();
        configManager = new ConfigManager();
        configManager.initialize();
        commandManager = new CommandManager();
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

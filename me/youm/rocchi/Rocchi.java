package me.youm.rocchi;

import com.darkmagician6.eventapi.EventManager;
import me.youm.rocchi.common.settings.SettingManager;
import me.youm.rocchi.core.module.ModuleManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.opengl.Display;

/**
 * @author You_M
 */
@SuppressWarnings("all")
public class Rocchi {
    private static final Rocchi instance = new Rocchi();
    public static Rocchi getInstance() {
        return instance;
    }
    public static Logger log = LogManager.getLogger();

    public String NAME = "Rocchi";
    public String VERSION = "alpha-0.1";
    public String[] dev = {"YouM"};
    private ModuleManager moduleManager;
    private SettingManager settingManager;
    public void startGame(){
        moduleManager = new ModuleManager();

        settingManager = new SettingManager();

        moduleManager.initialize();

        log.info("developer: " + dev.toString());
        Display.setTitle(NAME + " | " + VERSION);
    }
    public void shutDownGame(){
        log.info("Thank you to play Rocchi client. Goodbye");
    }
    public ModuleManager getModuleManager() {
        return moduleManager;
    }

    public SettingManager getSettingManager() {
        return settingManager;
    }
}

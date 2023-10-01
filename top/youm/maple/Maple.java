package top.youm.maple;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import de.florianmichael.viamcp.ViaMCP;
import top.youm.maple.alts.Account;
import top.youm.maple.alts.AltManager;
import top.youm.maple.core.command.CommandManager;
import top.youm.maple.core.config.ConfigManager;
import top.youm.maple.core.module.ModuleManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.opengl.Display;
import top.youm.maple.core.ui.clickgui.classic.theme.ColorTheme;
import top.youm.maple.core.ui.clickgui.classic.theme.Theme;
import top.youm.maple.utils.BadPacketsComponent;
import top.youm.maple.utils.math.MathUtil;

import java.util.List;

import static top.youm.maple.utils.tools.Catcher.runCatching;

/**
 * @author YouM
 * This mod's main class <br/>
 * include many important field and manager<br/>
 * handle game start and end
 */
@SuppressWarnings("all")
public class Maple {
    private static final Maple instance = new Maple();
    public static Gson gson;
    public static Maple getInstance() {
        return instance;
    }
    public static Logger log = LogManager.getLogger();
    public String NAME = "Maple";
    public String VERSION = "beta-3.0";
    public boolean DevMode = true;
    public Account account;
    public String username = NAME + "Dev" + MathUtil.getRandomInRange(1,999);
    private ModuleManager moduleManager;
    private ConfigManager configManager;
    private CommandManager commandManager;
    private AltManager altManager;
    public void startGame(){
        setTheme(Theme.themes.get("Blue"));

        altManager = new AltManager();
        gson = new GsonBuilder().setPrettyPrinting().excludeFieldsWithoutExposeAnnotation().create();
        moduleManager = new ModuleManager();
        moduleManager.initialize();

        commandManager = new CommandManager();
        commandManager.initialize();

        log.info("developers: YouM");
        Display.setTitle(NAME + " | " + VERSION);
        configManager = new ConfigManager();
        runCatching(() -> {
            configManager.initialize();
            configManager.load();
        });

        runCatching(()->{
            ViaMCP.create();
            ViaMCP.INSTANCE.initAsyncSlider();
            ViaMCP.INSTANCE.initAsyncSlider(100, 100, 110, 20);
        });
        new BadPacketsComponent().init();
    }
    public void shutDownGame(){
        log.info("Thank you to play Maple client. Goodbye");
        configManager.save();
    }
    public ModuleManager getModuleManager() {
        return moduleManager;
    }

    public ConfigManager getConfigManager() {
        return configManager;
    }

    public AltManager getAltManager() {
        return altManager;
    }

    public static void setTheme(ColorTheme theme){
        Theme.theme = theme.getTheme();
        Theme.themeHover = theme.getThemeHover();
        Theme.moduleTheme = theme.getModuleTheme();
        Theme.enableButton = theme.getEnableButton();
    }
}
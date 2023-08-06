package top.youm.maple;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import top.youm.maple.core.command.CommandManager;
import top.youm.maple.core.config.ConfigManager;
import top.youm.maple.core.module.ModuleManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.opengl.Display;
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
    public String VERSION = "beta-1.0";
    public String dev = "YouM";

    public String username = NAME + "Dev" + MathUtil.getRandomInRange(1,999);
    private ModuleManager moduleManager;
    private ConfigManager configManager;
    private CommandManager commandManager;
    public List<String> songList;
/*    public void initializeToolkit()
    {
        final CountDownLatch latch = new CountDownLatch(1);
        SwingUtilities.invokeLater(() -> {
            new JFXPanel();
            latch.countDown();
        });
        try {
            latch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }*/
    public void startGame(){
/*        initializeToolkit();*/
/*        musicPlayerScreen = new MusicPlayerScreen();*/
        gson = new GsonBuilder().setPrettyPrinting().excludeFieldsWithoutExposeAnnotation().create();
        moduleManager = new ModuleManager();
        moduleManager.initialize();

        commandManager = new CommandManager();
        commandManager.initialize();

        log.info("developer: " + dev.toString());
        Display.setTitle(NAME + " | " + VERSION);
        configManager = new ConfigManager();
        runCatching(() -> {
            configManager.initialize();
            configManager.load();
        });
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

    public List<String> getSongList() {
        return songList;
    }
}
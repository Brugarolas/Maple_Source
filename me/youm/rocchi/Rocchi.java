package me.youm.rocchi;

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

    public Logger log = LogManager.getLogger();
    public String NAME = "Rocchi";
    public String VERSION = "alpha-0.1";
    public String[] dev = {"YouM"};
    public void startGame(){
        log.info("developer: " + dev.toString());
        Display.setTitle(NAME + " | " + VERSION);
    }

    public void shutDownGame(){
        log.info("Thank you to play Rocchi client. Goodbye");
    }

}

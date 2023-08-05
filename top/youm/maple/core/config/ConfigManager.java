package top.youm.maple.core.config;

import com.google.gson.Gson;
import top.youm.maple.Maple;
import top.youm.maple.core.config.configs.ModuleConfig;
import net.minecraft.client.Minecraft;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

/**
 * @author YouM
 */
public class ConfigManager {
    public List<Config> configs = new ArrayList<>();
    public File mainDir = new File(Minecraft.getMinecraft().mcDataDir,"/"+ Maple.getInstance().NAME+"/configs/");
    public static Gson gson = Maple.gson;
    public void initialize(){
        configs.add(new ModuleConfig());
        boolean mkdirs = mainDir.mkdirs();
        if(mkdirs){
            Maple.log.info("create success");
        }else {
            Maple.log.warn("create warn");
        }
        for (Config config : configs){
            config.setFile(new File(this.mainDir,config.getName()));
        }
    }

    public void load(){
        configs.forEach(Config::loadConfig);
    }
    public void save(){
        for (Config config : configs) {
            boolean isExists = false;
            if (!config.file.exists()) {
                try {
                    isExists = config.file.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            try {
                Maple.log.info(isExists ? "create file success" : "create file fail");
                config.saveConfig();
                FileUtils.writeStringToFile(config.file,config.context, Charset.defaultCharset());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}

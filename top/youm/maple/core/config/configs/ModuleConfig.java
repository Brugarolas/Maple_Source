package top.youm.maple.core.config.configs;

import com.google.gson.reflect.TypeToken;
import top.youm.maple.Maple;
import top.youm.maple.common.config.ModuleConfiguration;
import top.youm.maple.common.settings.Setting;
import top.youm.maple.common.settings.impl.BoolSetting;
import top.youm.maple.common.settings.impl.ModeSetting;
import top.youm.maple.common.settings.impl.NumberSetting;
import top.youm.maple.core.config.Config;
import top.youm.maple.core.config.ConfigManager;
import top.youm.maple.core.module.Module;
import org.apache.commons.io.FileUtils;
import org.lwjgl.input.Keyboard;
import top.youm.maple.core.module.modules.visual.ClickGui;
import top.youm.maple.utils.tools.Catcher;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author YouM
 * read 
 */
public class ModuleConfig extends Config {
    public ModuleConfig() {
        super("module.json");
    }

    @Override
    public void loadConfig() {
        Catcher.runCatching(()->{
            List<ModuleConfiguration> configurations = ConfigManager.gson.fromJson(
                    FileUtils.readFileToString(this.file),
                    new TypeToken<List<ModuleConfiguration>>() {}.getType()
            );
            for (Module module : Maple.getInstance().getModuleManager().modules) {
                for (ModuleConfiguration configuration : configurations) {

                    if (!configuration.getName().equals(module.getName())) {
                        continue;
                    }
                    if (module.isToggle() != configuration.isEnable()) {
                        module.toggled();
                    }
                    module.setKey(Keyboard.getKeyIndex(configuration.getKey()));
                    List<Setting<?>> settings = module.getSettings();
                    for (Setting<?> setting : settings) {
                        for (Setting<?> configurationSetting : configuration.getSettings()) {
                            if (!setting.getName().equals(configurationSetting.getName())) {
                                continue;
                            }
                            if(setting instanceof NumberSetting){
                                ((NumberSetting)setting).setValue((Number) configurationSetting.getValue());
                            }else if(setting instanceof BoolSetting){
                                ((BoolSetting)setting).setValue((Boolean) configurationSetting.getValue());
                            }else if(setting instanceof ModeSetting){
                                ((ModeSetting)setting).setValue((String) configurationSetting.getValue());
                            }
                        }
                    }
                }
            }
        });
    }

    @Override
    public void saveConfig() {
        List<ModuleConfiguration> configurations = Maple.getInstance().getModuleManager().
                modules.stream().
                map(module -> {
                    if(module != Maple.getInstance().getModuleManager().getModuleByClass(ClickGui.class)){
                        return new ModuleConfiguration(module.getName(), module.isToggle(), Keyboard.getKeyName(module.getKey()),module.getSettings());
                    }
                    if(module.isToggle())module.toggled();
                    return new ModuleConfiguration(module.getName(), module.isToggle(), Keyboard.getKeyName(module.getKey()),module.getSettings());
                }).
                collect(Collectors.toList());

        this.context = ConfigManager.gson.toJson(configurations);
    }
}

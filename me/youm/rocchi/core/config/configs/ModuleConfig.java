package me.youm.rocchi.core.config.configs;

import com.google.gson.reflect.TypeToken;
import me.youm.rocchi.Rocchi;
import me.youm.rocchi.common.config.ModuleConfiguration;
import me.youm.rocchi.core.config.Config;
import me.youm.rocchi.core.config.ConfigManager;
import me.youm.rocchi.core.module.Module;
import org.apache.commons.io.FileUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import java.util.List;
import java.util.stream.Collectors;

public class ModuleConfig extends Config {
    public ModuleConfig() {
        super("module.json");
    }

    @Override
    public void loadConfig() {

        try {
            List<ModuleConfiguration> configurations = ConfigManager.gson.fromJson(
                    FileUtils.readFileToString(this.file),
                    new TypeToken<List<ModuleConfiguration>>() {
            }.getType());
            for (Module module : Rocchi.getInstance().getModuleManager().modules) {
                for (ModuleConfiguration configuration : configurations) {
                    if (configuration.getName().equals(module.getName())){
                        module.setToggle(configuration.isEnable());
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void saveConfig() {
        List<ModuleConfiguration> configurations = Rocchi.getInstance().getModuleManager().
                modules.stream().
                map(module -> new ModuleConfiguration(module.getName(), module.isToggle())).
                collect(Collectors.toList());

        this.context = ConfigManager.gson.toJson(configurations);
    }
}

package top.youm.rocchi.core.config.configs;


import com.google.gson.reflect.TypeToken;
import org.apache.commons.io.FileUtils;
import top.youm.rocchi.Rocchi;
import top.youm.rocchi.common.config.SettingConfiguration;
import top.youm.rocchi.common.settings.Setting;
import top.youm.rocchi.common.settings.impl.BoolSetting;
import top.youm.rocchi.common.settings.impl.ModeSetting;
import top.youm.rocchi.common.settings.impl.NumberSetting;
import top.youm.rocchi.core.config.Config;
import top.youm.rocchi.core.config.ConfigManager;
import top.youm.rocchi.core.module.Module;

import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class SettingConfig extends Config {
    public SettingConfig() {
        super("setting.json");
    }

    @Override
    public void loadConfig() {
    }
    @Override
    public void saveConfig() {
        List<SettingConfiguration> settingConfigs = Rocchi.getInstance().getModuleManager().modules.
                stream().map(
                        module -> new SettingConfiguration(module.getName(), module.getSettings())
                ).collect(Collectors.toList());
        this.context = ConfigManager.gson.toJson(settingConfigs);
    }
}

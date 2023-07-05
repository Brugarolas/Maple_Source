package top.youm.rocchi.core.config.configs;


import top.youm.rocchi.Rocchi;
import top.youm.rocchi.common.settings.BoolSetting;
import top.youm.rocchi.common.settings.Setting;
import top.youm.rocchi.core.config.Config;
import top.youm.rocchi.core.config.ConfigManager;
import top.youm.rocchi.core.module.Module;

import java.util.List;
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
        List<top.youm.rocchi.common.config.SettingConfiguration> settingConfigs = Rocchi.getInstance().getModuleManager().modules.stream().map(
                module -> new top.youm.rocchi.common.config.SettingConfiguration(module.getName(), module.getSettings())
        ).collect(Collectors.toList());

        this.context = ConfigManager.gson.toJson(settingConfigs);
    }
}

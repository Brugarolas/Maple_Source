package me.youm.rocchi.common.settings;

import me.youm.rocchi.core.module.Module;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SettingManager {
    public Map<Module, List<Setting<?>>> settings = new HashMap<>();

    public List<Setting<?>> getSettingsByModule(Module module){
        return settings.get(module);
    }
    public Setting<?> getSettingNameByModule(Module module,String name){
        List<Setting<?>> settingsModule = getSettingsByModule(module);
        for (Setting<?> setting : settingsModule) {
            if (setting.getName().equals(name)) {
                return setting;
            }
        }
        return null;
    }
}

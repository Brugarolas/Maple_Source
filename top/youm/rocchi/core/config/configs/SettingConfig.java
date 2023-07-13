//package top.youm.rocchi.core.config.configs;
//
//
//import com.google.gson.reflect.TypeToken;
//import org.apache.commons.io.FileUtils;
//import top.youm.rocchi.Rocchi;
//import top.youm.rocchi.common.config.SettingConfiguration;
//import top.youm.rocchi.core.config.Config;
//import top.youm.rocchi.core.config.ConfigManager;
//import top.youm.rocchi.core.module.Module;
//
//import java.io.IOException;
//import java.util.List;
//import java.util.stream.Collectors;
//
//public class SettingConfig extends Config {
//    public SettingConfig() {
//        super("setting.json");
//    }
//
//    @Override
//    public void loadConfig() {
//        try {
//            List<SettingConfiguration> configurations = ConfigManager.gson.fromJson(
//                    FileUtils.readFileToString(this.file),
//                    new TypeToken<List<SettingConfiguration>>() {}.getType()
//            );
//            for (SettingConfiguration configuration : configurations) {
//                for (Module module : Rocchi.getInstance().getModuleManager().modules) {
//                    if(!module.getName().equals(configuration.getModuleName())){
//                        continue;
//                    }
////                    for (Setting<?> settingConfigurationSetting : configuration.getSettings()) {
////                        for (Setting<?> setting : module.getSettings()) {
////                            if(Objects.equals(settingConfigurationSetting.getName(), setting.getName())){
////                                if(setting instanceof BoolSetting){
////                                    ((BoolSetting)setting).setValue( settingConfigurationSetting.getValue());
////                                }else if(setting instanceof NumberSetting){
////                                    ((NumberSetting)setting).setValue((settingConfigurationSetting.getValue()));
////                                }else if(setting instanceof ModeSetting && settingConfigurationSetting instanceof ModeSetting){
////                                    ((ModeSetting<?>)setting).setValueEnum(((ModeSetting<?>) settingConfigurationSetting).getValue());
////                                }
////                            }
////                        }
////                    }
//                }
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
//    @Override
//    public void saveConfig() {
//        List<SettingConfiguration> settingConfigs = Rocchi.getInstance().getModuleManager().modules.
//                stream().map(
//                        module -> new SettingConfiguration(module.getName(), module.getSettings())
//                ).collect(Collectors.toList());
//        this.context = ConfigManager.gson.toJson(settingConfigs);
//    }
//}

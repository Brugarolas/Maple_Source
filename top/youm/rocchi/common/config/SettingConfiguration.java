package top.youm.rocchi.common.config;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import top.youm.rocchi.common.settings.Setting;

import java.util.List;

/**
 * @author YouM
 */
public class SettingConfiguration {
    //module setting config name
    @Expose
    @SerializedName("moduleName")
    private String moduleName;
    //module settings list
    @Expose
    @SerializedName("settings")
    private List<Setting<?>> settings;

    public SettingConfiguration(String moduleName, List<Setting<?>> settings) {
        this.moduleName = moduleName;
        this.settings = settings;
    }

    public String getModuleName() {
        return moduleName;
    }

    public void setModuleName(String moduleName) {
        this.moduleName = moduleName;
    }

    public List<Setting<?>> getSettings() {
        return settings;
    }

    public void setSettings(List<Setting<?>> settings) {
        this.settings = settings;
    }

    @Override
    public String toString() {
        return "SettingConfiguration{" +
                "moduleName='" + moduleName + '\'' +
                ", settings=" + settings +
                '}';
    }
}

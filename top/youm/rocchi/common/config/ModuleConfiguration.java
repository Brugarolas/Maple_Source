package top.youm.rocchi.common.config;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import top.youm.rocchi.common.settings.Setting;

import java.io.Serializable;
import java.util.List;

/**
 * @author YouM
 */
public class ModuleConfiguration implements Serializable {
    //module config name
    @Expose
    @SerializedName("name")
    private String name;
    //module config is enable
    @Expose
    @SerializedName("enable")
    private boolean enable;
    //module config key bind
    @Expose
    @SerializedName("key")
    private String key;
    //module config settings
    @Expose
    @SerializedName("settings")
    private List<Setting<?>> settings;
    public ModuleConfiguration(String name,boolean enable,String key,List<Setting<?>> settings){
        this.name = name;
        this.enable = enable;
        this.key = key;
        this.settings = settings;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getName(){
        return this.name;
    }
    public void setName(String name){
        this.name = name;
    }
    public boolean isEnable(){
        return this.enable;
    }
    public void setEnable(boolean enable){
        this.enable = enable;
    }

    public List<Setting<?>> getSettings() {
        return settings;
    }

    public void setSettings(List<Setting<?>> settings) {
        this.settings = settings;
    }
}

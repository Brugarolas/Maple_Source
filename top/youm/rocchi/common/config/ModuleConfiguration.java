package top.youm.rocchi.common.config;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

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
    public ModuleConfiguration(String name,boolean enable,String key){
        this.name = name;
        this.enable = enable;
        this.key = key;
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
}

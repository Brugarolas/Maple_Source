package top.youm.rocchi.common.config;

import java.io.Serializable;

/**
 * @author YouM
 */
public class ModuleConfiguration implements Serializable {
    //module config name
    private String name;
    //module config is enable
    private boolean enable;
    //module config key bind
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

package top.youm.rocchi.common.config;

import java.io.Serializable;

public class ModuleConfiguration implements Serializable {
    private String name;
    private boolean enable;
    private String key;
    public ModuleConfiguration(){

    }
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

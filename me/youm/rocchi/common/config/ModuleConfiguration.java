package me.youm.rocchi.common.config;

import java.io.Serializable;

public class ModuleConfiguration implements Serializable {
    private String name;
    private boolean enable;
    public ModuleConfiguration(){

    }
    public ModuleConfiguration(String name,boolean enable){
        this.name = name;
        this.enable = enable;
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

package me.youm.rocchi.core.module;

import com.darkmagician6.eventapi.EventTarget;

import java.util.ArrayList;
import java.util.List;

public class ModuleManager {
    public List<Module> modules = new ArrayList<>();

    public void initialize(){

    }
    public <T extends Module> T getModuleByClass(Class<T> moduleClass){
        for (Module module : modules) {
            if (module.getClass().equals(moduleClass)) {
                return (T) module;
            }
        }
        return null;
    }

}

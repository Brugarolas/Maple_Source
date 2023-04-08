package me.youm.rocchi.core.module;

import com.darkmagician6.eventapi.EventManager;
import com.darkmagician6.eventapi.EventTarget;

import me.youm.rocchi.common.events.KeyEvent;
import me.youm.rocchi.core.module.modules.client.HUD;
import me.youm.rocchi.core.module.modules.combat.KillAura;
import me.youm.rocchi.core.module.modules.movement.Sprint;
import me.youm.rocchi.core.module.modules.visual.Animations;
import me.youm.rocchi.core.module.modules.visual.KeyStrokes;
import me.youm.rocchi.core.module.modules.visual.ModuleList;
import me.youm.rocchi.core.module.modules.player.NoSlow;
import java.util.ArrayList;
import java.util.List;

public class ModuleManager {
    public List<Module> modules = new ArrayList<>();

    public void initialize(){
        /* combat */
        this.modules.add(new KillAura());
        /* movement */
        this.modules.add(new Sprint());
        /* client */
        this.modules.add(new HUD());
        /* player */
        this.modules.add(new NoSlow());
        /* visual */
        this.modules.add(new Animations());
        this.modules.add(new KeyStrokes());
        this.modules.add(new ModuleList());
        EventManager.register(this);
    }
    public <T extends Module> T getModuleByClass(Class<T> moduleClass){
        for (Module module : modules) {
            if (module.getClass() == (moduleClass)) {
                return (T) module;
            }
        }
        return null;
    }
    @EventTarget
    public void onKey(KeyEvent event){
        this.modules.forEach(module -> {
            if(module.getKey() == event.getKey()){
                module.toggled();
            }
        });
    }
}

package top.youm.maple.core.module;

import com.darkmagician6.eventapi.EventManager;
import com.darkmagician6.eventapi.EventTarget;

import top.youm.maple.common.events.KeyEvent;
import top.youm.maple.core.module.modules.combat.*;
import top.youm.maple.core.module.modules.movement.*;
import top.youm.maple.core.module.modules.player.*;
import top.youm.maple.core.module.modules.visual.*;
import top.youm.maple.core.module.modules.world.Disabler;
import top.youm.maple.core.module.modules.world.Teams;

import java.util.ArrayList;
import java.util.List;

/**
 * @author YouM
 */
public class ModuleManager {
    public List<Module> modules = new ArrayList<>();

    public void initialize(){
        /* combat */
        this.modules.add(new KillAura());
        this.modules.add(new FastBow());
        this.modules.add(new AntiBot());
        this.modules.add(new Criticals());
        this.modules.add(new TargetStrafe());
        this.modules.add(new Velocity());
        this.modules.add(new SuperKnockback());
        /* movement */
        this.modules.add(new Sprint());
        this.modules.add(new Speed());
        this.modules.add(new Step());
        this.modules.add(new Fly());
        this.modules.add(new SafeScaffold());
        this.modules.add(new InventoryMove());
        this.modules.add(new NoSlow());
        /* player */
        this.modules.add(new FullBright());
        this.modules.add(new SafeWalk());
        this.modules.add(new InvManager());
        this.modules.add(new FastPlace());
        this.modules.add(new NoFall());
        this.modules.add(new Timer());
        this.modules.add(new Blink());
        this.modules.add(new AutoArmor());
        this.modules.add(new AutoTool());
        this.modules.add(new Freecam());
        this.modules.add(new AutoL());
        this.modules.add(new AntiVoid());
        this.modules.add(new ChestStealer());
        /* visual */
        this.modules.add(new Animations());
        this.modules.add(new KeyStrokes());
        this.modules.add(new HUD());
        this.modules.add(new ModuleList());
        this.modules.add(new ClickGui());
        this.modules.add(new ESP());
        this.modules.add(new Statistics());
        /* world*/
        this.modules.add(new Teams());
        this.modules.add(new Disabler());
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

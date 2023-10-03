package top.youm.maple.core.module;

import com.darkmagician6.eventapi.EventManager;
import com.darkmagician6.eventapi.EventTarget;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.config.plugins.ResolverUtil;
import top.youm.maple.common.events.KeyEvent;
import top.youm.maple.core.module.modules.combat.*;
import top.youm.maple.core.module.modules.combat.killaura.KillAura;
import top.youm.maple.core.module.modules.movement.*;
import top.youm.maple.core.module.modules.player.*;
import top.youm.maple.core.module.modules.visual.*;
import top.youm.maple.core.module.modules.world.*;

import java.lang.reflect.Modifier;
import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

/**
 * @author YouM
 */
public class ModuleManager {
    public List<Module> modules = new ArrayList<>();
    private static final Logger logger = LogManager.getLogger();
    public void initialize(){
        scanModules();
        modules.sort(Comparator.comparingInt(module -> Character.getNumericValue(module.getName().charAt(0))));

        EventManager.register(this);
        this.getModuleByClass(HUD.class).initRenderModule();
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
    public void scanModules(){
        ResolverUtil resolver = initResolverUtil();
        for (Class<?> aClass : resolver.getClasses()) {
            if (Module.class.isAssignableFrom(aClass) &&
                    !aClass.isInterface() &&
                    !aClass.isAnnotation() &&
                    !Modifier.isAbstract(aClass.getModifiers())) {
                try {
                    this.modules.add((Module) aClass.newInstance());
                } catch (InstantiationException | IllegalAccessException e) {
                    logger.error(e.getMessage());
                }
            }
        }
    }

    private ResolverUtil initResolverUtil() {
        ResolverUtil resolver = new ResolverUtil();
        resolver.findInPackage(new ResolverUtil.Test() {
            @Override
            public boolean matches(Class<?> aClass) {
                return true;
            }

            @Override
            public boolean matches(URI uri) {
                return true;
            }

            @Override
            public boolean doesMatchClass() {
                return true;
            }

            @Override
            public boolean doesMatchResource() {
                return true;
            }
        },getClass().getPackage().getName() +".modules");
        return resolver;
    }
}

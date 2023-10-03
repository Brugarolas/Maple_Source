package top.youm.maple.core.module;

import com.darkmagician6.eventapi.EventManager;
import net.minecraft.client.Minecraft;
import top.youm.maple.common.settings.Setting;
import top.youm.maple.core.ui.clickgui.classic.theme.Icon;
import top.youm.maple.core.ui.hud.components.noti.NotificationManager;
import top.youm.maple.utils.animation.Animation;
import top.youm.maple.utils.animation.Direction;
import top.youm.maple.utils.animation.SmoothStepAnimation;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author YouM
 */
public class Module {
    public static Minecraft mc = Minecraft.getMinecraft();
    private boolean enabled;
    public boolean isRenderModule = false;
    public Animation animation = new SmoothStepAnimation(200,1).setDirection(Direction.BACKWARDS);
    private String name;
    private String suffixes = "";
    private ModuleCategory category;
    private int key;
    private List<Setting<?>> settings = new ArrayList<>();
    public boolean wasRemoved;
    public float animX,animY;
    public Module(String name, ModuleCategory category, int key) {
        this(name,"",category,key);
    }

    public Module(String name, String suffixes, ModuleCategory category, int key) {
        this.name = name;
        this.suffixes = suffixes;
        this.category = category;
        this.key = key;
    }
    public boolean isEnabled() {
        return enabled;
    }
    public void setEnabled(boolean enabled){
        this.enabled = enabled;
        this.checkEnabled();
    }
    public void toggled(){
        this.enabled = !this.enabled;
        this.checkEnabled();
    }
    private void checkEnabled() {
        if (enabled) {
            EventManager.register(this);
            this.onEnable();
            NotificationManager.show(this.name,this.name + ": has enabled",this, Icon.SUCCESS);
        } else {
            EventManager.unregister(this);
            this.onDisable();
            NotificationManager.show(this.name,this.name + ": has disabled",this, Icon.FAILED);
        }

    }

    public void onEnable(){

    }
    public void onDisable(){

    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSuffixes() {
        return suffixes;
    }

    public void setSuffixes(String suffixes) {
        this.suffixes = suffixes;
    }

    public ModuleCategory getCategory() {
        return category;
    }

    public void setCategory(ModuleCategory category) {
        this.category = category;
    }

    public int getKey() {
        return key;
    }

    public void setKey(int key) {
        this.key = key;
    }

    public List<Setting<?>> getSettings() {
        return settings;
    }
    public void addSettings(Setting<?> ...settings){
        this.settings.addAll(Arrays.stream(settings).collect(Collectors.toCollection(ArrayList::new)));
    }
    public void addSetting(Setting<?> setting){
        this.settings.add(setting);
    }

}

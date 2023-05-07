package top.youm.rocchi.core.module;

import com.darkmagician6.eventapi.EventManager;
import top.youm.rocchi.Rocchi;
import top.youm.rocchi.common.settings.Setting;
import net.minecraft.client.Minecraft;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Module {
    protected Minecraft mc = Minecraft.getMinecraft();
    private boolean toggle;
    private String name;
    private String suffixes = "";
    private ModuleCategory category;
    private int key;
    private List<Setting<?>> settings = new ArrayList<>();
    public boolean wasRemoved;
    public float animX,animY;
    public Module(String name, ModuleCategory category, int key) {
        this.name = name;
        this.category = category;
        this.key = key;
    }

    public Module(String name, String suffixes, ModuleCategory category, int key) {
        this.name = name;
        this.suffixes = suffixes;
        this.category = category;
        this.key = key;
    }
    public boolean isToggle() {
        return toggle;
    }

    public void setToggle(boolean toggle) {
        this.toggle = toggle;
        this.isEnabled();
    }
    public void toggled(){
        this.toggle = !this.toggle;
        this.isEnabled();
    }
    public void isEnabled(){
        if(toggle) {
            EventManager.register(this);
            this.onEnable();
        } else {
            EventManager.unregister(this);
            this.onDisable();
        }
    }

    public void onEnable(){}
    public void onDisable(){}

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
    public void setSettings(List<Setting<?>> settings) {
        this.settings = settings;
    }
    public void addSetting(Setting<?> ...setting){
        this.settings.addAll(Arrays.stream(setting).collect(Collectors.toCollection(ArrayList::new)));
        Rocchi.getInstance().getSettingManager().settings.put(this,this.settings);
    }

}

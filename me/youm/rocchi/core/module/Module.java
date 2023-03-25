package me.youm.rocchi.core.module;

public class Module {
    private boolean toggle;
    private String name;
    private String suffixes;
    private ModuleCategory category;
    private int key;

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
}

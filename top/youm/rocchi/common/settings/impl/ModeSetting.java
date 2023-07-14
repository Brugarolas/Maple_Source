package top.youm.rocchi.common.settings.impl;

import top.youm.rocchi.common.settings.Setting;

/**
 * @author YouM
 * can update mode setting like dropdown
 */
public class ModeSetting extends Setting<String> {
    private final String[] modes;
    public ModeSetting(String name,String value,String... modes) {
        super(name, value);
        this.modes = modes;
    }

    public String[] getModes() {
        return modes;
    }
}

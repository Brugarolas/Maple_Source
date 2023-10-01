package top.youm.maple.common.settings.impl;

import top.youm.maple.common.settings.Setting;

/**
 * @author YouM
 * can update mode setting like dropdown
 */
public class SelectButtonSetting extends Setting<String> {
    private final String[] modes;
    public SelectButtonSetting(String name, String value, String... modes) {
        super(name, value);
        this.modes = modes;
    }

    public String[] getModes() {
        return modes;
    }
}

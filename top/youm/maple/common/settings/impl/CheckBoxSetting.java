package top.youm.maple.common.settings.impl;

import top.youm.maple.common.settings.Setting;

/**
 * @author YouM
 * can enable or disable setting like checkbox
 */
public class CheckBoxSetting extends Setting<Boolean> {
    public CheckBoxSetting(String name, boolean value) {
        super(name, value);
    }
}

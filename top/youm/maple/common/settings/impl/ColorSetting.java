package top.youm.maple.common.settings.impl;

import top.youm.maple.common.settings.Setting;

import java.awt.*;

/**
 * @author YouM
 * can update color setting like palette
 */
public class ColorSetting extends Setting<Color> {

    public ColorSetting(String name, Color value) {
        super(name, value);
    }

}

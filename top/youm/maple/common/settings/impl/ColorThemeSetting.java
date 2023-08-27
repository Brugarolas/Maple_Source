package top.youm.maple.common.settings.impl;

import top.youm.maple.common.settings.Setting;
import top.youm.maple.core.ui.clickgui.classic.theme.ColorTheme;
import top.youm.maple.core.ui.clickgui.classic.theme.Theme;

import java.awt.*;
import java.util.Map;

/**
 * @author YouM
 * Created on 2023/8/27
 */
public class ColorThemeSetting extends Setting<Map<String, ColorTheme>> {
    private ColorTheme currentTheme;
    public ColorThemeSetting(String name, Map<String,ColorTheme> colors) {
        super(name, colors);
    }

    public ColorTheme getCurrentTheme() {
        return currentTheme;
    }

    public void setCurrentTheme(ColorTheme currentTheme) {
        this.currentTheme = currentTheme;
    }

}

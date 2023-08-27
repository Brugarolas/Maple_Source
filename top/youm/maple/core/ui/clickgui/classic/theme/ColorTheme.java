package top.youm.maple.core.ui.clickgui.classic.theme;

import java.awt.*;

public class ColorTheme{
    private final Color theme;
    private final Color moduleTheme;
    private final Color themeHover;
    private final Color enableButton;

    public ColorTheme(Color theme, Color moduleTheme, Color themeHover, Color enableButton) {
        this.theme = theme;
        this.moduleTheme = moduleTheme;
        this.themeHover = themeHover;
        this.enableButton = enableButton;
    }

    public Color getTheme() {
        return theme;
    }

    public Color getModuleTheme() {
        return moduleTheme;
    }

    public Color getThemeHover() {
        return themeHover;
    }

    public Color getEnableButton() {
        return enableButton;
    }
}


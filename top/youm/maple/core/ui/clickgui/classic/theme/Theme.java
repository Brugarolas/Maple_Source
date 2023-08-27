package top.youm.maple.core.ui.clickgui.classic.theme;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @author YouM
 */
public class Theme {
    private static final ColorTheme blue = new ColorTheme(
                new Color(74, 97, 164),
                new Color(50, 66, 113),
                new Color(74, 97, 164, 69),
                new Color(110, 122, 153)
        );
    private static final ColorTheme pink = new ColorTheme(
            new Color(162, 96, 216),
            new Color(105, 58, 142),
            new Color(162, 96, 216, 69),
            new Color(131, 110, 153)
    );

    public static Color theme;
    public static Color moduleTheme;
    public static Color themeHover;
    public static Color enableButton;

    public static HashMap<String,ColorTheme> themes = new HashMap<>();

    static {
        themes.put("Pink",pink);
        themes.put("Blue",blue);
    }

    public static Color background = new Color(10,10,10);
    public static Color font = new Color(243, 243, 243);

}

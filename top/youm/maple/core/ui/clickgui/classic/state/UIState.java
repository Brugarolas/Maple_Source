package top.youm.maple.core.ui.clickgui.classic.state;

import top.youm.maple.core.module.ModuleCategory;
import top.youm.maple.core.ui.clickgui.classic.component.DialogComponent;
import top.youm.maple.core.ui.clickgui.classic.component.ModuleComponent;

public class UIState {
    public static ModuleCategory currentCategory = ModuleCategory.COMBAT;
    public static boolean settingFocused = false;
    public static ModuleComponent focusKey;
    public static DialogComponent dialog;
}

package top.youm.rocchi.core.ui.clickgui.modern.state;

import top.youm.rocchi.core.module.ModuleCategory;
import top.youm.rocchi.core.ui.clickgui.modern.component.DialogComponent;
import top.youm.rocchi.core.ui.clickgui.modern.component.ModuleComponent;

public class UIState {
    public static ModuleCategory currentCategory = ModuleCategory.COMBAT;
    public static boolean settingFocused = false;
    public static ModuleComponent focusKey;
    public static DialogComponent dialog;
}

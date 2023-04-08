package me.youm.rocchi.core.module.modules.visual;

import com.darkmagician6.eventapi.EventTarget;
import com.mojang.realmsclient.gui.ChatFormatting;
import me.youm.rocchi.Rocchi;
import me.youm.rocchi.common.events.Render2DEvent;
import me.youm.rocchi.common.settings.BoolSetting;
import me.youm.rocchi.common.settings.ModeSetting;
import me.youm.rocchi.common.settings.NumberSetting;
import me.youm.rocchi.core.module.Module;
import me.youm.rocchi.core.module.ModuleCategory;
import me.youm.rocchi.core.ui.hud.HUDComponent;
import me.youm.rocchi.core.ui.hud.ModuleListUI;
import me.youm.rocchi.utils.AnimationUtils;
import me.youm.rocchi.utils.TimerUtil;
import me.youm.rocchi.utils.render.ColorUtil;
import me.youm.rocchi.utils.render.RenderUtil;
import net.minecraft.client.gui.ScaledResolution;
import org.lwjgl.input.Keyboard;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ModuleList extends Module {
    public ModeSetting<Mode> mode = new ModeSetting<>("mode",Mode.values(),Mode.RAINBOW);
    public BoolSetting font = new BoolSetting("ttf-font",false);
    public BoolSetting rect = new BoolSetting("rect",true);
    public BoolSetting shadow = new BoolSetting("shadow",false);
    public BoolSetting border = new BoolSetting("border",true);
    public NumberSetting rectAlpha = new NumberSetting("alpha",100,255,0,1);
    public NumberSetting speed = new NumberSetting("speed",20,100,0,1);
    private final ModuleListUI moduleListUI = new ModuleListUI();
    public ModuleList() {
        super("ArrayList", ModuleCategory.VISUAL, Keyboard.KEY_U);
    }
    @EventTarget
    public void onRender2D(Render2DEvent event){
        moduleListUI.draw();
    }
    public enum Mode{
        RAINBOW,FADE
    }
}

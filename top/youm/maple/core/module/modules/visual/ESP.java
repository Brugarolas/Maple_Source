package top.youm.maple.core.module.modules.visual;

import com.darkmagician6.eventapi.EventTarget;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.player.EntityPlayer;
import org.lwjgl.input.Keyboard;
import org.lwjgl.util.vector.Vector4f;
import top.youm.maple.common.events.Render2DEvent;
import top.youm.maple.common.events.Render3DEvent;
import top.youm.maple.common.settings.impl.BoolSetting;
import top.youm.maple.core.module.Module;
import top.youm.maple.core.module.ModuleCategory;
import top.youm.maple.utils.render.ESPUtil;
import top.youm.maple.utils.render.RenderUtil;

import java.awt.*;
import java.util.*;

/**
 * @author YouM
 * Created on 2023/7/22
 */
public class ESP extends Module {

    public ESP() {
        super("ESP", ModuleCategory.VISUAL, Keyboard.KEY_NONE);
    }

}

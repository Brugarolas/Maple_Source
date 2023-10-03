package top.youm.maple.core.module.modules.visual;

import com.darkmagician6.eventapi.EventTarget;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.input.Keyboard;
import top.youm.maple.common.events.TickEvent;
import top.youm.maple.common.settings.impl.SliderSetting;
import top.youm.maple.core.module.Module;
import top.youm.maple.core.module.ModuleCategory;
import top.youm.maple.utils.render.MotionBlurResourceManager;

import java.util.HashMap;
import java.util.Map;

public class MotionBlur extends Module {
    float lastValue;
    public static SliderSetting amount = new SliderSetting("MotionBlur", 1f, 10f, 0f, 0.1f);
    private final Map<String, IResourceManager> domainResourceManagers = new HashMap<>();

    public MotionBlur() {
        super("Motion Blur", ModuleCategory.VISUAL, Keyboard.KEY_NONE);
        this.addSettings(amount);
    }
    @EventTarget
    public void onTick(TickEvent event){
        this.setSuffixes("" + amount.getValue().floatValue());
    }
    @Override
    public void onDisable() {
        mc.entityRenderer.stopUseShader();
        super.onDisable();
    }

    @EventTarget
    public void onClientTick(TickEvent event) {
        try {
            float curValue = amount.getValue().floatValue();

            if (!mc.entityRenderer.isShaderActive() && mc.theWorld != null) {
                mc.entityRenderer.loadShader(new ResourceLocation("shaders/post/motionblur.json"));
            }


            if (!domainResourceManagers.containsKey("motionblur")) {
                domainResourceManagers.put("motionblur", new MotionBlurResourceManager());
            }

            if (curValue != lastValue) {
                domainResourceManagers.remove("motionblur");
                domainResourceManagers.put("motionblur", new MotionBlurResourceManager());
                mc.entityRenderer.loadShader(new ResourceLocation("shaders/post/motionblur.json"));
            }

            lastValue = curValue;
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }
}

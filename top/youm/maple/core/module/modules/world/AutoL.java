package top.youm.maple.core.module.modules.world;

import com.darkmagician6.eventapi.EventTarget;
import org.lwjgl.input.Keyboard;
import top.youm.maple.common.events.TickEvent;
import top.youm.maple.common.settings.impl.SliderSetting;
import top.youm.maple.core.module.Module;
import top.youm.maple.core.module.ModuleCategory;
import top.youm.maple.utils.TimerUtil;
import top.youm.maple.utils.player.ChatUtil;

/**
 * @author YouM
 * Created on 2023/7/17
 */
public class AutoL extends Module {
    SliderSetting delay = new SliderSetting("delay",0,5000,1000,100);
    public AutoL() {
        super("AutoL", ModuleCategory.WORLD, Keyboard.KEY_NONE);
        this.addSetting(delay);
    }
    TimerUtil timer = new TimerUtil();
    private final String[] context = new String[]{
            "你属于什么够懒","看你神爹我郑肆你目","狗叫尼姆抽报你嘛","我抽爆拟目的臭碧",
            "你是不是想吃你爹我大迪克","无能为力只能开纪","别装逼，小心沙了你目"
    };

    @Override
    public void onEnable() {

        /*new Thread(()->{
            while (this.isToggle()){
                try {

                    ChatUtil.send(s);
                    Thread.sleep(delay.getValue().longValue());
                    if(!this.isToggle()){
                        break;
                    }
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }).start();*/
    }
    @EventTarget
    public void onTick(TickEvent event){
        for (String message : context) {
            if(timer.hasTimeElapsed(delay.getValue().longValue(),true)){
                ChatUtil.send(message);
            }
        }
    }
}

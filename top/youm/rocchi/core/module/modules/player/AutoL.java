package top.youm.rocchi.core.module.modules.player;

import com.darkmagician6.eventapi.EventTarget;
import org.lwjgl.input.Keyboard;
import top.youm.rocchi.common.events.TickEvent;
import top.youm.rocchi.common.settings.impl.NumberSetting;
import top.youm.rocchi.core.module.Module;
import top.youm.rocchi.core.module.ModuleCategory;
import top.youm.rocchi.utils.TimerUtil;
import top.youm.rocchi.utils.player.ChatUtil;

/**
 * @author YouM
 * Created on 2023/7/17
 */
public class AutoL extends Module {
    NumberSetting delay = new NumberSetting("delay",1000,5000,1000,100);
    public AutoL() {
        super("AutoL", ModuleCategory.PLAYER, Keyboard.KEY_NONE);
        this.addSetting(delay);
    }
    TimerUtil timer = new TimerUtil();
    private final String[] context = new String[]{
            "你属于什么够懒","看你神爹我郑肆你目","狗叫尼姆抽报你嘛","我抽爆拟目的臭碧",
            "你是不是想吃你爹我大迪克","无能为力只能开纪","别装逼，小心沙了你目"
    };

    @Override
    public void onEnable() {
        new Thread(()->{
            while (this.isToggle()){
                try {
                    for (String s : context) {
                        ChatUtil.send(s);
                        Thread.sleep(delay.getValue().longValue());
                        if(!this.isToggle()){
                            break;
                        }
                    }
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }).start();
    }
}

package top.youm.maple.core.ui.hud.noti;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import top.youm.maple.core.module.Module;
import top.youm.maple.core.module.modules.visual.HUD;
import top.youm.maple.core.ui.clickgui.classic.theme.Theme;
import top.youm.maple.core.ui.font.FontLoaders;
import top.youm.maple.utils.AnimationUtils;
import top.youm.maple.utils.TimerUtil;
import top.youm.maple.utils.render.RoundedUtil;

import java.awt.*;

import static top.youm.maple.core.ui.font.FontLoaders.rise18;
import static top.youm.maple.core.ui.font.FontLoaders.robotoB22;

/**
 * @author YouM
 * Created on 2023/8/9
 */
public class Notification {
    private float notiHeight,notiWidth;
    private Module module;
    private String message;
    private String title;
    private AnimationUtils animator = new AnimationUtils();
    private float animationX,animationY;
    private long stayTime;
    private TimerUtil timer;

    public Notification(String title, String message, long stayTime) {
        this(title, message, stayTime, null);
    }
    public Notification(String title, String message, long stayTime, Module module) {
        ScaledResolution sr = new ScaledResolution(Minecraft.getMinecraft());
        this.module = module;
        this.message = message;
        this.title = title;
        this.notiHeight = 25.0f;
        this.notiWidth = rise18.getStringWidth(message) + 45;
        this.animationX = sr.getScaledWidth();
        this.stayTime = stayTime;
        this.timer = new TimerUtil();
    }
    public void draw(float width ,float offsetY){
        float target;
        if(!isFinished()){
            target = width - this.notiWidth;
        }else{
            target = width;
        }
        this.animationX = animator.animate(target, this.animationX, 0.1f);
        if (animationY == 0) {
            animationY = offsetY;
        }
        this.animationY = animator.animate(offsetY, animationY,0.1f);

        RoundedUtil.drawRound(this.animationX,this.animationY,this.notiWidth, notiHeight,2, new Color(0,0,0,150));
        RoundedUtil.drawRound(this.animationX,this.animationY + this.notiHeight - 2,this.notiWidth, 2,2, HUD.getHUDThemeColor());
        robotoB22.drawStringWithShadow(title,animationX + 35,animationY + 3,-1);
        rise18.drawStringWithShadow(message,animationX + 35,animationY + 5 + robotoB22.getHeight(),-1);
    }
    public float getHeight() {
        return notiHeight;
    }
    public boolean shouldDelete() {
        ScaledResolution sr = new ScaledResolution(Minecraft.getMinecraft());
        return isFinished() && this.animationX >= sr.getScaledWidth() - 2;
    }
    private boolean isFinished() {
        return timer.hasTimeElapsed(stayTime);
    }
}

package top.youm.maple.core.ui.hud.components.noti;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import top.youm.maple.core.module.Module;
import top.youm.maple.core.module.modules.visual.HUD;
import top.youm.maple.core.ui.clickgui.classic.theme.Icon;
import top.youm.maple.core.ui.font.FontLoaders;
import top.youm.maple.utils.AnimationUtils;
import top.youm.maple.utils.TimerUtil;
import top.youm.maple.utils.render.RenderUtil;

import java.awt.*;

import static top.youm.maple.core.ui.font.FontLoaders.*;

/**
 * @author YouM
 * Created on 2023/8/9
 */
public class Notification {
    private float notificationHeight, notificationWidth;
    private Module module;
    private String message;
    private String title;
    private AnimationUtils animator = new AnimationUtils();
    private float animationX,animationY;
    private long stayTime;
    private TimerUtil timer;
    private Icon type;

    public Notification(String title, String message, long stayTime, Icon type) {
        this(title, message, stayTime, null,type);
    }
    public Notification(String title, String message, long stayTime, Module module, Icon type) {
        ScaledResolution sr = new ScaledResolution(Minecraft.getMinecraft());
        this.module = module;
        this.message = message;
        this.title = title;
        this.notificationHeight = 26.0f;
        this.notificationWidth = noti.getStringWidth(message) + 30;
        this.animationX = sr.getScaledWidth();
        this.stayTime = stayTime;
        this.timer = new TimerUtil();
        this.type = type;
    }
    public void draw(float width ,float offsetY){
        float target;
        if(!isFinished()){
            target = width - this.notificationWidth;
        }else{
            target = width;
        }
        this.animationX = animator.animate(target, this.animationX, 0.07f);
        if (animationY == 0) {
            animationY = offsetY;
        }
        this.animationY = animator.animate(offsetY, animationY,0.07f);

        RenderUtil.drawBorderedRect(this.animationX,this.animationY,this.notificationWidth, notificationHeight,1,new Color(0, 0, 0, 150), HUD.getHUDThemeColor());
        int color;

        FontLoaders.icon40.drawString(this.type.icon,this.animationX + 3,this.animationY + 5,HUD.getHUDThemeColor().getRGB());
        notiTitle.drawStringWithShadow(title,animationX + 25,animationY + 3,-1);
        noti.drawStringWithShadow(message,animationX + 25,animationY + 7 + robotoB22.getHeight(),-1);
    }
    public float getHeight() {
        return notificationHeight;
    }
    public boolean shouldDelete() {
        ScaledResolution sr = new ScaledResolution(Minecraft.getMinecraft());
        return isFinished() && this.animationX >= sr.getScaledWidth() - 2;
    }
    private boolean isFinished() {
        return timer.hasTimeElapsed(stayTime);
    }
}

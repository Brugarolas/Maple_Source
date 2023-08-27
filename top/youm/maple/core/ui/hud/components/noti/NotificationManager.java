package top.youm.maple.core.ui.hud.components.noti;

import top.youm.maple.core.module.Module;
import top.youm.maple.core.module.modules.visual.HUD;

import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @author YouM
 * Created on 2023/8/9
 */
public class NotificationManager {
    private static final CopyOnWriteArrayList<Notification> notifications = new CopyOnWriteArrayList<>();

    public static void draw(float width,float height){
        if (HUD.notification.getValue()) {
            float startY = height - 25;
            for (Notification notification : notifications) {
                if(notification == null)
                    continue;

                notification.draw(width,startY);
                startY -= notification.getHeight() + 5;
            }
            notifications.removeIf(Notification::shouldDelete);
        }else {
            if (!notifications.isEmpty()) {
                notifications.clear();
            }
        }
    }
    public static void show(String title, String message) {
        notifications.add(new Notification(title, message, 2500L));
    }

    public static void show(String title, String message, long stayTime) {
        notifications.add(new Notification(title, message, stayTime));
    }

    public static void show(String title, String message, Module module) {
        notifications.add(new Notification(title, message, 2500L, module));
    }

}

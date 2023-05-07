package top.youm.rocchi.utils;

import net.minecraft.client.Minecraft;

public final class AnimationUtils {
    public static double animate(double target, double current, double speed) {
        boolean larger;
        boolean bl = larger = target > current;
        if (speed < 0.0) speed = 0.0;
        else if (speed > 1.0) speed = 1.0;
        double dif = Math.max(target, current) - Math.min(target, current);
        double factor = dif * speed;
        if (factor < 0.1) factor = 0.1;
        if (larger) current += factor;
        else current -= factor;
        return current;
    }
    public static float animateF(float target, float current, float speed) {
        boolean larger;
        boolean bl = larger = target > current;
        if (speed < 0.0f) {
            speed = 0.0f;
        } else if (speed > 1.0) {
            speed = 1.0f;
        }
        float dif = Math.max(target, current) - Math.min(target, current);
        float factor = dif * speed;
        if (factor < 0.1f) {
            factor = 0.1f;
        }
        if (larger) {
            current += factor;
        } else {
            current -= factor;
        }
        if (Math.abs(current - target) < 0.2) {
            return target;
        } else {
            return current;
        }
    }
    public static int animateI(int target, int current, float speed) {
        boolean larger;
        boolean bl = larger = target > current;
        if (speed < 0.0f) {
            speed = 0.0f;
        } else if (speed > 1.0) {
            speed = 1.0f;
        }
        float dif = Math.max(target, current) - Math.min(target, current);
        float factor = dif * speed;
        if (factor < 0.1f) {
            factor = 0.1f;
        }
        if (larger) {
            current += factor;
        } else {
            current -= factor;
        }
        if (Math.abs(current - target) < 0.2) {
            return target;
        } else {
            return current;
        }
    }
}

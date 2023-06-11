package top.youm.rocchi.utils;

public final class AnimationUtils {
    public static double delta;
    public double animate(double target, double current, double speed) {
        boolean larger = target > current;
        if (speed < 0.0) {
            speed = 0.0;
        } else if (speed > 1.0) {
            speed = 1.0;
        }else {
            speed = (delta / 10) * speed;
        }
        double dif = Math.max(target, current) - Math.min(target, current);
        double factor = dif * speed;
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
    public float animate(float target, float current, float speed) {
        boolean larger;
        boolean bl = larger = target > current;
        if (speed < 0.0f) {
            speed = 0.0f;
        } else if (speed > 1.0) {
            speed = 1.0f;
        }else {
            speed = (float) ((delta / 10) * speed);
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
    public int animate(int target, int current, float speed) {
        boolean larger = target > current;
        if (speed < 0.0f) {
            speed = 0.0f;
        } else if (speed > 1.0) {
            speed = 1.0f;
        }else {
            speed = (float) ((delta / 10.0f) * speed);
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

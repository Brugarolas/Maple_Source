package top.youm.maple.utils.math;

public class RandomUtil {
    public static int getRandomInRange(int min, int max) {
        return (int) ((Math.random() * (max - min)) + min);
    }
}

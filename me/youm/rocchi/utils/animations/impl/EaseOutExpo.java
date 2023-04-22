package me.youm.rocchi.utils.animations.impl;

import me.youm.rocchi.utils.animations.Animation;
import me.youm.rocchi.utils.animations.Direction;

public class EaseOutExpo extends Animation {

    public EaseOutExpo(int ms, double endPoint) {
        super(ms, endPoint);
    }

    public EaseOutExpo(int ms, double endPoint, Direction direction) {
        super(ms, endPoint, direction);
    }

    @Override
    protected double getEquation(double x1) {
        double x = x1 / duration;
        return 1 - Math.pow(2, -10 * x);
    }
}

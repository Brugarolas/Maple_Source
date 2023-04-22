package me.youm.rocchi.utils.animations.impl;

import me.youm.rocchi.utils.animations.Animation;
import me.youm.rocchi.utils.animations.Direction;

public class EaseOutElastic extends Animation {

    float easeAmount;
    float smooth;
    boolean reallyElastic;

    public EaseOutElastic(int ms, double endPoint, float elasticity, float smooth, boolean moreElasticity) {
        super(ms, endPoint);
        this.easeAmount = elasticity;
        this.smooth = smooth;
        this.reallyElastic = moreElasticity;
    }

    public EaseOutElastic(int ms, double endPoint, float elasticity, float smooth, boolean moreElasticity, Direction direction) {
        super(ms, endPoint, direction);
        this.easeAmount = elasticity;
        this.smooth = smooth;
        this.reallyElastic = moreElasticity;
    }

    @Override
    protected double getEquation(double x) {
        double x1 = Math.pow(x / duration, smooth); //Used to force input to range from 0 - 1
        double elasticity = easeAmount * .1f;
        return Math.pow(2, -10 * (reallyElastic ? Math.sqrt(x1) : x1)) * Math.sin((x1 - (elasticity / 4)) * ((2 * Math.PI) / elasticity)) + 1;
    }
}

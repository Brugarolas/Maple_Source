package me.youm.rocchi.common.settings;

public class NumberSetting extends Setting<Number>{

    private final Number max,min,inc;

    public NumberSetting(String name,Number value,Number max, Number min, Number inc) {
        super(name, value);
        this.max = max;
        this.min = min;
        this.inc = inc;
    }
    public Number getMax() {
        return max;
    }
    public Number getMin() {
        return min;
    }
    public Number getInc() {
        return inc;
    }

}

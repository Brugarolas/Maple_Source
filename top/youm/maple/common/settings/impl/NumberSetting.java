package top.youm.maple.common.settings.impl;

import top.youm.maple.common.settings.Setting;

/**
 * @author YouM
 * can update number setting like slider
 */
public class NumberSetting extends Setting<Number> {

    private final Number max,min;
    private Number inc;
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

    public void setInc(Number inc) {
        this.inc = inc;
    }

}

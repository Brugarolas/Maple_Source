package top.youm.rocchi.common.settings;

public class ModeSetting<T extends Enum<?>> extends Setting<T>{
    private final Enum<?>[] enums;
    public ModeSetting(String name,T[] enums, T value) {
        super(name, value);
        this.enums = enums;
    }

    @Override
    public void setValue(T value) {
        this.value = value;
    }

    public Enum<?>[] getEnums() {
        return enums;
    }

}

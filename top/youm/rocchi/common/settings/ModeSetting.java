package top.youm.rocchi.common.settings;

public class ModeSetting<T extends Enum<?>> extends Setting<T>{
    private final T[] enums;
    public ModeSetting(String name,T[] enums, T value) {
        super(name, value);
        this.enums = enums;
    }

    public T[] getEnums() {
        return enums;
    }
    public void setValueEnum(Enum<?> value) {
        this.value = (T) value;
    }
}

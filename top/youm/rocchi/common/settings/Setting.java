package top.youm.rocchi.common.settings;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.function.Predicate;

/**
 * @author YouM
 * abstract setting entity
 */
public class Setting<T> {
    @Expose
    @SerializedName("name")
    private String name;
    @Expose
    @SerializedName("value")
    protected T value;

    public ParentAttribute<? extends Setting<?>> attribute;

    public Setting(String name, T value) {
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public T getValue() {
        return value;
    }

    public void setValue(T value) {
        this.value = value;
    }
    public void addParent(ParentAttribute<? extends Setting<?>> attribute) {
        this.attribute = attribute;
    }
    public <S extends Setting<?>> void addParent(S parent,Predicate<S> predicate) {
        addParent(new ParentAttribute<>(parent,predicate));
    }
    public boolean canDisplay(){
        if(attribute == null) return true;
        return attribute.isValid();
    }
    @Override
    public String toString() {
        return "Setting{" +
                "name='" + name + '\'' +
                ", value=" + value +
                '}';
    }
}

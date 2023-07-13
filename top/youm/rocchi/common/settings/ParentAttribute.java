package top.youm.rocchi.common.settings;

import java.util.function.Predicate;

public class ParentAttribute<T extends Setting<?>> {
    private final T parent;
    private final Predicate<T> condition;

    public ParentAttribute(T parent, Predicate<T> condition) {
        this.parent = parent;
        this.condition = condition;
    }

    public boolean isValid() {
        return condition.test(parent);
    }

    public T getParent() {
        return parent;
    }

}

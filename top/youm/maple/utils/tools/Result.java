package top.youm.maple.utils.tools;

import java.util.function.Consumer;

/**
 * @author YouM
 * Created on 2023/7/16
 */
public interface Result<T>{
    T get();
    Throwable getThrowable();
    int getState();
    int SUCCESS = 0;
    int FAILED = 1;
    default Result<T> onSuccess(Consumer<? super T> consumer){
        if(getState() == SUCCESS) consumer.accept(get());
        return this;
    }
    default Result<T> onFailed(Consumer<? super Throwable> consumer){
        if(getState() == FAILED)consumer.accept(getThrowable());
        return this;
    }
}

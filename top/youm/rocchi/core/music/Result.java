package top.youm.rocchi.core.music;

/**
 * @author YouM
 * Created on 2023/7/18
 */
public class Result<T> {
    private int code;
    private T context;
    public static <Msg> Result<?> success(Msg context) {
        return new Result<>(1, context);
    }
    public static <Msg> Result<?> failed(Msg context){
        return new Result<>(0, context);
    }
    public Result(int code, T context) {
        this.code = code;
        this.context = context;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public T getContext() {
        return context;
    }

    public void setContext(T context) {
        this.context = context;
    }

    @Override
    public String toString() {
        return "Result{" +
                "code=" + code +
                ", context='" + context + '\'' +
                '}';
    }
}

package top.youm.maple.utils.tools;

/**
 * @author YouM
 * Created on 2023/7/16
 *
 * given that "vavr" and kotlin runCatching function
 */
public class Catcher {
    public static <T> Result<T> runCatching(CatchHandlers.CatchHandler<T> catchHandler) {
        try {
            T t = catchHandler.handle();
            return new Success<>(t);
        } catch (Throwable throwable) {
            return new Failure<>(throwable);
        }
    }
    public static void runCatching(CatchHandlers.CatchRunnable catchRunnable) {
        try {
            catchRunnable.run();
        }catch (Throwable throwable){
            throwable.printStackTrace();
        }
    }
    static class Success<T> implements Result<T>{
        private final T t;

        public Success(T t) {
            this.t = t;
        }
        public T get(){
            return t;
        }

        @Override
        public Throwable getThrowable() {
            return null;
        }

        @Override
        public int getState() {
            return SUCCESS;
        }

    }
    static class Failure<T> implements Result<T> {
        private final Throwable throwable;

        public Failure(Throwable throwable) {
            this.throwable = throwable;
        }
        public T get(){
            return null;
        }

        @Override
        public Throwable getThrowable() {
            return throwable;
        }

        @Override
        public int getState() {
            return FAILED;
        }

    }

}

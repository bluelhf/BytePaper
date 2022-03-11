package blue.lhf.bsfp.util;

public class MayThrow {

    @FunctionalInterface
    public interface Supplier<T> extends java.util.function.Supplier<T> {
        @Override
        default T get() {
            try {
                return get0();
            } catch (Throwable t) {
                throw new RuntimeException(t);
            }
        }

        T get0() throws Throwable;

        static <T> MayThrow.Supplier<T> throwing(java.util.function.Supplier<T> supplier) {
            return supplier::get;
        }
    }

    @FunctionalInterface
    public interface Runnable extends java.lang.Runnable {
        @Override
        default void run() {
            try {
                run0();
            } catch (Throwable t) {
                throw new RuntimeException(t);
            }
        }

        void run0() throws Throwable;
        static MayThrow.Runnable throwing(java.lang.Runnable runnable) {
            return runnable::run;
        }
    }
}


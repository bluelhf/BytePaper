package blue.lhf.bytepaper.util;

import io.papermc.paper.util.MCUtil;

import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;

public class Threading {
    private Threading() {

    }

    public static <T> T forceMain(Supplier<T> tSupplier) {
        return CompletableFuture.supplyAsync(tSupplier, MCUtil.MAIN_EXECUTOR).join();
    }

    public static void forceMain(Runnable runnable) {
        CompletableFuture.runAsync(runnable, MCUtil.MAIN_EXECUTOR).join();
    }
}
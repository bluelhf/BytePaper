package blue.lhf.bytepaper;

import blue.lhf.bytepaper.util.MayThrow;
import mx.kenzie.foundation.language.PostCompileClass;
import org.byteskript.skript.error.ScriptCompileError;
import org.byteskript.skript.error.ScriptLoadError;
import org.byteskript.skript.error.ScriptParseError;
import org.byteskript.skript.runtime.Script;
import org.byteskript.skript.runtime.Skript;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;
import java.util.function.Supplier;

import static java.nio.file.StandardOpenOption.CREATE;
import static java.nio.file.StandardOpenOption.TRUNCATE_EXISTING;
import static java.util.Comparator.comparing;

public interface IScriptLoader {
    Skript getSkript();

    default Collection<Script> loadScript(Path path) throws IOException {
        return loadScript(path, null);
    }

    default Collection<Script> loadScript(Path path, Path outputDir) throws IOException {
        try (var is = new BufferedInputStream(Files.newInputStream(path))) {
            ScriptLoadError exc = new ScriptLoadError("Failed to load script " + path.getFileName().toString());
            var classes = getSkript().compileComplexScript(
                is, path.getFileName().toString());

            for (PostCompileClass pcc : classes) {
                Script[] scripts = getSkript().getScripts();
                for (Script sc : scripts) {
                    if (sc.getPath().equals(pcc.name())) {
                        getSkript().unloadScript(sc);
                    }
                }
            }


            if (outputDir != null) {
                for (PostCompileClass pcc : classes) {
                    Path target = outputDir.resolve(pcc.name() + ".class");
                    try (BufferedOutputStream out = new BufferedOutputStream(
                        Files.newOutputStream(target, CREATE, TRUNCATE_EXISTING))) {
                        out.write(pcc.code());
                    } catch (IOException e) {
                        exc.addSuppressed(e);
                    }
                }
            }

            Collection<Script> scripts = new ArrayList<>();
            try {
                scripts.addAll(getSkript().loadScripts(classes));
            } catch (ScriptParseError | ScriptCompileError | ScriptLoadError e) {
                exc.addSuppressed(e);
            }
            if (exc.getSuppressed().length > 0)
                throw exc;

            return scripts;
        }
    }

    default Collection<Script> loadScriptTree(Path path) throws IOException {
        return loadScriptTree(path, null);
    }

    default Collection<Script> loadScriptTree(Path path, Path outputDir) throws IOException {
        if (Files.isRegularFile(path)) {
            return loadScript(path, outputDir);
        }

        final Function<Path, String> name = p -> p.getFileName().toString();
        Function<String, Function<String, Boolean>> ends = datum -> input -> input.endsWith(datum);

        Collection<Script> scripts = new ArrayList<>();
        try (var list = Files.list(path)) {
            var iter = list
                .filter(p -> ends.apply(".bsk").compose(name).apply(p))
                .sorted(comparing(Path::toString))
                .iterator();

            while (iter.hasNext()) {
                scripts.addAll(loadScriptTree(iter.next(), outputDir));
            }
        }

        return scripts;
    }

    default <T> CompletableFuture<T> async(Supplier<T> supplier) {
        return CompletableFuture.supplyAsync(supplier, getSkript().getExecutor());
    }

    default CompletableFuture<Collection<Script>> loadScriptAsync(Path path) {
        return loadScriptAsync(path, null);
    }

    default CompletableFuture<Collection<Script>> loadScriptAsync(Path path, Path outputDir) {
        return async((MayThrow.Supplier<Collection<Script>>) () -> loadScript(path, outputDir));
    }

    default CompletableFuture<Collection<Script>> loadScriptTreeAsync(Path path) {
        return loadScriptTreeAsync(path, null);
    }

    default CompletableFuture<Collection<Script>> loadScriptTreeAsync(Path path, Path outputDir) {
        return async((MayThrow.Supplier<Collection<Script>>) () -> loadScriptTree(path, outputDir));
    }
}

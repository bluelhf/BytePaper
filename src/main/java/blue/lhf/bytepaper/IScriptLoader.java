package blue.lhf.bytepaper;

import blue.lhf.bytepaper.commands.arguments.ArgUtils;
import blue.lhf.bytepaper.library.syntax.command.*;
import blue.lhf.bytepaper.util.MayThrow;
import mx.kenzie.foundation.language.PostCompileClass;
import mx.kenzie.mirror.Mirror;
import org.byteskript.skript.error.*;
import org.byteskript.skript.runtime.*;
import org.byteskript.skript.runtime.internal.*;

import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.function.*;

import static java.nio.file.StandardOpenOption.CREATE;
import static java.nio.file.StandardOpenOption.TRUNCATE_EXISTING;
import static java.util.Comparator.comparing;

/**
 * Provides an interface for BytePaper load/unload mechanisms
 */
@SuppressWarnings("unused")
public interface IScriptLoader {
    Skript getSkript();

    CommandRegistrar getRegistrar();

    default Script loadScript(Path path) throws IOException {
        return loadScript(path, null);
    }

    default void unloadScript(Script script) {
        for (var entry : CommandRegistrar.findCommands(script.classes()).entrySet()) {
            getRegistrar().unregister(entry.getValue());
        }

        if (script.mainClass().getClassLoader() instanceof ScriptClassLoader scl) {
            //noinspection unchecked yes yes i know this is dangerous i had to do it
            final var list = ((WeakList<ScriptClassLoader>) Mirror.of(getSkript()).field("loaders").get());
            list.removeIf(ref -> ref.refersTo(scl));
        }

        getSkript().unloadScript(script);
    }

    default Script loadScript(Path path, Path outputDir) throws IOException {
        try (var is = new BufferedInputStream(Files.newInputStream(path))) {
            ScriptLoadError exc = new ScriptLoadError("Failed to load script " + path.getFileName().toString());
            var classes = getSkript().compileComplexScript(
                is, ArgUtils.toInternalScript(path.getFileName().toString(), false));

            for (PostCompileClass pcc : classes) {
                Script[] scripts = getSkript().getScripts();
                for (Script sc : scripts) {
                    if (sc.getPath().equals(pcc.name())) {
                        unloadScript(sc);
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

            Script script = null;
            try {
                Object loader = Mirror.of(getSkript()).method("createLoader").invoke();
                var mirror = Mirror.of(loader).method("loadClass", String.class, byte[].class);
                Class<?>[] loadedClasses = new Class[classes.length];
                for (int i = 0; i < classes.length; i++) {
                    PostCompileClass pcc = classes[i];
                    loadedClasses[i] = (Class<?>) mirror.invoke(pcc.name(), pcc.code());
                }
                script = getSkript().loadScript(loadedClasses);
            } catch (ScriptParseError | ScriptCompileError | ScriptLoadError e) {
                exc.addSuppressed(e);
            }

            if (script != null) {
                try {
                    getRegistrar().registerAll(script.classes());
                } catch (DuplicateCommandException e) {
                    exc.addSuppressed(e);
                    unloadScript(script);
                }
            }

            if (exc.getSuppressed().length > 0)
                throw exc;

            return script;
        }
    }

    default Collection<Script> loadScriptTree(Path path) throws IOException {
        return loadScriptTree(path, null);
    }

    default Collection<Script> loadScriptTree(Path path, Path outputDir) throws IOException {
        if (Files.isRegularFile(path)) {
            return Collections.singleton(loadScript(path, outputDir));
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

    default CompletableFuture<Script> loadScriptAsync(Path path) {
        return loadScriptAsync(path, null);
    }

    default CompletableFuture<Script> loadScriptAsync(Path path, Path outputDir) {
        return async((MayThrow.Supplier<Script>) () -> loadScript(path, outputDir));
    }

    default CompletableFuture<Collection<Script>> loadScriptTreeAsync(Path path) {
        return loadScriptTreeAsync(path, null);
    }

    default CompletableFuture<Collection<Script>> loadScriptTreeAsync(Path path, Path outputDir) {
        return async((MayThrow.Supplier<Collection<Script>>) () -> loadScriptTree(path, outputDir));
    }

    default <T> CompletableFuture<T> async(Supplier<T> supplier) {
        return new CompletableFuture<>() {{
            CompletableFuture.supplyAsync(() -> {
                try {
                    T val = supplier.get();
                    complete(val);
                    return val;
                } catch (Exception ex) {
                    completeExceptionally(ex);
                    return null;
                }
            }, getSkript().getExecutor());
        }};
    }
}

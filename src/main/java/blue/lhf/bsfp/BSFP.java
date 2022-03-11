package blue.lhf.bsfp;

import blue.lhf.bsfp.commands.BSKCommand;
import blue.lhf.bsfp.library.PaperBridgeSpec;
import blue.lhf.bsfp.util.MayThrow;
import mx.kenzie.foundation.language.PostCompileClass;
import mx.kenzie.jupiter.stream.Stream;
import net.kyori.adventure.audience.Audience;
import net.minecraft.server.MinecraftServer;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.byteskript.skript.compiler.DebugSkriptCompiler;
import org.byteskript.skript.compiler.SimpleSkriptCompiler;
import org.byteskript.skript.compiler.SkriptCompiler;
import org.byteskript.skript.error.ScriptCompileError;
import org.byteskript.skript.error.ScriptLoadError;
import org.byteskript.skript.error.ScriptParseError;
import org.byteskript.skript.runtime.Script;
import org.byteskript.skript.runtime.Skript;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;
import java.util.function.Supplier;

import static java.nio.file.StandardOpenOption.CREATE;
import static java.nio.file.StandardOpenOption.TRUNCATE_EXISTING;
import static java.util.Comparator.comparing;
import static net.kyori.adventure.text.minimessage.MiniMessage.miniMessage;

public final class BSFP extends JavaPlugin {

    private Skript skript;

    private final Path scriptsFolder = getDataFolder().toPath().resolve("scripts");
    private final Path compiledFolder = getDataFolder().toPath().resolve("compiled_scripts");

    @Override
    public void onEnable() {
        { // Skript registration
            DebugSkriptCompiler debugger = new DebugSkriptCompiler(Stream.controller(
                    new OutputStream() {
                        @Override
                        public void write(int b) {
                            if (b != 0) System.out.write(b);
                        }
                    }
            ), PaperBridgeSpec.LIBRARY);
            SkriptCompiler compiler = new SimpleSkriptCompiler(PaperBridgeSpec.LIBRARY);
            this.skript = new Skript(compiler);
            //skript.registerLibrary(PaperBridgeSpec.LIBRARY);
        }

        trying(Bukkit.getConsoleSender(), "creating the scripts folder",
                (MayThrow.Runnable) () -> Files.createDirectories(scriptsFolder));

        trying(Bukkit.getConsoleSender(), "creating the compiled scripts folder",
                (MayThrow.Runnable) () -> Files.createDirectories(compiledFolder));

        BSKCommand.register(MinecraftServer.getServer().getCommands().getDispatcher(), this);
    }

    public static boolean trying(Audience audience, String task, Runnable runnable) {
        try {
            runnable.run();
            return true;
        } catch (Throwable t) {
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            t.printStackTrace(pw);
            pw.close();

            var stackTrace = sw.toString().replace("\t", "  ").replace("\r", "");
            if (audience instanceof Player player) {
                player.sendMessage(miniMessage().deserialize("""
                        <hover:show_text:'%s'>
                        <gradient:#a62832:#33080c>An exception occurred while %s!</gradient>
                        <gradient:#736b6b:#332d2d>Hover to see details.</gradient>
                        </hover>""".formatted(stackTrace, task)));
            } else {
                audience.sendMessage(miniMessage().deserialize("""
                        <gradient:#a62832:#33080c>An exception occurred while %s!
                        
                        %s
                        </gradient>""".formatted(task, stackTrace)));
            }

            return false;
        }
    }

    public Collection<Script> loadScript(Path path) throws IOException {
        return loadScript(path, null);
    }

    public Collection<Script> loadScript(Path path, Path outputDir) throws IOException {
        try (var is = new BufferedInputStream(Files.newInputStream(path))) {
            ScriptLoadError exc = new ScriptLoadError("Failed to load script " + path.getFileName().toString());
            var classes = skript.compileComplexScript(
                    is, path.getFileName().toString());

            for (PostCompileClass pcc : classes) {
                Script[] scripts = skript.getScripts();
                for (Script sc : scripts) {
                    if (sc.getPath().equals(pcc.name()))
                        skript.unloadScript(sc);
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
                scripts.addAll(skript.loadScripts(classes));
            } catch (ScriptParseError | ScriptCompileError | ScriptLoadError e) {
                exc.addSuppressed(e);
            }
            if (exc.getSuppressed().length > 0)
                throw exc;

            return scripts;
        }
    }

    public Collection<Script> loadScriptTree(Path path) throws IOException {
        return loadScriptTree(path, null);
    }

    public Collection<Script> loadScriptTree(Path path, Path outputDir) throws IOException {
        if (Files.isRegularFile(path)){
            return loadScript(path, outputDir);
        }

        final Function<Path, String> name = p -> p.getFileName().toString();
        Function<String, Function<String, Boolean>> ends = (datum) -> (input) -> input.endsWith(datum);

        var iter = Files.list(path)
                .filter(p -> ends.apply(".bsk").compose(name).apply(p))
                .sorted(comparing(Path::toString))
                .iterator();

        Collection<Script> scripts = new ArrayList<>();
        while (iter.hasNext()) {
            scripts.addAll(loadScriptTree(iter.next(), outputDir));
        }

        return scripts;
    }

    public <T> CompletableFuture<T> async(Supplier<T> supplier) {
        return CompletableFuture.supplyAsync(supplier, skript.getExecutor());
    }

    public CompletableFuture<Collection<Script>> loadScriptAsync(Path path) {
        return loadScriptAsync(path, null);
    }

    public CompletableFuture<Collection<Script>> loadScriptAsync(Path path, Path outputDir) {
        return async((MayThrow.Supplier<Collection<Script>>) () -> loadScript(path, outputDir));
    }

    public CompletableFuture<Collection<Script>> loadScriptTreeAsync(Path path) {
        return loadScriptTreeAsync(path, null);
    }

    public CompletableFuture<Collection<Script>> loadScriptTreeAsync(Path path, Path outputDir) {
        return async((MayThrow.Supplier<Collection<Script>>) () -> loadScriptTree(path, outputDir));
    }



    public Skript getSkript() {
        return skript;
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        Arrays.stream(skript.getScripts()).forEachOrdered((s) -> {
            try {
                skript.unloadScript(s);
            } catch (UnsupportedOperationException ex) {
                // fail due to kenzie's bad record graveyarding
            }
        });
        skript.unregisterLibrary(PaperBridgeSpec.LIBRARY);
        skript = null;
    }

    public Path getScriptsFolder() {
        return scriptsFolder;
    }

    public Path getCompiledFolder() {
        return compiledFolder;
    }
}

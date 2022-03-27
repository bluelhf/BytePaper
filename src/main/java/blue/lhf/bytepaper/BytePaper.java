package blue.lhf.bytepaper;

import blue.lhf.bytepaper.commands.BPCommand;
import blue.lhf.bytepaper.library.PaperBridgeSpec;
import blue.lhf.bytepaper.library.annotations.CommandData;
import blue.lhf.bytepaper.util.Debugging;
import blue.lhf.bytepaper.util.Exceptions;
import blue.lhf.bytepaper.util.MayThrow;
import mx.kenzie.jupiter.stream.Stream;
import net.minecraft.server.MinecraftServer;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;
import org.byteskript.skript.compiler.DebugSkriptCompiler;
import org.byteskript.skript.compiler.SimpleSkriptCompiler;
import org.byteskript.skript.compiler.SkriptCompiler;
import org.byteskript.skript.runtime.Skript;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Method;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;

public final class BytePaper extends JavaPlugin implements IScriptLoader {

    private final Path scriptsFolder = getDataFolder().toPath().resolve("scripts");
    private final Path compiledFolder = getDataFolder().toPath().resolve("compiled_scripts");
    private Skript skript;

    @Override
    public void onEnable() {
        register(Debugging.OFF);

        Exceptions.trying(Bukkit.getConsoleSender(), "creating the scripts folder",
            (MayThrow.Runnable) () -> Files.createDirectories(scriptsFolder));

        Exceptions.trying(Bukkit.getConsoleSender(), "creating the compiled scripts folder",
            (MayThrow.Runnable) () -> Files.createDirectories(compiledFolder));
    }

    public Skript getSkript() {
        return skript;
    }

    @Override
    public void onDisable() {
        unregister();
    }

    public void register(Debugging debug) {
        SkriptCompiler compiler = debug.compiler() ?
            new DebugSkriptCompiler(Stream.controller(
                new Debugging.Stream(getLogger())),
                PaperBridgeSpec.LIBRARY)
            : new SimpleSkriptCompiler(PaperBridgeSpec.LIBRARY);

        if (debug.trace()) System.setProperty("debug_mode", "true");

        if (debug != Debugging.OFF) {
            getLogger().warning("You are running in debugging mode! Scripts will not compile properly.");
        }

        //noinspection deprecation
        BPCommand.register(MinecraftServer.getServer().getCommands().getDispatcher(), this);
        this.skript = new Skript(compiler);

        PaperBridgeSpec.INSTANCE.registerEvents(skript, this);
        abstract class Subcommand extends Command {
            protected Subcommand(@NotNull String name) {
                super(name);
            }
        }
        PaperBridgeSpec.INSTANCE.setCommandRegistrar((name, type) -> {
            Exceptions.trying(Bukkit.getConsoleSender(), "registering a BytePaper command",
                (MayThrow.Runnable) () -> {
                    Class<?> classy = skript.getLoader().loadClass(type.getTypeName());
                    Method target = null;
                    for (Method m : classy.getDeclaredMethods()) {
                        if (m.getAnnotation(CommandData.class) == null) continue;
                        target = m;
                    }

                    if (target == null)
                        throw new NoSuchMethodException("The command executor class was compiled incorrectly.");

                    Method finalTarget = target;
                    Bukkit.getCommandMap().register(name, "bytepaper", new Subcommand(name) {
                        @Override
                        public boolean execute(@NotNull CommandSender sender, @NotNull String commandLabel, @NotNull String[] args) {
                            return Exceptions.trying(sender, "run command",
                                (MayThrow.Runnable) () -> {
                                    finalTarget.invoke(null, sender, this, commandLabel, args);
                                });
                        }
                    });
                });

        });
    }

    public void unregister() {
        Arrays.stream(skript.getScripts()).forEachOrdered(skript::unloadScript);
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

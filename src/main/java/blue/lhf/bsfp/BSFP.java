package blue.lhf.bsfp;

import blue.lhf.bsfp.commands.BSKCommand;
import blue.lhf.bsfp.library.PaperBridgeSpec;
import blue.lhf.bsfp.util.Debugging;
import blue.lhf.bsfp.util.Exceptions;
import blue.lhf.bsfp.util.MayThrow;
import mx.kenzie.jupiter.stream.Stream;
import net.minecraft.server.MinecraftServer;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.byteskript.skript.compiler.DebugSkriptCompiler;
import org.byteskript.skript.compiler.SimpleSkriptCompiler;
import org.byteskript.skript.compiler.SkriptCompiler;
import org.byteskript.skript.runtime.Skript;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;

public final class BSFP extends JavaPlugin implements IScriptLoader {

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
        BSKCommand.register(MinecraftServer.getServer().getCommands().getDispatcher(), this);
        this.skript = new Skript(compiler);

        PaperBridgeSpec.INSTANCE.registerEvents(skript, this);
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

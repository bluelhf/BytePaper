package blue.lhf.bytepaper;

import blue.lhf.bytepaper.commands.BPCommand;
import blue.lhf.bytepaper.library.PaperBridgeSpec;
import blue.lhf.bytepaper.library.syntax.command.CommandRegistrar;
import blue.lhf.bytepaper.util.*;
import mx.kenzie.jupiter.stream.Stream;
import net.minecraft.server.MinecraftServer;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.byteskript.skript.compiler.*;
import org.byteskript.skript.runtime.Skript;

import java.nio.file.*;
import java.util.Arrays;

public final class BytePaper extends JavaPlugin implements IScriptLoader {

    private final Path scriptsFolder = getDataFolder().toPath().resolve("scripts");
    private final Path compiledFolder = getDataFolder().toPath().resolve("compiled_scripts");
    private PaperBridgeSpec spec;
    private Skript skript;
    private CommandRegistrar registrar;

    @Override
    public void onEnable() {
        register(Debugging.OFF);
    }

    public Skript getSkript() {
        return skript;
    }

    @Override
    public CommandRegistrar getRegistrar() {
        return registrar;
    }

    @Override
    public void onDisable() {
        unregister();
    }

    public void register(Debugging debug) {
        SkriptCompiler compiler = debug.compiler() ?
                new DebugSkriptCompiler(Stream.controller(
                        new Debugging.Stream(getLogger())))
                : new SimpleSkriptCompiler();

        if (debug.trace()) System.setProperty("debug_mode", "true");

        if (debug != Debugging.OFF) {
            getLogger().warning("You are running in debugging mode! Scripts will not compile properly.");
        }

        //noinspection deprecation
        BPCommand.register(MinecraftServer.getServer().getCommands().getDispatcher(), this);

        this.skript = new Skript(compiler);
        this.registrar = new CommandRegistrar(getLogger(), skript);
        this.spec = new PaperBridgeSpec(skript, this, registrar);
        spec.registerAll();
        skript.registerLibrary(spec);
    }

    public void unregister() {
        Arrays.stream(skript.getScripts()).forEachOrdered(this::unloadScript);
        skript.unregisterLibrary(spec);
        skript = null;
    }

    public Path obtainScriptsFolder() {
        if (!Files.isDirectory(scriptsFolder)) {
            Exceptions.trying(Bukkit.getConsoleSender(), "creating the scripts folder",
                (MayThrow.Runnable) () -> Files.createDirectories(scriptsFolder));
        }

        return scriptsFolder;
    }

    public Path obtainCompiledFolder() {
        if (!Files.isDirectory(compiledFolder)) {
            Exceptions.trying(Bukkit.getConsoleSender(), "creating the compiled scripts folder",
                (MayThrow.Runnable) () -> Files.createDirectories(compiledFolder));
        }


        return compiledFolder;
    }
}

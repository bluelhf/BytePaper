package blue.lhf.bytepaper;

import blue.lhf.bytepaper.commands.BytePaperCommand;
import blue.lhf.bytepaper.library.PaperBridgeSpec;
import blue.lhf.bytepaper.library.syntax.command.CommandRegistrar;
import blue.lhf.bytepaper.util.*;
import mx.kenzie.jupiter.stream.Stream;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.tag.Tag;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.byteskript.skript.compiler.*;
import org.byteskript.skript.runtime.*;

import java.io.InputStream;
import java.nio.file.*;
import java.util.*;
import java.util.logging.Level;

import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;
import static net.kyori.adventure.text.Component.text;

public final class BytePaper extends JavaPlugin implements IScriptLoader {

    private final Path scriptsFolder = getDataFolder().toPath().resolve("scripts");
    private final Path compiledFolder = getDataFolder().toPath().resolve("compiled_scripts");
    private final Path languageFolder = getDataFolder().toPath().resolve("language");

    private PaperBridgeSpec spec;
    private Skript skript;
    private CommandRegistrar registrar;

    private Script langScript;

    @Override
    public void onEnable() {
        register(Debugging.BOTH);
    }

    @SuppressWarnings("unused")
    public Skript getSkript() {
        return skript;
    }

    @SuppressWarnings("unused")
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
            new Debugging.Compiler(Stream.controller(new Debugging.Stream(getLogger())))
            : new SimpleSkriptCompiler();

        if (debug.trace()) System.setProperty("debug_mode", "true");

        this.skript = new Skript(compiler);
        this.registrar = new CommandRegistrar(getLogger(), skript);
        this.spec = new PaperBridgeSpec(skript, this, registrar);
        spec.registerAll();
        skript.registerLibrary(spec);

        BytePaperCommand commander = new BytePaperCommand(this);

        var command = getCommand("bytepaper");
        Exceptions.trying(getLogger(), Level.WARNING, "registering the BytePaper command", () -> {
            //noinspection ConstantConditions
            command.setTabCompleter(commander);
            command.setExecutor(commander);
        });

        Exceptions.trying(getLogger(), Level.SEVERE, "loading the language script", (MayThrow.Runnable) () -> {
            Path langPath = obtainLanguageFolder().resolve("en_gb.bsk");
            if (Files.notExists(langPath)) {
                Files.createFile(langPath);

                try (InputStream is = BytePaper.class.getResourceAsStream("/assets/en_gb.bsk")) {
                    if (is == null) throw new IllegalStateException("BytePaper JAR is corrupt");
                    Files.copy(is, langPath, REPLACE_EXISTING);
                }
            }
            langScript = loadScript(langPath);
        });
    }

    private String getRaw(String langPath) {
        return Skript.convert(langScript.getFunction("get").invoke(langPath), String.class, true);
    }

    public Component getComponent(String langPath, Map<String, Object> tags) {
        TagResolver[] resolvers = new TagResolver[tags.size()];

        int i = 0;
        for (var entry : tags.entrySet()) {
            var obj = entry.getValue();
            resolvers[i++] = TagResolver.resolver(entry.getKey(),
                Tag.inserting(obj instanceof Component c ? c : text(String.valueOf(obj))));
        }

        return UI.miniMessage().deserialize(getRaw(langPath), resolvers);
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

    public Path obtainLanguageFolder() {
        if (!Files.isDirectory(languageFolder)) {
            Exceptions.trying(Bukkit.getConsoleSender(), "creating the language folder",
                (MayThrow.Runnable) () -> Files.createDirectories(languageFolder));
        }

        return languageFolder;
    }

    public Path obtainCompiledFolder() {
        if (!Files.isDirectory(compiledFolder)) {
            Exceptions.trying(Bukkit.getConsoleSender(), "creating the compiled scripts folder",
                (MayThrow.Runnable) () -> Files.createDirectories(compiledFolder));
        }


        return compiledFolder;
    }
}

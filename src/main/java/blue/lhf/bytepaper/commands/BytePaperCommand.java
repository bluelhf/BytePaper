package blue.lhf.bytepaper.commands;

import blue.lhf.bytepaper.BytePaper;
import blue.lhf.bytepaper.commands.arguments.*;
import blue.lhf.bytepaper.util.*;
import com.moderocky.mask.command.Commander;
import org.bukkit.command.*;
import org.byteskript.skript.runtime.Script;
import org.jetbrains.annotations.*;

import java.io.IOException;
import java.nio.file.*;
import java.util.*;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Stream;

@SuppressWarnings("CodeBlock2Expr")
public class BytePaperCommand extends Commander<CommandSender> implements CommandExecutor, TabCompleter {
    private final BytePaper host;

    public BytePaperCommand(@NotNull BytePaper host) {
        this.host = host;
    }

    public BytePaper getHost() {
        return host;
    }

    public Commander<CommandSender>.SubArg load() {
        return arg(desc("Loads a script."), (sender, input) -> {
            long start = System.nanoTime();
            Path path = (Path) input[0];
            Path relative = getHost().obtainScriptsFolder().relativize(path);
            host.loadScriptTreeAsync(
                path,
                host.obtainCompiledFolder()
            ).thenRun(() -> {
                sender.sendMessage(host.getComponent("commands.bytepaper.load.success",
                    Map.of("time", "%.2f".formatted((System.nanoTime() - start) / 1E6),
                        "script", relative)));
            }).exceptionally((MayThrow.Function<Throwable, Void>)
                (throwable) -> {
                    Exceptions.throwing(sender, "loading the script", throwable);
                    return null;
                });
        }, new ArgLoadableScript(() -> getHost().obtainScriptsFolder()).setLabel("script"));
    }

    public Commander<CommandSender>.SubArg unload() {
        return arg(desc("Unloads a script."), (sender, input) -> {
            //noinspection unchecked - we know
            Optional<Script> targetPath = (Optional<Script>) input[0];
            if (targetPath.isEmpty()) {
                sender.sendMessage(host.getComponent("commands.bytepaper.unload.not_loaded", Map.of()));
                return;
            }

            Script script = targetPath.get();
            String scriptName = ArgUtils.toExternalScript(script.getPath(), true);

            long start = System.nanoTime();

            if (Exceptions.trying(sender, "unloading the script",
                (MayThrow.Runnable) () -> host.unloadScript(script))) {
                sender.sendMessage(host.getComponent("commands.bytepaper.unload.success",
                    Map.of("time", "%.2f".formatted((System.nanoTime() - start) / 1E6),
                        "script", scriptName)));
            }
        }, new ArgLoadedScript(this::getHost,
            s -> !s.equals(host.getLanguageScript()) // Don't allow unloading the language file
        ).setLabel("script"));
    }

    @Override
    protected Commander<CommandSender>.CommandImpl create() {
        return command("bytepaper", "bp", "bsk")
            .arg(new String[]{"load", "reload"}, load())
            .arg("unload", unload())
            .arg((sender) -> {
                long start = System.nanoTime();
                final IOException exc = new IOException("Failed to delete all files");
                AtomicLong amount = new AtomicLong(0);
                if (Exceptions.trying(sender, "cleaning the compiled scripts directory", (MayThrow.Runnable) () -> {
                    try (final Stream<Path> files = Files.walk(host.obtainCompiledFolder())) {
                        files.filter(Files::isRegularFile).forEach(path -> {
                                try {
                                    Files.deleteIfExists(path);
                                    amount.getAndIncrement();
                                } catch (IOException e) {
                                    exc.addSuppressed(e);
                                }
                        });
                    }
                })) {
                    if (exc.getSuppressed().length > 0) {
                        Exceptions.throwing(sender, "cleaning the compiled scripts directory", exc);
                        return;
                    }

                    sender.sendMessage(host.getComponent("commands.bytepaper.clean.success",
                        Map.of("time", "%.2f".formatted((System.nanoTime() - start) / 1E6),
                            "amount", amount.get())));
                }
            }, desc("Cleans the compiled scripts directory"), "clean")
            .onException((sender, exc) -> {
                Exceptions.trying(sender, "running the command", (MayThrow.Runnable) () -> {
                    throw exc;
                });
                return true;
            });
    }

    @Override
    public CommandSingleAction<CommandSender> getDefault() {
        StringBuilder mmBuilder = new StringBuilder("<secondary>Help for <primary>ByteSkript</primary></secondary>\n");
        for (String pattern : getPatterns()) {
            mmBuilder.append("<primary>- /%s %s</primary> <info>-</info> <secondary>%s</secondary>"
                .formatted(getCommand(), pattern, getPatternDescriptions().get(pattern)));
            mmBuilder.append("\n");
        }
        return sender -> sender.sendMessage(UI.miniMessage().deserialize(mmBuilder.toString()));
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label,
                             @NotNull String[] args) {
        return execute(sender, args);
    }


    //TODO(ilari): better tab completer — ask kenzie to update commander?
    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command,
                                                @NotNull String label, @NotNull String[] args) {
        String input = String.join(" ", args);
        if (input.startsWith("load ") || input.startsWith("reload ")) return load().argument.getCompletions();
        if (input.startsWith("unload ")) return unload().argument.getCompletions();
        if (!input.contains(" ")) return List.of("load", "unload", "clean");
        return Collections.emptyList();
    }
}

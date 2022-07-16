package blue.lhf.bytepaper.commands;

import blue.lhf.bytepaper.BytePaper;
import blue.lhf.bytepaper.commands.arguments.*;
import blue.lhf.bytepaper.util.*;
import com.moderocky.mask.command.Commander;
import org.bukkit.command.*;
import org.byteskript.skript.runtime.Script;
import org.jetbrains.annotations.*;

import java.nio.file.Path;
import java.util.*;

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
                sender.sendMessage(UI.miniMessage().deserialize(
                    "<error>A script with that path isn't loaded!</error>"));
                return;
            }

            if (Exceptions.trying(sender, "unloading the script",
                (MayThrow.Runnable) () -> host.unloadScript(targetPath.get()))) {
                sender.sendMessage(UI.miniMessage().deserialize(
                    "<info>Successfully unloaded the script!</info>"));
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

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command,
                                                @NotNull String label, @NotNull String[] args) {
        return getTabCompletions(args);
    }
}

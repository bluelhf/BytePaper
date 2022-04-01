package blue.lhf.bytepaper.commands;

import blue.lhf.bytepaper.*;
import blue.lhf.bytepaper.util.*;
import com.mojang.brigadier.*;
import com.mojang.brigadier.builder.*;
import com.mojang.brigadier.suggestion.*;
import net.minecraft.commands.*;
import org.byteskript.skript.runtime.*;

import java.nio.file.*;
import java.util.function.*;

import static blue.lhf.bytepaper.util.UI.*;
import static com.mojang.brigadier.arguments.StringArgumentType.*;
import static net.minecraft.commands.Commands.*;

public class BPCommand {
    private BPCommand() {
    }


    protected static SuggestionProvider<CommandSourceStack> suggestScripts(Path root) {
        return (context, builder) -> {
            Exceptions.trying(context.getSource().getBukkitSender(),
                "getting the command suggestions",
                (MayThrow.Runnable) () -> Files.walk(root)
                    .map(root::relativize)
                    .map(Path::toString)
                    .filter(p -> p.startsWith(builder.getRemaining()))
                    .filter(p -> p.endsWith(".bsk"))
                    .forEachOrdered(builder::suggest));
            return builder.buildFuture();
        };
    }

    protected static SuggestionProvider<CommandSourceStack> suggestLoadedScripts(IScriptLoader loader) {
        return (context, builder) -> {
            for (Script script : loader.getSkript().getScripts()) {
                builder.suggest(script.getPath());
            }
            return builder.buildFuture();
        };
    }

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher, BytePaper host) {
        UnaryOperator<LiteralArgumentBuilder<CommandSourceStack>> tail = builder -> builder.then(
            literal("load").then(
                argument("path", greedyString())
                    .suggests(suggestScripts(host.getScriptsFolder()))
                    .executes(context -> {
                        if (Exceptions.trying(context.getSource().getBukkitSender(), "loading the script",
                            (MayThrow.Runnable) () ->
                                host.loadScriptTreeAsync(
                                    host.getScriptsFolder().resolve(getString(context, "path")),
                                    host.getCompiledFolder()
                                ).join())) {
                            context.getSource().sendSuccess(toMC(UI.miniMessage().deserialize("<info>Successfully loaded the script!</info>")), false);
                            return 1;
                        }
                        return 0;
                    })
            )
        ).then(
            literal("unload").then(
                argument("path", greedyString())
                    .suggests(suggestLoadedScripts(host))
                    .executes(context -> {
                        String targetPath = getString(context, "path");
                        Script target = null;
                        for (Script script : host.getSkript().getScripts()) {
                            if (script.getPath().equals(targetPath))
                                target = script;
                        }

                        if (target == null) {
                            context.getSource().sendFailure(toMC(UI.miniMessage().deserialize("<error>A script with that path isn't loaded!</error>")));
                            return 0;
                        }

                        Script finalTarget = target;
                        if (Exceptions.trying(context.getSource().getBukkitSender(), "unloading the script",
                            (MayThrow.Runnable) () -> host.unloadScript(finalTarget))) {
                            context.getSource().sendSuccess(toMC(UI.miniMessage().deserialize("<info>Successfully unloaded the script!</info>")), false);
                            return 1;
                        }
                        return 0;
                    })
            )
        );

        dispatcher.getRoot().removeCommand("bp");
        dispatcher.register(tail.apply(literal("bp")));
    }
}

package blue.lhf.bytepaper.commands;

import blue.lhf.bytepaper.*;
import blue.lhf.bytepaper.util.*;
import com.mojang.brigadier.*;
import com.mojang.brigadier.builder.*;
import com.mojang.brigadier.suggestion.*;
import net.minecraft.commands.*;

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
        );

        dispatcher.getRoot().removeCommand("bp");
        dispatcher.register(tail.apply(literal("bp")));
    }
}

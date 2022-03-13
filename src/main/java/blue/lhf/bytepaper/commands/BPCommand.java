package blue.lhf.bytepaper.commands;

import blue.lhf.bytepaper.BytePaper;
import blue.lhf.bytepaper.util.Exceptions;
import blue.lhf.bytepaper.util.MayThrow;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import net.minecraft.commands.CommandSourceStack;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.function.UnaryOperator;

import static blue.lhf.bytepaper.util.Components.toMC;
import static com.mojang.brigadier.arguments.StringArgumentType.getString;
import static com.mojang.brigadier.arguments.StringArgumentType.greedyString;
import static net.kyori.adventure.text.minimessage.MiniMessage.miniMessage;
import static net.minecraft.commands.Commands.argument;
import static net.minecraft.commands.Commands.literal;

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
                            context.getSource().sendSuccess(toMC(miniMessage().deserialize(
                                "<gradient:#40e6cf:#40e65c>Successfully loaded the script!</gradient>")), false);
                            return 1;
                        }
                        return 0;
                    })
            )
        );

        dispatcher.register(tail.apply(literal("bp")));
    }
}

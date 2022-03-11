package blue.lhf.bsfp.commands;

import blue.lhf.bsfp.BSFP;
import blue.lhf.bsfp.util.MayThrow;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.chat.Component;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.UnaryOperator;

import static blue.lhf.bsfp.util.Components.toMC;
import static com.mojang.brigadier.arguments.StringArgumentType.getString;
import static com.mojang.brigadier.arguments.StringArgumentType.greedyString;
import static net.kyori.adventure.text.minimessage.MiniMessage.miniMessage;
import static net.minecraft.commands.Commands.argument;
import static net.minecraft.commands.Commands.literal;

public class BSKCommand {
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher, BSFP host) {
        UnaryOperator<LiteralArgumentBuilder<CommandSourceStack>> tail =
                builder ->
                        builder.then(
                                literal("load")
                                        .then(argument("path", greedyString())
                                                .suggests((context, suggestor) -> {
                                                    BSFP.trying(context.getSource().getBukkitSender(), "getting the command suggestions",
                                                            (MayThrow.Runnable) () ->
                                                                    Files.walk(host.getScriptsFolder())
                                                                            .map(host.getScriptsFolder()::relativize)
                                                                            .map(Path::toString)
                                                                            .filter(p -> p.startsWith(suggestor.getRemaining()))
                                                                            .forEachOrdered(suggestor::suggest)
                                                    );

                                                    return suggestor.buildFuture();
                                                }).executes(context -> {
                                                    if (BSFP.trying(context.getSource().getBukkitSender(), "loading the script",
                                                            (MayThrow.Runnable) () ->
                                                                    host.loadScriptTreeAsync(
                                                                            host.getScriptsFolder()
                                                                                    .resolve(getString(context, "path")),
                                                                            host.getCompiledFolder()
                                                                    ).join())) {
                                                        context.getSource().sendSuccess(toMC(miniMessage().deserialize("""
                                                                <gradient:#40e6cf:#40e65c>Successfully loaded the script!</gradient>""")), false);
                                                        return 1;
                                                    }

                                                    return 0;
                                                })
                                        )
                        );

        dispatcher.register(tail.apply(literal("bsk")));
    }
}

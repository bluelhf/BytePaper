package blue.lhf.bytepaper.library.syntax.command;

import blue.lhf.bytepaper.util.*;
import mx.kenzie.foundation.Type;
import org.bukkit.Bukkit;
import org.bukkit.command.*;
import org.byteskript.skript.runtime.Skript;
import org.jetbrains.annotations.NotNull;

import java.lang.invoke.*;
import java.lang.reflect.Method;
import java.util.logging.*;

public class CommandRegistrar {
    private static final String FALLBACK_PREFIX = "bytepaper";

    private final Skript host;
    private final Logger logger;
    private MayThrow.Runnable commandUpdater;

    public CommandRegistrar(Logger logger, Skript host) {
        this.logger = logger;
        this.host = host;
    }

    public void register(String label, Type executor) throws DuplicateCommandException {

        // This class exists solely to provide access to the Command constructor...
        abstract class Subcommand extends Command {
            protected Subcommand(@NotNull String name) {
                super(name);
            }
        }

        /*
         * The class must be loaded when the command is executed — the registrar
         * is called during compilation, which means the class is inaccessible at this point.
         * */
        boolean success = Bukkit.getCommandMap().register(label, FALLBACK_PREFIX, new Subcommand(label) {
            @Override
            public boolean execute(@NotNull CommandSender sender,
                                   @NotNull String commandLabel,
                                   @NotNull String[] args) {
                return Exceptions.trying(Bukkit.getConsoleSender(), "running a BytePaper command",
                        (MayThrow.Runnable) () -> {
                            Class<?> classy = host.getLoader().loadClass(executor.getTypeName());
                            Method target = null;
                            for (Method m : classy.getDeclaredMethods()) {
                                if (m.getAnnotation(CommandData.class) == null) continue;
                                target = m;
                            }

                            assert target != null : "Tried to execute an unloaded command";
                            target.invoke(null, sender, this, commandLabel, args);
                        });
            }
        });

        if (!success)
            throw new DuplicateCommandException("A command by that name already exists!");

        Exceptions.trying(logger, Level.WARNING, "sending command updates on script load",
                (MayThrow.Runnable) this::updateCommands);

    }

    protected void updateCommands() throws Throwable {
        if (commandUpdater == null) {
            Class<?> craftServer;

            String revision = Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3];
            craftServer = Class.forName("org.bukkit.craftbukkit." + revision + ".CraftServer");

            var handle = MethodHandles.lookup().findVirtual(craftServer, "syncCommands", MethodType.methodType(void.class));
            commandUpdater = () -> handle.bindTo(Bukkit.getServer()).invoke();
        }

        commandUpdater.run();
    }

    public boolean exists(String name) {
        return Bukkit.getCommandMap().getCommand("%s:%s".formatted(FALLBACK_PREFIX, name)) != null;
    }

    public void unregister(String name) {
        Bukkit.getCommandMap().getKnownCommands().remove(name);
        Bukkit.getCommandMap().getKnownCommands().remove("%s:%s".formatted(FALLBACK_PREFIX, name));
        Exceptions.trying(logger, Level.WARNING, "sending command unregistration update",
            (MayThrow.Runnable) this::updateCommands);
    }
}

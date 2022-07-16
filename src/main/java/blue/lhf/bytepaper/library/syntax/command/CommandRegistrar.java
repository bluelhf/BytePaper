package blue.lhf.bytepaper.library.syntax.command;

import blue.lhf.bytepaper.util.*;
import org.bukkit.Bukkit;
import org.bukkit.command.*;
import org.byteskript.skript.runtime.Skript;
import org.jetbrains.annotations.NotNull;

import java.lang.invoke.*;
import java.lang.reflect.*;
import java.util.*;
import java.util.logging.*;

import static blue.lhf.bytepaper.util.Classes.flattenClassTrees;

public class CommandRegistrar {
    private static final String FALLBACK_PREFIX = "bytepaper";

    private final Skript host;
    private final Logger logger;
    private MayThrow.Runnable commandUpdater;

    public CommandRegistrar(Logger logger, Skript host) {
        this.logger = logger;
        this.host = host;
    }

    public static Map<Method, String> findCommands(Class<?>... classes) {
        Map<Method, String> map = new HashMap<>();
        for (Class<?> sub : flattenClassTrees(classes)) {
            for (Method m : sub.getDeclaredMethods()) {
                var ann = m.getAnnotation(CommandData.class);
                if (ann == null) continue;
                map.put(m, ann.label());
            }
        }
        return map;
    }

    public void registerAll(Class<?>[] classes) throws DuplicateCommandException {
        var exception = new DuplicateCommandException("Could not register one or more commands from " + Arrays.asList(classes));
        for (var entry : findCommands(classes).entrySet()) {
            try {
                register(entry.getValue(), entry.getKey());
            } catch (DuplicateCommandException e) {
                exception.addSuppressed(e);
            }
        }

        if (exception.getSuppressed().length > 0)
            throw exception;
    }

    public void register(String label, Method method) throws DuplicateCommandException {

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
                    (MayThrow.Runnable) () -> host.runScript(() -> {
                        try {
                            method.invoke(null, sender, this, commandLabel, args);
                        } catch (IllegalAccessException e) {
                            throw new AssertionError("Cannot happen");
                        } catch (InvocationTargetException e) {
                            e.printStackTrace();
                        }
                    }));
            }
        });

        if (!success)
            throw new DuplicateCommandException("A command by that name already exists!");

        Exceptions.trying(logger, Level.WARNING, "sending command registration update",
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

    public void unregister(String name) {
        Bukkit.getCommandMap().getKnownCommands().remove(name);
        Bukkit.getCommandMap().getKnownCommands().remove("%s:%s".formatted(FALLBACK_PREFIX, name));
        Exceptions.trying(logger, Level.WARNING, "sending command unregistration update",
            (MayThrow.Runnable) this::updateCommands);
    }
}

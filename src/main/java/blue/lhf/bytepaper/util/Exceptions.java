package blue.lhf.bytepaper.util;

import net.kyori.adventure.audience.Audience;
import org.bukkit.entity.Player;
import org.byteskript.skript.error.ScriptError;

import java.io.*;
import java.util.function.Consumer;
import java.util.logging.*;

public class Exceptions {
    private Exceptions() {
    }

    public static String getStackTrace(Throwable t) {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        t.printStackTrace(pw);
        pw.close();
        return sw.toString();
    }

    public static boolean trying(Consumer<Throwable> errorHandler, Runnable runnable) {
        try {
            runnable.run();
            return true;
        } catch (Exception exc) {
            errorHandler.accept(exc);
            return false;
        }
    }

    public static boolean trying(Logger logger, Level level, String task, Runnable runnable) {
        return trying((exc) -> logger.log(level, """
            An exception occurred while %s!
                                    
            %s
            """.formatted(task, getStackTrace(exc))), runnable);
    }

    public static String plain(String stackTrace) {
        return stackTrace
            .replace("\t", "  ")
            .replace("\r", "")
            .replaceAll("(\\x9B|\\x1B\\[)[0-?]*[ -/]*[@-~]", "");
    }

    public static void throwing(Audience audience, String task, Throwable throwable) {
        trying(audience, task, (MayThrow.Runnable) () -> {
            throw throwable;
        });
    }

    public static boolean trying(Audience audience, String task, Runnable runnable) {
        return trying((exc) -> {
            String stackTrace = getStackTrace(exc);
            Throwable curr = exc;
            while (curr.getCause() != null) {
                if (curr.getCause() instanceof ScriptError) {
                    stackTrace = curr.getCause().getLocalizedMessage();
                    Throwable metaCause = curr.getCause().getCause();
                    if (metaCause != null) {
                        stackTrace += "\n" + getStackTrace(metaCause);
                    }
                }
                curr = curr.getCause();
            }
            stackTrace = plain(stackTrace);
            if (audience instanceof Player player) {
                player.sendMessage(UI.miniMessage().deserialize("""
                    <info>An exception occurred while %s:</info>
                    <error>
                        %s!
                    </error>
                    <secondary>Hover to see details.</secondary>
                    """.formatted(task, UI.RAW.escapeTags(curr.getLocalizedMessage()).replace("'", "''"))).hoverEvent(UI.RAW.deserialize("""
                    <error>%s</error>""".formatted(UI.RAW.escapeTags(plain(getStackTrace(curr))).replace("\n",
                    "<br>").replace("'", "''")))));
            } else {
                audience.sendMessage(UI.miniMessage().deserialize("""
                    <info>An exception occurred while %s!</info>
                    <error>
                    %s
                                            
                    Stack trace:
                    %s
                    </error>""".formatted(task, stackTrace, getStackTrace(curr))));
            }
        }, runnable);
    }
}

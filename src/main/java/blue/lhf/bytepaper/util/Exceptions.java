package blue.lhf.bytepaper.util;

import net.kyori.adventure.audience.Audience;
import org.bukkit.entity.Player;
import org.byteskript.skript.error.ScriptError;

import java.io.*;
import java.util.function.Consumer;
import java.util.logging.*;

import static net.kyori.adventure.text.minimessage.MiniMessage.miniMessage;

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
        return trying((exc) -> {
            logger.log(level, """
                        An exception occurred while %s!
                                                
                        %s
                        """.formatted(task, getStackTrace(exc)));
        }, runnable);
    }

    public static boolean trying(Audience audience, String task, Runnable runnable) {
        return trying((exc) -> {
            String stackTrace = getStackTrace(exc);
            Throwable curr = exc;
            while (curr.getCause() != null) {
                if (curr.getCause() instanceof ScriptError) {
                    stackTrace = curr.getCause().getLocalizedMessage();
                }
                curr = curr.getCause();
            }
            stackTrace = stackTrace
                    .replace("\t", "  ")
                    .replace("\r", "")
                    .replaceAll("(\\x9B|\\x1B\\[)[0-?]*[ -/]*[@-~]", "");
            if (audience instanceof Player player) {
                player.sendMessage(UI.miniMessage().deserialize("""
                        <hover:show_text:'<error>%s</error>'>
                        <error>An exception occurred while %s:
                            %s!
                        </error>
                        <secondary>Hover to see details.</secondary>
                        </hover>""".formatted(UI.miniMessage().escapeTags(stackTrace).replace("\n", "<br>").replace("'", "''"),
                        task, miniMessage().escapeTags(exc.getLocalizedMessage()).replace("'", "''"))));
            } else {
                audience.sendMessage(UI.miniMessage().deserialize("""
                        <error>An exception occurred while %s!
                                                
                        %s
                        </error>""".formatted(task, stackTrace)));
            }
        }, runnable);
    }
}

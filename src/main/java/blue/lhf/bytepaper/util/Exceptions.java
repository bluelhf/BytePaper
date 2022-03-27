package blue.lhf.bytepaper.util;

import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.entity.Player;

import java.io.PrintWriter;
import java.io.StringWriter;

import static net.kyori.adventure.text.minimessage.MiniMessage.miniMessage;

public class Exceptions {
    private Exceptions() {
    }

    public static boolean trying(Audience audience, String task, Runnable runnable) {
        try {
            runnable.run();
            return true;
        } catch (Exception exc) {
            Throwable t = exc;
            while (t.getCause() != null) t = t.getCause();
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            t.printStackTrace(pw);
            pw.close();

            var stackTrace = sw.toString().replace("\t", "  ").replace("\r", "");
            stackTrace = stackTrace.replaceAll("(\\x9B|\\x1B\\[)[0-?]*[ -/]*[@-~]", "");
            if (audience instanceof Player player) {
                player.sendMessage(UI.miniMessage().deserialize("""
                        <hover:show_text:'<error>%s</error>'>
                        <error>An exception occurred while %s:
                            %s!
                        </error>
                        <secondary>Hover to see details.</secondary>
                        </hover>""".formatted(UI.miniMessage().escapeTags(stackTrace).replace("\n", "<br>").replace("'", "''"),
                        task, miniMessage().escapeTags(t.getLocalizedMessage()).replace("'", "''"))));
            } else {
                audience.sendMessage(UI.miniMessage().deserialize("""
                        <error>An exception occurred while %s!
                                                
                        %s
                        </error>""".formatted(task, stackTrace)));
            }

            return false;
        }
    }
}

package blue.lhf.bsfp.util;

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
                player.sendMessage(MiniMessage.builder().strict(true).build().deserialize("""
                        <hover:show_text:'<gradient:#33080c:#a62832>%s</gradient>'>
                        <gradient:#a62832:#33080c>An exception occurred while %s:
                            %s!
                        </gradient>
                        <gradient:#736b6b:#332d2d>Hover to see details.</gradient>
                        </hover>""".formatted(miniMessage().escapeTags(stackTrace).replace("\n", "<br>").replace("'", "''"),
                        task, miniMessage().escapeTags(t.getLocalizedMessage()).replace("'", "''"))));
            } else {
                audience.sendMessage(miniMessage().deserialize("""
                        <gradient:#a62832:#33080c>An exception occurred while %s!
                                                
                        %s
                        </gradient>""".formatted(task, stackTrace)));
            }

            return false;
        }
    }
}

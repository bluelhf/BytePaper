package blue.lhf.bytepaper.library.syntax.event.player;

import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.byteskript.skript.api.Event;
import org.byteskript.skript.api.Library;
import org.byteskript.skript.api.note.Documentation;
import org.byteskript.skript.api.note.EventValue;
import org.byteskript.skript.api.syntax.EventHolder;

@Documentation(
        name = "Player Command Preprocess",
        description = "Run when a player executes a command (by placing a slash at the start of their message). It is called early in the command handling process.",
        examples = {
                """
                on player command preprocess:
                    trigger:
                        send "You are typing a command!" to event-player"
                """
        }
)
public class EventPlayerCommandPreprocess extends EventHolder {

    public EventPlayerCommandPreprocess(Library provider) {
        super(provider, "on [player] command preprocess");
    }

    @Override
    public Class<? extends Event> eventClass() {
        return EventPlayerCommandPreprocess.Data.class;
    }

    @SuppressWarnings("unused")
    public static class Data extends Event {
        protected final PlayerCommandPreprocessEvent event;

        public Data(PlayerCommandPreprocessEvent event) {
            this.event = event;
        }

        @Override
        public boolean isAsync() {
            return false;
        }

        @EventValue("player")
        public Player player() {
            return event.getPlayer();
        }

        @EventValue("message")
        public String message() {
            return event.getMessage();
        }

    }

}

package blue.lhf.bytepaper.library.syntax.player;

import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerCommandSendEvent;
import org.byteskript.skript.api.Event;
import org.byteskript.skript.api.Library;
import org.byteskript.skript.api.note.Documentation;
import org.byteskript.skript.api.note.EventValue;
import org.byteskript.skript.api.syntax.EventHolder;

import java.util.Collection;

@Documentation(
        name = "Player Command Send",
        description = "Run when the list of available server commands is sent to the player",
        examples = {
                """
                on player command send:
                    trigger:
                        send "You've receive the list of available server commands!" to event-player"
                """
        }
)
public class EventPlayerCommandSend extends EventHolder {

    public EventPlayerCommandSend(Library provider) {
        super(provider, "on [player] command send");
    }

    @Override
    public Class<? extends Event> eventClass() {
        return EventPlayerCommandSend.Data.class;
    }

    @SuppressWarnings("unused")
    public static class Data extends Event {
        protected final PlayerCommandSendEvent event;

        public Data(PlayerCommandSendEvent event) {
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

        @EventValue("commands")
        public Collection<String> commands() {
            return event.getCommands();
        }

    }

}

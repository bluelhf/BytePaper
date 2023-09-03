package blue.lhf.bytepaper.library.syntax.event.player;

import blue.lhf.bytepaper.util.EventMapsTo;
import org.bukkit.entity.Player;
import org.bukkit.event.player.*;
import org.byteskript.skript.api.Event;
import org.byteskript.skript.api.Library;
import org.byteskript.skript.api.note.Documentation;
import org.byteskript.skript.api.note.EventValue;
import org.byteskript.skript.api.syntax.EventHolder;

@Documentation(
        name = "Player Toggle Flight",
        description = "Run when a player toggles flight.",
        examples = {
                """
                on player toggle flight:
                    trigger:
                        send "You toggled flight!" to event-player
                """
        }
)
@EventMapsTo(PlayerToggleFlightEvent.class)
public class EventPlayerToggleFlight extends EventHolder {
    public EventPlayerToggleFlight(Library provider) {
        super(provider, "on [player] (flight|fly) [toggle]");
    }

    @Override
    public Class<? extends Event> eventClass() {
        return Data.class;
    }

    @SuppressWarnings("unused")
    public static class Data extends Event {
        protected final PlayerToggleFlightEvent event;

        public Data(PlayerToggleFlightEvent event) {
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

    }
}
